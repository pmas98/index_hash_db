package hashindex.core;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private int pageNumber;
    private List<Tuple> tuples;
    private int maxTuples;
    
    public Page(int pageNumber, int maxTuples) {
        this.pageNumber = pageNumber;
        this.maxTuples = maxTuples;
        this.tuples = new ArrayList<>();
    }
    
    public boolean addTuple(Tuple tuple) {
        if (tuples.size() < maxTuples) {
            tuples.add(tuple);
            return true;
        }
        return false; 
    }
    
    public Tuple findTuple(String key) {
        for (Tuple tuple : tuples) {
            if (tuple.getKey().equals(key)) {
                return tuple;
            }
        }
        return null;
    }
    
    public boolean isFull() {
        return tuples.size() >= maxTuples;
    }
    
    public int getPageNumber() {
        return pageNumber;
    }
    
    public List<Tuple> getTuples() {
        return new ArrayList<>(tuples);
    }
    
    public int getTupleCount() {
        return tuples.size();
    }
    
    public int getMaxTuples() {
        return maxTuples;
    }
    
    @Override
    public String toString() {
        return "Page{pageNumber=" + pageNumber + ", tuples=" + tuples.size() + "/" + maxTuples + "}";
    }
}
