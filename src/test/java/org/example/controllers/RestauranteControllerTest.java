package org.example.controllers;

import org.example.entities.Restaurante;
import org.example.services.RestauranteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestauranteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestauranteService restauranteService;

    @InjectMocks
    private RestauranteController restauranteController;

    private Restaurante restaurante;
    private UUID restauranteId;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        restauranteId = UUID.randomUUID();
        restaurante = Restaurante.builder()
                .id(restauranteId)
                .nome("Restaurante Teste")
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testCadastrarRestaurante() throws Exception {
        // Arrange
        when(restauranteService.cadastrarRestaurante(any(Restaurante.class))).thenReturn(restaurante);

        // Act & Assert
        mockMvc.perform(post("/restaurantes/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Restaurante Teste\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste"));

        verify(restauranteService, times(1)).cadastrarRestaurante(any(Restaurante.class));
    }

    @Test
    void testProcurarPeloNome_Success() throws Exception {
        // Arrange
        String nome = "Restaurante Teste";
        Restaurante restaurantesPage = restaurante;
        when(restauranteService.procurarPeloNome(nome))
                .thenReturn(Optional.of(restaurantesPage));

        // Act & Assert
        mockMvc.perform(get("/restaurantes/nome/{nome}", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste"));

        verify(restauranteService, times(1)).procurarPeloNome(nome);
    }

    @Test
    void testProcurarPeloNome_NotFound() throws Exception {
        // Arrange
        String nome = "Restaurante Inexistente";
        int page = 0;
        int size = 10;
        when(restauranteService.procurarPeloNome(nome)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/restaurantes/nome/{nome}", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(restauranteService, times(1)).procurarPeloNome(nome);
    }

    @Test
    void testProcurarPelaLocalizacao_Success() throws Exception {
        // Arrange
        String localizacao = "Rua X, 123";
        Restaurante restaurantesPage = restaurante;
        when(restauranteService.procurarPelaLocalizacao(localizacao))
                .thenReturn(Optional.of(restaurantesPage));

        // Act & Assert
        mockMvc.perform(get("/restaurantes/localizacao/{localizacao}", localizacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste"));

        verify(restauranteService, times(1)).procurarPelaLocalizacao(localizacao);
    }

    @Test
    void testProcurarPelaLocalizacao_NotFound() throws Exception {
        // Arrange
        String localizacao = "Rua X, 123";
        when(restauranteService.procurarPelaLocalizacao(localizacao)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/restaurantes/localizacao/{localizacao}", localizacao)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(restauranteService, times(1)).procurarPelaLocalizacao(localizacao);
    }

    @Test
    void testProcurarPeloTipoCozinha_Success() throws Exception {
        // Arrange
        String tipoCozinha = "Italiana";
        Restaurante restaurantesPage = restaurante;
        when(restauranteService.procurarPeloTipoCozinha(tipoCozinha))
                .thenReturn(Optional.of(restaurantesPage));

        // Act & Assert
        mockMvc.perform(get("/restaurantes/tipoCozinha/{tipoCozinha}", tipoCozinha)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste"));

        verify(restauranteService, times(1)).procurarPeloTipoCozinha(tipoCozinha);
    }

    @Test
    void testProcurarPeloTipoCozinha_NotFound() throws Exception {
        // Arrange
        String tipoCozinha = "Italiana";
        int page = 0;
        int size = 10;
        when(restauranteService.procurarPeloTipoCozinha(tipoCozinha)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/restaurantes/tipoCozinha/{tipoCozinha}", tipoCozinha)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(restauranteService, times(1)).procurarPeloTipoCozinha(tipoCozinha);
    }

    @Test
    void testAvaliarRestaurante_Success() throws Exception {
        // Arrange
        Integer avaliacao = 5;
        Integer mesa = 1;
        Restaurante restauranteAvaliado = Restaurante.builder()
                .id(restauranteId)
                .build();
        when(restauranteService.adicionarAvaliacao(restauranteId, avaliacao, mesa)).thenReturn(restauranteAvaliado);

        // Act & Assert
        mockMvc.perform(put("/restaurantes/avaliacao/{id}", restauranteId)
                        .contentType("application/json")
                        .content("{\"avaliacao\": 5, \"mesa\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restauranteId.toString()));

        verify(restauranteService, times(1)).adicionarAvaliacao(restauranteId, avaliacao, mesa);
    }

    @Test
    void testAvaliarRestaurante_NotFound() throws Exception {
        // Arrange
        Integer avaliacao = 5;
        Integer mesa = 1;

        when(restauranteService.adicionarAvaliacao(restauranteId, avaliacao, mesa)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/restaurantes/avaliacao/{id}", restauranteId)
                        .contentType("application/json")
                        .content("{\"avaliacao\": 5, \"mesa\": 1}"))
                .andExpect(status().isNotFound());

        verify(restauranteService, times(1)).adicionarAvaliacao(restauranteId, avaliacao, mesa);
    }

    @Test
    void testComentarRestaurante_Success() throws Exception {
        // Arrange
        String comentario = "Ótimo restaurante!";
        Integer mesa = 1;
        Restaurante restauranteComentado = Restaurante.builder()
                .id(restauranteId)
                .build();
        when(restauranteService.adicionarComentario(restauranteId, comentario, mesa)).thenReturn(restauranteComentado);

        // Act & Assert
        mockMvc.perform(put("/restaurantes/comentario/{id}", restauranteId)
                        .contentType("application/json")
                        .content("{\"comentario\": \"Ótimo restaurante!\", \"mesa\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restauranteId.toString()));

        verify(restauranteService, times(1)).adicionarComentario(restauranteId, comentario, mesa);
    }

    @Test
    void testComentarRestaurante_NotFound() throws Exception {
        // Arrange
        String comentario = "Ótimo restaurante!";
        Integer mesa = 1;

        when(restauranteService.adicionarComentario(restauranteId, comentario, mesa)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/restaurantes/comentario/{id}", restauranteId)
                        .contentType("application/json")
                        .content("{\"comentario\": \"Ótimo restaurante!\", \"mesa\": 1}"))
                .andExpect(status().isNotFound());

        verify(restauranteService, times(1)).adicionarComentario(restauranteId, comentario, mesa);
    }
}