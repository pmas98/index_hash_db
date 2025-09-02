package hashindex.core;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Page> pages;
    private int pageSize;
    
    public Table(int pageSize) {
        this.pageSize = pageSize;
        this.pages = new ArrayList<>();
    }
    
    public void addTuple(Tuple tuple) {
        Page targetPage = null;
        
        for (Page page : pages) {
            if (!page.isFull()) {
                targetPage = page;
                break;
            }
        }
        
        if (targetPage == null) {
            targetPage = new Page(pages.size(), pageSize);
            pages.add(targetPage);
        }
        
        targetPage.addTuple(tuple);
    }
    
    public Tuple findTuple(String key) {
        for (Page page : pages) {
            Tuple tuple = page.findTuple(key);
            if (tuple != null) {
                return tuple;
            }
        }
        return null;
    }
    
    public Tuple findTupleWithPageInfo(String key) {
        for (Page page : pages) {
            Tuple tuple = page.findTuple(key);
            if (tuple != null) {
                return tuple;
            }
        }
        return null;
    }
    
    public Page getPage(int pageNumber) {
        if (pageNumber >= 0 && pageNumber < pages.size()) {
            return pages.get(pageNumber);
        }
        return null;
    }
    
    public List<Page> getAllPages() {
        return new ArrayList<>(pages);
    }
    
    public int getPageCount() {
        return pages.size();
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public int getTotalTupleCount() {
        return pages.stream().mapToInt(Page::getTupleCount).sum();
    }
    
    @Override
    public String toString() {
        return "Table{pages=" + pages.size() + ", totalTuples=" + getTotalTupleCount() + ", pageSize=" + pageSize + "}";
    }
}
