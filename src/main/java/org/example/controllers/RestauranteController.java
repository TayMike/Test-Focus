package org.example.controllers;

import org.example.dto.AvaliacaoRequest;
import org.example.dto.ComentarioRequest;
import org.example.entities.Restaurante;
import org.example.services.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Optional<Restaurante>> procurarPeloNome(
            @PathVariable String nome) {

        Optional<Restaurante> resultado = restauranteService.procurarPeloNome(nome);

        if (resultado.isPresent()) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/localizacao/{localizacao}")
    public ResponseEntity<Optional<Restaurante>> procurarPelaLocalizacao(
            @PathVariable String localizacao) {

        Optional<Restaurante> resultado = restauranteService.procurarPelaLocalizacao(localizacao);

        if (resultado.isPresent()) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tipoCozinha/{tipoCozinha}")
    public ResponseEntity<Optional<Restaurante>> procurarPeloTipoCozinha(
            @PathVariable String tipoCozinha) {

        Optional<Restaurante> resultado = restauranteService.procurarPeloTipoCozinha(tipoCozinha);

        if (resultado.isPresent()) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Restaurante> cadastrarRestaurante(@RequestBody Restaurante restaurante) {
        Restaurante resultado = restauranteService.cadastrarRestaurante(restaurante);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @PutMapping("/avaliacao/{id}")
    public ResponseEntity<Restaurante> avaliarRestaurante(
            @PathVariable UUID id,
            @RequestBody AvaliacaoRequest avaliacaoRequest) {
        Integer avaliacao = avaliacaoRequest.getAvaliacao();
        Integer mesa = avaliacaoRequest.getMesa();

        Restaurante restauranteAvaliado = restauranteService.adicionarAvaliacao(id, avaliacao, mesa);
        if (restauranteAvaliado != null) {
            return new ResponseEntity<>(restauranteAvaliado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comentario/{id}")
    public ResponseEntity<Restaurante> comentarRestaurante(
            @PathVariable UUID id,
            @RequestBody ComentarioRequest comentarioRequest) {
        String comentario = comentarioRequest.getComentario();
        Integer mesa = comentarioRequest.getMesa();

        Restaurante restauranteComentado = restauranteService.adicionarComentario(id, comentario, mesa);
        if (restauranteComentado != null) {
            return new ResponseEntity<>(restauranteComentado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}