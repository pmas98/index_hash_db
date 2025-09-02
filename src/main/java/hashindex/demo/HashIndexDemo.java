package hashindex.demo;

import java.util.ArrayList;
import java.util.List;
import hashindex.core.Tuple;
import hashindex.core.HashIndex;
import hashindex.functions.HashFunction;
import hashindex.functions.SimpleHashFunction;

/**
 * Demo class to show how to use the hash index
 */
public class HashIndexDemo {
    public static void main(String[] args) {
        List<Tuple> tuples = createSampleData();
        
        int pageSize = 10;
        int tuplesPerBucket = 5;
        
        HashFunction hashFunction = new SimpleHashFunction();
        // Funções hash alternativas:
        // HashFunction hashFunction = new DivisionHashFunction();
        // HashFunction hashFunction = new MultiplicationHashFunction();
        
        // Create and build hash index
        HashIndex hashIndex = new HashIndex(pageSize, tuplesPerBucket, hashFunction);
        hashIndex.buildIndex(tuples);
        
        // Print statistics
        printStatistics(hashIndex);
        
    }
    
    public static List<Tuple> createSampleData() {
        List<Tuple> tuples = new ArrayList<>();
        String[] words = {
            "apple", "banana", "cherry", "date", "elderberry",
            "fig", "grape", "honeydew", "kiwi", "lemon",
            "mango", "nectarine", "orange", "papaya", "quince",
            "raspberry", "strawberry", "tangerine", "watermelon", "zucchini"
        };
        
        for (String word : words) {
            tuples.add(new Tuple(word, "Data for " + word));
        }
        
        return tuples;
    }
    
    private static void printStatistics(HashIndex hashIndex) {
        System.out.println("=== Hash Index Statistics ===");
        System.out.println("Number of Buckets: " + hashIndex.getNumberOfBuckets());
        System.out.println("Tuples per Bucket: " + hashIndex.getTuplesPerBucket());
        System.out.println("Total Tuples: " + hashIndex.getTable().getTotalTupleCount());
        System.out.println("Total Pages: " + hashIndex.getTable().getPageCount());
        System.out.println("Page Size: " + hashIndex.getTable().getPageSize());
        System.out.println("Collision Count: " + hashIndex.getCollisionCount());
        System.out.println("Overflow Count: " + hashIndex.getOverflowCount());
        System.out.println("Collision Rate: " + String.format("%.2f%%", hashIndex.getCollisionRate() * 100));
        System.out.println("Overflow Rate: " + String.format("%.2f%%", hashIndex.getOverflowRate() * 100));
        System.out.println();
    }
    
}
