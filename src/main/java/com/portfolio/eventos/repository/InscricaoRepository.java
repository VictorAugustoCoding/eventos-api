package com.portfolio.eventos.repository;

import com.portfolio.eventos.entity.Inscricao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    /**
     * Busca inscrição específica por participante e evento
     */
    Optional<Inscricao> findByParticipanteIdAndEventoId(Long participanteId, Long eventoId);

    /**
     * Verifica se existe inscrição para um participante em um evento
     */
    boolean existsByParticipanteIdAndEventoId(Long participanteId, Long eventoId);

    /**
     * Busca todas as inscrições de um participante
     */
    List<Inscricao> findByParticipanteId(Long participanteId);

    /**
     * Busca todas as inscrições de um participante com paginação
     */
    Page<Inscricao> findByParticipanteId(Long participanteId, Pageable pageable);

    /**
     * Busca todas as inscrições de um evento
     */
    List<Inscricao> findByEventoId(Long eventoId);

    /**
     * Busca todas as inscrições de um evento com paginação
     */
    Page<Inscricao> findByEventoId(Long eventoId, Pageable pageable);

    /**
     * Busca inscrições por status
     */
    List<Inscricao> findByStatus(Inscricao.StatusInscricao status);

    /**
     * Busca inscrições por status com paginação
     */
    Page<Inscricao> findByStatus(Inscricao.StatusInscricao status, Pageable pageable);

    /**
     * Busca inscrições de um participante por status
     */
    List<Inscricao> findByParticipanteIdAndStatus(Long participanteId, Inscricao.StatusInscricao status);

    /**
     * Busca inscrições de um evento por status
     */
    List<Inscricao> findByEventoIdAndStatus(Long eventoId, Inscricao.StatusInscricao status);

    /**
     * Busca inscrições por intervalo de datas
     */
    List<Inscricao> findByDataInscricaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Conta inscrições por status para um evento
     */
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.evento.id = :eventoId AND i.status = :status")
    long countByEventoIdAndStatus(@Param("eventoId") Long eventoId, @Param("status") Inscricao.StatusInscricao status);

    /**
     * Conta inscrições por status para um participante
     */
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.participante.id = :participanteId AND i.status = :status")
    long countByParticipanteIdAndStatus(@Param("participanteId") Long participanteId, @Param("status") Inscricao.StatusInscricao status);

    /**
     * Busca inscrições mais recentes
     */
    List<Inscricao> findAllByOrderByDataInscricaoDesc();

    /**
     * Busca inscrições de eventos de uma categoria específica
     */
    @Query("SELECT i FROM Inscricao i WHERE i.evento.categoria.id = :categoriaId")
    List<Inscricao> findByEventoCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Busca inscrições de eventos em um local específico
     */
    @Query("SELECT i FROM Inscricao i WHERE i.evento.local.id = :localId")
    List<Inscricao> findByEventoLocalId(@Param("localId") Long localId);

    /**
     * Busca inscrições com filtros múltiplos
     */
    @Query("SELECT i FROM Inscricao i WHERE " +
           "(:participanteId IS NULL OR i.participante.id = :participanteId) AND " +
           "(:eventoId IS NULL OR i.evento.id = :eventoId) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:dataInicio IS NULL OR i.dataInscricao >= :dataInicio) AND " +
           "(:dataFim IS NULL OR i.dataInscricao <= :dataFim)")
    Page<Inscricao> findInscricoesComFiltros(
            @Param("participanteId") Long participanteId,
            @Param("eventoId") Long eventoId,
            @Param("status") Inscricao.StatusInscricao status,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);

    /**
     * Busca participantes mais ativos (com mais inscrições confirmadas)
     */
    @Query("SELECT i.participante, COUNT(i) as total FROM Inscricao i " +
           "WHERE i.status = 'CONFIRMADA' " +
           "GROUP BY i.participante " +
           "ORDER BY total DESC")
    List<Object[]> findParticipantesMaisAtivos(Pageable pageable);
}

