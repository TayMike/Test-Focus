package org.example.services;

import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.repositories.MesaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private RestauranteService restauranteService;

    @InjectMocks
    private MesaService mesaService;

    private AutoCloseable openMocks;
    private Restaurante restaurante;
    private Mesa mesa;
    private Mesa mesa1;
    private Mesa mesa2;
    private Mesa mesa3;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        restaurante = Restaurante.builder()
                .nome("Restaurante Teste")
                .capacidade(10)
                .horario(List.of(LocalTime.of(8, 0), LocalTime.of(14, 0)))
                .build();

        mesa = Mesa.builder()
                .restaurante(restaurante)
                .numero(1)
                .dataHora(List.of(LocalDateTime.of(2025, 12, 21, 9, 0)))
                .estado(List.of(true))
                .build();

        mesa1 = Mesa.builder()
                .restaurante(restaurante)
                .numero(1)
                .dataHora(List.of(LocalDateTime.of(2025, 12, 21, 9, 0).plusMinutes(30)))
                .estado(List.of(true))
                .build();

        mesa2 = Mesa.builder()
                .restaurante(restaurante)
                .numero(1)
                .dataHora(List.of(LocalDateTime.of(2025, 12, 21, 9, 0).plusHours(2)))
                .estado(List.of(true))
                .build();

        mesa3 = Mesa.builder()
                .restaurante(restaurante)
                .numero(1)
                .dataHora(List.of(LocalDateTime.of(2025, 12, 21, 7, 0)))
                .estado(List.of(true))
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testCadastrarMesa_Success() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        // Act
        Mesa resultado = mesaService.cadastrarMesa(mesa);

        // Assert
        assertNotNull(resultado);
        assertEquals(mesa.getId(), resultado.getId());
        verify(mesaRepository, times(1)).save(mesa);
    }

    @Test
    void testCadastrarMesa_RestauranteNaoExistente() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.empty());

        // Act && Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mesaService.cadastrarMesa(mesa);
        });
        assertEquals("O restaurante não existe.", exception.getMessage());
    }

    @Test
    void testCadastrarMesa_CapacidadeRestauranteInvalida() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.of(restaurante));
        mesa.setNumero(20);

        // Act && Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mesaService.cadastrarMesa(mesa);
        });

        assertEquals("O restaurante não tem capacidade suficiente para essa nova mesa", exception.getMessage());
    }

    @Test
    void testCadastrarMesa_MesaJaCadastrada() {
        // Arrange
        UUID restauranteId = mesa.getRestaurante().getId();
        List<Mesa> mesasExistentes = List.of(Mesa.builder().numero(1).build());
        when(restauranteService.procurarPeloId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(mesaService.encontrarTodos(restauranteId)).thenReturn(Optional.of(mesasExistentes));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            mesaService.cadastrarMesa(mesa);
        });

        assertEquals("Mesa já cadastrada.", thrown.getMessage());
        verify(restauranteService, times(1)).procurarPeloId(restauranteId);
    }
    @Test
    void testReservarMesa_Success() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(mesaRepository.findById(mesa.getId())).thenReturn(Optional.of(mesa));
        when(mesaRepository.getReferenceById(mesa.getId())).thenReturn(mesa);
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        // Act
        Mesa resultado = mesaService.reservarMesa(mesa2);

        // Assert
        assertNotNull(resultado);
        verify(mesaRepository, times(1)).save(mesa);
    }

    @Test
    void testReservarMesa_HorarioIndisponivel() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(mesaRepository.findById(mesa.getId())).thenReturn(Optional.of(mesa));
        when(mesaRepository.getReferenceById(mesa.getId())).thenReturn(mesa);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mesaService.reservarMesa(mesa1);
        });
        assertEquals("Horário indisponível.", exception.getMessage());
    }

    @Test
    void testReservarMesa_RestauranteFechado() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(mesaRepository.findById(mesa3.getId())).thenReturn(Optional.of(mesa3));
        when(mesaRepository.getReferenceById(mesa3.getId())).thenReturn(mesa3);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mesaService.reservarMesa(mesa3);
        });
        assertEquals("Restaurante fechado.", exception.getMessage());
    }

    @Test
    void testReservarMesa_RestauranteOuMesaNaoExistente() {
        // Arrange
        when(restauranteService.procurarPeloId(restaurante.getId())).thenReturn(Optional.empty());

        // Act && Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mesaService.reservarMesa(mesa);
        });
        assertEquals("Restaurante ou mesa inexistente.", exception.getMessage());
    }

    @Test
    void testAtualizarMesa_Success() {
        // Arrange
        when(mesaRepository.existsById(mesa.getId())).thenReturn(true);
        when(mesaRepository.getReferenceById(mesa.getId())).thenReturn(mesa);
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        // Act
        Mesa resultado = mesaService.atualizar(mesa);

        // Assert
        assertNotNull(resultado);
        verify(mesaRepository, times(1)).save(mesa);
    }

    @Test
    void testAtualizarMesa_MesaNaoExistente() {
        // Arrange
        when(mesaRepository.existsById(mesa.getId())).thenReturn(false);

        // Act
        Mesa resultado = mesaService.atualizar(mesa);

        // Assert
        assertNull(resultado);
        verify(mesaRepository, times(0)).save(mesa);
    }

    @Test
    void testDeletarMesa() {
        // Arrange
        doNothing().when(mesaRepository).deleteById(mesa.getId());

        // Act
        mesaService.deletar(mesa);

        // Assert
        verify(mesaRepository, times(1)).deleteById(mesa.getId());
    }

    @Test
    void testEncontrarMesa() {
        // Arrange
        when(mesaRepository.findById(mesa.getId())).thenReturn(Optional.ofNullable(mesa));

        // Act
        mesaService.encontrarMesa(mesa.getId());

        // Assert
        verify(mesaRepository, times(1)).findById(mesa.getId());
    }

    @Test
    void testEncontrarPeloNumeroDaMesa() {
        // Arrange
        when(mesaRepository.findByTableNumber(mesa.getRestaurante().getId(), mesa.getNumero())).thenReturn(Optional.ofNullable(mesa));

        // Act
        mesaService.encontrarPeloNumeroDaMesa(mesa.getRestaurante().getId(), mesa.getNumero());

        // Assert
        verify(mesaRepository, times(1)).findByTableNumber(mesa.getRestaurante().getId(), mesa.getNumero());
    }
}