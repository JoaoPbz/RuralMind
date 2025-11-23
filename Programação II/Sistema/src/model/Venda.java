package model;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe que representa uma venda do sistema RuralMind.
 * 
 * Armazena os dados principais da venda, como cliente, vendedor,
 * data, valores totais e observações, além de campos auxiliares usados
 * nos relatórios e consultas do sistema.
 */
public class Venda {

    /** Id da venda. */
    private int id;

    /** Id do cliente ligado à venda. */
    private int idCliente;

    /** Id do usuário (vendedor). */
    private int idUsuario;

    /** Data da venda. */
    private LocalDate dataVenda;

    /** Valor total da venda. */
    private double valorTotal;

    /** Quantidade total de itens vendidos. */
    private int quantidade;

    /** Observação opcional da venda. */
    private String observacao;

    /* Campos auxiliares usados em relatórios */

    /** Nome do cliente exibido em relatórios. */
    private String nomeCliente;

    /** Nome do vendedor exibido em relatórios. */
    private String nomeUsuario;

    /** Nome/modelo da máquina (quando exibido individualmente). */
    private String nomeMaquina;

    /** Condição da máquina (nova ou usada). */
    private String condicaoMaquina;

    /** Preço de custo da máquina referente ao item. */
    private double precoCusto;

    /** Preço de venda atual da máquina (pode não ser histórico). */
    private double precoVenda;

    /** Nome do fornecedor da máquina. */
    private String fornecedor;

    /** Margem de lucro calculada. */
    private double margemLucro;

    /** Lista completa das máquinas vendidas. */
    private List<Maquina> maquinasVendidas;

    /** Soma total dos custos das máquinas. */
    private double custoTotal;

    /* Novos campos para suportar histórico do item */

    /** Preço unitário registrado na venda (histórico). */
    private double precoUnitarioHistorico;

    /** Subtotal registrado na venda (quantidade * preço histórico). */
    private double subtotalHistorico;

    /** Construtor padrão. */
    public Venda() {}

    /**
     * Construtor completo com os dados principais da venda.
     *
     * @param id id da venda
     * @param idCliente id do cliente
     * @param idUsuario id do vendedor
     * @param dataVenda data da venda
     * @param valorTotal valor total da venda
     * @param quantidade quantidade total de itens
     * @param observacao observação opcional
     */
    public Venda(int id, int idCliente, int idUsuario,
                 LocalDate dataVenda, double valorTotal,
                 int quantidade, String observacao) {

        this.id = id;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    /* Getters e Setters */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeMaquina() {
        return nomeMaquina;
    }

    public void setNomeMaquina(String nomeMaquina) {
        this.nomeMaquina = nomeMaquina;
    }

    public String getCondicaoMaquina() {
        return condicaoMaquina;
    }

    public void setCondicaoMaquina(String condicaoMaquina) {
        this.condicaoMaquina = condicaoMaquina;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(double margemLucro) {
        this.margemLucro = margemLucro;
    }

    public List<Maquina> getMaquinasVendidas() {
        return maquinasVendidas;
    }

    public void setMaquinasVendidas(List<Maquina> maquinasVendidas) {
        this.maquinasVendidas = maquinasVendidas;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(double custoTotal) {
        this.custoTotal = custoTotal;
    }

    /** @return preço unitário usado na venda (histórico) */
    public double getPrecoUnitarioHistorico() {
        return precoUnitarioHistorico;
    }

    /** Define o preço unitário histórico armazenado na venda. */
    public void setPrecoUnitarioHistorico(double precoUnitarioHistorico) {
        this.precoUnitarioHistorico = precoUnitarioHistorico;
    }

    /** @return subtotal histórico (qtd × preço unitário) */
    public double getSubtotalHistorico() {
        return subtotalHistorico;
    }

    /** Define o subtotal histórico registrado na venda. */
    public void setSubtotalHistorico(double subtotalHistorico) {
        this.subtotalHistorico = subtotalHistorico;
    }

    /*Métodos utilitários*/

    /**
     * Calcula o lucro bruto considerando os preços cadastrados.
     *
     * @return lucro bruto
     */
    public double calcularLucro() {
        return precoVenda - precoCusto;
    }

    /**
     * Calcula a margem de lucro percentual.
     *
     * @return margem percentual
     */
    public double calcularMargemLucro() {
        if (precoCusto > 0) {
            return ((precoVenda - precoCusto) / precoCusto) * 100;
        }
        return 0.0;
    }
}
