package view;

import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.ClienteDAO;
import dao.MaquinaDAO;
import dao.VendaDAO;
import model.Cliente;
import model.Maquina;
import model.Usuario;
import model.Venda;

/**
 * Tela usada para registrar as vendas no sistema.
 *
 * Aqui o usuário seleciona o cliente, escolhe as máquinas disponíveis,
 * define quantidades, observa o total e confirma a venda. Também é feito
 * o controle básico do estoque para evitar erros.
 */
public class TelaVenda extends JFrame {

    private JComboBox<String> cbCliente, cbMaquina;
    private JTextField txtPrecoUnitario, txtQuantidade, txtValorTotal, txtVendedor, txtEstoque;
    private JTextArea txtObservacao;
    private JButton btnAdicionar, btnRemover, btnRegistrar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    private final VendaDAO vendaDAO = new VendaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MaquinaDAO maquinaDAO = new MaquinaDAO();
    private final Usuario usuarioLogado;

    private final List<Maquina> maquinasSelecionadas = new ArrayList<>();
    private final List<Integer> quantidadesSelecionadas = new ArrayList<>();

    /** Listas adicionadas para enviar o histórico ao VendaDAO */
    private final List<Double> precosUnitariosSelecionados = new ArrayList<>();
    private final List<Double> subtotaisSelecionados = new ArrayList<>();

    private Maquina maquinaSelecionada;

    public TelaVenda(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Registro de Vendas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        configurarCampos();
        configurarTabela();
        configurarBotoes();
        carregarCombos();
        configurarEventos();
    }

    /** Configuração dos campos da tela */
    private void configurarCampos() {
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setBounds(60, 60, 120, 25);
        add(lblCliente);

        cbCliente = new JComboBox<>();
        cbCliente.setBounds(180, 60, 300, 25);
        add(cbCliente);

        JLabel lblVendedor = new JLabel("Vendedor:");
        lblVendedor.setBounds(60, 100, 120, 25);
        add(lblVendedor);

        txtVendedor = new JTextField(usuarioLogado.getNome());
        txtVendedor.setBounds(180, 100, 300, 25);
        txtVendedor.setEditable(false);
        add(txtVendedor);

        JLabel lblMaquina = new JLabel("Máquina:");
        lblMaquina.setBounds(60, 140, 120, 25);
        add(lblMaquina);

        cbMaquina = new JComboBox<>();
        cbMaquina.setBounds(180, 140, 350, 25);
        add(cbMaquina);

        JLabel lblEstoque = new JLabel("Estoque disponível:");
        lblEstoque.setBounds(550, 140, 150, 25);
        add(lblEstoque);

        txtEstoque = new JTextField();
        txtEstoque.setBounds(690, 140, 60, 25);
        txtEstoque.setEditable(false);
        add(txtEstoque);

        JLabel lblPrecoUnitario = new JLabel("Preço unitário (R$):");
        lblPrecoUnitario.setBounds(60, 180, 150, 25);
        add(lblPrecoUnitario);

        txtPrecoUnitario = new JTextField();
        txtPrecoUnitario.setBounds(200, 180, 100, 25);
        txtPrecoUnitario.setEditable(false);
        add(txtPrecoUnitario);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setBounds(320, 180, 100, 25);
        add(lblQuantidade);

        txtQuantidade = new JTextField();
        txtQuantidade.setBounds(410, 180, 60, 25);
        add(txtQuantidade);

        JLabel lblValorTotal = new JLabel("Valor total (R$):");
        lblValorTotal.setBounds(60, 230, 120, 25);
        add(lblValorTotal);

        txtValorTotal = new JTextField("0.00");
        txtValorTotal.setBounds(180, 230, 120, 25);
        txtValorTotal.setEditable(false);
        add(txtValorTotal);

        JLabel lblObs = new JLabel("Observação:");
        lblObs.setBounds(60, 270, 120, 25);
        add(lblObs);

        txtObservacao = new JTextArea();
        JScrollPane scrollObs = new JScrollPane(txtObservacao);
        scrollObs.setBounds(180, 270, 400, 80);
        add(scrollObs);
    }

    /** Tabela de máquinas adicionadas */
    private void configurarTabela() {
        modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Modelo", "Qtd", "Preço (R$)", "Subtotal (R$)"}, 0
        );

        tabelaItens = new JTable(modeloTabela);
        tabelaItens.setRowHeight(24);
        tabelaItens.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setBounds(60, 370, 820, 250);
        add(scrollTabela);
    }

    /** Botões principais */
    private void configurarBotoes() {
        btnAdicionar = new JButton("Adicionar Máquina");
        btnAdicionar.setBounds(500, 180, 180, 25);
        add(btnAdicionar);

        btnRemover = new JButton("Remover Selecionada");
        btnRemover.setBounds(700, 180, 180, 25);
        add(btnRemover);

        btnRegistrar = new JButton("Registrar Venda");
        btnRegistrar.setBounds(350, 650, 200, 40);
        add(btnRegistrar);
    }

    /** Carrega combos de cliente e máquina */
    private void carregarCombos() {
        try {
            cbCliente.removeAllItems();
            for (Cliente c : clienteDAO.listar()) {
                cbCliente.addItem(c.getId() + " - " + c.getNome());
            }

            cbMaquina.removeAllItems();
            for (Maquina m : maquinaDAO.listar()) {
                if (m.getStatus() != null && m.getStatus().toLowerCase().contains("dispon")
                        && m.getQuantia() > 0) {
                    cbMaquina.addItem(m.getId() + " - " + m.getModelo() + " (" + m.getNovaUsada() + ")");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados: " + e.getMessage());
        }
    }

    /** Eventos principais da tela */
    private void configurarEventos() {
        cbMaquina.addActionListener(e -> atualizarPrecoMaquina());
        btnAdicionar.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> removerItem());
        btnRegistrar.addActionListener(e -> registrarVenda());
    }

    /** Atualiza preço e estoque ao trocar a máquina */
    private void atualizarPrecoMaquina() {
        if (cbMaquina.getSelectedItem() == null) {
            txtPrecoUnitario.setText("");
            txtEstoque.setText("");
            maquinaSelecionada = null;
            return;
        }

        try {
            int idMaquina = Integer.parseInt(
                cbMaquina.getSelectedItem().toString().split(" - ")[0]
            );

            for (Maquina m : maquinaDAO.listar()) {
                if (m.getId() == idMaquina) {
                    maquinaSelecionada = m;
                    txtPrecoUnitario.setText(String.format("%.2f", m.getPrecoVenda()));
                    txtEstoque.setText(String.valueOf(m.getQuantia()));
                    break;
                }
            }
        } catch (Exception e) {
            maquinaSelecionada = null;
        }
    }

    /**
     * Adiciona item à venda e registra preço unitário + subtotal
     */
    private void adicionarItem() {
        if (maquinaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma máquina.");
            return;
        }

        for (Maquina m : maquinasSelecionadas) {
            if (m.getId() == maquinaSelecionada.getId()) {
                JOptionPane.showMessageDialog(this,
                        "Essa máquina já foi adicionada.");
                return;
            }
        }

        try {
            int qtd = Integer.parseInt(txtQuantidade.getText());

            if (qtd <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida.");
                return;
            }

            if (qtd > maquinaSelecionada.getQuantia()) {
                JOptionPane.showMessageDialog(this, "Estoque insuficiente.");
                return;
            }

            double preco = maquinaSelecionada.getPrecoVenda();
            double subtotal = preco * qtd;

            /** salva para enviar ao DAO */
            precosUnitariosSelecionados.add(preco);
            subtotaisSelecionados.add(subtotal);

            maquinasSelecionadas.add(maquinaSelecionada);
            quantidadesSelecionadas.add(qtd);

            modeloTabela.addRow(new Object[]{
                maquinaSelecionada.getId(),
                maquinaSelecionada.getModelo(),
                qtd,
                String.format("%.2f", preco),
                String.format("%.2f", subtotal)
            });

            atualizarValorTotal();
            limparSelecaoMaquina();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Digite uma quantidade válida.");
        }
    }

    /** Remove item da venda e das listas históricas */
    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma máquina para remover.");
            return;
        }

        maquinasSelecionadas.remove(linha);
        quantidadesSelecionadas.remove(linha);
        precosUnitariosSelecionados.remove(linha);
        subtotaisSelecionados.remove(linha);

        modeloTabela.removeRow(linha);

        atualizarValorTotal();
    }

    /** Atualiza total geral da venda */
    private void atualizarValorTotal() {
        double total = 0;

        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            total += Double.parseDouble(
                modeloTabela.getValueAt(i, 4).toString().replace(",", ".")
            );
        }

        txtValorTotal.setText(String.format("%.2f", total));
    }

    /**
     * Salva a venda e envia dados completos ao DAO
     */
    private void registrarVenda() {
        try {
            if (cbCliente.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente.");
                return;
            }

            if (modeloTabela.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Adicione ao menos uma máquina.");
                return;
            }

            double totalVenda =
                    Double.parseDouble(txtValorTotal.getText().replace(",", "."));

            Venda v = new Venda();
            v.setIdCliente(Integer.parseInt(
                    cbCliente.getSelectedItem().toString().split(" - ")[0]));
            v.setIdUsuario(usuarioLogado.getId());
            v.setDataVenda(LocalDate.now());
            v.setValorTotal(totalVenda);
            v.setObservacao(txtObservacao.getText());

            /** aqui agora passa os valores históricos */
            vendaDAO.registrarVenda(
                    v,
                    maquinasSelecionadas,
                    quantidadesSelecionadas,
                    precosUnitariosSelecionados,
                    subtotaisSelecionados
            );

            JOptionPane.showMessageDialog(this,
                    "Venda registrada com sucesso.\n\n" +
                    "Itens vendidos: " + maquinasSelecionadas.size() +
                    "\nValor total: R$ " + String.format("%.2f", totalVenda));

            limparCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao registrar venda: " + ex.getMessage());
        }
    }

    /** Limpa os campos da máquina atual */
    private void limparSelecaoMaquina() {
        txtQuantidade.setText("");
        txtPrecoUnitario.setText("");
        txtEstoque.setText("");
        cbMaquina.setSelectedIndex(-1);
        maquinaSelecionada = null;
    }

    /** Reseta toda a tela após concluir venda */
    private void limparCampos() {
        cbCliente.setSelectedIndex(-1);
        cbMaquina.setSelectedIndex(-1);
        txtPrecoUnitario.setText("");
        txtQuantidade.setText("");
        txtEstoque.setText("");
        txtValorTotal.setText("0.00");
        txtObservacao.setText("");
        modeloTabela.setRowCount(0);

        maquinasSelecionadas.clear();
        quantidadesSelecionadas.clear();
        precosUnitariosSelecionados.clear();
        subtotaisSelecionados.clear();

        carregarCombos();
    }

    public static void main(String[] args) {
        Usuario u = new Usuario();
        u.setId(1);
        u.setNome("Administrador");
        u.setTipo("admin");
        SwingUtilities.invokeLater(() -> new TelaVenda(u).setVisible(true));
    }
}
