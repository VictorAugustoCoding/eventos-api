package com.portfolio.eventos.repository;

import com.portfolio.eventos.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {

    /**
     * Busca um local pelo nome (case-insensitive)
     */
    Optional<Local> findByNomeIgnoreCase(String nome);

    /**
     * Verifica se existe um local com o nome especificado (case-insensitive)
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Busca locais que contenham o texto especificado no nome ou endereço
     */
    @Query("SELECT l FROM Local l WHERE " +
           "LOWER(l.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(l.endereco) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Local> findByTextoContaining(@Param("texto") String texto);

    /**
     * Busca locais com capacidade maior ou igual ao valor especificado
     */
    List<Local> findByCapacidadeGreaterThanEqual(Integer capacidadeMinima);

    /**
     * Busca locais com capacidade entre os valores especificados
     */
    List<Local> findByCapacidadeBetween(Integer capacidadeMinima, Integer capacidadeMaxima);

    /**
     * Conta o número de eventos por local
     */
    @Query("SELECT COUNT(e) FROM Evento e WHERE e.local.id = :localId")
    long countEventosByLocalId(@Param("localId") Long localId);

    /**
     * Busca locais ordenados por capacidade (decrescente)
     */
    List<Local> findAllByOrderByCapacidadeDesc();
}

