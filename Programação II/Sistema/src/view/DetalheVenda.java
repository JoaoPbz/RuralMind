package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dao.RelatorioVendaDAO;
import model.Maquina;
import model.Venda;

/**
 * Tela usada para apresentar os detalhes de uma venda especifica.
 * Exibe as informacoes gerais, observacoes, itens vendidos e
 * um pequeno resumo financeiro do periodo.
 */
public class DetalheVenda extends JDialog {

    /**
     * Construtor que cria a janela de detalhes.
     *
     * @param parent tela que chamou esta
     * @param venda selecionada
     * @param df formatador para valores monetarios
     */
    public DetalheVenda(JFrame parent, Venda venda, DecimalFormat df) {

        super(parent, "Detalhes da Venda #" + venda.getId(), true);
        setSize(650, 540);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Observações
        JTextArea txtObs = new JTextArea(
                (venda.getObservacao() != null && !venda.getObservacao().isBlank())
                        ? venda.getObservacao()
                        : "Nenhuma observacao registrada."
        );
        txtObs.setEditable(false);
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        txtObs.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollObs = new JScrollPane(txtObs);
        scrollObs.setBorder(new TitledBorder("Observacoes da Venda"));
        scrollObs.setPreferredSize(new Dimension(600, 90));
        painel.add(scrollObs);

        // Carregar itens da venda
        RelatorioVendaDAO relatorioDAO = new RelatorioVendaDAO();
        List<Maquina> itensVenda = relatorioDAO.listarMaquinasPorVenda(venda.getId());

        double faturamentoTotal = 0;
        double custoTotal = 0;

        if (itensVenda.isEmpty()) {
            painel.add(Box.createVerticalStrut(10));
            painel.add(new JLabel("Nenhum item registrado nessa venda."));
        } else {

            String[] colunas = {"Modelo", "Marca", "Fornecedor", "Qtd", "Preço (R$)", "Subtotal (R$)"};

            Object[][] dados = new Object[itensVenda.size()][colunas.length];

            for (int i = 0; i < itensVenda.size(); i++) {

                Maquina m = itensVenda.get(i);

                // valores históricos mantidos pela venda
                double precoUnit = m.getPrecoUnitarioHistorico();
                double subtotal = m.getSubtotalHistorico();
                double custo = m.getPrecoCusto() * m.getQuantia();

                faturamentoTotal += subtotal;
                custoTotal += custo;

                dados[i][0] = m.getModelo();
                dados[i][1] = m.getMarca();
                dados[i][2] = m.getFornecedor();
                dados[i][3] = m.getQuantia();
                dados[i][4] = df.format(precoUnit);
                dados[i][5] = df.format(subtotal);
            }

            JTable tabela = new JTable(dados, colunas);
            tabela.setEnabled(false);
            tabela.setRowHeight(22);

            JScrollPane scrollTabela = new JScrollPane(tabela);
            scrollTabela.setBorder(new TitledBorder("Itens da Venda"));
            scrollTabela.setPreferredSize(new Dimension(600, 210));

            painel.add(Box.createVerticalStrut(10));
            painel.add(scrollTabela);
        }

        // Cálculos finais
        double lucroLiquido = faturamentoTotal - custoTotal;
        double margem = (custoTotal > 0) ? (lucroLiquido / custoTotal) * 100 : 0;

        painel.add(Box.createVerticalStrut(10));

        JLabel lblFat = new JLabel("Faturamento total: R$ " + df.format(faturamentoTotal));
        lblFat.setFont(new Font("Arial", Font.BOLD, 14));
        lblFat.setForeground(new Color(0, 100, 0));

        JLabel lblCusto = new JLabel("Custo total: R$ " + df.format(custoTotal));
        lblCusto.setFont(new Font("Arial", Font.BOLD, 14));
        lblCusto.setForeground(new Color(150, 50, 50));

        JLabel lblLucro = new JLabel("Lucro liquido: R$ " + df.format(lucroLiquido)
                + "  (" + df.format(margem) + "%)");
        lblLucro.setFont(new Font("Arial", Font.BOLD, 15));
        lblLucro.setForeground(lucroLiquido >= 0 ? new Color(0, 60, 170) : Color.RED);

        JPanel painelTotais = new JPanel(new GridLayout(3, 1));
        painelTotais.setBorder(new TitledBorder("Resumo Financeiro"));
        painelTotais.add(lblFat);
        painelTotais.add(lblCusto);
        painelTotais.add(lblLucro);

        painel.add(painelTotais);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        JPanel rodape = new JPanel();
        rodape.add(btnFechar);

        add(painel, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }
}
