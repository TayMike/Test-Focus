package org.example.repositories;

import jakarta.transaction.Transactional;
import org.example.entities.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class RestauranteRepositoryIT {

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Restaurante restaurante1;
    private Restaurante restaurante2;

    @BeforeEach
    public void setUp() {
        restaurante1 = Restaurante.builder()
                .nome("Restaurante A")
                .localizacao("Local A")
                .tipoCozinha("Italiana")
                .capacidade(50)
                .build();

        restaurante2 = Restaurante.builder()
                .nome("Restaurante B")
                .localizacao("Local B")
                .tipoCozinha("Japonesa")
                .capacidade(60)
                .build();

        restauranteRepository.save(restaurante1);
        restauranteRepository.save(restaurante2);
    }

    @Test
    public void testFindByName_Success() {
        Optional<Restaurante> result = restauranteRepository.findByName("Restaurante A");
        assertTrue(result.isPresent());
        assertEquals("Restaurante A", result.get().getNome());
    }

    @Test
    public void testFindByName_NotFound() {
        Optional<Restaurante> result = restauranteRepository.findByName("Restaurante X");
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByLocation_Success() {
        Optional<Restaurante> result = restauranteRepository.findByLocation("Local A");
        assertTrue(result.isPresent());
        assertEquals("Local A", result.get().getLocalizacao());
    }

    @Test
    public void testFindByLocation_NotFound() {
        Optional<Restaurante> result = restauranteRepository.findByLocation("Local Z");
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByCuisineType_Success() {
        Optional<Restaurante> result = restauranteRepository.findByCuisineType("Italiana");
        assertTrue(result.isPresent());
        assertEquals("Italiana", result.get().getTipoCozinha());
    }

    @Test
    public void testFindByCuisineType_NotFound() {
        Optional<Restaurante> result = restauranteRepository.findByCuisineType("Mexicana");
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByCuisineType_Pageable_Success() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Restaurante> pageResult = restauranteRepository.findByCuisineType("Italiana", pageable);
        assertEquals(1, pageResult.getTotalElements());
    }

    @Test
    public void testFindByCuisineType_Pageable_NoResults() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Restaurante> pageResult = restauranteRepository.findByCuisineType("Mexicana", pageable);
        assertEquals(0, pageResult.getTotalElements());
    }

    @Test
    public void testFindById_Success() {
        Restaurante savedRestaurante = restauranteRepository.save(Restaurante.builder()
                .nome("Restaurante C")
                .localizacao("Local C")
                .tipoCozinha("Francesa")
                .capacidade(80)
                .build());

        Optional<Restaurante> result = restauranteRepository.findById(savedRestaurante.getId());
        assertTrue(result.isPresent());
        assertEquals(savedRestaurante.getNome(), result.get().getNome());
    }

    @Test
    public void testDelete_Success() {
        restauranteRepository.delete(restaurante1);
        Optional<Restaurante> result = restauranteRepository.findById(restaurante1.getId());
        assertFalse(result.isPresent());
    }
}