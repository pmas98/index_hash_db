package hashindex.demo;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HashIndexUI extends JFrame {

    private final JTextField txtFile = new JTextField();
    private final JTextField txtPageSize = new JTextField("100");
    private final JTextField txtBucketSize = new JTextField("4");
    private final JTextField txtSearchKey = new JTextField();

    private final JButton btnBrowse = new JButton("Escolher arquivo...");
    private final JButton btnBuild = new JButton("Construir Índice");
    private final JButton btnSearch = new JButton("Procurar (Índice)");
    private final JButton btnScan = new JButton("Table Scan");

    private final JTextArea txtLog = new JTextArea();

    private final JLabel lblNb = new JLabel("NB (buckets): -");
    private final JLabel lblPages = new JLabel("Páginas: -");
    private final JLabel lblTotalTuples = new JLabel("Total de Tuplas: -");
    private final JLabel lblCollisions = new JLabel("Colisões: -");
    private final JLabel lblOverflows = new JLabel("Overflows: -");
    private final JLabel lblCollisionRate = new JLabel("Taxa de Colisão: -");
    private final JLabel lblOverflowRate = new JLabel("Taxa de Overflow: -");
    private final JLabel lblSearchCost = new JLabel("Custo da Busca (páginas lidas): -");
    private final JLabel lblScanCost = new JLabel("Custo Table Scan (páginas lidas): -");
    private final JLabel lblFoundPage = new JLabel("Página encontrada: -");

    public HashIndexUI() {
        super("Índice Hash - Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildStatsPanel(), BorderLayout.EAST);

        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);

        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt", "csv"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtFile.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    private JPanel buildTopPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new TitledBorder("Parâmetros (4 inputs)"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        c.gridx = 0; c.gridy = row; p.add(new JLabel("Arquivo (uma palavra por linha):"), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1; p.add(txtFile, c);
        c.gridx = 2; c.gridy = row; c.weightx = 0; p.add(btnBrowse, c);

        row++;
        c.gridx = 0; c.gridy = row; p.add(new JLabel("Tamanho da Página (tuplas/página):"), c);
        c.gridx = 1; c.gridy = row; p.add(txtPageSize, c);

        row++;
        c.gridx = 0; c.gridy = row; p.add(new JLabel("Tamanho do Bucket FR (tuplas/bucket):"), c);
        c.gridx = 1; c.gridy = row; p.add(txtBucketSize, c);

        row++;
        c.gridx = 0; c.gridy = row; p.add(new JLabel("Chave de busca (tupla/palavra):"), c);
        c.gridx = 1; c.gridy = row; p.add(txtSearchKey, c);

        row++;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(btnBuild);
        btns.add(btnSearch);
        btns.add(btnScan);
        c.gridx = 0; c.gridy = row; c.gridwidth = 3; p.add(btns, c);

        return p;
    }

    private JPanel buildCenterPanel() {
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBorder(new TitledBorder("Saída / Log"));

        txtLog.setEditable(false);
        txtLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtLog);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private JPanel buildStatsPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder("Estatísticas"));

        for (JLabel l : new JLabel[]{lblNb,lblPages,lblTotalTuples,lblCollisions,lblOverflows,
                lblCollisionRate,lblOverflowRate,lblSearchCost,lblScanCost,lblFoundPage}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(l);
            p.add(Box.createVerticalStrut(6));
        }
        return p;
    }

    public void appendLog(String s) {
        txtLog.append(s + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    public String getDataFilePath() { return txtFile.getText().trim(); }
    public String getPageSizeText() { return txtPageSize.getText().trim(); }
    public String getBucketSizeText() { return txtBucketSize.getText().trim(); }
    public String getSearchKey() { return txtSearchKey.getText().trim(); }

    public void setNbBuckets(int nb) { lblNb.setText("NB (buckets): " + nb); }
    public void setPages(int pages) { lblPages.setText("Páginas: " + pages); }
    public void setTotalTuples(int total) { lblTotalTuples.setText("Total de Tuplas: " + total); }
    public void setCollisions(int c) { lblCollisions.setText("Colisões: " + c); }
    public void setOverflows(int o) { lblOverflows.setText("Overflows: " + o); }
    public void setCollisionRate(double r) { lblCollisionRate.setText(String.format("Taxa de Colisão: %.2f%%", r * 100.0)); }
    public void setOverflowRate(double r) { lblOverflowRate.setText(String.format("Taxa de Overflow: %.2f%%", r * 100.0)); }
    public void setSearchCost(int cost) { lblSearchCost.setText("Custo da Busca (páginas lidas): " + cost); }
    public void setScanCost(int cost) { lblScanCost.setText("Custo Table Scan (páginas lidas): " + cost); }
    public void setFoundPage(Integer page) {
        if (page == null || page < 0) lblFoundPage.setText("Página encontrada: -");
        else lblFoundPage.setText("Página encontrada: " + page);
    }

    public void addBuildActionListener(ActionListener l) { btnBuild.addActionListener(l); }
    public void addSearchActionListener(ActionListener l) { btnSearch.addActionListener(l); }
    public void addScanActionListener(ActionListener l) { btnScan.addActionListener(l); }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HashIndexUI ui = new HashIndexUI();
            ui.setVisible(true);

            ui.setNbBuckets(0);
            ui.setPages(0);
            ui.setTotalTuples(0);
            ui.setCollisions(0);
            ui.setOverflows(0);
            ui.setCollisionRate(0.0);
            ui.setOverflowRate(0.0);
            ui.setSearchCost(0);
            ui.setScanCost(0);
            ui.setFoundPage(null);

            new HashIndexController(ui);
        });
    }
}