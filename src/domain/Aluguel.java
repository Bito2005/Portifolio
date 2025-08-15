package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;

/**
 * Entidade Aluguel - Representa um aluguel de veículo
 * Status: ATIVO, FINALIZADO, CANCELADO
 */
public class Aluguel {
    public enum StatusAluguel {
        ATIVO("Ativo"),
        FINALIZADO("Finalizado"),
        CANCELADO("Cancelado"),
        ATRASADO("Atrasado");

        private final String descricao;

        StatusAluguel(String descricao) {
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
    private String clienteId;
    private String veiculoId;
    private String funcionarioId;
    private LocalDate dataInicio;
    private LocalDate dataFimPrevista;
    private LocalDate dataFimReal;
    private LocalDateTime dataHoraCriacao;
    private int quilometragemInicial;
    private int quilometragemFinal;
    private BigDecimal valorDiaria;
    private BigDecimal valorTotal;
    private BigDecimal valorMulta;
    private StatusAluguel status;
    private String observacoes;

    // Referencias para facilitar exibição
    private Cliente cliente;
    private Veiculo veiculo;
    private Funcionario funcionario;

    // Construtores
    public Aluguel() {
        this.dataHoraCriacao = LocalDateTime.now();
        this.status = StatusAluguel.ATIVO;
        this.valorTotal = BigDecimal.ZERO;
        this.valorMulta = BigDecimal.ZERO;
    }

    public Aluguel(String clienteId, String veiculoId, String funcionarioId,
                   LocalDate dataInicio, LocalDate dataFimPrevista,
                   BigDecimal valorDiaria, int quilometragemInicial) {
        this();
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.funcionarioId = funcionarioId;
        this.dataInicio = dataInicio;
        this.dataFimPrevista = dataFimPrevista;
        this.valorDiaria = valorDiaria;
        this.quilometragemInicial = quilometragemInicial;
        calcularValorTotal();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(String veiculoId) {
        this.veiculoId = veiculoId;
    }

    public String getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(String funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
        calcularValorTotal();
    }

    public LocalDate getDataFimPrevista() {
        return dataFimPrevista;
    }

    public void setDataFimPrevista(LocalDate dataFimPrevista) {
        this.dataFimPrevista = dataFimPrevista;
        calcularValorTotal();
    }

    public LocalDate getDataFimReal() {
        return dataFimReal;
    }

    public void setDataFimReal(LocalDate dataFimReal) {
        this.dataFimReal = dataFimReal;
        calcularValorTotal();
        calcularMulta();
    }

    public LocalDateTime getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public int getQuilometragemInicial() {
        return quilometragemInicial;
    }

    public void setQuilometragemInicial(int quilometragemInicial) {
        this.quilometragemInicial = quilometragemInicial;
    }

    public int getQuilometragemFinal() {
        return quilometragemFinal;
    }

    public void setQuilometragemFinal(int quilometragemFinal) {
        this.quilometragemFinal = quilometragemFinal;
    }

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(BigDecimal valorDiaria) {
        this.valorDiaria = valorDiaria;
        calcularValorTotal();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public StatusAluguel getStatus() {
        return status;
    }

    public void setStatus(StatusAluguel status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // Referencias
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    // Métodos de cálculo
    public long getDiasAluguel() {
        LocalDate dataFim = dataFimReal != null ? dataFimReal : dataFimPrevista;
        return ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
    }

    public long getDiasAtraso() {
        if (dataFimReal != null && dataFimReal.isAfter(dataFimPrevista)) {
            return ChronoUnit.DAYS.between(dataFimPrevista, dataFimReal);
        }
        return 0;
    }

    public boolean isAtrasado() {
        return LocalDate.now().isAfter(dataFimPrevista) && status == StatusAluguel.ATIVO;
    }

    private void calcularValorTotal() {
        if (valorDiaria != null && dataInicio != null && dataFimPrevista != null) {
            long dias = getDiasAluguel();
            valorTotal = valorDiaria.multiply(BigDecimal.valueOf(dias));
        }
    }

    private void calcularMulta() {
        long diasAtraso = getDiasAtraso();
        if (diasAtraso > 0 && valorDiaria != null) {
            // Multa de 10% por dia de atraso
            BigDecimal multa = valorDiaria.multiply(BigDecimal.valueOf(diasAtraso))
                                          .multiply(new BigDecimal("0.10"));
            valorMulta = multa;
        }
    }

    public void finalizar(LocalDate dataFim, int quilometragemFinal) {
        this.dataFimReal = dataFim;
        this.quilometragemFinal = quilometragemFinal;
        this.status = StatusAluguel.FINALIZADO;
        calcularValorTotal();
        calcularMulta();
    }

    @Override
    public String toString() {
        return "Aluguel #" + id + " - " + 
               (cliente != null ? cliente.getNome() : clienteId) + " - " +
               (veiculo != null ? veiculo.toString() : veiculoId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Aluguel aluguel = (Aluguel) obj;
        return id != null ? id.equals(aluguel.id) : aluguel.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
