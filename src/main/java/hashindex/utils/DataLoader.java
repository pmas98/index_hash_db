package hashindex.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import hashindex.core.Tuple;

public class DataLoader {
    
    public static List<Tuple> loadWordsFromFile(String filename) throws IOException {
        List<Tuple> tuples = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    tuples.add(new Tuple(line, "Word data for: " + line + " (line " + lineNumber + ")"));
                    lineNumber++;
                }
            }
        }
        
        return tuples;
    }
    
    public static List<Tuple> loadWordsFromFile(String filename, int maxWords) throws IOException {
        List<Tuple> tuples = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 1;
            int wordCount = 0;
            
            while ((line = reader.readLine()) != null && wordCount < maxWords) {
                line = line.trim();
                if (!line.isEmpty()) {
                    tuples.add(new Tuple(line, "Word data for: " + line + " (line " + lineNumber + ")"));
                    lineNumber++;
                    wordCount++;
                }
            }
        }
        
        return tuples;
    }
}
