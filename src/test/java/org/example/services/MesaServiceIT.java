package org.example.services;

import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.repositories.MesaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.UUID;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class MesaServiceIT {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private MesaService mesaService;

    @Test
    void testCadastrarMesa_Success() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .id(UUID.randomUUID())
                .nome("Teste")
                .capacidade(10)
                .tipoCozinha("Sushi")
                .localizacao("Rua X")
                .build();
        Mesa mesa = Mesa.builder()
                .id(UUID.randomUUID())
                .restaurante(restaurante)
                .numero(1)
                .build();

        // Act
        Mesa resultado = mesaService.cadastrarMesa(mesa);

        // Assert
        assertNotNull(resultado);
        assertEquals(mesa.getId(), resultado.getId());
        verify(mesaRepository, times(1)).save(mesa);
    }

    @Test
    void testCadastrarMesa_RestauranteNaoExistente() {
        fail("Teste não implementado");
    }

    @Test
    void testCadastrarMesa_CapacidadeRestauranteInvalida() {
        fail("Teste não implementado");
    }

    @Test
    void testCadastrarMesa_MesaJaCadastrada() {
        fail("Teste não implementado");
    }

    @Test
    void testReservarMesa_Success() {
        fail("Teste não implementado");
    }

    @Test
    void testReservarMesa_HorarioIndisponivel() {
        fail("Teste não implementado");
    }

    @Test
    void testReservarMesa_RestauranteFechado() {
        fail("Teste não implementado");
    }

    @Test
    void testReservarMesa_RestauranteOuMesaNaoExistente() {
        fail("Teste não implementado");
    }

    @Test
    void testAtualizarMesa_Success() {
        fail("Teste não implementado");
    }

    @Test
    void testAtualizarMesa_MesaNaoExistente() {
        fail("Teste não implementado");
    }

    @Test
    void testDeletarMesa() {
        fail("Teste não implementado");
    }

    @Test
    void testEncontrarMesa() {
        fail("Teste não implementado");
    }

    @Test
    void testEncontrarPeloNumeroDaMesa() {
        fail("Teste");
    }

}