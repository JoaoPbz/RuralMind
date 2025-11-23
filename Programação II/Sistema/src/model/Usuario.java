package model;

/**
 * Representa um usuário do sistema RuralMind.
 * Contém apenas os dados necessários para autenticação e controle de acesso.
 */
public class Usuario {

    /** Identificador único do usuário. */
    private int id;

    /** Nome do usuário (para exibição no sistema). */
    private String nome;

    /** Login de acesso ao sistema. */
    private String login;

    /** Senha vinculada ao login. */
    private String senha;

    /** Tipo do usuário (admin ou vendedor). */
    private String tipo;

    /** Construtor padrão. */
    public Usuario() {}

    /**
     * Construtor completo.
     *
     * @param id identificador do usuário
     * @param nome nome do usuário
     * @param login login de acesso
     * @param senha senha de acesso
     * @param tipo tipo do usuário (admin ou vendedor)
     */
    public Usuario(int id, String nome, String login, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    /** @return id do usuário */
    public int getId() {
        return id;
    }

    /** Define o id. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return nome do usuário */
    public String getNome() {
        return nome;
    }

    /** Define o nome. */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return login do usuário */
    public String getLogin() {
        return login;
    }

    /** Define o login. */
    public void setLogin(String login) {
        this.login = login;
    }

    /** @return senha do usuário */
    public String getSenha() {
        return senha;
    }

    /** Define a senha. */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /** @return tipo do usuário */
    public String getTipo() {
        return tipo;
    }

    /** Define o tipo. */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
