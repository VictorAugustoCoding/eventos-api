package com.portfolio.eventos.controller;

import com.portfolio.eventos.dto.LocalDTO;
import com.portfolio.eventos.service.LocalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locais")
public class LocalController {

    private final LocalService localService;

    @Autowired
    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @PostMapping
    public ResponseEntity<LocalDTO> criarLocal(@Valid @RequestBody LocalDTO localDTO) {
        LocalDTO novoLocal = localService.criarLocal(localDTO);
        return new ResponseEntity<>(novoLocal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LocalDTO>> listarLocais() {
        List<LocalDTO> locais = localService.listarLocais();
        return ResponseEntity.ok(locais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalDTO> buscarLocalPorId(@PathVariable Long id) {
        LocalDTO local = localService.buscarLocalPorId(id);
        return ResponseEntity.ok(local);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalDTO> atualizarLocal(@PathVariable Long id, @Valid @RequestBody LocalDTO localDTO) {
        LocalDTO localAtualizado = localService.atualizarLocal(id, localDTO);
        return ResponseEntity.ok(localAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLocal(@PathVariable Long id) {
        localService.deletarLocal(id);
        return ResponseEntity.noContent().build();
    }
}

