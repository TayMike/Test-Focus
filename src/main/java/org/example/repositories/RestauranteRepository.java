package org.example.repositories;

import org.example.entities.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, UUID> {

    // Query to find a Restaurante by its name
    @Query("SELECT r FROM Restaurante r WHERE r.nome = :nome")
    Optional<Restaurante> findByName(String nome);

    // Query to find a Restaurante by its location
    @Query("SELECT r FROM Restaurante r WHERE r.localizacao = :localizacao")
    Optional<Restaurante> findByLocation(String localizacao);

    // Query to find a Restaurante by its cuisine type
    @Query("SELECT r FROM Restaurante r WHERE r.tipoCozinha = :tipoCozinha")
    Optional<Restaurante> findByCuisineType(String tipoCozinha);

    // Query to find a Restaurante by its cuisine type with pagination
    @Query("SELECT r FROM Restaurante r WHERE r.tipoCozinha = :tipoCozinha")
    Optional<Page<Restaurante>> findByCuisineType(String tipoCozinha, Pageable pageable);
}