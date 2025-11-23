package view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import model.Usuario;
import util.BackupBD;

/**
 * Tela principal do RuralMind.
 *
 * Aqui ficam os acessos aos módulos principais como cadastros,
 * vendas, relatorios e a parte de usuários. Também tem os botões
 * de backup/restauração do banco (somente pra admin).
 */
public class TelaPrincipal extends JFrame {

    private JButton btnClientes, btnFornecedores, btnMaquinas, btnVendas;
    private JButton btnRelatorios, btnUsuarios, btnBackup, btnRestore, btnSair;
    private JLabel lblUsuarioLogado;
    private Usuario usuarioLogado;

    /**
     * Construtor que inicia a tela principal com base no usuario já logado.
     */
    public TelaPrincipal(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("RuralMind");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        configurarComponentes();
        configurarAcoes();
        aplicarPermissoes();
        setVisible(true);
    }

    /**
     * Configura os componentes da interface.
     */
    private void configurarComponentes() {
        JLabel lblTitulo = new JLabel("Home", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(150, 20, 300, 30);
        add(lblTitulo);

        lblUsuarioLogado = new JLabel(
                "Usuário: " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipo() + ")"
        );
        lblUsuarioLogado.setBounds(20, 60, 400, 25);
        add(lblUsuarioLogado);

        btnClientes = new JButton("Clientes");
        btnClientes.setBounds(50, 120, 200, 40);
        add(btnClientes);

        btnFornecedores = new JButton("Fornecedores");
        btnFornecedores.setBounds(330, 120, 200, 40);
        add(btnFornecedores);

        btnMaquinas = new JButton("Máquinas");
        btnMaquinas.setBounds(50, 180, 200, 40);
        add(btnMaquinas);

        btnVendas = new JButton("Vendas");
        btnVendas.setBounds(330, 180, 200, 40);
        add(btnVendas);

        btnRelatorios = new JButton("Relatórios");
        btnRelatorios.setBounds(50, 240, 200, 40);
        add(btnRelatorios);

        btnUsuarios = new JButton("Usuarios");
        btnUsuarios.setBounds(330, 240, 200, 40);
        add(btnUsuarios);

        btnBackup = new JButton("Backup");
        btnBackup.setToolTipText("Gerar backup completo do banco");
        btnBackup.setBounds(510, 20, 110, 35);
        add(btnBackup);

        btnRestore = new JButton("Restaurar");
        btnRestore.setToolTipText("Restaurar backup salvo");
        btnRestore.setBounds(380, 20, 120, 35);
        add(btnRestore);

        btnSair = new JButton("Sair");
        btnSair.setBounds(190, 340, 250, 40);
        add(btnSair);
    }

    /**
     * Registra as ações dos botões.
     */
    private void configurarAcoes() {
        btnClientes.addActionListener(e -> new TelaCliente().setVisible(true));
        btnFornecedores.addActionListener(e -> new TelaFornecedor().setVisible(true));
        btnMaquinas.addActionListener(e -> new TelaMaquina().setVisible(true));
        btnVendas.addActionListener(e -> new TelaVenda(usuarioLogado).setVisible(true));
        btnRelatorios.addActionListener(e -> new TelaRelatorioVendas().setVisible(true));
        btnUsuarios.addActionListener(e -> new TelaUsuario().setVisible(true));

        btnBackup.addActionListener(e -> realizarBackupComProgresso());
        btnRestore.addActionListener(e -> restaurarBackupComProgresso());

        btnSair.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair do sistema?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (opcao == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    /**
     * Aplica restrições dependendo do tipo do usuário logado.
     */
    private void aplicarPermissoes() {
        if (!usuarioLogado.getTipo().equalsIgnoreCase("admin")) {
            btnRelatorios.setVisible(false);
            btnUsuarios.setVisible(false);
            btnBackup.setVisible(false);
            btnRestore.setVisible(false);
        }
    }

    /**
     * Exibe uma confirmação antes de gerar um novo backup.
     */
    private void realizarBackupComProgresso() {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Gerar backup completo do banco?",
                "Confirmar Backup",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            executarProcessoAssincrono(
                    "Gerando Backup...",
                    "Aguarde, isso pode levar alguns instantes.",
                    true
            );
        }
    }

    /**
     * Seleciona um arquivo e restaura o backup.
     */
    private void restaurarBackupComProgresso() {
        JFileChooser chooser = new JFileChooser("C:\\RuralMindBackups");
        chooser.setDialogTitle("Escolha o arquivo de backup");

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = chooser.getSelectedFile();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Restaurar este backup irá substituir todos os dados atuais.\n\n" +
                        "Arquivo: " + arquivo.getName() +
                        "\nContinuar?",
                "Confirmação de Restauração",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            executarProcessoAssincrono(
                    "Restaurando Backup...",
                    "Aguarde enquanto os dados são restaurados.",
                    false,
                    arquivo.getAbsolutePath()
            );
        }
    }

    /**
     * Versão reduzida do método assíncrono (só pra backup).
     */
    private void executarProcessoAssincrono(String titulo, String mensagem, boolean isBackup) {
        executarProcessoAssincrono(titulo, mensagem, isBackup, null);
    }

    /**
     * Executa backup ou restauração, exibindo uma barra de progresso.
     */
    private void executarProcessoAssincrono(String titulo, String mensagem, boolean isBackup, String caminhoArquivo) {

        JDialog progressoDialog = new JDialog(this, titulo, true);
        progressoDialog.setSize(350, 120);
        progressoDialog.setLocationRelativeTo(this);
        progressoDialog.setLayout(new BorderLayout(10, 10));

        JLabel lblMensagem = new JLabel(mensagem, SwingConstants.CENTER);
        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true);

        progressoDialog.add(lblMensagem, BorderLayout.NORTH);
        progressoDialog.add(barra, BorderLayout.CENTER);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private String erro = null;
            private String caminhoGerado = null;

            @Override
            protected Void doInBackground() {
                try {
                    if (isBackup) {
                        caminhoGerado = BackupBD.realizarBackup();
                    } else {
                        BackupBD.restaurarBackup(caminhoArquivo);
                    }
                } catch (Exception ex) {
                    erro = ex.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                progressoDialog.dispose();

                if (erro == null) {
                    if (isBackup) {
                        int escolha = JOptionPane.showOptionDialog(
                                TelaPrincipal.this,
                                "Backup criado com sucesso.\nArquivo salvo em:\n" + caminhoGerado,
                                "Backup Finalizado",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new String[]{"Abrir pasta", "Fechar"},
                                "Abrir pasta"
                        );

                        if (escolha == JOptionPane.YES_OPTION) {
                            try {
                                Desktop.getDesktop().open(new File("C:\\RuralMindBackups"));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(
                                        TelaPrincipal.this,
                                        "Não foi possível abrir a pasta: " + ex.getMessage()
                                );
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            TelaPrincipal.this,
                            "Ocorreu um erro no processo:\n" + erro,
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
        progressoDialog.setVisible(true);
    }

    /**
     * Main apenas para testes isolados.
     */
    public static void main(String[] args) {
        Usuario teste = new Usuario();
        teste.setId(1);
        teste.setNome("Admin");
        teste.setTipo("admin");
        SwingUtilities.invokeLater(() -> new TelaPrincipal(teste));
    }
}
