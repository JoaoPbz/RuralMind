package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import util.ConexaoBD;

/**
 * Classe usada para acessar e manipular os dados da tabela usuario.
 * Aqui ficam os metodos de autenticação, cadastro e manutencao dos usuarios do sistema.
 */
public class UsuarioDAO {

    private Connection conn;

    /**
     * Construtor padrão que já abre a conexao com o banco.
     */
    public UsuarioDAO() {
        try {
            this.conn = ConexaoBD.getConnection();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    /**
     * Construtor alternativo, permite usar uma conexao já existente.
     *
     * @param conn conexao ativa com o banco
     */
    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Autentica um usuario usando login e senha.
     *
     * @param login informado
     * @param senha informada
     * @return usuario encontrado, ou null se nao existir
     */
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setTipo(rs.getString("tipo"));
                    return usuario;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuario: " + e.getMessage());
        }

        return null;
    }

    /**
     * Cadastra um novo usuario no banco.
     *
     * @param usuario objeto Usuario com os dados que serao inseridos
     */
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, login, senha, tipo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuario: " + e.getMessage());
        }
    }

    /**
     * Atualiza os dados de um usuario já cadastrado.
     *
     * @param usuario objeto Usuario com informaçoes atualizadas
     */
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome=?, login=?, senha=?, tipo=? WHERE id_usuario=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Remove um usuario do banco usando seu ID.
     *
     * @param id identificador do usuario
     */
    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuario: " + e.getMessage());
        }
    }

    /**
     * Lista todos os usuarios cadastrados.
     *
     * @return lista de objetos Usuario encontrados
     */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY id_usuario";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setTipo(rs.getString("tipo"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuarios: " + e.getMessage());
        }

        return lista;
    }
}
