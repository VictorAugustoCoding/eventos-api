package com.portfolio.eventos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(max = 200, message = "Nome do evento deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nome;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String descricao;

    @NotNull(message = "Data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    @Min(value = 0, message = "Capacidade máxima deve ser maior ou igual a zero")
    @Column(name = "capacidade_maxima")
    private Integer capacidadeMaxima;

    @DecimalMin(value = "0.0", inclusive = true, message = "Preço deve ser maior ou igual a zero")
    @Column(precision = 10, scale = 2)
    private BigDecimal preco = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEvento status = StatusEvento.EM_BREVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id", nullable = false)
    @NotNull(message = "Local é obrigatório")
    private Local local;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoes;

    // Enum para status do evento
    public enum StatusEvento {
        EM_BREVE, ATIVO, CANCELADO, CONCLUIDO
    }

    // Construtores
    public Evento() {}

    public Evento(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim, 
                  LocalTime horaInicio, LocalTime horaFim, Integer capacidadeMaxima, 
                  BigDecimal preco, Local local, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.capacidadeMaxima = capacidadeMaxima;
        this.preco = preco != null ? preco : BigDecimal.ZERO;
        this.local = local;
        this.categoria = categoria;
        this.status = StatusEvento.EM_BREVE;
    }

    // Métodos de negócio
    public boolean temCapacidadeIlimitada() {
        return capacidadeMaxima == null || capacidadeMaxima == 0;
    }

    public boolean isGratuito() {
        return preco.compareTo(BigDecimal.ZERO) == 0;
    }

    public long getNumeroInscricoesConfirmadas() {
        if (inscricoes == null) return 0;
        return inscricoes.stream()
                .filter(inscricao -> inscricao.getStatus() == Inscricao.StatusInscricao.CONFIRMADA)
                .count();
    }

    public boolean temVagasDisponiveis() {
        if (temCapacidadeIlimitada()) return true;
        return getNumeroInscricoesConfirmadas() < capacidadeMaxima;
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

    public StatusEvento getStatus() {
        return status;
    }

    public void setStatus(StatusEvento status) {
        this.status = status;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", capacidadeMaxima=" + capacidadeMaxima +
                ", preco=" + preco +
                ", status=" + status +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}

