package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.RelatorioVendaDAO;
import model.Venda;

/**
 * Tela usada para gerar e visualizar relatórios de vendas.
 *
 * Permite filtrar por datas, cliente e vendedor e mostra também
 * os totais de lucro e faturamento do período. A ideia é dar uma
 * visão geral das vendas que já foram realizadas.
 */
public class TelaRelatorioVendas extends JFrame {

    private JTable tabela;
    private JTextField txtCliente, txtVendedor;
    private JSpinner spInicio, spFim;
    private JButton btnFiltrar, btnFechar, btnDetalhes;
    private JLabel lblTotal, lblLucroTotal;

    private final RelatorioVendaDAO dao = new RelatorioVendaDAO();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private List<Venda> vendas;

    /**
     * Construtor que inicia a tela de relatórios.
     */
    public TelaRelatorioVendas() {
        setTitle("Relatório de Vendas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        configurarFiltros();
        configurarTabela();
        configurarRodape();
        configurarEventos();

        carregarTabela();
    }

    /**
     * Configura os filtros (datas, cliente, vendedor etc.).
     */
    private void configurarFiltros() {
        JLabel lblInicio = new JLabel("Data início:");
        lblInicio.setBounds(40, 40, 90, 25);
        add(lblInicio);

        spInicio = new JSpinner(new SpinnerDateModel());
        spInicio.setEditor(new JSpinner.DateEditor(spInicio, "dd/MM/yyyy"));
        spInicio.setValue(Date.from(LocalDate.now().minusMonths(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spInicio.setBounds(120, 40, 130, 25);
        add(spInicio);

        JLabel lblFim = new JLabel("Data fim:");
        lblFim.setBounds(270, 40, 70, 25);
        add(lblFim);

        spFim = new JSpinner(new SpinnerDateModel());
        spFim.setEditor(new JSpinner.DateEditor(spFim, "dd/MM/yyyy"));
        spFim.setValue(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spFim.setBounds(340, 40, 130, 25);
        add(spFim);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setBounds(500, 40, 70, 25);
        add(lblCliente);

        txtCliente = new JTextField();
        txtCliente.setBounds(560, 40, 140, 25);
        add(txtCliente);

        JLabel lblVendedor = new JLabel("Vendedor:");
        lblVendedor.setBounds(720, 40, 80, 25);
        add(lblVendedor);

        txtVendedor = new JTextField();
        txtVendedor.setBounds(800, 40, 140, 25);
        add(txtVendedor);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(970, 35, 100, 35);
        add(btnFiltrar);

        btnFechar = new JButton("Fechar");
        btnFechar.setBounds(1080, 35, 100, 35);
        add(btnFechar);
    }

    /**
     * Configura a tabela onde as vendas filtradas são exibidas.
     */
    private void configurarTabela() {
        String[] colunas = {"ID", "Data", "Cliente", "Vendedor",
                "Faturamento (R$)", "Lucro Líquido (R$)"};

        tabela = new JTable(new DefaultTableModel(colunas, 0));
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.setRowHeight(24);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(40, 100, 1220, 500);
        add(scroll);

        btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.setBounds(40, 620, 150, 35);
        add(btnDetalhes);
    }

    /**
     * Configura a parte inferior da tela com os totais.
     */
    private void configurarRodape() {
        lblTotal = new JLabel("Faturamento: R$ 0,00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(new Color(0, 102, 0));
        lblTotal.setBounds(780, 620, 220, 30);
        add(lblTotal);

        lblLucroTotal = new JLabel("Lucro líquido: R$ 0,00", SwingConstants.RIGHT);
        lblLucroTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblLucroTotal.setForeground(new Color(0, 80, 180));
        lblLucroTotal.setBounds(1020, 620, 220, 30);
        add(lblLucroTotal);
    }

    /**
     * Registra os eventos dos botões e da tabela.
     */
    private void configurarEventos() {
        btnFiltrar.addActionListener(e -> carregarTabela());
        btnFechar.addActionListener(e -> dispose());
        btnDetalhes.addActionListener(e -> abrirDetalhes());

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() != -1) {
                    abrirDetalhes();
                }
            }
        });
    }

    /**
     * Carrega as vendas do banco de dados conforme os filtros escolhidos.
     */
    private void carregarTabela() {
        try {
            Date dataInicio = (Date) spInicio.getValue();
            Date dataFim = (Date) spFim.getValue();

            LocalDate inicio = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fim = dataFim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            vendas = dao.listarVendas(inicio, fim, txtCliente.getText(), txtVendedor.getText());
            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0);

            double totalFaturamento = 0;
            double totalLucro = 0;

            for (Venda v : vendas) {
                double faturamento = v.getValorTotal();
                double lucro = faturamento - v.getCustoTotal();

                totalFaturamento += faturamento;
                totalLucro += lucro;

                model.addRow(new Object[]{
                        v.getId(),
                        v.getDataVenda(),
                        v.getNomeCliente(),
                        v.getNomeUsuario(),
                        df.format(faturamento),
                        df.format(lucro)
                });
            }

            lblTotal.setText("Faturamento: R$ " + df.format(totalFaturamento));
            lblLucroTotal.setText("Lucro líquido: R$ " + df.format(totalLucro));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Abre a janela com os detalhes da venda selecionada.
     */
    private void abrirDetalhes() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma venda primeiro.");
            return;
        }

        Venda v = vendas.get(linha);
        new DetalheVenda(this, v, df).setVisible(true);
    }

    /**
     * Main para testes.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaRelatorioVendas().setVisible(true));
    }
}
