package org.example.services;

import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.repositories.MesaRepository;
import org.example.repositories.RestauranteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestauranteServiceTest {

    @InjectMocks
    private RestauranteService restauranteService;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private MesaService mesaService;

    private Restaurante restaurante;
    private Mesa mesa;

    private UUID restauranteId;
    private UUID mesaId;
    private Integer avaliacao;
    private Integer numeroMesa;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        restauranteId = UUID.randomUUID();
        mesaId = UUID.randomUUID();
        avaliacao = 5;
        numeroMesa = 1;

        restaurante = Restaurante.builder()
                .id(restauranteId)
                .nome("Restaurante Teste")
                .tipoCozinha("Italiana")
                .localizacao("Rua X, 123")
                .build();

        mesa = Mesa.builder()
                .id(mesaId)
                .restaurante(restaurante)
                .numero(1)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testCadastrarRestaurante() {
        // Arrange
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);

        // Act
        Restaurante resultado = restauranteService.cadastrarRestaurante(restaurante);

        // Assert
        assertNotNull(resultado);
        assertEquals(restauranteId, resultado.getId());
        verify(restauranteRepository, times(1)).save(restaurante);
    }

    @Test
    void testProcurarPeloId_Success() {
        // Arrange
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> resultado = restauranteService.procurarPeloId(restauranteId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(restauranteId, resultado.get().getId());
    }

    @Test
    void testProcurarPeloId_NotFound() {
        // Arrange
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurante> resultado = restauranteService.procurarPeloId(restauranteId);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void testAdicionarAvaliacao_Success_WithExistingAvaliacoes() {
        // Arrange
        when(restauranteRepository.existsById(restauranteId)).thenReturn(true);
        when(restauranteRepository.getReferenceById(restauranteId)).thenReturn(restaurante);
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);
        when(mesaRepository.findByTableNumber(restauranteId, numeroMesa)).thenReturn(Optional.of(mesa));

        // Act
        Restaurante resultado = restauranteService.adicionarAvaliacao(restauranteId, avaliacao, numeroMesa);

        // Assert
        assertNotNull(resultado);
        assertEquals(List.of(avaliacao), resultado.getAvaliacao(), "Avaliacao deve ser 5.");
        verify(restauranteRepository, times(1)).save(restaurante);
    }

    @Test
    void testAdicionarAvaliacao_Success_WithNoExistingAvaliacoes() {
        // Arrange
        restaurante.setAvaliacao(null);
        when(restauranteRepository.existsById(restauranteId)).thenReturn(true);
        when(restauranteRepository.getReferenceById(restauranteId)).thenReturn(restaurante);
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);
        when(mesaRepository.findByTableNumber(restauranteId, numeroMesa)).thenReturn(Optional.of(mesa));

        // Act
        Restaurante resultado = restauranteService.adicionarAvaliacao(restauranteId, avaliacao, numeroMesa);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getAvaliacao().contains(avaliacao), "Avaliacao deve ser adicionada.");
        verify(restauranteRepository, times(1)).save(restaurante);
    }

    @Test
    void testAdicionarAvaliacao_RestauranteNaoExistente() {
        // Arrange
        when(restauranteRepository.existsById(restauranteId)).thenReturn(false);

        // Act
        Restaurante resultado = restauranteService.adicionarAvaliacao(restauranteId, avaliacao, numeroMesa);

        // Assert
        assertNull(resultado, "The result should be null when the restaurant does not exist.");
    }

    @Test
    void testAdicionarAvaliacao_MesaNaoExistente() {
        // Arrange
        when(restauranteRepository.existsById(restauranteId)).thenReturn(true);
        when(restauranteRepository.getReferenceById(restauranteId)).thenReturn(restaurante);

        // Act
        Restaurante resultado = restauranteService.adicionarAvaliacao(restauranteId, avaliacao, numeroMesa);

        // Assert
        assertNull(resultado, "The result should be null when the table does not exist.");
    }

    @Test
    void testAdicionarComentario_Success() {
        // Arrange
        String comentario = "Ótimo restaurante!";
        Integer numeroMesa = 1;
        when(restauranteRepository.existsById(restauranteId)).thenReturn(true);
        when(restauranteRepository.getReferenceById(restauranteId)).thenReturn(restaurante);
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);
        when(mesaRepository.findByTableNumber(restauranteId, numeroMesa)).thenReturn(Optional.of(mesa));

        // Act
        Restaurante resultado = restauranteService.adicionarComentario(restauranteId, comentario, numeroMesa);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getComentario().contains(comentario));
    }

    @Test
    void testAdicionarComentario_RestauranteNotFound() {
        // Arrange
        String comentario = "Ótimo restaurante!";
        Integer numeroMesa = 1;
        when(restauranteRepository.existsById(restauranteId)).thenReturn(false);

        // Act
        Restaurante resultado = restauranteService.adicionarComentario(restauranteId, comentario, numeroMesa);

        // Assert
        assertNull(resultado);
    }

    @Test
    void testProcurarPeloNome_Success() {
        // Arrange
        String nome = "Restaurante Teste";
        when(restauranteRepository.findByName(nome)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> resultado = restauranteService.procurarPeloNome(nome);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(nome, resultado.get().getNome());
    }

    @Test
    void testProcurarPeloNome_NotFound() {
        // Arrange
        String nome = "Restaurante Teste";
        Pageable pageable = PageRequest.of(0, 10);
        when(restauranteRepository.findByName(nome)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurante> resultado = restauranteService.procurarPeloNome(nome);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void testProcurarPelaLocalizacao_Success() {
        // Arrange
        String localizacao = "Rua X, 123";
        Pageable pageable = PageRequest.of(0, 10);
        when(restauranteRepository.findByLocation(localizacao)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> resultado = restauranteService.procurarPelaLocalizacao(localizacao);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(localizacao, resultado.get().getLocalizacao());
    }

    @Test
    void testProcurarPeloTipoCozinha_Success() {
        // Arrange
        String tipoCozinha = "Italiana";
        when(restauranteRepository.findByCuisineType(tipoCozinha)).thenReturn(Optional.of(restaurante));

        // Act
        Optional<Restaurante> resultado = restauranteService.procurarPeloTipoCozinha(tipoCozinha);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(tipoCozinha, resultado.get().getTipoCozinha());
    }
}