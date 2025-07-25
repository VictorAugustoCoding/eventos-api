package com.portfolio.eventos.service;

import com.portfolio.eventos.dto.EventoDTO;
import com.portfolio.eventos.entity.Categoria;
import com.portfolio.eventos.entity.Evento;
import com.portfolio.eventos.entity.Local;
import com.portfolio.eventos.exception.ResourceNotFoundException;
import com.portfolio.eventos.exception.ValidationException;
import com.portfolio.eventos.repository.CategoriaRepository;
import com.portfolio.eventos.repository.EventoRepository;
import com.portfolio.eventos.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final LocalRepository localRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository, CategoriaRepository categoriaRepository, LocalRepository localRepository) {
        this.eventoRepository = eventoRepository;
        this.categoriaRepository = categoriaRepository;
        this.localRepository = localRepository;
    }

    @Transactional
    public EventoDTO criarEvento(EventoDTO eventoDTO) {
        Categoria categoria = categoriaRepository.findById(eventoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + eventoDTO.getCategoriaId()));
        Local local = localRepository.findById(eventoDTO.getLocalId())
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + eventoDTO.getLocalId()));

        if (eventoDTO.getDataInicio().isAfter(eventoDTO.getDataFim())) {
            throw new ValidationException("Data de início não pode ser depois da data de fim.");
        }

        Evento evento = new Evento(
                eventoDTO.getNome(),
                eventoDTO.getDescricao(),
                eventoDTO.getDataInicio(),
                eventoDTO.getDataFim(),
                eventoDTO.getHoraInicio(),
                eventoDTO.getHoraFim(),
                eventoDTO.getCapacidadeMaxima(),
                eventoDTO.getPreco(),
                local,
                categoria
        );
        evento.setStatus(eventoDTO.getStatus() != null ? eventoDTO.getStatus() : Evento.StatusEvento.EM_BREVE);
        evento = eventoRepository.save(evento);
        return toDTO(evento);
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> listarEventos(Pageable pageable) {
        return eventoRepository.findAll(pageable).map(this::toDTOComTotais);
    }

    @Transactional(readOnly = true)
    public EventoDTO buscarEventoPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
        return toDTOComTotais(evento);
    }

    @Transactional
    public EventoDTO atualizarEvento(Long id, EventoDTO eventoDTO) {
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));

        Categoria categoria = categoriaRepository.findById(eventoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + eventoDTO.getCategoriaId()));
        Local local = localRepository.findById(eventoDTO.getLocalId())
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + eventoDTO.getLocalId()));

        if (eventoDTO.getDataInicio().isAfter(eventoDTO.getDataFim())) {
            throw new ValidationException("Data de início não pode ser depois da data de fim.");
        }

        eventoExistente.setNome(eventoDTO.getNome());
        eventoExistente.setDescricao(eventoDTO.getDescricao());
        eventoExistente.setDataInicio(eventoDTO.getDataInicio());
        eventoExistente.setDataFim(eventoDTO.getDataFim());
        eventoExistente.setHoraInicio(eventoDTO.getHoraInicio());
        eventoExistente.setHoraFim(eventoDTO.getHoraFim());
        eventoExistente.setCapacidadeMaxima(eventoDTO.getCapacidadeMaxima());
        eventoExistente.setPreco(eventoDTO.getPreco());
        eventoExistente.setStatus(eventoDTO.getStatus());
        eventoExistente.setLocal(local);
        eventoExistente.setCategoria(categoria);

        eventoExistente = eventoRepository.save(eventoExistente);
        return toDTO(eventoExistente);
    }

    @Transactional
    public void deletarEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));

        if (eventoRepository.countInscricoesConfirmadasByEventoId(id) > 0) {
            throw new ValidationException("Não é possível deletar evento com inscrições confirmadas.");
        }
        eventoRepository.delete(evento);
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> buscarEventosComFiltros(
            Long categoriaId,
            Long localId,
            Evento.StatusEvento status,
            LocalDate dataInicio,
            LocalDate dataFim,
            BigDecimal precoMaximo,
            Pageable pageable) {
        return eventoRepository.findEventosComFiltros(
                categoriaId, localId, status, dataInicio, dataFim, precoMaximo, pageable)
                .map(this::toDTOComTotais);
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> buscarEventosProximos(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(dias);
        return eventoRepository.findEventosProximos(hoje, dataLimite).stream()
                .map(this::toDTOComTotais)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> buscarEventosMaisPopulares(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return eventoRepository.findEventosMaisPopulares(pageable).stream()
                .map(this::toDTOComTotais)
                .collect(Collectors.toList());
    }

    // Métodos de conversão Entity para DTO
    private EventoDTO toDTO(Evento evento) {
        return new EventoDTO(
                evento.getId(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getDataInicio(),
                evento.getDataFim(),
                evento.getHoraInicio(),
                evento.getHoraFim(),
                evento.getCapacidadeMaxima(),
                evento.getPreco(),
                evento.getStatus(),
                evento.getLocal().getId(),
                evento.getCategoria().getId(),
                evento.getDataCriacao(),
                evento.getDataAtualizacao()
        );
    }

    private EventoDTO toDTOComTotais(Evento evento) {
        EventoDTO dto = toDTO(evento);
        dto.setVagasDisponiveis(evento.temCapacidadeIlimitada() ? -1 : (int)(evento.getCapacidadeMaxima() - evento.getNumeroInscricoesConfirmadas()));
        dto.setNumeroInscricoesConfirmadas(evento.getNumeroInscricoesConfirmadas());
        return dto;
    }
}


