package com.portfolio.eventos.repository;

import com.portfolio.eventos.entity.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Busca eventos por status
     */
    List<Evento> findByStatus(Evento.StatusEvento status);

    /**
     * Busca eventos por status com paginação
     */
    Page<Evento> findByStatus(Evento.StatusEvento status, Pageable pageable);

    /**
     * Busca eventos por categoria
     */
    List<Evento> findByCategoriaId(Long categoriaId);

    /**
     * Busca eventos por local
     */
    List<Evento> findByLocalId(Long localId);

    /**
     * Busca eventos que contenham o texto especificado no nome ou descrição
     */
    @Query("SELECT e FROM Evento e WHERE " +
           "LOWER(e.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(e.descricao) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<Evento> findByTextoContaining(@Param("texto") String texto, Pageable pageable);

    /**
     * Busca eventos por intervalo de datas
     */
    List<Evento> findByDataInicioBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca eventos que começam a partir de uma data específica
     */
    List<Evento> findByDataInicioGreaterThanEqual(LocalDate data);

    /**
     * Busca eventos que terminam até uma data específica
     */
    List<Evento> findByDataFimLessThanEqual(LocalDate data);

    /**
     * Busca eventos gratuitos
     */
    List<Evento> findByPreco(BigDecimal preco);

    /**
     * Busca eventos com preço menor ou igual ao valor especificado
     */
    List<Evento> findByPrecoLessThanEqual(BigDecimal precoMaximo);

    /**
     * Busca eventos com vagas disponíveis (capacidade ilimitada ou com vagas)
     */
    @Query("SELECT e FROM Evento e WHERE " +
           "e.capacidadeMaxima IS NULL OR e.capacidadeMaxima = 0 OR " +
           "(SELECT COUNT(i) FROM Inscricao i WHERE i.evento = e AND i.status = 'CONFIRMADA') < e.capacidadeMaxima")
    List<Evento> findEventosComVagasDisponiveis();

    /**
     * Busca eventos próximos (nos próximos N dias)
     */
    @Query("SELECT e FROM Evento e WHERE e.dataInicio BETWEEN :hoje AND :dataLimite ORDER BY e.dataInicio")
    List<Evento> findEventosProximos(@Param("hoje") LocalDate hoje, @Param("dataLimite") LocalDate dataLimite);

    /**
     * Busca eventos com filtros múltiplos
     */
    @Query("SELECT e FROM Evento e WHERE " +
           "(:categoriaId IS NULL OR e.categoria.id = :categoriaId) AND " +
           "(:localId IS NULL OR e.local.id = :localId) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:dataInicio IS NULL OR e.dataInicio >= :dataInicio) AND " +
           "(:dataFim IS NULL OR e.dataFim <= :dataFim) AND " +
           "(:precoMaximo IS NULL OR e.preco <= :precoMaximo)")
    Page<Evento> findEventosComFiltros(
            @Param("categoriaId") Long categoriaId,
            @Param("localId") Long localId,
            @Param("status") Evento.StatusEvento status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("precoMaximo") BigDecimal precoMaximo,
            Pageable pageable);

    /**
     * Conta o número de inscrições confirmadas para um evento
     */
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.evento.id = :eventoId AND i.status = 'CONFIRMADA'")
    long countInscricoesConfirmadasByEventoId(@Param("eventoId") Long eventoId);

    /**
     * Busca eventos ordenados por data de início (mais próximos primeiro)
     */
    List<Evento> findAllByOrderByDataInicioAsc();

    /**
     * Busca eventos mais populares (com mais inscrições confirmadas)
     */
    @Query("SELECT e FROM Evento e LEFT JOIN e.inscricoes i " +
           "WHERE i.status = 'CONFIRMADA' OR i.status IS NULL " +
           "GROUP BY e " +
           "ORDER BY COUNT(i) DESC")
    List<Evento> findEventosMaisPopulares(Pageable pageable);
}

