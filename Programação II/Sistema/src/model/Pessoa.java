package model;

/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Ela serve de base para outras classes como Cliente e Fornecdor,
 * concentrando os dados comuns entre elas.
 */
public abstract class Pessoa {

    /** Identificador unico da pessoa. */
    protected int id;

    /** Nome completo da pessoa. */
    protected String nome;

    /** Telefone de contato. */
    protected String telefone;

    /** Endereco de e-mail. */
    protected String email;

    /** Endereco residencial ou comercial. */
    protected String endereco;

    /** Cidade de residencia. */
    protected String cidade;

    /** Estado da residencia. */
    protected String estado;

    /** Construtor padrao. */
    public Pessoa() {}

    /**
     * Construtor completo para criar uma pessoa com todas as informaçoes.
     *
     * @param id id da pessoa
     * @param nome nome completo
     * @param telefone telefone de contato
     * @param email email da pessoa
     * @param endereco endereco completo
     * @param cidade cidade onde mora
     * @param estado estado onde mora
     */
    public Pessoa(int id, String nome, String telefone, String email,
                  String endereco, String cidade, String estado) {

        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
    }

    /** @return id da pessoa */
    public int getId() {
        return id;
    }

    /** Define o id da pessoa. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return nome completo */
    public String getNome() {
        return nome;
    }

    /** Atualiza o nome da pessoa. */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return telefone */
    public String getTelefone() {
        return telefone;
    }

    /** Define o telefone da pessoa. */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /** @return email */
    public String getEmail() {
        return email;
    }

    /** Atualiza o email. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return endereco completo */
    public String getEndereco() {
        return endereco;
    }

    /** Define o endereco. */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /** @return cidade */
    public String getCidade() {
        return cidade;
    }

    /** Atualiza a cidade da pessoa. */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /** @return estado */
    public String getEstado() {
        return estado;
    }

    /** Define o estado da pessoa. */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
