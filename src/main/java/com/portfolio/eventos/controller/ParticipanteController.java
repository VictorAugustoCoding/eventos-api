package com.portfolio.eventos.controller;

import com.portfolio.eventos.dto.ParticipanteDTO;
import com.portfolio.eventos.dto.ParticipanteRequestDTO;
import com.portfolio.eventos.service.ParticipanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @Autowired
    public ParticipanteController(ParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    @PostMapping
    public ResponseEntity<ParticipanteDTO> criarParticipante(@Valid @RequestBody ParticipanteRequestDTO participanteDTO) {
        ParticipanteDTO novoParticipante = participanteService.criarParticipante(participanteDTO);
        return new ResponseEntity<>(novoParticipante, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipanteDTO>> listarParticipantes() {
        List<ParticipanteDTO> participantes = participanteService.listarParticipantes();
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteDTO> buscarParticipantePorId(@PathVariable Long id) {
        ParticipanteDTO participante = participanteService.buscarParticipantePorId(id);
        return ResponseEntity.ok(participante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipanteDTO> atualizarParticipante(@PathVariable Long id, @Valid @RequestBody ParticipanteRequestDTO participanteDTO) {
        ParticipanteDTO participanteAtualizado = participanteService.atualizarParticipante(id, participanteDTO);
        return ResponseEntity.ok(participanteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarParticipante(@PathVariable Long id) {
        participanteService.deletarParticipante(id);
        return ResponseEntity.noContent().build();
    }
}

