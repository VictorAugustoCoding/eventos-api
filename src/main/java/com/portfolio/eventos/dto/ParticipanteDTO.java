package com.portfolio.eventos.dto;

import com.portfolio.eventos.entity.Participante;

import java.time.LocalDateTime;

public class ParticipanteDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Participante.Role role;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Long totalInscricoes;
    private Long totalInscricoesConfirmadas;

    // Construtores
    public ParticipanteDTO() {}

    public ParticipanteDTO(Long id, String nome, String email, String telefone, 
                          Participante.Role role, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.role = role;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Participante.Role getRole() {
        return role;
    }

    public void setRole(Participante.Role role) {
        this.role = role;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getTotalInscricoes() {
        return totalInscricoes;
    }

    public void setTotalInscricoes(Long totalInscricoes) {
        this.totalInscricoes = totalInscricoes;
    }

    public Long getTotalInscricoesConfirmadas() {
        return totalInscricoesConfirmadas;
    }

    public void setTotalInscricoesConfirmadas(Long totalInscricoesConfirmadas) {
        this.totalInscricoesConfirmadas = totalInscricoesConfirmadas;
    }

    @Override
    public String toString() {
        return "ParticipanteDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", role=" + role +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                ", totalInscricoes=" + totalInscricoes +
                ", totalInscricoesConfirmadas=" + totalInscricoesConfirmadas +
                '}';
    }
}

