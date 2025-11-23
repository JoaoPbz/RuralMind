package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Maquina;
import model.Venda;
import util.ConexaoBD;

/**
 * Classe responsável por gerar relatórios de vendas e consultar
 * as máquinas relacionadas a cada venda.
 *
 */
public class RelatorioVendaDAO {

    /**
     * Lista todas as vendas dentro de um intervalo, com filtros opcionais
     * por nome do cliente e nome do vendedor.
     *
     * Os valores totais e custos são calculados usando os subtotais
     * registrados no momento da venda.
     */
    public List<Venda> listarVendas(LocalDate dataInicio, LocalDate dataFim,
                                    String clienteFiltro, String vendedorFiltro) {

        List<Venda> lista = new ArrayList<>();

        String sql = """
            SELECT 
                v.id_venda,
                v.data_venda,
                c.nome AS nome_cliente,
                u.nome AS nome_vendedor,
                COALESCE(SUM(vm.subtotal), 0) AS valor_total,
                COALESCE(SUM(m.preco_custo * vm.quantidade), 0) AS custo_total,
                v.observacao
            FROM venda v
            JOIN cliente c ON v.id_cliente = c.id_cliente
            JOIN usuario u ON v.id_usuario = u.id_usuario
            JOIN venda_maquina vm ON v.id_venda = vm.id_venda
            JOIN maquina m ON vm.id_maquina = m.id_maquina
            WHERE v.data_venda BETWEEN ? AND ?
        """;

        if (clienteFiltro != null && !clienteFiltro.isBlank()) {
            sql += " AND LOWER(c.nome) LIKE LOWER(?) ";
        }

        if (vendedorFiltro != null && !vendedorFiltro.isBlank()) {
            sql += " AND LOWER(u.nome) LIKE LOWER(?) ";
        }

        sql += """
            GROUP BY v.id_venda, v.data_venda, c.nome, u.nome, v.observacao
            ORDER BY v.data_venda DESC
        """;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));

            int index = 3;

            if (clienteFiltro != null && !clienteFiltro.isBlank()) {
                stmt.setString(index++, "%" + clienteFiltro + "%");
            }
            if (vendedorFiltro != null && !vendedorFiltro.isBlank()) {
                stmt.setString(index++, "%" + vendedorFiltro + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Venda v = new Venda();
                    v.setId(rs.getInt("id_venda"));
                    v.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    v.setNomeCliente(rs.getString("nome_cliente"));
                    v.setNomeUsuario(rs.getString("nome_vendedor"));
                    v.setObservacao(rs.getString("observacao"));

                    v.setValorTotal(rs.getDouble("valor_total"));
                    v.setCustoTotal(rs.getDouble("custo_total"));

                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Retorna todas as máquinas vinculadas a uma venda específica,
     * incluindo os valores históricos de preço unitário e subtotal
     * registrados na venda.
     */
    public List<Maquina> listarMaquinasPorVenda(int idVenda) {

        List<Maquina> lista = new ArrayList<>();

        String sql = """
            SELECT 
                m.id_maquina,
                m.modelo,
                m.marca,
                m.nova_usada AS condicao,
                m.preco_custo,
                vm.preco_unitario,
                vm.subtotal,
                vm.quantidade,
                f.nome AS fornecedor
            FROM venda_maquina vm
            JOIN maquina m ON vm.id_maquina = m.id_maquina
            JOIN fornecedor f ON m.id_fornecedor = f.id_fornecedor
            WHERE vm.id_venda = ?
        """;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Maquina m = new Maquina();
                    m.setId(rs.getInt("id_maquina"));
                    m.setModelo(rs.getString("modelo"));
                    m.setMarca(rs.getString("marca"));
                    m.setNovaUsada(rs.getString("condicao"));
                    m.setPrecoCusto(rs.getDouble("preco_custo"));
                    m.setQuantia(rs.getInt("quantidade"));
                    m.setFornecedor(rs.getString("fornecedor"));

                    // valores históricos usados no relatório
                    m.setPrecoUnitarioHistorico(rs.getDouble("preco_unitario"));
                    m.setSubtotalHistorico(rs.getDouble("subtotal"));

                    lista.add(m);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar máquinas da venda: " + e.getMessage());
        }

        return lista;
    }
}
