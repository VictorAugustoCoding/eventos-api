package com.portfolio.eventos.service;

import com.portfolio.eventos.dto.LocalDTO;
import com.portfolio.eventos.entity.Local;
import com.portfolio.eventos.exception.ResourceNotFoundException;
import com.portfolio.eventos.exception.ValidationException;
import com.portfolio.eventos.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {

    private final LocalRepository localRepository;

    @Autowired
    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    @Transactional
    public LocalDTO criarLocal(LocalDTO localDTO) {
        if (localRepository.existsByNomeIgnoreCase(localDTO.getNome())) {
            throw new ValidationException("Já existe um local com este nome.");
        }
        Local local = new Local(localDTO.getNome(), localDTO.getEndereco(), localDTO.getCapacidade());
        local = localRepository.save(local);
        return toDTO(local);
    }

    @Transactional(readOnly = true)
    public List<LocalDTO> listarLocais() {
        return localRepository.findAll().stream()
                .map(this::toDTOComTotalEventos)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocalDTO buscarLocalPorId(Long id) {
        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + id));
        return toDTOComTotalEventos(local);
    }

    @Transactional
    public LocalDTO atualizarLocal(Long id, LocalDTO localDTO) {
        Local localExistente = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + id));

        if (!localExistente.getNome().equalsIgnoreCase(localDTO.getNome()) &&
            localRepository.existsByNomeIgnoreCase(localDTO.getNome())) {
            throw new ValidationException("Já existe outro local com este nome.");
        }

        localExistente.setNome(localDTO.getNome());
        localExistente.setEndereco(localDTO.getEndereco());
        localExistente.setCapacidade(localDTO.getCapacidade());
        localExistente = localRepository.save(localExistente);
        return toDTO(localExistente);
    }

    @Transactional
    public void deletarLocal(Long id) {
        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + id));

        if (localRepository.countEventosByLocalId(id) > 0) {
            throw new ValidationException("Não é possível deletar local com eventos associados.");
        }
        localRepository.delete(local);
    }

    // Métodos de conversão Entity para DTO
    private LocalDTO toDTO(Local local) {
        return new LocalDTO(local.getId(), local.getNome(), local.getEndereco(), local.getCapacidade(),
                            local.getDataCriacao(), local.getDataAtualizacao());
    }

    private LocalDTO toDTOComTotalEventos(Local local) {
        LocalDTO dto = toDTO(local);
        dto.setTotalEventos(localRepository.countEventosByLocalId(local.getId()));
        return dto;
    }
}


