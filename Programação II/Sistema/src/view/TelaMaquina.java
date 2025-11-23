package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.FornecedorDAO;
import dao.MaquinaDAO;
import model.Fornecedor;
import model.Maquina;
import util.LimiteCaracteres;

/**
 * Tela de cadastro e gerenciamento de máquinas no RuralMind.
 *
 * Permite inserir, listar, excluir e atualizar máquinas.
 */
public class TelaMaquina extends JFrame {

    private JTextField txtModelo, txtMarca, txtAno, txtPrecoCusto, txtPrecoVenda, txtQuantia;
    private JComboBox<String> cbNovaUsada, cbFornecedor;
    private JTable tabela;
    private DefaultTableModel modelo;
    private MaquinaDAO dao = new MaquinaDAO();
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public TelaMaquina() {
        setTitle("Cadastro de Máquinas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        configurarCampos();
        configurarBotoes();
        configurarTabela();
    }

    private void configurarCampos() {

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(30, 30, 120, 25);
        add(lblModelo);

        txtModelo = new JTextField();
        txtModelo.setBounds(160, 30, 250, 25);
        txtModelo.setDocument(new LimiteCaracteres(80));
        add(txtModelo);

        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(30, 70, 120, 25);
        add(lblMarca);

        txtMarca = new JTextField();
        txtMarca.setBounds(160, 70, 250, 25);
        txtMarca.setDocument(new LimiteCaracteres(40));
        add(txtMarca);

        JLabel lblAno = new JLabel("Ano:");
        lblAno.setBounds(30, 110, 120, 25);
        add(lblAno);

        txtAno = new JTextField();
        txtAno.setBounds(160, 110, 100, 25);
        txtAno.setDocument(new LimiteCaracteres(4));
        txtAno.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()))
                    e.consume();
            }
        });
        add(txtAno);

        JLabel lblNovaUsada = new JLabel("Condição:");
        lblNovaUsada.setBounds(30, 150, 120, 25);
        add(lblNovaUsada);

        cbNovaUsada = new JComboBox<>(new String[]{"nova", "usada"});
        cbNovaUsada.setBounds(160, 150, 120, 25);
        add(cbNovaUsada);

        JLabel lblFornecedor = new JLabel("Fornecedor:");
        lblFornecedor.setBounds(30, 190, 120, 25);
        add(lblFornecedor);

        cbFornecedor = new JComboBox<>();
        cbFornecedor.setBounds(160, 190, 250, 25);
        add(cbFornecedor);
        carregarFornecedores();

        JLabel lblPrecoCusto = new JLabel("Preço de Custo (R$):");
        lblPrecoCusto.setBounds(30, 230, 150, 25);
        add(lblPrecoCusto);

        txtPrecoCusto = new JTextField();
        txtPrecoCusto.setBounds(180, 230, 120, 25);
        add(txtPrecoCusto);

        JLabel lblPrecoVenda = new JLabel("Preço de Venda (R$):");
        lblPrecoVenda.setBounds(320, 230, 160, 25);
        add(lblPrecoVenda);

        txtPrecoVenda = new JTextField();
        txtPrecoVenda.setBounds(470, 230, 120, 25);
        add(txtPrecoVenda);

        JLabel lblQuantia = new JLabel("Quantia:");
        lblQuantia.setBounds(30, 270, 160, 25);
        add(lblQuantia);

        txtQuantia = new JTextField();
        txtQuantia.setBounds(190, 270, 100, 25);
        txtQuantia.setDocument(new LimiteCaracteres(3));
        txtQuantia.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()))
                    e.consume();
            }
        });
        add(txtQuantia);
    }

    private void configurarBotoes() {

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(700, 30, 120, 30);
        add(btnSalvar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(700, 70, 120, 30);
        add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(700, 110, 120, 30);
        add(btnAtualizar);

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(700, 150, 120, 30);
        add(btnListar);

        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> atualizar());
        btnListar.addActionListener(e -> listar());
    }

    private void configurarTabela() {
        modelo = new DefaultTableModel(
                new String[]{"ID", "Modelo", "Marca", "Ano", "Condição", "Fornecedor",
                        "Preço Custo", "Preço Venda", "Quantia", "Status"}, 0
        );

        tabela = new JTable(modelo);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 320, 1200, 400);
        add(scroll);

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int linha = tabela.getSelectedRow();
                if (linha != -1) {

                    txtModelo.setText(tabela.getValueAt(linha, 1).toString());
                    txtMarca.setText(tabela.getValueAt(linha, 2).toString());
                    txtAno.setText(tabela.getValueAt(linha, 3).toString());
                    cbNovaUsada.setSelectedItem(tabela.getValueAt(linha, 4).toString());

                    txtPrecoCusto.setText(
                            tabela.getValueAt(linha, 6).toString().replace("R$ ", "")
                    );

                    txtPrecoVenda.setText(
                            tabela.getValueAt(linha, 7).toString().replace("R$ ", "")
                    );

                    txtQuantia.setText(tabela.getValueAt(linha, 8).toString());

                    String idFornecedor = tabela.getValueAt(linha, 5).toString();
                    for (int i = 0; i < cbFornecedor.getItemCount(); i++) {
                        if (cbFornecedor.getItemAt(i).startsWith(idFornecedor + " -")) {
                            cbFornecedor.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void carregarFornecedores() {
        cbFornecedor.removeAllItems();
        for (Fornecedor f : fornecedorDAO.listar()) {
            cbFornecedor.addItem(f.getId() + " - " + f.getNome());
        }
    }

    /**
     * Parser para preços em formato BR.
     */
    private double parsePreco(String texto) {
        try {
            texto = texto.replace("R$", "").trim();
            texto = texto.replace(".", "").replace(",", ".");
            return Double.parseDouble(texto);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void salvar() {
        try {
            if (txtModelo.getText().isEmpty() || txtMarca.getText().isEmpty()
                    || txtAno.getText().isEmpty() || txtPrecoCusto.getText().isEmpty()
                    || txtPrecoVenda.getText().isEmpty() || txtQuantia.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
                return;
            }

            Maquina m = new Maquina();
            m.setModelo(txtModelo.getText());
            m.setMarca(txtMarca.getText());
            m.setAno(Integer.parseInt(txtAno.getText()));
            m.setNovaUsada(cbNovaUsada.getSelectedItem().toString().toLowerCase());
            m.setPrecoCusto(parsePreco(txtPrecoCusto.getText()));
            m.setPrecoVenda(parsePreco(txtPrecoVenda.getText()));
            m.setQuantia(Integer.parseInt(txtQuantia.getText()));
            m.setStatus(m.getQuantia() > 0 ? "disponivel" : "fora_de_estoque");
            m.setIdFornecedor(Integer.parseInt(cbFornecedor.getSelectedItem().toString().split(" - ")[0]));

            dao.inserir(m);
            JOptionPane.showMessageDialog(this, "Máquina cadastrada com sucesso!");
            limparCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();

        if (linha != -1) {
            int id = (int) tabela.getValueAt(linha, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir esta máquina?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dao.excluir(id);
                listar();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma máquina na tabela.");
        }
    }

    private void atualizar() {
        int linha = tabela.getSelectedRow();
        if (linha != -1) {

            try {
                Maquina m = new Maquina();
                m.setId((int) tabela.getValueAt(linha, 0));
                m.setModelo(txtModelo.getText());
                m.setMarca(txtMarca.getText());
                m.setAno(Integer.parseInt(txtAno.getText()));
                m.setNovaUsada(cbNovaUsada.getSelectedItem().toString().toLowerCase());
                m.setPrecoCusto(parsePreco(txtPrecoCusto.getText()));
                m.setPrecoVenda(parsePreco(txtPrecoVenda.getText()));
                m.setQuantia(Integer.parseInt(txtQuantia.getText()));
                m.setStatus(m.getQuantia() > 0 ? "disponivel" : "fora_de_estoque");
                m.setIdFornecedor(Integer.parseInt(cbFornecedor.getSelectedItem().toString().split(" - ")[0]));

                dao.atualizar(m);
                listar();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma máquina para atualizar.");
        }
    }

    private void listar() {
        modelo.setRowCount(0);

        List<Maquina> lista = dao.listar();
        for (Maquina m : lista) {
            modelo.addRow(new Object[]{
                    m.getId(),
                    m.getModelo(),
                    m.getMarca(),
                    m.getAno(),
                    m.getNovaUsada(),
                    m.getFornecedor(),
                    "R$ " + df.format(m.getPrecoCusto()),
                    "R$ " + df.format(m.getPrecoVenda()),
                    m.getQuantia(),
                    m.getStatus()
            });
        }
    }

    private void limparCampos() {
        txtModelo.setText("");
        txtMarca.setText("");
        txtAno.setText("");
        cbNovaUsada.setSelectedIndex(0);
        txtPrecoCusto.setText("");
        txtPrecoVenda.setText("");
        txtQuantia.setText("");

        if (cbFornecedor.getItemCount() > 0)
            cbFornecedor.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaMaquina().setVisible(true));
    }
}
