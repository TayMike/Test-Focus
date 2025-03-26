package org.example.services;

import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.repositories.MesaRepository;
import org.example.repositories.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    MesaRepository mesaRepository;

    public Restaurante cadastrarRestaurante(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public Optional<Restaurante> procurarPeloId(UUID id) {
        return restauranteRepository.findById(id);
    }

    public Optional<Restaurante> procurarPeloNome(String nome) {
        return restauranteRepository.findByName(nome);
    }

    public Optional<Restaurante> procurarPelaLocalizacao(String localizacao) {
        return restauranteRepository.findByLocation(localizacao);
    }

    public Optional<Restaurante> procurarPeloTipoCozinha(String tipoCozinha) {
        return restauranteRepository.findByCuisineType(tipoCozinha);
    }

    public Restaurante adicionarAvaliacao(UUID id, Integer avaliacao, Integer numeroMesa) {
        if(restauranteRepository.existsById(id)) {
            Restaurante restauranteUpdate = restauranteRepository.getReferenceById(id);
            Optional<Mesa> mesa = mesaRepository.findByTableNumber(id, numeroMesa);
            if (mesa.isPresent()) {
                if (restauranteUpdate.getAvaliacao() != null) {
                    restauranteUpdate.getAvaliacao().add(avaliacao);
                } else {
                    restauranteUpdate.setAvaliacao(List.of(avaliacao));
                }
            }
            return restauranteRepository.save(restauranteUpdate);
        } else {
            return null;
        }
    }

    public Restaurante adicionarComentario(UUID id, String comentario, Integer numeroMesa) {
        if(restauranteRepository.existsById(id)) {
            Restaurante restauranteUpdate = restauranteRepository.getReferenceById(id);
            Optional<Mesa> mesa = mesaRepository.findByTableNumber(id, numeroMesa);
            if (mesa.isPresent()) {
                if (restauranteUpdate.getComentario() != null) {
                    restauranteUpdate.getComentario().add(comentario);
                } else {
                    restauranteUpdate.setComentario(List.of(comentario));
                }
            }
            return restauranteRepository.save(restauranteUpdate);
        } else {
            return null;
        }
    }
}