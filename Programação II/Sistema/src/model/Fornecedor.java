package model;

/**
 * Classe que representa um fornecedor do sistema.
 * Usa herança da classe Pessoa e adiciona o campo CNPJ,
 * usado para a identificacao das empresas cadastradas.
 */
public class Fornecedor extends Pessoa {

    /** CNPJ do fornecedor. */
    private String cnpj;

    /** Construtor padrão. */
    public Fornecedor() {}

    /**
     * Construtor completo para criar um fornecedor com todos os dados.
     *
     * @param id id do fornecedor
     * @param nome nome do fornecedor
     * @param cnpj cnpj do fornecedor
     * @param telefone telefone para contato
     * @param email endereco de e-mail
     * @param endereco endereco completo
     * @param cidade cidade onde está localizado
     * @param estado estado do fornecedor
     */
    public Fornecedor(int id, String nome, String cnpj, String telefone, String email,
                      String endereco, String cidade, String estado) {

        super(id, nome, telefone, email, endereco, cidade, estado);
        this.cnpj = cnpj;
    }

    /**
     * Retorna o CNPJ do fornecedor.
     *
     * @return cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * Atualiza o CNPJ do fornecedor.
     *
     * @param cnpj novo
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
