package com.portfolio.eventos.controller;

import com.portfolio.eventos.dto.InscricaoDTO;
import com.portfolio.eventos.entity.Inscricao;
import com.portfolio.eventos.service.InscricaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    @Autowired
    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @PostMapping
    public ResponseEntity<InscricaoDTO> criarInscricao(@Valid @RequestBody InscricaoDTO inscricaoDTO) {
        InscricaoDTO novaInscricao = inscricaoService.criarInscricao(inscricaoDTO);
        return new ResponseEntity<>(novaInscricao, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<InscricaoDTO>> listarInscricoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<InscricaoDTO> inscricoes = inscricaoService.listarInscricoes(pageable);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscricaoDTO> buscarInscricaoPorId(@PathVariable Long id) {
        InscricaoDTO inscricao = inscricaoService.buscarInscricaoPorId(id);
        return ResponseEntity.ok(inscricao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InscricaoDTO> atualizarInscricao(@PathVariable Long id, @Valid @RequestBody InscricaoDTO inscricaoDTO) {
        InscricaoDTO inscricaoAtualizada = inscricaoService.atualizarInscricao(id, inscricaoDTO);
        return ResponseEntity.ok(inscricaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInscricao(@PathVariable Long id) {
        inscricaoService.deletarInscricao(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<InscricaoDTO> confirmarInscricao(@PathVariable Long id) {
        InscricaoDTO inscricaoConfirmada = inscricaoService.confirmarInscricao(id);
        return ResponseEntity.ok(inscricaoConfirmada);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<InscricaoDTO> cancelarInscricao(@PathVariable Long id) {
        InscricaoDTO inscricaoCancelada = inscricaoService.cancelarInscricao(id);
        return ResponseEntity.ok(inscricaoCancelada);
    }

    @GetMapping("/participante/{participanteId}")
    public ResponseEntity<Page<InscricaoDTO>> buscarInscricoesPorParticipante(
            @PathVariable Long participanteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<InscricaoDTO> inscricoes = inscricaoService.buscarInscricoesPorParticipante(participanteId, pageable);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<Page<InscricaoDTO>> buscarInscricoesPorEvento(
            @PathVariable Long eventoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<InscricaoDTO> inscricoes = inscricaoService.buscarInscricoesPorEvento(eventoId, pageable);
        return ResponseEntity.ok(inscricoes);
    }
}

