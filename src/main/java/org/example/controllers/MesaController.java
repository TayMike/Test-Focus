package org.example.controllers;

import org.example.entities.Mesa;
import org.example.services.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarMesa(@RequestBody Mesa mesa) {
        try {
            mesaService.cadastrarMesa(mesa);
            return ResponseEntity.status(HttpStatus.CREATED).body("Mesa cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reservar")
    public ResponseEntity<String> reservarMesa(@RequestBody Mesa mesa) {
        try {
            Mesa mesaReservada = mesaService.reservarMesa(mesa);
            return ResponseEntity.status(HttpStatus.OK).body("Mesa reservada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Mesa>> encontrarTodasAsMesas(@PathVariable UUID restauranteId) {
        List<Mesa> mesas = mesaService.encontrarTodos(restauranteId);
        if (mesas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(mesas);
    }


    @GetMapping("/restaurante/{restauranteId}/numero/{numero}")
    public ResponseEntity<Mesa> encontrarPeloNumeroDaMesa(
            @PathVariable UUID restauranteId,
            @PathVariable Integer numero) {
        Optional<Mesa> mesa = mesaService.encontrarPeloNumeroDaMesa(restauranteId, numero);
        return mesa.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarMesa(@RequestBody Mesa mesa) {
        Mesa mesaAtualizada = mesaService.atualizar(mesa);
        if (mesaAtualizada != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Mesa atualizada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mesa não encontrada.");
        }
    }

    @DeleteMapping("/deletar/{mesaId}")
    public ResponseEntity<String> deletarMesa(@PathVariable UUID mesaId) {
        Optional<Mesa> mesa = mesaService.encontrarMesa(mesaId);
        if (mesa.isPresent()) {
            mesaService.deletar(mesa.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Mesa deletada com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mesa não encontrada.");
        }
    }
}