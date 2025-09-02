package hashindex.core;

import java.util.ArrayList;
import java.util.List;
import hashindex.functions.HashFunction;

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
        this.numberOfBuckets = (int) Math.ceil((double) totalTuples / tuplesPerBucket) + 1;
        
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
        
        int bucketNumber = hashFunction.hash(tuple.getKey(), numberOfBuckets);
        Bucket bucket = buckets.get(bucketNumber);
        
        if (!bucket.addEntry(tuple.getKey(), pageNumber)) {
            // Bucket tá cheio
            overflowCount++;
            
            // overflow: encontra o próximo bucket disponível
            int nextBucket = (bucketNumber + 1) % numberOfBuckets;
            while (nextBucket != bucketNumber) {
                Bucket nextBucketObj = buckets.get(nextBucket);
                if (nextBucketObj.addEntry(tuple.getKey(), pageNumber)) {
                    break;
                }
                nextBucket = (nextBucket + 1) % numberOfBuckets;
            }
        }
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
}
