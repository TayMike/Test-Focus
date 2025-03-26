package org.example.services;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.repositories.MesaRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MesaService {

    @Autowired
    MesaRepository mesaRepository;

    @Autowired
    @Lazy
    RestauranteService restauranteService;

    @Transactional
    public Mesa cadastrarMesa(Mesa mesa) {
        UUID restauranteId = mesa.getRestaurante().getId();
        Optional<Restaurante> restauranteOpcional = restauranteService.procurarPeloId(restauranteId);
        Restaurante restaurante = restauranteOpcional.orElse(null);
        if (restaurante != null) {
            Optional<List<Mesa>> lista = this.encontrarTodos(restauranteId);
            if (restaurante.getCapacidade() >= mesa.getNumero()) {
                if (lista.isPresent()) {
                    List<Mesa> listaVerificada = lista.get().stream().filter(mesa1 -> mesa1.getNumero() == mesa.getNumero()).toList();
                    if (listaVerificada.isEmpty()){
                        return mesaRepository.save(mesa);
                    } else {
                        throw new IllegalArgumentException("Mesa já cadastrada.");
                    }
                } else {
                    return mesaRepository.save(mesa);
                }
            } else {
                throw new IllegalArgumentException("O restaurante não tem capacidade suficiente para essa nova mesa");
            }
        } else {
            throw new IllegalArgumentException("O restaurante não existe.");
        }
    }

    @Transactional
    public Mesa reservarMesa(Mesa mesa) {
        LocalDateTime reservaDiaHorarioSolicitado = mesa.getDataHora().getFirst();
        Optional<Restaurante> restauranteOpcional = restauranteService.procurarPeloId(mesa.getRestaurante().getId());
        Restaurante restaurante = restauranteOpcional.orElse(null);
        boolean existeMesa = mesaRepository.findById(mesa.getId()).isPresent();
        if (restaurante != null && existeMesa) {
            if ((restaurante.getHorario().getFirst().isBefore(LocalTime.from(reservaDiaHorarioSolicitado))) && (restaurante.getHorario().getLast().isAfter(LocalTime.from(reservaDiaHorarioSolicitado)))) {
                Mesa mesaUpdate = mesaRepository.getReferenceById(mesa.getId());
                LocalDateTime verificarDisponibilidade = mesaUpdate.getDataHora().stream()
                        .filter(localDateTime -> !localDateTime.isBefore(reservaDiaHorarioSolicitado.minusHours(1)) && !localDateTime.isAfter(reservaDiaHorarioSolicitado.plusHours(1)))
                        .findFirst()
                        .orElse(null);
                if (verificarDisponibilidade == null) {
                    List<Boolean> estado = new ArrayList<>(mesaUpdate.getEstado());
                    estado.add(mesa.getEstado().getFirst());
                    mesaUpdate.setEstado(estado);
                    List<LocalDateTime> dataHora = new ArrayList<>(mesaUpdate.getDataHora());
                    dataHora.add(mesa.getDataHora().getFirst());
                    mesaUpdate.setDataHora(dataHora);
                    return mesaRepository.save(mesaUpdate);
                } else {
                    throw new IllegalArgumentException("Horário indisponível.");
                }
            } else {
                throw new IllegalArgumentException("Restaurante fechado.");
            }
        } else {
            throw new IllegalArgumentException("Restaurante ou mesa inexistente.");
        }
    }

    public Optional<List<Mesa>> encontrarTodos(UUID restauranteID) {
        return mesaRepository.findByRestauranteID(restauranteID);
    }

    public Optional<Mesa> encontrarPeloNumeroDaMesa(UUID restauranteId, Integer numero) {
        return mesaRepository.findByTableNumber(restauranteId, numero);
    }

    @Transactional
    public Mesa atualizar(Mesa mesa) {
        if(mesaRepository.existsById(mesa.getId())) {
            Mesa mesaUpdate = mesaRepository.getReferenceById(mesa.getId());
            mesaUpdate.setRestaurante(mesa.getRestaurante());
            mesaUpdate.setDataHora(mesa.getDataHora());
            mesaUpdate.setNumero(mesa.getNumero());
            mesaUpdate.setEstado(mesa.getEstado());
            mesaUpdate.setConfirmacao(mesa.getConfirmacao());
            return mesaRepository.save(mesaUpdate);
        } else {
            return null;
        }
    }

    public Optional<Mesa> encontrarMesa(UUID id) {
        return mesaRepository.findById(id);
    }

    @Transactional
    public void deletar(Mesa mesa) {
        mesaRepository.deleteById(mesa.getId());
    }

}