package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade Funcionário - Representa um funcionário da locadora
 * Tipos: ADMIN, FUNCIONARIO
 */
public class Funcionario {
    public enum TipoFuncionario {
        ADMIN("Administrador"),
        FUNCIONARIO("Funcionário");

        private final String descricao;

        TipoFuncionario(String descricao) {
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
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    private double salario;
    private String usuario;
    private String senha;
    private TipoFuncionario tipo;
    private LocalDate dataAdmissao;
    private LocalDate dataCadastro;
    private LocalDateTime ultimoLogin;
    private boolean ativo;
    private String observacoes;

    // Construtores
    public Funcionario() {
        this.dataAdmissao = LocalDate.now();
        this.dataCadastro = LocalDate.now();
        this.ativo = true;
        this.tipo = TipoFuncionario.FUNCIONARIO;
    }

    public Funcionario(String nome, String cpf, String email, String telefone, 
                       String usuario, String senha, TipoFuncionario tipo) {
        this();
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.usuario = usuario;
        this.senha = senha;
        this.tipo = tipo;
    }
    
    public Funcionario(String id, String nome, String cpf, String email, String telefone, 
                       String endereco, double salario, TipoFuncionario tipo, LocalDate dataCadastro) {
        this();
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.salario = salario;
        this.tipo = tipo;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoFuncionario getTipo() {
        return tipo;
    }

    public void setTipo(TipoFuncionario tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public boolean isAdmin() {
        return tipo == TipoFuncionario.ADMIN;
    }

    @Override
    public String toString() {
        return nome + " (" + usuario + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Funcionario funcionario = (Funcionario) obj;
        return usuario != null ? usuario.equals(funcionario.usuario) : funcionario.usuario == null;
    }

    @Override
    public int hashCode() {
        return usuario != null ? usuario.hashCode() : 0;
    }
}
