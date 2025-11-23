package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.UsuarioDAO;
import model.Usuario;
import util.ConexaoBD;
import util.LimiteCaracteres;

/**
 * Tela usada para o cadastro e controle dos usuários do sistema.
 *
 * Aqui somente administradores conseguem entrar. É possível criar
 * novos usuários, editar, listar e também excluir alguém do sistema.
 */
public class TelaUsuario extends JFrame {

    private JTextField txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbTipo;
    private JTable tabela;
    private DefaultTableModel modelo;
    private UsuarioDAO dao = new UsuarioDAO();

    /**
     * Construtor geral da tela. Apenas configura tudo que é necessário
     * para o usuário conseguir trabalhar.
     */
    public TelaUsuario() {
        setTitle("Cadastro de Usuários");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        configurarCampos();
        configurarBotoes();
        configurarTabela();
        configurarEventos();
    }

    /**
     * Configuração dos campos básicos.
     */
    private void configurarCampos() {
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(50, 60, 100, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(140, 60, 250, 25);
        txtNome.setDocument(new LimiteCaracteres(100));
        add(txtNome);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(50, 100, 100, 25);
        add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(140, 100, 250, 25);
        txtLogin.setDocument(new LimiteCaracteres(50));
        add(txtLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 140, 100, 25);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(140, 140, 250, 25);
        txtSenha.setDocument(new LimiteCaracteres(255));
        add(txtSenha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(50, 180, 100, 25);
        add(lblTipo);

        cbTipo = new JComboBox<>(new String[]{"admin", "vendedor"});
        cbTipo.setBounds(140, 180, 250, 25);
        add(cbTipo);
    }

    /**
     * Adiciona os botões (Salvar, Atualizar, Excluir etc).
     */
    private void configurarBotoes() {
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(450, 60, 120, 30);
        add(btnSalvar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(450, 100, 120, 30);
        add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(450, 140, 120, 30);
        add(btnExcluir);

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(450, 180, 120, 30);
        add(btnListar);

        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnListar.addActionListener(e -> listar());
    }

    /**
     * Configura a tabela onde são exibidos os usuários cadastrados.
     */
    private void configurarTabela() {
        modelo = new DefaultTableModel(
                new String[]{"ID", "Nome", "Login", "Tipo"}, 0
        );

        tabela = new JTable(modelo);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(50, 260, 1150, 400);
        add(scroll);
    }

    /**
     * Registra os eventos da tabela,o clique
     * que carrega os dados para edição.
     */
    private void configurarEventos() {
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    txtNome.setText(modelo.getValueAt(linha, 1).toString());
                    txtLogin.setText(modelo.getValueAt(linha, 2).toString());
                    cbTipo.setSelectedItem(modelo.getValueAt(linha, 3).toString());
                    txtSenha.setText("");
                }
            }
        });
    }

    /**
     * Salva um novo usuário no banco.
     */
    private void salvar() {
        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        String tipo = cbTipo.getSelectedItem().toString();

        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            return;
        }

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO usuario (nome, login, senha, tipo) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, nome);
            stmt.setString(2, login);
            stmt.setString(3, senha);
            stmt.setString(4, tipo);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso.");
            limparCampos();
            listar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar usuário: " + ex.getMessage());
        }
    }

    /**
     * Atualiza o usuário selecionado.
     */
    private void atualizar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário primeiro.");
            return;
        }

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE usuario SET nome=?, login=?, senha=?, tipo=? WHERE id_usuario=?")) {

            stmt.setString(1, txtNome.getText());
            stmt.setString(2, txtLogin.getText());
            stmt.setString(3, new String(txtSenha.getPassword()));
            stmt.setString(4, cbTipo.getSelectedItem().toString());
            stmt.setInt(5, (int) tabela.getValueAt(linha, 0));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso.");
            listar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar usuário: " + ex.getMessage());
        }
    }

    /**
     * Exclui um usuário do banco.
     */
    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário antes de excluir.");
            return;
        }

        int id = (int) tabela.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este usuário?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement("DELETE FROM usuario WHERE id_usuario=?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuário excluído.");
            listar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir usuário: " + ex.getMessage());
        }
    }

    /**
     * Lista os usuários cadastrados.
     */
    private void listar() {
        modelo.setRowCount(0);
        List<Usuario> lista = dao.listarTodos();

        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getLogin(), u.getTipo()
            });
        }
    }

    /**
     * Limpa os campos da tela.
     */
    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        cbTipo.setSelectedIndex(0);
    }

    /**
     * Main para testes isolados.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaUsuario().setVisible(true));
    }
}
