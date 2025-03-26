package org.example.repositories;

import org.example.entities.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, UUID> {

    @Query("SELECT m FROM Mesa m WHERE m.restaurante.id = :restauranteId")
    Optional<List<Mesa>> findByRestauranteID(UUID restauranteId);

    @Query("SELECT m FROM Mesa m WHERE m.restaurante.id = :restauranteId AND m.numero = :numeroMesa")
    Optional<Mesa> findByTableNumber(UUID restauranteId, Integer numeroMesa);
}