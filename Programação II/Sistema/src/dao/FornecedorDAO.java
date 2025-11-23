package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.Fornecedor;
import util.ConexaoBD;

/**
 * Classe usada para acessar e manipular os dados da tabela fornecedor.
 * Aqui ficam os metodos para cadastrar, atualizar, remover, listar e buscar fornecedores.
 */
public class FornecedorDAO {

    /**
     * Verifica se já existe um CNPJ cadastrado.
     *
     * @param cnpj cnpj a ser verificado
     * @return true se existir, false caso contrário
     */
    public boolean existeCnpj(String cnpj) {
        String sql = "SELECT 1 FROM fornecedor WHERE cnpj = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar CNPJ: " + e.getMessage());
        }
        return false;
    }

    /**
     * Insere um novo fornecedor no banco, desde que o CNPJ ainda não tenha sido cadastrado.
     *
     * @param fornecedor objeto Fornecedor com as informacoes a serem gravadas
     * @return true se cadastrou, false se não
     */
    public boolean inserir(Fornecedor fornecedor) {
        if (existeCnpj(fornecedor.getCnpj())) {
            JOptionPane.showMessageDialog(null, "CNPJ já cadastrado no sistema.");
            return false;
        }

        String sql = "INSERT INTO fornecedor (nome, cnpj, telefone, email, endereco, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getEndereco());
            stmt.setString(6, fornecedor.getCidade());
            stmt.setString(7, fornecedor.getEstado());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Fornecedor cadastrado com sucesso.");

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar fornecedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza os dados de um fornecedor ja existente no banco.
     *
     * @param fornecedor objeto Fornecedor com os dados atualizados
     */
    public void atualizar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET nome=?, cnpj=?, telefone=?, email=?, endereco=?, cidade=?, estado=? WHERE id_fornecedor=?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getEndereco());
            stmt.setString(6, fornecedor.getCidade());
            stmt.setString(7, fornecedor.getEstado());
            stmt.setInt(8, fornecedor.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Fornecedor atualizado com sucesso");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar fonecedor: " + e.getMessage());
        }
    }

    /**
     * Remove um fornecedor usando seu ID.
     *
     * @param id do fornecedor
     */
    public void excluir(int id) {
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor=?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Fornecedor excluido com sucesso.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir fornecedor: " + e.getMessage());
        }
    }

    /**
     * Lista todos os fornecedores cadastrados.
     *
     * @return lista de fornecedores encontrados
     */
    public List<Fornecedor> listar() {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor ORDER BY id_fornecedor";

        try (Connection conn = ConexaoBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(rs.getInt("id_fornecedor"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setTelefone(rs.getString("telefone"));
                fornecedor.setEmail(rs.getString("email"));
                fornecedor.setEndereco(rs.getString("endereco"));
                fornecedor.setCidade(rs.getString("cidade"));
                fornecedor.setEstado(rs.getString("estado"));
                lista.add(fornecedor);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar fornecedores: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca um fornecedor pelo seu ID.
     *
     * @param id do fornecedor procurado
     * @return objeto Fornecedor encontrado, ou null se não existir
     */
    public Fornecedor buscarPorId(int id) {
        String sql = "SELECT * FROM fornecedor WHERE id_fornecedor = ?";
        Fornecedor fornecedor = null;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fornecedor = new Fornecedor();
                    fornecedor.setId(rs.getInt("id_fornecedor"));
                    fornecedor.setNome(rs.getString("nome"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("email"));
                    fornecedor.setEndereco(rs.getString("endereco"));
                    fornecedor.setCidade(rs.getString("cidade"));
                    fornecedor.setEstado(rs.getString("estado"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar fornecedor por ID: " + e.getMessage());
        }

        return fornecedor;
    }
}
