package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe que gerencia a conexao com o banco PostgreSQL usado pelo sistema.
 * Mantem um ponto central para criar novas conexoes quando necessario.
 */
public class ConexaoBD {

    /** URL de conexao com o banco. */
    private static final String URL = "jdbc:postgresql://localhost:5432/ruralmind";

    /** Usuario do banco. */
    private static final String USER = "postgres"; // (alterar)

    /** Senha do usuario. */
    private static final String PASSWORD = "0"; // (alterar)

    /**
     * Retorna uma conexao ativa com o banco RuralMind.
     *
     * @return objeto Connection
     * @throws SQLException caso nao seja possivel conectar
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
