package com.portfolio.eventos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class LocalDTO {

    private Long id;

    @NotBlank(message = "Nome do local é obrigatório")
    @Size(max = 150, message = "Nome do local deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 300, message = "Endereço deve ter no máximo 300 caracteres")
    private String endereco;

    @Min(value = 1, message = "Capacidade deve ser maior que zero")
    private Integer capacidade;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Long totalEventos;

    // Construtores
    public LocalDTO() {}

    public LocalDTO(String nome, String endereco, Integer capacidade) {
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
    }

    public LocalDTO(Long id, String nome, String endereco, Integer capacidade, 
                    LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
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

    public Long getTotalEventos() {
        return totalEventos;
    }

    public void setTotalEventos(Long totalEventos) {
        this.totalEventos = totalEventos;
    }

    @Override
    public String toString() {
        return "LocalDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", capacidade=" + capacidade +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                ", totalEventos=" + totalEventos +
                '}';
    }
}

