package view;

import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.UsuarioDAO;
import model.Usuario;
import util.ConexaoBD;

/**
 * Tela de login do sistema. Permite que o usuario entre no RuralMind usando um
 * login e senha cadastrados.
 * 
 * Caso o banco esteja vazio, cria automaticamente um usuario admin padrão para
 * evitar que o sistema fique sem acesso. (a qual sera usado na primeira vez que rodar o projeto para
 * iniciar funcionamento a partir disso,ou para rodar um restore de um backup previo
 */
public class TelaLogin extends JFrame {

	private JTextField txtLogin;
	private JPasswordField txtSenha;
	private JButton btnEntrar;
	private JButton btnSair;

	/**
	 * Construtor principal. Monta a interface e verifica se precisa criar o usuario
	 * padrão.
	 */
	public TelaLogin() {
		setTitle("Login - RuralMind");
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);

		configurarComponentes();
		criarUsuarioPadrao();

		setVisible(true);
	}

	/**
	 * Configuração dos campos e botoes do login.
	 */
	private void configurarComponentes() {

		JLabel lblTitulo = new JLabel("Insira seu Login", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitulo.setBounds(80, 20, 240, 30);
		add(lblTitulo);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(60, 70, 80, 25);
		add(lblLogin);

		txtLogin = new JTextField();
		txtLogin.setBounds(140, 70, 180, 25);
		add(txtLogin);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(60, 110, 80, 25);
		add(lblSenha);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(140, 110, 180, 25);
		add(txtSenha);

		btnEntrar = new JButton("Entrar");
		btnEntrar.setBounds(80, 160, 100, 30);
		add(btnEntrar);

		btnSair = new JButton("Sair");
		btnSair.setBounds(220, 160, 100, 30);
		add(btnSair);

		btnEntrar.addActionListener(e -> logar());
		btnSair.addActionListener(e -> System.exit(0));
	}

	/**
	 * Verifica se há usuarios cadastrados. Se nao, cria um admin padrao (admin /
	 * 123) pra garantir acesso.
	 */
	private void criarUsuarioPadrao() {

		try (Connection conn = ConexaoBD.getConnection()) {

			UsuarioDAO dao = new UsuarioDAO(conn);
			List<Usuario> usuarios = dao.listarTodos();

			if (usuarios.isEmpty()) {

				Usuario admin = new Usuario();
				admin.setNome("Administrador");
				admin.setLogin("admin");
				admin.setSenha("123");
				admin.setTipo("admin");

				dao.inserir(admin);

				JOptionPane.showMessageDialog(this, "Nenhum usuario encontrado.\n\n" + "Foi criado um usuario padrao:\n"
						+ "Login: admin\nSenha: 123", "Usuario Padrão", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao verificar ou criar usuario padrao: " + e.getMessage());
		}
	}

	/**
	 * Realiza o login no banco. Se autenticado, abre a TelaPrincipal.
	 */
	private void logar() {

		String login = txtLogin.getText();
		String senha = String.valueOf(txtSenha.getPassword());

		if (login.isBlank() || senha.isBlank()) {
			JOptionPane.showMessageDialog(this, "Preencha o login e a senha antes de continuar.");
			return;
		}

		try (Connection conn = ConexaoBD.getConnection()) {

			UsuarioDAO dao = new UsuarioDAO(conn);
			Usuario u = dao.autenticar(login, senha);

			if (u != null) {
				JOptionPane.showMessageDialog(this, "Bem-vindo, " + u.getNome());
				dispose();
				new TelaPrincipal(u);

			} else {
				JOptionPane.showMessageDialog(this, "Login ou senha incorretos.");
			}

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Erro de conexao: " + ex.getMessage());

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Erro inesperado ao tentar autenticar: " + ex.getMessage());
		}
	}

	/**
	 * Abre a tela de login quando rodado diretamente.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(TelaLogin::new);
	}
}
