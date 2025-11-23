package model;

/**
 * Classe que representa um cliente do sistema.
 * Utiliza a herança da classe Pessoa e adiciona o campo CPF,
 * usado para a identificacao individual do cliente.
 */
public class Cliente extends Pessoa {

    /** CPF do cliente. */
    private String cpf;

    /** Construtor padrão. */
    public Cliente() {}

    /**
     * Construtor completo para criar um cliente com todos os dados.
     *
     * @param id id do cliente
     * @param nome nome completo
     * @param cpf cpf do cliente
     * @param telefone telefone para contato
     * @param email endereco de e-mail
     * @param endereco endereco completo
     * @param cidade cidade onde o cliente mora
     * @param estado estado do cliente
     */
    public Cliente(int id, String nome, String cpf, String telefone, String email,
                   String endereco, String cidade, String estado) {

        super(id, nome, telefone, email, endereco, cidade, estado);
        this.cpf = cpf;
    }

    /**
     * Retorna o CPF do cliente.
     *
     * @return cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Atualiza o CPF do cliente.
     *
     * @param cpf novo
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
