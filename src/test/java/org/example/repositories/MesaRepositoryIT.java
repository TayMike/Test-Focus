package org.example.repositories;

import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class MesaRepositoryIT {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Restaurante restaurante;
    private Mesa mesa1;
    private Mesa mesa2;

    @BeforeEach
    public void setUp() {
        restaurante = Restaurante.builder()
                .nome("Restaurante Teste")
                .localizacao("Local Teste")
                .tipoCozinha("Italiana")
                .capacidade(10)
                .build();

        restauranteRepository.saveAndFlush(restaurante);

        mesa1 = Mesa.builder()
                .restaurante(restaurante)
                .numero(1)
                .build();

        mesa2 = Mesa.builder()
                .restaurante(restaurante)
                .numero(2)
                .build();

        mesaRepository.saveAndFlush(mesa1);
        mesaRepository.saveAndFlush(mesa2);
    }

    @Test
    public void testFindByRestauranteID_Success() {
        Optional<List<Mesa>> mesas = mesaRepository.findByRestauranteID(restaurante.getId());

        assertTrue(mesas.isPresent());
        assertEquals(2, mesas.get().size());
    }

    @Test
    public void testFindByRestauranteID_NotFound() {
        Optional<List<Mesa>> mesas = mesaRepository.findByRestauranteID(UUID.fromString("5697acc9-297d-4999-b8ec-c736a34b5abd"));
        assertTrue(mesas.get().isEmpty());
    }

    @Test
    public void testFindByTableNumber_Success() {
        Optional<Mesa> mesa = mesaRepository.findByTableNumber(restaurante.getId(), 1);

        assertTrue(mesa.isPresent());
        assertEquals(1, mesa.get().getNumero());
    }

    @Test
    public void testFindByTableNumber_NotFound() {
        Optional<Mesa> mesa = mesaRepository.findByTableNumber(restaurante.getId(), 3);

        assertFalse(mesa.isPresent());
    }

    @Test
    public void testFindByTableNumber_RestauranteNotFound() {
        Optional<Mesa> mesa = mesaRepository.findByTableNumber(UUID.randomUUID(), 1);

        assertFalse(mesa.isPresent());
    }
}
