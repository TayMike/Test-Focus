package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "restaurante")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Use GenerationType.AUTO for UUID generation
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "nome não pode estar vazio")
    private String nome;

    @Column(nullable = false)
    @NotNull(message = "localizacao não pode estar vazio")
    private String localizacao;

    @Column(nullable = false)
    @NotNull(message = "tipo não pode estar vazio")
    private String tipoCozinha;

    @Column(nullable = false)
    @NotNull(message = "capacidade não pode estar vazio")
    private Integer capacidade;

    @ElementCollection
    @CollectionTable(name = "restaurante_horarios", joinColumns = @JoinColumn(name = "restaurante_id"))
    @Column(name = "horario")
    private List<LocalTime> horario;

    @ElementCollection
    @CollectionTable(name = "restaurante_avaliacoes", joinColumns = @JoinColumn(name = "restaurante_id"))
    @Column(name = "avaliacao")
    private List<Integer> avaliacao;

    @ElementCollection
    @CollectionTable(name = "restaurante_comentarios", joinColumns = @JoinColumn(name = "restaurante_id"))
    @Column(name = "comentario")
    private List<String> comentario;

}