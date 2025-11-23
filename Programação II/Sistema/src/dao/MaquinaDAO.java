package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.Maquina;
import util.ConexaoBD;

/**
 * Classe responsável por acessar e manipular os dados da tabela maquina.
 * Possui operações de inserção, atualização, remoção e listagem.
 */
public class MaquinaDAO {

    /**
     * Insere uma nova máquina no banco de dados.
     *
     * @param maquina objeto Maquina contendo os dados a serem gravados
     * @return true se a operação foi bem sucedida
     */
    public boolean inserir(Maquina maquina) {

        String sql = """
            INSERT INTO maquina
            (modelo, marca, ano, nova_usada, preco_custo, preco_venda, status, quantia, id_fornecedor)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maquina.getModelo());
            ps.setString(2, maquina.getMarca());
            ps.setInt(3, maquina.getAno());
            ps.setString(4, maquina.getNovaUsada());
            ps.setDouble(5, maquina.getPrecoCusto());
            ps.setDouble(6, maquina.getPrecoVenda());
            ps.setString(7, maquina.getStatus());
            ps.setInt(8, maquina.getQuantia());
            ps.setInt(9, maquina.getIdFornecedor());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Maquina cadastrada com sucesso.");
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar máquina: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza os dados de uma máquina registrada no sistema.
     *
     * Se ela já tiver sido usada em vendas, apenas:
     * - preco_custo
     * - preco_venda
     * - quantia (estoque)
     * - status
     *
     * podem ser atualizados. Esse comportamento impede que dados
     * históricos sejam alterados,mas permite atualizar informações essenciais
     *
     * @param maquina objeto Maquina com os dados ajustados
     */
    public void atualizar(Maquina maquina) {

        String sqlVerifica = "SELECT 1 FROM venda_maquina WHERE id_maquina = ?";

        // atualização completa (para máquinas nunca usadas)
        String sqlCompleto = """
            UPDATE maquina
            SET modelo=?, marca=?, ano=?, nova_usada=?, preco_custo=?, 
                preco_venda=?, status=?, quantia=?, id_fornecedor=?
            WHERE id_maquina=?
        """;

        // atualização permitida quando máquina já participou de venda
        String sqlRestrito = """
            UPDATE maquina
            SET preco_custo=?, preco_venda=?, quantia=?, status=?
            WHERE id_maquina=?
        """;

        try (Connection conn = ConexaoBD.getConnection()) {

            boolean usadaEmVenda = false;

            // verifica participação em vendas
            try (PreparedStatement check = conn.prepareStatement(sqlVerifica)) {
                check.setInt(1, maquina.getId());
                try (ResultSet rs = check.executeQuery()) {
                    usadaEmVenda = rs.next();
                }
            }

            if (usadaEmVenda) {

                try (PreparedStatement ps = conn.prepareStatement(sqlRestrito)) {
                    ps.setDouble(1, maquina.getPrecoCusto());
                    ps.setDouble(2, maquina.getPrecoVenda());
                    ps.setInt(3, maquina.getQuantia());
                    ps.setString(4, maquina.getStatus());
                    ps.setInt(5, maquina.getId());
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(null,
                    "A máquina já participou de vendas. Apenas preço, estoque e status foram atualizados.");

            } else {

                try (PreparedStatement ps = conn.prepareStatement(sqlCompleto)) {

                    ps.setString(1, maquina.getModelo());
                    ps.setString(2, maquina.getMarca());
                    ps.setInt(3, maquina.getAno());
                    ps.setString(4, maquina.getNovaUsada());
                    ps.setDouble(5, maquina.getPrecoCusto());
                    ps.setDouble(6, maquina.getPrecoVenda());
                    ps.setString(7, maquina.getStatus());
                    ps.setInt(8, maquina.getQuantia());
                    ps.setInt(9, maquina.getIdFornecedor());
                    ps.setInt(10, maquina.getId());

                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Máquina atualizada com sucesso.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar máquina: " + e.getMessage());
        }
    }

    /**
     * Remove uma máquina do banco.
     * Máquinas usadas em vendas não podem ser excluídas.
     *
     * @param id identificador da máquina que será removida
     */
    public void excluir(int id) {

        String sql = "DELETE FROM maquina WHERE id_maquina=?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Máquina excluída com sucesso.");

        } catch (SQLException e) {

            String msg = e.getMessage().toLowerCase();

            if (msg.contains("foreign key") || msg.contains("chave estrangeira")) {
                JOptionPane.showMessageDialog(null,
                        "A máquina não pode ser excluída pois já foi utilizada em uma venda.",
                        "Ação bloqueada",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Erro ao excluir máquina: " + e.getMessage());
            }
        }
    }

    /**
     * Lista todas as máquinas cadastradas no sistema.
     *
     * @return lista de objetos Maquina
     */
    public List<Maquina> listar() {

        List<Maquina> lista = new ArrayList<>();

        String sql = """
            SELECT m.*, f.nome AS nome_fornecedor
            FROM maquina m
            JOIN fornecedor f ON f.id_fornecedor = m.id_fornecedor
            ORDER BY m.id_maquina
        """;

        try (Connection conn = ConexaoBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Maquina m = new Maquina();

                m.setId(rs.getInt("id_maquina"));
                m.setModelo(rs.getString("modelo"));
                m.setMarca(rs.getString("marca"));
                m.setAno(rs.getInt("ano"));
                m.setNovaUsada(rs.getString("nova_usada"));
                m.setPrecoCusto(rs.getDouble("preco_custo"));
                m.setPrecoVenda(rs.getDouble("preco_venda"));
                m.setStatus(rs.getString("status"));
                m.setQuantia(rs.getInt("quantia"));
                m.setIdFornecedor(rs.getInt("id_fornecedor"));
                m.setFornecedor(rs.getString("nome_fornecedor"));

                lista.add(m);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar máquinas: " + e.getMessage());
        }

        return lista;
    }
}
