package com.portfolio.eventos.service;

import com.portfolio.eventos.dto.InscricaoDTO;
import com.portfolio.eventos.entity.Evento;
import com.portfolio.eventos.entity.Inscricao;
import com.portfolio.eventos.entity.Participante;
import com.portfolio.eventos.exception.ResourceNotFoundException;
import com.portfolio.eventos.exception.ValidationException;
import com.portfolio.eventos.repository.EventoRepository;
import com.portfolio.eventos.repository.InscricaoRepository;
import com.portfolio.eventos.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;

    @Autowired
    public InscricaoService(InscricaoRepository inscricaoRepository, 
                            ParticipanteRepository participanteRepository, 
                            EventoRepository eventoRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.participanteRepository = participanteRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public InscricaoDTO criarInscricao(InscricaoDTO inscricaoDTO) {
        Participante participante = participanteRepository.findById(inscricaoDTO.getParticipanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado com ID: " + inscricaoDTO.getParticipanteId()));
        Evento evento = eventoRepository.findById(inscricaoDTO.getEventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + inscricaoDTO.getEventoId()));

        if (inscricaoRepository.existsByParticipanteIdAndEventoId(participante.getId(), evento.getId())) {
            throw new ValidationException("Participante já inscrito neste evento.");
        }

        if (evento.getStatus() == Evento.StatusEvento.CANCELADO || evento.getStatus() == Evento.StatusEvento.CONCLUIDO) {
            throw new ValidationException("Não é possível inscrever-se em eventos cancelados ou concluídos.");
        }

        if (!evento.temCapacidadeIlimitada() && !evento.temVagasDisponiveis()) {
            throw new ValidationException("Evento lotado. Não há vagas disponíveis.");
        }

        Inscricao inscricao = new Inscricao(participante, evento);
        // Se o evento for gratuito, a inscrição já pode ser confirmada
        if (evento.isGratuito()) {
            inscricao.confirmar();
        }
        inscricao = inscricaoRepository.save(inscricao);
        return toDTO(inscricao);
    }

    @Transactional(readOnly = true)
    public Page<InscricaoDTO> listarInscricoes(Pageable pageable) {
        return inscricaoRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public InscricaoDTO buscarInscricaoPorId(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada com ID: " + id));
        return toDTO(inscricao);
    }

    @Transactional
    public InscricaoDTO atualizarInscricao(Long id, InscricaoDTO inscricaoDTO) {
        Inscricao inscricaoExistente = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada com ID: " + id));

        // Apenas o status pode ser atualizado via DTO, ou se for um admin
        // Outras alterações (participante/evento) implicariam em nova inscrição
        if (inscricaoDTO.getStatus() != null) {
            inscricaoExistente.setStatus(inscricaoDTO.getStatus());
        }
        
        inscricaoExistente = inscricaoRepository.save(inscricaoExistente);
        return toDTO(inscricaoExistente);
    }

    @Transactional
    public void deletarInscricao(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada com ID: " + id));
        inscricaoRepository.delete(inscricao);
    }

    @Transactional
    public InscricaoDTO confirmarInscricao(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada com ID: " + id));
        if (inscricao.isConfirmada()) {
            throw new ValidationException("Inscrição já está confirmada.");
        }
        if (inscricao.isCancelada()) {
            throw new ValidationException("Não é possível confirmar uma inscrição cancelada.");
        }
        inscricao.confirmar();
        return toDTO(inscricaoRepository.save(inscricao));
    }

    @Transactional
    public InscricaoDTO cancelarInscricao(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada com ID: " + id));
        if (inscricao.isCancelada()) {
            throw new ValidationException("Inscrição já está cancelada.");
        }
        inscricao.cancelar();
        return toDTO(inscricaoRepository.save(inscricao));
    }

    @Transactional(readOnly = true)
    public Page<InscricaoDTO> buscarInscricoesPorParticipante(Long participanteId, Pageable pageable) {
        return inscricaoRepository.findByParticipanteId(participanteId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<InscricaoDTO> buscarInscricoesPorEvento(Long eventoId, Pageable pageable) {
        return inscricaoRepository.findByEventoId(eventoId, pageable).map(this::toDTO);
    }

    // Métodos de conversão Entity para DTO
    private InscricaoDTO toDTO(Inscricao inscricao) {
        return new InscricaoDTO(
                inscricao.getId(),
                inscricao.getParticipante().getId(),
                inscricao.getEvento().getId(),
                inscricao.getDataInscricao(),
                inscricao.getStatus(),
                inscricao.getDataAtualizacao(),
                inscricao.getParticipante().getNome(), // Adiciona nome do participante
                inscricao.getEvento().getNome() // Adiciona nome do evento
        );
    }
}


