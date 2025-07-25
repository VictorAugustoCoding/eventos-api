package com.portfolio.eventos.dto;

import com.portfolio.eventos.entity.Evento;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventoDTO {

    private Long id;

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(max = 200, message = "Nome do evento deve ter no máximo 200 caracteres")
    private String nome;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate dataFim;

    private LocalTime horaInicio;
    private LocalTime horaFim;

    @Min(value = 0, message = "Capacidade máxima deve ser maior ou igual a zero")
    private Integer capacidadeMaxima;

    @DecimalMin(value = "0.0", inclusive = true, message = "Preço deve ser maior ou igual a zero")
    private BigDecimal preco;

    private Evento.StatusEvento status;

    @NotNull(message = "ID do local é obrigatório")
    private Long localId;

    @NotNull(message = "ID da categoria é obrigatório")
    private Long categoriaId;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    private Integer vagasDisponiveis;
    private Long numeroInscricoesConfirmadas;

    // Construtores
    public EventoDTO() {}

    public EventoDTO(Long id, String nome, String descricao, LocalDate dataInicio, LocalDate dataFim,
                     LocalTime horaInicio, LocalTime horaFim, Integer capacidadeMaxima, BigDecimal preco,
                     Evento.StatusEvento status, Long localId, Long categoriaId, LocalDateTime dataCriacao,
                     LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.capacidadeMaxima = capacidadeMaxima;
        this.preco = preco;
        this.status = status;
        this.localId = localId;
        this.categoriaId = categoriaId;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Evento.StatusEvento getStatus() {
        return status;
    }

    public void setStatus(Evento.StatusEvento status) {
        this.status = status;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
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

    public Integer getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(Integer vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public Long getNumeroInscricoesConfirmadas() {
        return numeroInscricoesConfirmadas;
    }

    public void setNumeroInscricoesConfirmadas(Long numeroInscricoesConfirmadas) {
        this.numeroInscricoesConfirmadas = numeroInscricoesConfirmadas;
    }

    @Override
    public String toString() {
        return "EventoDTO{" +
                "id=" + id +
                ", nome=\'" + nome + "\'" +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", status=" + status +
                ", localId=" + localId +
                ", categoriaId=" + categoriaId +
                "}";
    }
}


