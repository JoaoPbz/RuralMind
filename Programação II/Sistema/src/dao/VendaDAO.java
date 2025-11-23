package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import model.Maquina;
import model.Venda;
import util.ConexaoBD;

/**
 * Classe responsável pelo registro completo das vendas no sistema.
 *
 * Garante que a venda, seus itens e a atualização de estoque sejam
 * executados dentro da mesma transação, evitando inconsistências.
 */
public class VendaDAO {

    /**
     * Registra uma venda completa no banco de dados.
     *
     * Inclui:
     * 
     *     registro da venda
     *     registro dos itens, usando preço histórico e subtotal informados
     *     atualização do estoque das máquinas envolvidas
     * 
     *
     * @param venda objeto com os dados principais da venda
     * @param maquinas lista das máquinas vendidas
     * @param quantidades quantidades correspondentes de cada máquina
     * @param precosUnitarios lista com o preço unitário aplicado na venda (histórico)
     * @param subtotais lista com o subtotal calculado para cada item
     */
    public void registrarVenda(
            Venda venda,
            List<Maquina> maquinas,
            List<Integer> quantidades,
            List<Double> precosUnitarios,
            List<Double> subtotais
    ) {

        String sqlVenda = """
            INSERT INTO venda (id_cliente, id_usuario, data_venda, lucro, observacao)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id_venda
        """;

        String sqlItem = """
            INSERT INTO venda_maquina (id_venda, id_maquina, quantidade, preco_unitario, subtotal)
            VALUES (?, ?, ?, ?, ?)
        """;

        String sqlAtualizaEstoque = """
            UPDATE maquina
            SET quantia = quantia - ?,
                status = CASE WHEN quantia - ? <= 0 THEN 'fora_de_estoque' ELSE 'disponivel' END
            WHERE id_maquina = ?
        """;

        try (Connection conn = ConexaoBD.getConnection()) {

            conn.setAutoCommit(false);

            if (maquinas == null || maquinas.isEmpty()) {
                throw new SQLException("Nenhuma máquina informada para registrar a venda.");
            }

            String obsFinal = (venda.getObservacao() != null && !venda.getObservacao().isBlank())
                    ? venda.getObservacao()
                    : "";

            //  Registrar a venda e pegar o ID gerado
            int idVendaGerado;
            try (PreparedStatement psVenda = conn.prepareStatement(sqlVenda)) {

                psVenda.setInt(1, venda.getIdCliente());
                psVenda.setInt(2, venda.getIdUsuario());
                psVenda.setDate(3, Date.valueOf(LocalDate.now()));
                psVenda.setDouble(4, venda.getValorTotal());
                psVenda.setString(5, obsFinal);

                try (ResultSet rs = psVenda.executeQuery()) {
                    if (rs.next()) {
                        idVendaGerado = rs.getInt("id_venda");
                    } else {
                        throw new SQLException("Falha ao gerar o ID da venda.");
                    }
                }
            }

            //  Registrar cada item com PREÇO HISTÓRICO + SUBTOTAL fixo
            for (int i = 0; i < maquinas.size(); i++) {

                Maquina m = maquinas.get(i);
                int qtd = quantidades.get(i);
                double precoUnitario = precosUnitarios.get(i);  // vem da tela
                double subtotal = subtotais.get(i);             // vem da tela

                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    psItem.setInt(1, idVendaGerado);
                    psItem.setInt(2, m.getId());
                    psItem.setInt(3, qtd);
                    psItem.setDouble(4, precoUnitario);
                    psItem.setDouble(5, subtotal);
                    psItem.executeUpdate();
                }

                //  Atualizar estoque
                try (PreparedStatement psEstoque = conn.prepareStatement(sqlAtualizaEstoque)) {
                    psEstoque.setInt(1, qtd);
                    psEstoque.setInt(2, qtd);
                    psEstoque.setInt(3, m.getId());
                    psEstoque.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar venda: " + e.getMessage());
        }
    }
}
