package hashindex.core;

import java.util.ArrayList;
import java.util.List;

public class ScanResult {
    public static class Step {
        public final int pageIndex;
        public final int tuplesCheckedOnPage;
        public Step(int pageIndex, int tuplesCheckedOnPage) {
            this.pageIndex = pageIndex;
            this.tuplesCheckedOnPage = tuplesCheckedOnPage;
        }
    }

    private boolean found;
    private int foundPageIndex = -1;
    private int foundTupleIndex = -1;
    private int pagesRead = 0;     // custo pedido: páginas lidas
    private int tuplesRead = 0;    // opcional (pra log)
    private final List<Step> trace = new ArrayList<>();

    public boolean isFound() { return found; }
    public int getFoundPageIndex() { return foundPageIndex; }
    public int getFoundTupleIndex() { return foundTupleIndex; }
    public int getPagesRead() { return pagesRead; }
    public int getTuplesRead() { return tuplesRead; }
    public List<Step> getTrace() { return trace; }

    public void markFound(int pageIndex, int tupleIndex) {
        this.found = true;
        this.foundPageIndex = pageIndex;
        this.foundTupleIndex = tupleIndex;
    }
    public void incPagesRead() { pagesRead++; }
    public void incTuplesRead(int n) { tuplesRead += n; }
    public void addStep(int pageIndex, int tuplesCheckedOnPage) {
        trace.add(new Step(pageIndex, tuplesCheckedOnPage));
    }
}

