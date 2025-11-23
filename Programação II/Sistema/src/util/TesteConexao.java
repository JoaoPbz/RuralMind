package util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe simples para testar a conexao com o banco RuralMind.
 * Pode ser executada separadamente para verificar se os dados
 * de acesso estao corretos e se o PostgreSQL está respondendo.
 */
public class TesteConexao {

    /**
     * Metodo principal que realiza o teste de conexao.
     *
     * @param args argumentos da linha de comando (nao usados)
     */
    public static void main(String[] args) {

        try (Connection conexao = ConexaoBD.getConnection()) {

            if (conexao != null) {
                System.out.println("Conexao com o banco estabelecida com sucesso.");
            } else {
                System.out.println("A conexao retornou nula, verificar configuracao.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao conectar no banco: " + e.getMessage());
        }
    }
}
