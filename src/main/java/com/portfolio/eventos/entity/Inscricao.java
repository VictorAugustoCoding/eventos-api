package com.portfolio.eventos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"participante_id", "evento_id"}))
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    @NotNull(message = "Participante é obrigatório")
    private Participante participante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    @NotNull(message = "Evento é obrigatório")
    private Evento evento;

    @CreationTimestamp
    @Column(name = "data_inscricao", nullable = false, updatable = false)
    private LocalDateTime dataInscricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInscricao status = StatusInscricao.PENDENTE;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Enum para status da inscrição
    public enum StatusInscricao {
        PENDENTE, CONFIRMADA, CANCELADA
    }

    // Construtores
    public Inscricao() {}

    public Inscricao(Participante participante, Evento evento) {
        this.participante = participante;
        this.evento = evento;
        this.status = StatusInscricao.PENDENTE;
    }

    public Inscricao(Participante participante, Evento evento, StatusInscricao status) {
        this.participante = participante;
        this.evento = evento;
        this.status = status;
    }

    // Métodos de negócio
    public boolean isPendente() {
        return status == StatusInscricao.PENDENTE;
    }

    public boolean isConfirmada() {
        return status == StatusInscricao.CONFIRMADA;
    }

    public boolean isCancelada() {
        return status == StatusInscricao.CANCELADA;
    }

    public void confirmar() {
        this.status = StatusInscricao.CONFIRMADA;
    }

    public void cancelar() {
        this.status = StatusInscricao.CANCELADA;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public StatusInscricao getStatus() {
        return status;
    }

    public void setStatus(StatusInscricao status) {
        this.status = status;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Inscricao{" +
                "id=" + id +
                ", participante=" + (participante != null ? participante.getNome() : "null") +
                ", evento=" + (evento != null ? evento.getNome() : "null") +
                ", dataInscricao=" + dataInscricao +
                ", status=" + status +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}

