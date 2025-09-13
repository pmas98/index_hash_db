package hashindex.demo;

import hashindex.core.HashIndex;
import hashindex.core.ScanResult;
import hashindex.core.TableScanService;
import hashindex.core.Tuple;
import hashindex.functions.DivisionHashFunction;
import hashindex.functions.HashFunction;
import hashindex.functions.SimpleHashFunction;
import hashindex.utils.DataLoader;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

public class HashIndexController {
    private final HashIndexUI ui;
    private HashIndex hashIndex;
    private List<Tuple> loadedTuples;

    public HashIndexController(HashIndexUI ui) {
        this.ui = ui;
        wireActions();
    }

    private void wireActions() {
        ui.addBuildActionListener(e -> onBuildIndex());
        ui.addSearchActionListener(e -> onSearchByIndex());
        ui.addScanActionListener(e -> onTableScan());
    }

    private void onBuildIndex() {
        String file = ui.getDataFilePath();
        int pageSize = parseIntOr(ui.getPageSizeText(), 100);
        int tuplesPerBucket = parseIntOr(ui.getBucketSizeText(), 4);
        HashFunction hashFunction = new DivisionHashFunction();

        try {
            ui.appendLog("[CTRL] Carregando dados de: " + file);
            loadedTuples = DataLoader.loadWordsFromFile(file);
            ui.appendLog("[CTRL] Tuplas carregadas: " + loadedTuples.size());

            hashIndex = new HashIndex(pageSize, tuplesPerBucket, hashFunction);
            long start = System.currentTimeMillis();
            hashIndex.buildIndex(loadedTuples);
            long end = System.currentTimeMillis();
            ui.appendLog("[CTRL] Índice construído em " + (end - start) + " ms");

            // atualizar stats
            ui.setNbBuckets(hashIndex.getNumberOfBuckets());
            ui.setPages(hashIndex.getTable().getPageCount());
            ui.setTotalTuples(hashIndex.getTable().getTotalTupleCount());
            ui.setCollisions(hashIndex.getCollisionCount());
            ui.setOverflows(hashIndex.getOverflowCount());
            ui.setCollisionRate(hashIndex.getCollisionRate());
            ui.setOverflowRate(hashIndex.getOverflowRate());
            ui.setFoundPage(null);
        } catch (IOException ex) {
            ui.appendLog("[ERRO] Falha ao carregar arquivo: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro lendo arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ui.appendLog("[ERRO] Falha ao construir índice: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao construir índice: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearchByIndex() {
        if (hashIndex == null) {
            ui.appendLog("[CTRL] Construa o índice antes de procurar.");
            return;
        }
        String key = ui.getSearchKey();
        if (key == null || key.isEmpty()) {
            ui.appendLog("[CTRL] Informe uma chave para buscar.");
            return;
        }
        ScanResult res = hashIndex.searchByIndex(key);
        ui.setSearchCost(res.getPagesRead());
        ui.setFoundPage(res.isFound() ? res.getFoundPageIndex() : null);
        ui.appendLog("[CTRL] Busca por índice '" + key + "' => páginas lidas=" + res.getPagesRead() + (res.isFound()? (", página=" + res.getFoundPageIndex()) : ", não encontrado"));
    }

    private void onTableScan() {
        if (hashIndex == null) {
            ui.appendLog("[CTRL] Construa o índice antes de realizar table scan.");
            return;
        }
        String key = ui.getSearchKey();
        if (key == null || key.isEmpty()) {
            ui.appendLog("[CTRL] Informe uma chave para table scan.");
            return;
        }
        TableScanService scanner = new TableScanService(hashIndex.getTable());
        ScanResult res = scanner.tableScanByKey(key);
        ui.setScanCost(res.getPagesRead());
        ui.setFoundPage(res.isFound() ? res.getFoundPageIndex() : null);
        ui.appendLog("[CTRL] Table scan '" + key + "' => páginas lidas=" + res.getPagesRead() + (res.isFound()? (", página=" + res.getFoundPageIndex()) : ", não encontrado"));
    }

    private int parseIntOr(String text, int fallback) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return fallback;
        }
    }
}


