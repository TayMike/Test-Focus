package org.example.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.example.dto.AvaliacaoRequest;
import org.example.dto.ComentarioRequest;
import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.example.repositories.MesaRepository;
import org.example.repositories.RestauranteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AvaliacaoComentarioTestBDD {

    private AvaliacaoRequest avaliacaoRequest = new AvaliacaoRequest();
    private ComentarioRequest comentarioRequest = new ComentarioRequest();
    private Response responseAvaliacao;
    private Response responseComentario;
    private String ENDPOINT_API_AVALIACAO = "http://localhost:8080/restaurantes/avaliacao";
    private String ENDPOINT_API_COMENTARIO = "http://localhost:8080/restaurantes/comentario";
    private RestauranteRepository restauranteRepository;
    private MesaRepository mesaRepository;

    @Dado("que o usuário fez uma visita ao restaurante")
    public void que_o_usuário_fez_uma_visita_ao_restaurante() {
        Restaurante restaurante = Restaurante.builder()
                .id(UUID.fromString("c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4"))
                .nome("Teste")
                .capacidade(10)
                .tipoCozinha("Italiana")
                .build();
        restauranteRepository.save(restaurante);

        Mesa mesa = Mesa.builder()
                        .id(UUID.fromString("c0fd5f16-6d42-4f98-b7d1-d32a5c63bba6"))
                        .restaurante(restaurante)
                        .numero(1)
                        .build();
        mesaRepository.save(mesa);

        avaliacaoRequest.setAvaliacao(5);
        avaliacaoRequest.setMesa(1);

        comentarioRequest.setMesa(1);
        comentarioRequest.setComentario("Teste");
    }

    @Quando("o usuário avaliar o restaurante de 1 a 5 estrelas")
    public void o_usuário_avaliar_o_restaurante_de_1_a_5_estrelas() {
        responseAvaliacao = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(avaliacaoRequest)
                .when()
                .put(ENDPOINT_API_AVALIACAO +"/{id}",UUID.fromString("c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4"));    }

    @Quando("deixar um comentário sobre a sua experiência")
    public void deixar_um_comentário_sobre_a_sua_experiência() {
        responseComentario = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(comentarioRequest)
                .when()
                .put(ENDPOINT_API_COMENTARIO +"/{id}",UUID.fromString("c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4"));    }

    @Então("a avaliação e o comentário serão registrados no sistema")
    public void a_avaliação_e_o_comentário_serão_registrados_no_sistema() {
        responseAvaliacao.then()
                .statusCode(HttpStatus.CREATED.value());

        responseComentario.then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Então("outros usuários poderão visualizar a avaliação")
    public void outros_usuários_poderão_visualizar_a_avaliação() {
        responseAvaliacao.then()
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));

        responseComentario.then()
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }
}
