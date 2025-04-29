package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "restauranteid", nullable = false)
    private Restaurante restaurante;

    @Version
    private Long version;

    @Column(nullable = false)
    @NotNull(message = "número da mesa não pode estar vazio")
    private Integer numero;

    @ElementCollection
    @CollectionTable(name = "mesadatahora", joinColumns = @JoinColumn(name = "mesa_id"))
    @Column(name = "data_hora")
    private List<LocalDateTime> dataHora;

    @ElementCollection
    @CollectionTable(name = "mesaestado", joinColumns = @JoinColumn(name = "mesa_id"))
    @Column(name = "estado")
    private List<Boolean> estado;

    @ElementCollection
    @CollectionTable(name = "mesaconfirmacao", joinColumns = @JoinColumn(name = "mesa_id"))
    @Column(name = "confirmacao")
    private List<Boolean> confirmacao;

}