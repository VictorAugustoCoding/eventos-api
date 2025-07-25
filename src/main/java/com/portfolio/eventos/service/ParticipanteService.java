package com.portfolio.eventos.service;

import com.portfolio.eventos.dto.ParticipanteDTO;
import com.portfolio.eventos.dto.ParticipanteRequestDTO;
import com.portfolio.eventos.entity.Participante;
import com.portfolio.eventos.exception.ResourceNotFoundException;
import com.portfolio.eventos.exception.ValidationException;
import com.portfolio.eventos.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ParticipanteService(ParticipanteRepository participanteRepository, PasswordEncoder passwordEncoder) {
        this.participanteRepository = participanteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ParticipanteDTO criarParticipante(ParticipanteRequestDTO participanteDTO) {
        if (participanteRepository.existsByEmail(participanteDTO.getEmail())) {
            throw new ValidationException("Já existe um participante com este email.");
        }

        Participante participante = new Participante(
                participanteDTO.getNome(),
                participanteDTO.getEmail(),
                participanteDTO.getTelefone(),
                passwordEncoder.encode(participanteDTO.getSenha())
        );
        participante.setRole(participanteDTO.getRole());
        participante = participanteRepository.save(participante);
        return toDTO(participante);
    }

    @Transactional(readOnly = true)
    public List<ParticipanteDTO> listarParticipantes() {
        return participanteRepository.findAll().stream()
                .map(this::toDTOComTotais)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParticipanteDTO buscarParticipantePorId(Long id) {
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado com ID: " + id));
        return toDTOComTotais(participante);
    }

    @Transactional
    public ParticipanteDTO atualizarParticipante(Long id, ParticipanteRequestDTO participanteDTO) {
        Participante participanteExistente = participanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado com ID: " + id));

        if (!participanteExistente.getEmail().equalsIgnoreCase(participanteDTO.getEmail()) &&
            participanteRepository.existsByEmail(participanteDTO.getEmail())) {
            throw new ValidationException("Já existe outro participante com este email.");
        }

        participanteExistente.setNome(participanteDTO.getNome());
        participanteExistente.setEmail(participanteDTO.getEmail());
        participanteExistente.setTelefone(participanteDTO.getTelefone());
        // A senha só deve ser atualizada se uma nova for fornecida
        if (participanteDTO.getSenha() != null && !participanteDTO.getSenha().isEmpty()) {
            participanteExistente.setSenha(passwordEncoder.encode(participanteDTO.getSenha()));
        }
        participanteExistente.setRole(participanteDTO.getRole());
        participanteExistente = participanteRepository.save(participanteExistente);
        return toDTO(participanteExistente);
    }

    @Transactional
    public void deletarParticipante(Long id) {
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado com ID: " + id));

        if (participanteRepository.countInscricoesByParticipanteId(id) > 0) {
            throw new ValidationException("Não é possível deletar participante com inscrições associadas.");
        }
        participanteRepository.delete(participante);
    }

    // Métodos de conversão Entity para DTO
    private ParticipanteDTO toDTO(Participante participante) {
        return new ParticipanteDTO(
                participante.getId(),
                participante.getNome(),
                participante.getEmail(),
                participante.getTelefone(),
                participante.getRole(),
                participante.getDataCriacao(),
                participante.getDataAtualizacao()
        );
    }

    private ParticipanteDTO toDTOComTotais(Participante participante) {
        ParticipanteDTO dto = toDTO(participante);
        dto.setTotalInscricoes(participanteRepository.countInscricoesByParticipanteId(participante.getId()));
        dto.setTotalInscricoesConfirmadas(participanteRepository.countInscricoesConfirmadasByParticipanteId(participante.getId()));
        return dto;
    }
}


