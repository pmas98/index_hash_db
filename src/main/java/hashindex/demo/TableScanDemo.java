package hashindex.demo;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import hashindex.core.ScanResult;
import hashindex.core.Table;
import hashindex.core.TableScanService;
import hashindex.core.Tuple;
import hashindex.utils.DataLoader;

public class TableScanDemo {
    public static void main(String[] args) {
        String filename = "words_alpha.txt"; // alterar arquivo

        // parâmetros
        int pageSize = 10;     // tuplas por página 
        int maxWords = 5000;    // limite para demo rápida (0 para carregar todas)

        try {
            System.out.println("Carregando palavras de: " + filename);
            List<Tuple> tuples = (maxWords > 0)
                ? DataLoader.loadWordsFromFile(filename, maxWords)
                : DataLoader.loadWordsFromFile(filename);

            // monta a tabela
            Table table = new Table(pageSize);
            for (Tuple t : tuples) table.addTuple(t);

            System.out.println("Tabela pronta: " + table.getPageCount() +
                               " páginas; " + table.getTotalTupleCount() + " tuplas.\n");

            // serviço de table scan
            TableScanService scanner = new TableScanService(table);

            // 1) três buscas automáticas para demonstrar
            //String keyStart = tuples.get(0).getKey();                       // começo
            //String keyMiddle = tuples.get(tuples.size()/2).getKey();        // meio
            //String keyMissing = "zzzzzzzzzzzzzzzz";                         // provável ausência

            //runScan(scanner, keyStart);
            //runScan(scanner, keyMiddle);
            //runScan(scanner, keyMissing);

            // 2) modo interativo (opcional)
            try (Scanner in = new Scanner(System.in)) {
                while (true) {
                    System.out.print("\nDigite uma chave: ");
                    String key = in.nextLine().trim();
                    if (key.isEmpty()) break;
                    runScan(scanner, key);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo: " + e.getMessage());
            System.err.println("Coloque o arquivo na raiz do projeto e ajuste o nome em TableScanDemo.");
        }
    }

    private static void runScan(TableScanService scanner, String key) {
        System.out.println("=== TABLE SCAN | chave: " + key + " ===");
        ScanResult res = scanner.tableScanByKey(key);

        // listar o percurso (páginas lidas)
        for (ScanResult.Step s : res.getTrace()) {
            System.out.println("Página " + s.pageIndex +
                               " lida; tuplas checadas nesta página: " + s.tuplesCheckedOnPage);
        }

        if (res.isFound()) {
            System.out.println("Encontrado na página: " + res.getFoundPageIndex());
        } else {
            System.out.println("Chave NÃO encontrada.");
        }
        System.out.println("Custo (páginas lidas): " + res.getPagesRead() + "\n");
    }
}
