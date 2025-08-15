package domain;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Entidade Veículo - Representa um veículo da locadora
 * Status: DISPONIVEL, ALUGADO, MANUTENCAO, INATIVO
 */
public class Veiculo {
    public enum Status {
        DISPONIVEL("Disponível"),
        ALUGADO("Alugado"),
        MANUTENCAO("Manutenção"),
        INATIVO("Inativo");

        private final String descricao;

        Status(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    public enum Categoria {
        ECONOMICO("Econômico"),
        COMPACTO("Compacto"),
        INTERMEDIARIO("Intermediário"),
        EXECUTIVO("Executivo"),
        LUXO("Luxo"),
        SUV("SUV"),
        PICKUP("Pickup");

        private final String descricao;

        Categoria(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    private String id;
    private String marca;
    private String modelo;
    private String placa;
    private String cor;
    private int ano;
    private Categoria categoria;
    private BigDecimal valorDiaria;
    private int quilometragem;
    private String combustivel;
    private Status status;
    private LocalDate dataCadastro;
    private String observacoes;

    // Construtores
    public Veiculo() {
        this.dataCadastro = LocalDate.now();
        this.status = Status.DISPONIVEL;
        this.valorDiaria = BigDecimal.ZERO;
    }

    public Veiculo(String marca, String modelo, String placa, String cor, int ano, 
                   Categoria categoria, BigDecimal valorDiaria, String combustivel) {
        this();
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
        this.ano = ano;
        this.categoria = categoria;
        this.valorDiaria = valorDiaria;
        this.combustivel = combustivel;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(BigDecimal valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public boolean isDisponivel() {
        return status == Status.DISPONIVEL;
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + placa + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Veiculo veiculo = (Veiculo) obj;
        return placa != null ? placa.equals(veiculo.placa) : veiculo.placa == null;
    }

    @Override
    public int hashCode() {
        return placa != null ? placa.hashCode() : 0;
    }
}
