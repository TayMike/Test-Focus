package org.example.controllers;

import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.services.MesaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MesaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MesaService mesaService;

    @InjectMocks
    private MesaController mesaController;

    private Mesa mesa;
    private Restaurante restaurante;
    private UUID mesaId;
    private UUID restauranteId;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mesaController).build();

        mesaId = UUID.randomUUID();
        restauranteId = UUID.randomUUID();

        restaurante = Restaurante.builder()
                .id(restauranteId)
                .nome("Restaurante Teste")
                .build();

        mesa = Mesa.builder()
                .id(mesaId)
                .restaurante(restaurante)
                .numero(1)
                .build(); // Example constructor for Mesa
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testCadastrarMesa_Success() throws Exception {
        // Arrange
        when(mesaService.cadastrarMesa(any(Mesa.class))).thenReturn(mesa);

        // Act & Assert
        mockMvc.perform(post("/mesas/cadastrar")
                        .contentType("application/json")
                        .content("{\"restauranteId\": \"" + restauranteId + "\", \"numero\": 1, \"reservada\": false}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Mesa cadastrada com sucesso!"));

        verify(mesaService, times(1)).cadastrarMesa(any(Mesa.class));
    }

    @Test
    void testCadastrarMesa_BadRequest() throws Exception {
        // Arrange
        when(mesaService.cadastrarMesa(any(Mesa.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act & Assert
        mockMvc.perform(post("/mesas/cadastrar")
                        .contentType("application/json")
                        .content("{\"restauranteId\": \"" + restauranteId + "\", \"numero\": 1, \"reservada\": false}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados inválidos"));

        verify(mesaService, times(1)).cadastrarMesa(any(Mesa.class));
    }

    @Test
    void testReservarMesa_Success() throws Exception {
        // Arrange
        when(mesaService.reservarMesa(any(Mesa.class))).thenReturn(mesa);

        // Act & Assert
        mockMvc.perform(post("/mesas/reservar")
                        .contentType("application/json")
                        .content("{\"restauranteId\": \"" + restauranteId + "\", \"numero\": 1, \"reservada\": false}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mesa reservada com sucesso!"));

        verify(mesaService, times(1)).reservarMesa(any(Mesa.class));
    }

    @Test
    void testReservarMesa_BadRequest() throws Exception {
        // Arrange
        when(mesaService.reservarMesa(any(Mesa.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act & Assert
        mockMvc.perform(post("/mesas/reservar")
                        .contentType("application/json")
                        .content("{\"restauranteId\": \"" + restauranteId + "\", \"numero\": 1, \"reservada\": false}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados inválidos"));

        verify(mesaService, times(1)).reservarMesa(any(Mesa.class));
    }

    @Test
    void testEncontrarTodasAsMesas_Success() throws Exception {
        // Arrange
        when(mesaService.encontrarTodos(restauranteId)).thenReturn(Optional.of(List.of(mesa)));

        // Act & Assert
        mockMvc.perform(get("/mesas/restaurante/{restauranteId}", restauranteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value(1));

        verify(mesaService, times(1)).encontrarTodos(restauranteId);
    }

    @Test
    void testEncontrarTodasAsMesas_NotFound() throws Exception {
        // Arrange
        when(mesaService.encontrarTodos(restauranteId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/mesas/restaurante/{restauranteId}", restauranteId))
                .andExpect(status().isNotFound());

        verify(mesaService, times(1)).encontrarTodos(restauranteId);
    }

    @Test
    void testEncontrarPeloNumeroDaMesa_Success() throws Exception {
        // Arrange
        when(mesaService.encontrarPeloNumeroDaMesa(restauranteId, 1)).thenReturn(Optional.of(mesa));

        // Act & Assert
        mockMvc.perform(get("/mesas/restaurante/{restauranteId}/numero/{numero}", restauranteId, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1));

        verify(mesaService, times(1)).encontrarPeloNumeroDaMesa(restauranteId, 1);
    }

    @Test
    void testEncontrarPeloNumeroDaMesa_NotFound() throws Exception {
        // Arrange
        when(mesaService.encontrarPeloNumeroDaMesa(restauranteId, 1)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/mesas/restaurante/{restauranteId}/numero/{numero}", restauranteId, 1))
                .andExpect(status().isNotFound());

        verify(mesaService, times(1)).encontrarPeloNumeroDaMesa(restauranteId, 1);
    }

    @Test
    void testDeletarMesa_Success() throws Exception {
        // Arrange
        when(mesaService.encontrarMesa(mesaId)).thenReturn(Optional.of(mesa));

        // Act & Assert
        mockMvc.perform(delete("/mesas/deletar/{mesaId}", mesaId))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Mesa deletada com sucesso."));

        verify(mesaService, times(1)).deletar(mesa);
    }

    @Test
    void testDeletarMesa_NotFound() throws Exception {
        // Arrange
        when(mesaService.encontrarMesa(mesaId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/mesas/deletar/{mesaId}", mesaId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Mesa não encontrada."));

        verify(mesaService, times(1)).encontrarMesa(mesaId);
    }

    @Test
    void testAtualizarMesa_Success() throws Exception {
        // Arrange
        Mesa mesaAtualizada = Mesa.builder()
                .id(mesaId)
                .restaurante(restaurante)
                .numero(1)
                .build();
        when(mesaService.atualizar(any(Mesa.class))).thenReturn(mesaAtualizada);

        // Act & Assert
        mockMvc.perform(put("/mesas/atualizar")
                        .contentType("application/json")
                        .content("{\"id\": \"" + mesaId + "\", \"restauranteId\": \"" + restauranteId + "\", \"numero\": 1, \"reservada\": true}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mesa atualizada com sucesso!"));

        verify(mesaService, times(1)).atualizar(any(Mesa.class));
    }

    @Test
    void testAtualizarMesa_NotFound() throws Exception {
        // Arrange
        when(mesaService.atualizar(any(Mesa.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/mesas/atualizar")
                        .contentType("application/json")
                        .content("{\"id\": \"" + mesaId + "\", \"restauranteId\": \"" + restauranteId + "\", \"numero\": 1, \"reservada\": true}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Mesa não encontrada."));

        verify(mesaService, times(1)).atualizar(any(Mesa.class));
    }
}