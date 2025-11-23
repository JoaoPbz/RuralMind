package model;

/**
 * Classe que representa uma máquina agrícola usada no sistema.
 * Armazena informacoes do modelo, marca, condicao, preços e estoque,
 * e do fornecedor ligado a essa máquina.  
 * 
 * Também possui campos opcionais usados apenas para exibição em relatórios,
 * contendo o preço histórico da venda e o subtotal calculado
 */
public class Maquina {

    private int id;
    private String modelo;
    private String marca;
    private int ano;
    private String novaUsada;
    private double precoCusto;
    private double precoVenda;
    private int quantia;
    private String status;
    private int idFornecedor;
    private String fornecedor;

    /** Preço unitário da máquina da venda (somente para relatórios). */
    private Double precoUnitarioHistorico;

    /** Subtotal calculado na venda (somente para relatórios). */
    private Double subtotalHistorico;

    public Maquina() {}

    public Maquina(int id, String modelo, String marca, int ano, String novaUsada,
                   double precoCusto, double precoVenda, int quantia,
                   String status, int idFornecedor) {

        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.novaUsada = novaUsada;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantia = quantia;
        this.status = status;
        this.idFornecedor = idFornecedor;
    }

    //getters e setters padrões//

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public String getNovaUsada() { return novaUsada; }
    public void setNovaUsada(String novaUsada) { this.novaUsada = novaUsada; }

    public double getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }

    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }

    public int getQuantia() { return quantia; }
    public void setQuantia(int quantia) { this.quantia = quantia; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(int idFornecedor) { this.idFornecedor = idFornecedor; }

    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }

    //campos históricos (somente relatório)//

    /** @return preço unitário no momento da venda */
    public Double getPrecoUnitarioHistorico() {
        return precoUnitarioHistorico;
    }

    /** Define o preço histórico do item na venda */
    public void setPrecoUnitarioHistorico(Double precoUnitarioHistorico) {
        this.precoUnitarioHistorico = precoUnitarioHistorico;
    }

    /** @return subtotal histórico do item */
    public Double getSubtotalHistorico() {
        return subtotalHistorico;
    }

    /** Define o subtotal calculado na venda */
    public void setSubtotalHistorico(Double subtotalHistorico) {
        this.subtotalHistorico = subtotalHistorico;
    }
}
