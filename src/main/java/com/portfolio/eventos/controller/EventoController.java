package com.portfolio.eventos.controller;

import com.portfolio.eventos.dto.EventoDTO;
import com.portfolio.eventos.entity.Evento;
import com.portfolio.eventos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoDTO> criarEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO novoEvento = eventoService.criarEvento(eventoDTO);
        return new ResponseEntity<>(novoEvento, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EventoDTO>> listarEventos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<EventoDTO> eventos = eventoService.listarEventos(pageable);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> buscarEventoPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.buscarEventoPorId(id);
        return ResponseEntity.ok(evento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> atualizarEvento(@PathVariable Long id, @Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO eventoAtualizado = eventoService.atualizarEvento(id, eventoDTO);
        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        eventoService.deletarEvento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
    public ResponseEntity<Page<EventoDTO>> buscarEventosComFiltros(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long localId,
            @RequestParam(required = false) Evento.StatusEvento status,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<EventoDTO> eventos = eventoService.buscarEventosComFiltros(
                categoriaId, localId, status, dataInicio, dataFim, precoMaximo, pageable);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<EventoDTO>> buscarEventosProximos(@RequestParam(defaultValue = "7") int dias) {
        List<EventoDTO> eventos = eventoService.buscarEventosProximos(dias);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/populares")
    public ResponseEntity<List<EventoDTO>> buscarEventosMaisPopulares(@RequestParam(defaultValue = "5") int limit) {
        List<EventoDTO> eventos = eventoService.buscarEventosMaisPopulares(limit);
        return ResponseEntity.ok(eventos);
    }
}

