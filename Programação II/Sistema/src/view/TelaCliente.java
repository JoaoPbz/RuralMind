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

import dao.ClienteDAO;
import model.Cliente;
import util.LimiteCaracteres;

/**
 * Tela usada para gerenciar os clientes do sistema.
 * Aqui da pra cadastrar, listar, atualizar e excluir clientes.
 * A comunicação com o banco acontece pelo ClienteDAO.
 */
public class TelaCliente extends JFrame {

    private JTextField txtNome, txtEmail, txtEndereco, txtNumero, txtCidade;
    private JFormattedTextField txtCpf, txtTelefone;
    private JComboBox<String> cbEstado;
    private JTable tabela;
    private DefaultTableModel modelo;
    private ClienteDAO dao = new ClienteDAO();

    /**
     * Construtor principal da tela.
     * Prepara os componentes e deixa tudo pronto pra uso.
     */
    public TelaCliente() {
        setTitle("Cadastro de Clientes");
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
     * Configura os campos de texto e aplica as mascaras.
     * Alguns limites de caracteres também são definidos aqui.
     */
    private void configurarCampos() {

        MaskFormatter mascaraCpf = null;
        MaskFormatter mascaraTelefone = null;

        try {
            mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');

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

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 70, 100, 25);
        add(lblCpf);

        txtCpf = new JFormattedTextField(mascaraCpf);
        txtCpf.setBounds(120, 70, 200, 25);
        add(txtCpf);

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
     * Adiciona e configura os botoes da tela.
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
     * Configura a tabela que mostra os clientes cadastrados.
     */
    private void configurarTabela() {

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nome", "CPF", "Telefone", "Email", "Endereço", "Cidade", "Estado"}, 
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
                    txtCpf.setText(modelo.getValueAt(linha, 2).toString());
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
     * Tira os caracteres da mascara e deixa só os numeros.
     */
    private String limparMascara(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    /**
     * Faz o cadastro de um novo cliente apos validar os campos.
     */
    private void salvar() {

        if (txtNome.getText().trim().isEmpty()
                || limparMascara(txtCpf.getText()).length() != 11
                || limparMascara(txtTelefone.getText()).length() < 10
                || txtEndereco.getText().trim().isEmpty()
                || txtCidade.getText().trim().isEmpty()
                || cbEstado.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatorios!");
            return;
        }

        Cliente c = new Cliente();
        c.setNome(txtNome.getText());
        c.setCpf(limparMascara(txtCpf.getText()));
        c.setTelefone(limparMascara(txtTelefone.getText()));
        c.setEmail(txtEmail.getText());

        String enderecoCompleto = txtEndereco.getText().trim();
        if (!txtNumero.getText().trim().isEmpty()) {
            enderecoCompleto += ", nº " + txtNumero.getText().trim();
        }

        c.setEndereco(enderecoCompleto);
        c.setCidade(txtCidade.getText());
        c.setEstado(cbEstado.getSelectedItem().toString());

        if (dao.inserir(c)) {
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            limparCampos();
            listar();
        }
    }

    /**
     * Exclui o cliente que foi selecionado na tabela.
     * Há validação para evitar excluir quem já possui venda.
     */
    private void excluir() {

        int linha = tabela.getSelectedRow();

        if (linha != -1) {

            int id = (int) tabela.getValueAt(linha, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este cliente?",
                    "Confirmacao",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                try {
                    dao.excluir(id);
                    JOptionPane.showMessageDialog(this, "Cliente excluido com sucesso!");
                    listar();

                } catch (Exception ex) {

                    if (ex.getMessage() != null && ex.getMessage().contains("foreign key")) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Este cliente nao pode ser excluido pois ja participou de uma venda.",
                                "Operacao não permitida",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.");
        }
    }

    /**
     * Atualiza os dados do cliente selecionado.
     */
    private void atualizar() {

        int linha = tabela.getSelectedRow();

        if (linha != -1) {

            if (txtNome.getText().trim().isEmpty()
                    || limparMascara(txtCpf.getText()).length() != 11
                    || limparMascara(txtTelefone.getText()).length() < 10
                    || txtEndereco.getText().trim().isEmpty()
                    || txtCidade.getText().trim().isEmpty()
                    || cbEstado.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos obrigatorios antes de atualizar!");
                return;
            }

            Cliente c = new Cliente();
            c.setId((int) tabela.getValueAt(linha, 0));
            c.setNome(txtNome.getText());
            c.setCpf(limparMascara(txtCpf.getText()));
            c.setTelefone(limparMascara(txtTelefone.getText()));
            c.setEmail(txtEmail.getText());

            String enderecoCompleto = txtEndereco.getText().trim();
            if (!txtNumero.getText().trim().isEmpty()) {
                enderecoCompleto += ", nº " + txtNumero.getText().trim();
            }

            c.setEndereco(enderecoCompleto);
            c.setCidade(txtCidade.getText());
            c.setEstado(cbEstado.getSelectedItem().toString());

            dao.atualizar(c);

            JOptionPane.showMessageDialog(this, "Cliente atualizado!");
            listar();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para atualizar.");
        }
    }

    /**
     * Carrega todos os clientes do banco e coloca eles na tabela.
     */
    private void listar() {

        modelo.setRowCount(0);

        List<Cliente> lista = dao.listar();

        for (Cliente c : lista) {

            String cpf = c.getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            String telefone = c.getTelefone()
                    .replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");

            modelo.addRow(new Object[]{
                    c.getId(), c.getNome(), cpf, telefone,
                    c.getEmail(), c.getEndereco(), c.getCidade(), c.getEstado()
            });
        }
    }

    /**
     * Limpa todos os campos da tela.
     */
    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setValue(null);
        txtTelefone.setValue(null);
        txtEmail.setText("");
        txtEndereco.setText("");
        txtNumero.setText("");
        txtCidade.setText("");
        cbEstado.setSelectedIndex(0);
    }

    /**
     * Permite abrir a tela isolada.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCliente().setVisible(true));
    }
}
