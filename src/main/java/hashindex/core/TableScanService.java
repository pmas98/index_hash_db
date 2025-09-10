package hashindex.core;

import java.util.List;

public class TableScanService {
    private final Table table;

    public TableScanService(Table table) {
        this.table = table;
    }

    /** Varredura sequencial: lê páginas 0..N-1, conta custo (páginas lidas) e para ao encontrar a chave. */
    public ScanResult tableScanByKey(String key) {
        ScanResult res = new ScanResult();

        List<Page> pages = table.getAllPages(); // cópia segura; pode iterar direto
        for (int p = 0; p < pages.size(); p++) {
            Page page = pages.get(p);
            int tuplesCheckedHere = 0;

            // leu esta página -> conta 1 acesso de página
            res.incPagesRead();

            List<Tuple> tuples = page.getTuples();
            for (int t = 0; t < tuples.size(); t++) {
                Tuple tup = tuples.get(t);
                tuplesCheckedHere++;
                res.incTuplesRead(1);

                if (tup.getKey().equals(key)) {
                    res.addStep(p, tuplesCheckedHere);
                    res.markFound(p, t);
                    return res;
                }
            }

            // não encontrou nesta página; registra passo
            res.addStep(p, tuplesCheckedHere);
        }

        // varreu tudo e não achou
        return res;
    }
}

