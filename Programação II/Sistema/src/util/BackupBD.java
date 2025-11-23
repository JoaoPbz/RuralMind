package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Classe usada para gerar e restaurar backups do banco RuralMind.
 * Funciona com PostgreSQL 17 no Windows. Os backups ficam salvos
 * na pasta C:\RuralMindBackups com nome baseado na data.
 */
public class BackupBD {

    // Configuraçoes principais do postgres e caminhos dos executaveis
    private static final String PG_DUMP_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump.exe";
    private static final String PSQL_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\psql.exe";
    private static final String DATABASE_NAME = "ruralmind"; // nome do banco
    private static final String USER = "postgres"; // user do banco (alterar)
    private static final String PASSWORD = "0"; // senha do usuario (alterar)
    private static final String HOST = "localhost"; // host do banco
    private static final String PORT = "5432"; // porta do banco
    private static final String BACKUP_DIR = "C:\\RuralMindBackups";

    /**
     * Gera um backup completo do banco.
     *
     * @return caminho completo do arquivo criado
     * @throws IOException caso ocorra falha no pg_dump
     * @throws InterruptedException se o processo for interrompido
     */
    public static String realizarBackup() throws IOException, InterruptedException {

        File pasta = new File(BACKUP_DIR);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
        String backupFile = BACKUP_DIR + "\\backup_ruralmind_" + timestamp + ".sql";

        ProcessBuilder pb = new ProcessBuilder(
                PG_DUMP_PATH,
                "-h", HOST,
                "-p", PORT,
                "-U", USER,
                "-d", DATABASE_NAME,
                "-F", "p",
                "-f", backupFile
        );

        pb.environment().put("PGPASSWORD", PASSWORD);
        pb.redirectErrorStream(true);

        System.out.println("Executando pg_dump para gerar backup...");
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Backup gerado em: " + backupFile);
            return backupFile;
        } else {
            throw new IOException("Falha ao executar pg_dump (codigo " + exitCode + ")");
        }
    }

    /**
     * Restaura um backup existente.
     * Remove o schema atual e recria antes de aplicar o .sql informado.
     *
     * @param caminhoBackup caminho completo do arquivo sql
     * @throws IOException erro ao chamar psql
     * @throws InterruptedException se o processo for interrompido
     */
    public static void restaurarBackup(String caminhoBackup) throws IOException, InterruptedException {

        File arquivo = new File(caminhoBackup);
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo de backup nao encontrado:\n" + caminhoBackup);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Restaurar o backup apagara todos os dados atuais.\nDeseja continuar?",
                "Confirmacao",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Limpa schema atual
        System.out.println("Limpando schema atual...");
        ProcessBuilder limparSchema = new ProcessBuilder(
                PSQL_PATH,
                "-h", HOST,
                "-p", PORT,
                "-U", USER,
                "-d", DATABASE_NAME,
                "-c", "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
        );

        limparSchema.environment().put("PGPASSWORD", PASSWORD);
        limparSchema.redirectErrorStream(true);

        Process processoLimpeza = limparSchema.start();
        processoLimpeza.waitFor();

        // Restaura o backup
        System.out.println("Restaurando backup de: " + caminhoBackup);

        ProcessBuilder pb = new ProcessBuilder(
                PSQL_PATH,
                "-h", HOST,
                "-p", PORT,
                "-U", USER,
                "-d", DATABASE_NAME,
                "-f", caminhoBackup
        );

        pb.environment().put("PGPASSWORD", PASSWORD);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Backup restaurado com sucesso!\nO sistema será reiniciado.",
                    "Restauração concluida",
                    JOptionPane.INFORMATION_MESSAGE
            );
            System.exit(0);
        } else {
            throw new IOException("Falha ao executar psql (codigo " + exitCode + ")");
        }
    }
}
