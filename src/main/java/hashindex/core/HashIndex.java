package hashindex.core;

import hashindex.functions.HashFunction;
import java.util.ArrayList;
import java.util.List;

public class HashIndex {
    private Table table;
    private List<Bucket> buckets;
    private HashFunction hashFunction;
    private int numberOfBuckets;
    private int tuplesPerBucket;
    
    private int collisionCount;
    private int overflowCount;
    
    public HashIndex(int pageSize, int tuplesPerBucket, HashFunction hashFunction) {
        this.table = new Table(pageSize);
        this.tuplesPerBucket = tuplesPerBucket;
        this.hashFunction = hashFunction;
        this.collisionCount = 0;
        this.overflowCount = 0;
    }
    
    public void buildIndex(List<Tuple> tuples) {
        int totalTuples = tuples.size();
        // Choose number of buckets to target a lower load factor (alpha), then round up to next prime.
        // alpha = totalTuples / (numberOfBuckets * tuplesPerBucket)
        // => numberOfBuckets >= totalTuples / (alpha * tuplesPerBucket)
        double targetLoadFactor = 0.70; // 70% occupancy target to reduce clustering/collisions
        int minBuckets = (int) Math.ceil(totalTuples / (targetLoadFactor * (double) tuplesPerBucket));
        this.numberOfBuckets = nextPrime(minBuckets);
        
        buckets = new ArrayList<>();
        for (int i = 0; i < numberOfBuckets; i++) {
            buckets.add(new Bucket(i, tuplesPerBucket));
        }
        
        for (Tuple tuple : tuples) {
            addTupleToIndex(tuple);
        }
    }
    

    private void addTupleToIndex(Tuple tuple) {
        table.addTuple(tuple);
        int pageNumber = table.getPageCount() - 1;

        int homeBucket = hashFunction.hash(tuple.getKey(), numberOfBuckets);
        Bucket bucket = buckets.get(homeBucket);

        // Linear probing across buckets. Count collisions as extra probes beyond the first attempt.
        int probes = 0;
        if (!bucket.addEntry(tuple.getKey(), pageNumber)) {
            // Could not insert in home bucket -> overflow chain
            int nextBucket = (homeBucket + 1) % numberOfBuckets;
            while (nextBucket != homeBucket) {
                Bucket candidate = buckets.get(nextBucket);
                probes++;
                if (candidate.addEntry(tuple.getKey(), pageNumber)) {
                    break;
                }
                nextBucket = (nextBucket + 1) % numberOfBuckets;
            }
        }

        // Update statistics
        collisionCount += probes;           // number of extra probes performed for this insertion
        if (probes > 0) {
            overflowCount++;               // tuple stored away from its home bucket
        }
    }

    /** Busca via índice: retorna custo em páginas lidas e página encontrada (se houver). */
    public ScanResult searchByIndex(String key) {
        ScanResult res = new ScanResult();
        if (buckets == null || buckets.isEmpty()) return res;

        int homeBucket = hashFunction.hash(key, numberOfBuckets);
        int current = homeBucket;
        do {
            Bucket b = buckets.get(current);
            Integer pageIdx = b.findPageNumber(key);
            if (pageIdx != null) {
                // Acesso à página de dados
                res.incPagesRead();
                res.markFound(pageIdx, -1);
                return res;
            }
            // Se o bucket não está cheio, a cadeia de probing termina aqui (open addressing)
            if (!b.isFull()) {
                return res;
            }
            current = (current + 1) % numberOfBuckets;
        } while (current != homeBucket);

        return res;
    }
    public double getCollisionRate() {
        int totalEntries = buckets.stream().mapToInt(Bucket::getEntryCount).sum();
        return totalEntries > 0 ? (double) collisionCount / totalEntries : 0.0;
    }
    
    public double getOverflowRate() {
        int totalEntries = buckets.stream().mapToInt(Bucket::getEntryCount).sum();
        return totalEntries > 0 ? (double) overflowCount / totalEntries : 0.0;
    }
    
    // Getters
    public Table getTable() {
        return table;
    }
    
    public List<Bucket> getBuckets() {
        return new ArrayList<>(buckets);
    }
    
    public int getNumberOfBuckets() {
        return numberOfBuckets;
    }
    
    public int getTuplesPerBucket() {
        return tuplesPerBucket;
    }
    
    public int getCollisionCount() {
        return collisionCount;
    }
    
    public int getOverflowCount() {
        return overflowCount;
    }

    // Utilities
    private static int nextPrime(int n) {
        if (n <= 2) return 2;
        int candidate = (n % 2 == 0) ? n + 1 : n;
        while (!isPrime(candidate)) {
            candidate += 2;
        }
        return candidate;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int i = 3; (long) i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
