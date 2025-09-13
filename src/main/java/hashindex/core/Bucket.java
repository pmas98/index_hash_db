package hashindex.core;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private int bucketNumber;
    private List<BucketEntry> entries;
    private int maxEntries;
    
    public Bucket(int bucketNumber, int maxEntries) {
        this.bucketNumber = bucketNumber;
        this.maxEntries = maxEntries;
        this.entries = new ArrayList<>();
    }
    
    public boolean addEntry(String key, int pageNumber) {
        if (entries.size() < maxEntries) {
            entries.add(new BucketEntry(key, pageNumber));
            return true;
        }
        return false; 
    }
    
    public Integer findPageNumber(String key) {
        for (BucketEntry entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getPageNumber();
            }
        }
        return null;
    }
    
    public boolean isFull() {
        return entries.size() >= maxEntries;
    }
    
    public int getBucketNumber() {
        return bucketNumber;
    }
    
    public List<BucketEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public int getEntryCount() {
        return entries.size();
    }
    
    public int getMaxEntries() {
        return maxEntries;
    }
    
    @Override
    public String toString() {
        return "Bucket{bucketNumber=" + bucketNumber + ", entries=" + entries.size() + "/" + maxEntries + "}";
    }
    
    public static class BucketEntry {
        private String key;
        private int pageNumber;
        
        public BucketEntry(String key, int pageNumber) {
            this.key = key;
            this.pageNumber = pageNumber;
        }
        
        public String getKey() {
            return key;
        }
        
        public int getPageNumber() {
            return pageNumber;
        }
        
        @Override
        public String toString() {
            return "BucketEntry{key='" + key + "', pageNumber=" + pageNumber + "}";
        }
    }
}
