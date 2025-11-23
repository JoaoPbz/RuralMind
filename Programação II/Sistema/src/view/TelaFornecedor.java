package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import dao.FornecedorDAO;
import model.Fornecedor;
import util.LimiteCaracteres;

/**
 * Tela usada para gerenciar os fornecedores do sistema.
 * Aqui da pra cadastrar, listar, atualizar e excluir fornecedores.
 * A parte de salvar e buscar os dados é feita pelo FornecedorDAO.
 */
public class TelaFornecedor extends JFrame {

    private JTextField txtNome, txtEmail, txtEndereco, txtNumero, txtCidade;
    private JFormattedTextField txtCnpj, txtTelefone;
    private JComboBox<String> cbEstado;
    private JTable tabela;
    private DefaultTableModel modelo;
    private FornecedorDAO dao = new FornecedorDAO();

    /**
     * Construtor principal da tela de fornecedor.
     * Inicializa os componentes e deixa a interface pronta pra uso.
     */
    public TelaFornecedor() {
        setTitle("Cadastro de Fornecedores");
        setSize(720, 560);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        configurarCampos();
        configurarBotoes();
        configurarTabela();
    }

    /**
     * Configura os campos e aplica mascaras e limites.
     */
    private void configurarCampos() {
        MaskFormatter mascaraCnpj = null;
        MaskFormatter mascaraTelefone = null;

        try {
            mascaraCnpj = new MaskFormatter("##.###.###/####-##");
            mascaraCnpj.setPlaceholderCharacter('_');

            mascaraTelefone = new MaskFormatter("(##) #####-####");
            mascaraTelefone.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(120, 30, 200, 25);
        txtNome.setDocument(new LimiteCaracteres(60));
        add(txtNome);

        JLabel lblCnpj = new JLabel("CNPJ:");
        lblCnpj.setBounds(30, 70, 100, 25);
        add(lblCnpj);

        txtCnpj = new JFormattedTextField(mascaraCnpj);
        txtCnpj.setBounds(120, 70, 200, 25);
        add(txtCnpj);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 110, 100, 25);
        add(lblTelefone);

        txtTelefone = new JFormattedTextField(mascaraTelefone);
        txtTelefone.setBounds(120, 110, 200, 25);
        add(txtTelefone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 150, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(120, 150, 200, 25);
        txtEmail.setDocument(new LimiteCaracteres(80));
        add(txtEmail);

        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setBounds(30, 190, 100, 25);
        add(lblEndereco);

        txtEndereco = new JTextField();
        txtEndereco.setBounds(120, 190, 200, 25);
        txtEndereco.setDocument(new LimiteCaracteres(100));
        add(txtEndereco);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(340, 190, 80, 25);
        add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setBounds(420, 190, 80, 25);
        txtNumero.setDocument(new LimiteCaracteres(10));
        add(txtNumero);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(30, 230, 100, 25);
        add(lblCidade);

        txtCidade = new JTextField();
        txtCidade.setBounds(120, 230, 200, 25);
        txtCidade.setDocument(new LimiteCaracteres(60));
        add(txtCidade);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 270, 100, 25);
        add(lblEstado);

        cbEstado = new JComboBox<>(new String[]{
                "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
                "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
                "RS","RO","RR","SC","SP","SE","TO"
        });
        cbEstado.setBounds(120, 270, 80, 25);
        add(cbEstado);
    }

    /**
     * Adiciona os botoes principais.
     */
    private void configurarBotoes() {

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(550, 30, 120, 30);
        add(btnSalvar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(550, 70, 120, 30);
        add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(550, 110, 120, 30);
        add(btnAtualizar);

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(550, 150, 120, 30);
        add(btnListar);

        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> atualizar());
        btnListar.addActionListener(e -> listar());
    }

    /**
     * Tabela usada pra mostrar os fornecedores cadastrados.
     */
    private void configurarTabela() {

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nome", "CNPJ", "Telefone", "Email", "Endereço", "Cidade", "Estado"}, 
                0
        );

        tabela = new JTable(modelo);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 320, 1200, 400);
        add(scroll);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int linha = tabela.getSelectedRow();
                if (linha != -1) {

                    txtNome.setText(modelo.getValueAt(linha, 1).toString());
                    txtCnpj.setText(modelo.getValueAt(linha, 2).toString());
                    txtTelefone.setText(modelo.getValueAt(linha, 3).toString());
                    txtEmail.setText(modelo.getValueAt(linha, 4).toString());
                    txtEndereco.setText(modelo.getValueAt(linha, 5).toString());
                    txtCidade.setText(modelo.getValueAt(linha, 6).toString());
                    cbEstado.setSelectedItem(modelo.getValueAt(linha, 7).toString());
                }
            }
        });
    }

    /**
     * Tira os caracteres da mascara.
     */
    private String limparMascara(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    /**
     * Salva um novo fornecedor.
     * Contem validação dos campos pra evitar erro besta.
     */
    private void salvar() {

        if (txtNome.getText().trim().isEmpty()
                || limparMascara(txtCnpj.getText()).length() != 14
                || limparMascara(txtTelefone.getText()).length() < 10
                || txtEndereco.getText().trim().isEmpty()
                || txtCidade.getText().trim().isEmpty()
                || cbEstado.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(this, 
                    "Preencha todos os campos obrigatorios corretamente!");
            return;
        }

        Fornecedor f = new Fornecedor();
        f.setNome(txtNome.getText());
        f.setCnpj(limparMascara(txtCnpj.getText()));
        f.setTelefone(limparMascara(txtTelefone.getText()));
        f.setEmail(txtEmail.getText());

        String enderecoCompleto = txtEndereco.getText().trim();
        if (!txtNumero.getText().trim().isEmpty()) {
            enderecoCompleto += ", nº " + txtNumero.getText().trim();
        }

        f.setEndereco(enderecoCompleto);
        f.setCidade(txtCidade.getText());
        f.setEstado(cbEstado.getSelectedItem().toString());

        if (dao.inserir(f)) {
            JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
            limparCampos();
            listar();
        }
    }

    /**
     * Exclui o fornecedor selecionado.
     * Caso esteja vinculado a alguma maquina, não deixa excluir.
     */
    private void excluir() {

        int linha = tabela.getSelectedRow();

        if (linha != -1) {

            int id = (int) tabela.getValueAt(linha, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este fornecedor?",
                    "Confirmacao",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                try {
                    dao.excluir(id);
                    JOptionPane.showMessageDialog(this, "Fornecedor excluido com sucesso!");
                    listar();

                } catch (Exception ex) {

                    if (ex.getMessage() != null && ex.getMessage().contains("foreign key")) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Este fornecedor não pode ser excluido pois esta vinculado a uma maquina ou venda.",
                                "Operacao não permitida",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela.");
        }
    }

    /**
     * Atualiza os dados do fornecedor selecionado.
     */
    private void atualizar() {

        int linha = tabela.getSelectedRow();

        if (linha != -1) {

            if (txtNome.getText().trim().isEmpty()
                    || limparMascara(txtCnpj.getText()).length() != 14
                    || limparMascara(txtTelefone.getText()).length() < 10
                    || txtEndereco.getText().trim().isEmpty()
                    || txtCidade.getText().trim().isEmpty()
                    || cbEstado.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos obrigatorios antes de atualizar!");
                return;
            }

            Fornecedor f = new Fornecedor();
            f.setId((int) tabela.getValueAt(linha, 0));
            f.setNome(txtNome.getText());
            f.setCnpj(limparMascara(txtCnpj.getText()));
            f.setTelefone(limparMascara(txtTelefone.getText()));
            f.setEmail(txtEmail.getText());

            String enderecoCompleto = txtEndereco.getText().trim();
            if (!txtNumero.getText().trim().isEmpty()) {
                enderecoCompleto += ", nº " + txtNumero.getText().trim();
            }

            f.setEndereco(enderecoCompleto);
            f.setCidade(txtCidade.getText());
            f.setEstado(cbEstado.getSelectedItem().toString());

            dao.atualizar(f);
            JOptionPane.showMessageDialog(this, "Fornecedor atualizado!");
            listar();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para atualizar.");
        }
    }

    /**
     * Lista todos os fornecedores cadastrados e coloca na tabela.
     */
    private void listar() {

        modelo.setRowCount(0);

        List<Fornecedor> lista = dao.listar();

        for (Fornecedor f : lista) {

            String cnpj = f.getCnpj()
                    .replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");

            String telefone = f.getTelefone()
                    .replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");

            modelo.addRow(new Object[]{
                    f.getId(), f.getNome(), cnpj, telefone,
                    f.getEmail(), f.getEndereco(), f.getCidade(), f.getEstado()
            });
        }
    }

    /**
     * Limpa todos os campos da tela.
     */
    private void limparCampos() {
        txtNome.setText("");
        txtCnpj.setValue(null);
        txtTelefone.setValue(null);
        txtEmail.setText("");
        txtEndereco.setText("");
        txtNumero.setText("");
        txtCidade.setText("");
        cbEstado.setSelectedIndex(0);
    }

    /**
     * Permite abrir a tela separadamente.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaFornecedor().setVisible(true));
    }
}
