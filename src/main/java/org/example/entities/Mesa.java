package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Version
    private Long version;

    @Column(nullable = false)
    @NotNull(message = "número da mesa não pode estar vazio")
    private Integer numero;

    @ElementCollection
    @CollectionTable(name = "mesa_datahora", joinColumns = @JoinColumn(name = "mesa_id"))
    @Column(name = "data_hora")
    private List<LocalDateTime> dataHora;

    @ElementCollection
    @CollectionTable(name = "mesa_estado", joinColumns = @JoinColumn(name = "mesa_id"))
    @Column(name = "estado")
    private List<Boolean> estado;

    @ElementCollection
    @CollectionTable(name = "mesa_confirmacao", joinColumns = @JoinColumn(name = "mesa_id"))
    @Column(name = "confirmacao")
    private List<Boolean> confirmacao;

}