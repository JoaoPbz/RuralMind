package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.Cliente;
import util.ConexaoBD;

/**
 * Classe responsável por realizar as operações de acesso e manipulação dos dados
 * relacionados à tabela cliente no banco de dados.
 * 
 * Contém métodos para inserir, atualizar, excluir e listar clientes,
 * garantindo a persistência e integridade das informações.
 */
public class ClienteDAO {

    /**
     * Verifica se um CPF já está cadastrado no banco de dados.
     * 
     * @param cpf CPF do cliente a ser verificado.
     * @return true se o CPF já existe, false caso contrário.
     */
    public boolean existeCpf(String cpf) {
        String sql = "SELECT 1 FROM cliente WHERE cpf = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar CPF: " + e.getMessage());
        }
        return false;
    }

    /**
     * Insere um novo cliente no banco de dados, caso o CPF ainda não exista.
     * 
     * @param cliente a ser inserido.
     * @return true se o cliente foi inserido com sucesso,false caso contrário.
     */
    public boolean inserir(Cliente cliente) {
        if (existeCpf(cliente.getCpf())) {
            JOptionPane.showMessageDialog(null, "CPF já cadastrado no sistema.");
            return false;
        }

        String sql = "INSERT INTO cliente (nome, cpf, telefone, email, endereco, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getEstado());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso.");
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza os dados de um cliente existente no banco de dados.
     * 
     * @param cliente Objeto Cliente com os dados atualizados.
     */
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, cpf=?, telefone=?, email=?, endereco=?, cidade=?, estado=? WHERE id_cliente=?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getEstado());
            stmt.setInt(8, cliente.getId());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    /**
     * Exclui um cliente do banco de dados com base em seu ID.
     * 
     * @param id Identificador do cliente a ser excluído.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente=?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cliente excluido com sucesso.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir cliente: " + e.getMessage());
        }
    }

    /**
     * Retorna uma lista contendo todos os clientes cadastrados no banco de dados.
     * 
     * @return Lista de objetos Cliente
     */
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY id_cliente";

        try (Connection conn = ConexaoBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setCidade(rs.getString("cidade"));
                cliente.setEstado(rs.getString("estado"));
                lista.add(cliente);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar clientes: " + e.getMessage());
        }

        return lista;
    }
}
