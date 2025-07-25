package com.portfolio.eventos.dto;

import com.portfolio.eventos.entity.Inscricao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class InscricaoDTO {

    private Long id;

    @NotNull(message = "ID do participante é obrigatório")
    private Long participanteId;

    @NotNull(message = "ID do evento é obrigatório")
    private Long eventoId;

    private LocalDateTime dataInscricao;
    private Inscricao.StatusInscricao status;
    private LocalDateTime dataAtualizacao;

    private String nomeParticipante;
    private String nomeEvento;

    // Construtores
    public InscricaoDTO() {}

    public InscricaoDTO(Long id, Long participanteId, Long eventoId, LocalDateTime dataInscricao,
                        Inscricao.StatusInscricao status, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.participanteId = participanteId;
        this.eventoId = eventoId;
        this.dataInscricao = dataInscricao;
        this.status = status;
        this.dataAtualizacao = dataAtualizacao;
    }

    public InscricaoDTO(Long id, Long participanteId, Long eventoId, LocalDateTime dataInscricao,
                        Inscricao.StatusInscricao status, LocalDateTime dataAtualizacao, 
                        String nomeParticipante, String nomeEvento) {
        this.id = id;
        this.participanteId = participanteId;
        this.eventoId = eventoId;
        this.dataInscricao = dataInscricao;
        this.status = status;
        this.dataAtualizacao = dataAtualizacao;
        this.nomeParticipante = nomeParticipante;
        this.nomeEvento = nomeEvento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(Long participanteId) {
        this.participanteId = participanteId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public Inscricao.StatusInscricao getStatus() {
        return status;
    }

    public void setStatus(Inscricao.StatusInscricao status) {
        this.status = status;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    @Override
    public String toString() {
        return "InscricaoDTO{" +
                "id=" + id +
                ", participanteId=" + participanteId +
                ", eventoId=" + eventoId +
                ", dataInscricao=" + dataInscricao +
                ", status=" + status +
                ", dataAtualizacao=" + dataAtualizacao +
                ", nomeParticipante=\'" + nomeParticipante + "\'" +
                ", nomeEvento=\'" + nomeEvento + "\'" +
                "}";
    }
}


