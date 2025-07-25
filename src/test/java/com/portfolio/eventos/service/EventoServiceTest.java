package com.portfolio.eventos.service;

import com.portfolio.eventos.dto.EventoDTO;
import com.portfolio.eventos.entity.Categoria;
import com.portfolio.eventos.entity.Evento;
import com.portfolio.eventos.entity.Local;
import com.portfolio.eventos.exception.ResourceNotFoundException;
import com.portfolio.eventos.repository.CategoriaRepository;
import com.portfolio.eventos.repository.EventoRepository;
import com.portfolio.eventos.repository.LocalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;
    private EventoDTO eventoDTO;
    private Categoria categoria;
    private Local local;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");

        local = new Local();
        local.setId(1L);
        local.setNome("Local Teste");

        evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento Teste");
        evento.setDescricao("Descrição do evento teste");
        evento.setDataInicio(LocalDate.now().plusDays(1));
        evento.setDataFim(LocalDate.now().plusDays(2));
        evento.setHoraInicio(LocalTime.of(9, 0));
        evento.setHoraFim(LocalTime.of(17, 0));
        evento.setCapacidadeMaxima(100);
        evento.setPreco(BigDecimal.valueOf(50.0));
        evento.setCategoria(categoria);
        evento.setLocal(local);

        eventoDTO = new EventoDTO();
        eventoDTO.setId(1L);
        eventoDTO.setNome("Evento Teste");
        eventoDTO.setDescricao("Descrição do evento teste");
        eventoDTO.setDataInicio(LocalDate.now().plusDays(1));
        eventoDTO.setDataFim(LocalDate.now().plusDays(2));
        eventoDTO.setHoraInicio(LocalTime.of(9, 0));
        eventoDTO.setHoraFim(LocalTime.of(17, 0));
        eventoDTO.setCapacidadeMaxima(100);
        eventoDTO.setPreco(BigDecimal.valueOf(50.0));
        eventoDTO.setCategoriaId(1L);
        eventoDTO.setLocalId(1L);
    }

    @Test
    void listarEventos_DeveRetornarPaginaDeEventos() {
        List<Evento> eventos = Arrays.asList(evento);
        Page<Evento> pageEventos = new PageImpl<>(eventos);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(eventoRepository.findAll(pageable)).thenReturn(pageEventos);

        Page<EventoDTO> resultado = eventoService.listarEventos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("Evento Teste", resultado.getContent().get(0).getNome());
        verify(eventoRepository, times(1)).findAll(pageable);
    }

    @Test
    void buscarEventoPorId_ComIdValido_DeveRetornarEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        EventoDTO resultado = eventoService.buscarEventoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Evento Teste", resultado.getNome());
        verify(eventoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarEventoPorId_ComIdInvalido_DeveLancarExcecao() {
        when(eventoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventoService.buscarEventoPorId(999L);
        });

        verify(eventoRepository, times(1)).findById(999L);
    }

    @Test
    void criarEvento_DeveRetornarEventoCriado() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(localRepository.findById(1L)).thenReturn(Optional.of(local));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoDTO resultado = eventoService.criarEvento(eventoDTO);

        assertNotNull(resultado);
        assertEquals("Evento Teste", resultado.getNome());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(localRepository, times(1)).findById(1L);
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void atualizarEvento_ComIdValido_DeveRetornarEventoAtualizado() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(localRepository.findById(1L)).thenReturn(Optional.of(local));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoDTO resultado = eventoService.atualizarEvento(1L, eventoDTO);

        assertNotNull(resultado);
        assertEquals("Evento Teste", resultado.getNome());
        verify(eventoRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(localRepository, times(1)).findById(1L);
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void atualizarEvento_ComIdInvalido_DeveLancarExcecao() {
        when(eventoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventoService.atualizarEvento(999L, eventoDTO);
        });

        verify(eventoRepository, times(1)).findById(999L);
        verify(eventoRepository, never()).save(any(Evento.class));
    }

    @Test
    void deletarEvento_ComIdValido_DeveExecutarSemErro() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(eventoRepository.countInscricoesConfirmadasByEventoId(1L)).thenReturn(0L);
        doNothing().when(eventoRepository).delete(evento);

        assertDoesNotThrow(() -> {
            eventoService.deletarEvento(1L);
        });

        verify(eventoRepository, times(1)).findById(1L);
        verify(eventoRepository, times(1)).countInscricoesConfirmadasByEventoId(1L);
        verify(eventoRepository, times(1)).delete(evento);
    }

    @Test
    void deletarEvento_ComIdInvalido_DeveLancarExcecao() {
        when(eventoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventoService.deletarEvento(999L);
        });

        verify(eventoRepository, times(1)).findById(999L);
        verify(eventoRepository, never()).delete(any(Evento.class));
    }
}

