package com.portfolio.eventos.service;

import com.portfolio.eventos.dto.CategoriaDTO;
import com.portfolio.eventos.entity.Categoria;
import com.portfolio.eventos.exception.ResourceNotFoundException;
import com.portfolio.eventos.exception.ValidationException;
import com.portfolio.eventos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO) {
        if (categoriaRepository.existsByNomeIgnoreCase(categoriaDTO.getNome())) {
            throw new ValidationException("Já existe uma categoria com este nome.");
        }
        Categoria categoria = new Categoria(categoriaDTO.getNome(), categoriaDTO.getDescricao());
        categoria = categoriaRepository.save(categoria);
        return toDTO(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTOComTotalEventos)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO buscarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));
        return toDTOComTotalEventos(categoria);
    }

    @Transactional
    public CategoriaDTO atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        if (!categoriaExistente.getNome().equalsIgnoreCase(categoriaDTO.getNome()) &&
            categoriaRepository.existsByNomeIgnoreCase(categoriaDTO.getNome())) {
            throw new ValidationException("Já existe outra categoria com este nome.");
        }

        categoriaExistente.setNome(categoriaDTO.getNome());
        categoriaExistente.setDescricao(categoriaDTO.getDescricao());
        categoriaExistente = categoriaRepository.save(categoriaExistente);
        return toDTO(categoriaExistente);
    }

    @Transactional
    public void deletarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        if (categoriaRepository.countEventosByCategoriaId(id) > 0) {
            throw new ValidationException("Não é possível deletar categoria com eventos associados.");
        }
        categoriaRepository.delete(categoria);
    }

    // Métodos de conversão Entity para DTO
    private CategoriaDTO toDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNome(), categoria.getDescricao(), 
                                categoria.getDataCriacao(), categoria.getDataAtualizacao());
    }

    private CategoriaDTO toDTOComTotalEventos(Categoria categoria) {
        CategoriaDTO dto = toDTO(categoria);
        dto.setTotalEventos(categoriaRepository.countEventosByCategoriaId(categoria.getId()));
        return dto;
    }
}


