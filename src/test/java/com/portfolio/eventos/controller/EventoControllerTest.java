package com.portfolio.eventos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.eventos.dto.EventoDTO;
import com.portfolio.eventos.service.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestSecurityConfig.class)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoService eventoService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventoDTO eventoDTO;

    @BeforeEach
    void setUp() {
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
    void listarEventos_DeveRetornarPaginaDeEventos() throws Exception {
        List<EventoDTO> eventos = Arrays.asList(eventoDTO);
        Page<EventoDTO> pageEventos = new PageImpl<>(eventos);
        when(eventoService.listarEventos(any(Pageable.class))).thenReturn(pageEventos);

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Evento Teste"));
    }

    @Test
    void buscarEventoPorId_DeveRetornarEvento() throws Exception {
        when(eventoService.buscarEventoPorId(1L)).thenReturn(eventoDTO);

        mockMvc.perform(get("/api/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Evento Teste"));
    }

    @Test
    void criarEvento_DeveRetornarEventoCriado() throws Exception {
        when(eventoService.criarEvento(any(EventoDTO.class))).thenReturn(eventoDTO);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Evento Teste"));
    }

    @Test
    void atualizarEvento_DeveRetornarEventoAtualizado() throws Exception {
        when(eventoService.atualizarEvento(anyLong(), any(EventoDTO.class))).thenReturn(eventoDTO);

        mockMvc.perform(put("/api/eventos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Evento Teste"));
    }

    @Test
    void deletarEvento_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/eventos/1"))
                .andExpect(status().isNoContent());
    }
}

