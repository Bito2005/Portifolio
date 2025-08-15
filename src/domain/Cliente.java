package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade Cliente - Representa um cliente da locadora
 * Campos obrigatórios: nome, cpf, email, telefone, cep, senha
 */
public class Cliente {
    private String id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String cep;
    private String endereco;
    private String senha;
    private LocalDate dataCadastro;
    private LocalDateTime ultimoLogin;
    private boolean ativo;

    // Construtores
    public Cliente() {
        this.dataCadastro = LocalDate.now();
        this.ativo = true;
    }

    public Cliente(String nome, String cpf, String email, String telefone, String cep, String senha) {
        this();
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.cep = cep;
        this.senha = senha;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    @Override
    public String toString() {
        return nome + " (" + cpf + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return cpf != null ? cpf.equals(cliente.cpf) : cliente.cpf == null;
    }

    @Override
    public int hashCode() {
        return cpf != null ? cpf.hashCode() : 0;
    }
}
