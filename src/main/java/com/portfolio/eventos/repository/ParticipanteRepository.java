package com.portfolio.eventos.repository;

import com.portfolio.eventos.entity.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    /**
     * Busca um participante pelo email (usado para autenticação)
     */
    Optional<Participante> findByEmail(String email);

    /**
     * Verifica se existe um participante com o email especificado
     */
    boolean existsByEmail(String email);

    /**
     * Busca participantes pelo nome (case-insensitive, busca parcial)
     */
    List<Participante> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca participantes por role
     */
    List<Participante> findByRole(Participante.Role role);

    /**
     * Busca participantes que contenham o texto especificado no nome ou email
     */
    @Query("SELECT p FROM Participante p WHERE " +
           "LOWER(p.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Participante> findByTextoContaining(@Param("texto") String texto);

    /**
     * Conta o número de inscrições de um participante
     */
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.participante.id = :participanteId")
    long countInscricoesByParticipanteId(@Param("participanteId") Long participanteId);

    /**
     * Conta o número de inscrições confirmadas de um participante
     */
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.participante.id = :participanteId AND i.status = 'CONFIRMADA'")
    long countInscricoesConfirmadasByParticipanteId(@Param("participanteId") Long participanteId);

    /**
     * Busca participantes ordenados por data de criação (mais recentes primeiro)
     */
    List<Participante> findAllByOrderByDataCriacaoDesc();
}

