package com.portfolio.eventos.repository;

import com.portfolio.eventos.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca uma categoria pelo nome (case-insensitive)
     */
    Optional<Categoria> findByNomeIgnoreCase(String nome);

    /**
     * Verifica se existe uma categoria com o nome especificado (case-insensitive)
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Busca categorias que contenham o texto especificado no nome ou descrição
     */
    @Query("SELECT c FROM Categoria c WHERE " +
           "LOWER(c.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.descricao) LIKE LOWER(CONCAT('%', :texto, '%'))")
    java.util.List<Categoria> findByTextoContaining(@Param("texto") String texto);

    /**
     * Conta o número de eventos por categoria
     */
    @Query("SELECT COUNT(e) FROM Evento e WHERE e.categoria.id = :categoriaId")
    long countEventosByCategoriaId(@Param("categoriaId") Long categoriaId);
}

