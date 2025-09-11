package hashindex.demo;

import hashindex.core.HashIndex;
import hashindex.core.Tuple;
import hashindex.functions.DivisionHashFunction;
import hashindex.functions.HashFunction;
import hashindex.functions.MultiplicationHashFunction;
import hashindex.functions.SimpleHashFunction;
import hashindex.utils.DataLoader;
import java.io.IOException;
import java.util.List;

/**
 * Enhanced demo that can work with the actual 466k words file
 */
public class HashIndexDemoWithFile {
    public static void main(String[] args) {
        String filename = "words.txt"; // Change this to your actual file path
        
        try {
            // Load data from file
            System.out.println("Loading words from file: " + filename);
            List<Tuple> tuples = DataLoader.loadWordsFromFile(filename);
            System.out.println("Loaded " + tuples.size() + " words");
            System.out.println();
            
            // Configuration
            int pageSize = 100; // tuples per page
            int tuplesPerBucket = 50; // tuples per bucket
            
            // Test different hash functions
            testHashFunctions(tuples, pageSize, tuplesPerBucket);
            
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            System.out.println("Using sample data instead...");
            
            // Fallback to sample data
            List<Tuple> sampleTuples = HashIndexDemo.createSampleData();
            testHashFunctions(sampleTuples, 10, 5);
        }
    }
    
    private static void testHashFunctions(List<Tuple> tuples, int pageSize, int tuplesPerBucket) {
        HashFunction[] hashFunctions = {
            new SimpleHashFunction(),
            new DivisionHashFunction(),
            new MultiplicationHashFunction()
        };
        
        String[] functionNames = {
            "Função Hash Simples",
            "Função Hash por Divisão", 
            "Função Hash por Multiplicação"
        };
        
        for (int i = 0; i < hashFunctions.length; i++) {
            System.out.println("=== Testando " + functionNames[i] + " ===");
            
            HashIndex hashIndex = new HashIndex(pageSize, tuplesPerBucket, hashFunctions[i]);
            hashIndex.buildIndex(tuples);
            
            printStatistics(hashIndex);
            
            System.out.println();
        }
    }
    
    private static void printStatistics(HashIndex hashIndex) {
        System.out.println("Número de Buckets: " + hashIndex.getNumberOfBuckets());
        System.out.println("Tuplas por Bucket: " + hashIndex.getTuplesPerBucket());
        System.out.println("Total de Tuplas: " + hashIndex.getTable().getTotalTupleCount());
        System.out.println("Total de Páginas: " + hashIndex.getTable().getPageCount());
        System.out.println("Tamanho da Página: " + hashIndex.getTable().getPageSize());
        System.out.println("Contagem de Colisões: " + hashIndex.getCollisionCount());
        System.out.println("Contagem de Overflow: " + hashIndex.getOverflowCount());
        System.out.println("Taxa de Colisões: " + String.format("%.2f%%", hashIndex.getCollisionRate() * 100));
        System.out.println("Taxa de Overflow: " + String.format("%.2f%%", hashIndex.getOverflowRate() * 100));
    }
}
