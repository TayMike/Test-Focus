package org.example.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.example.entities.Mesa;
import org.example.entities.Restaurante;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ReservaTestBDD {

    Response response;
    Restaurante restaurante;
    Mesa mesa;
    private final String ENDPOINT_API_RESERVA = "http://localhost:8080/mesas/reservar";

    @Dado("o restaurante tem mesas disponíveis")
    public void o_restaurante_tem_mesas_disponíveis() {
        restaurante = Restaurante.builder()
                .nome("Teste")
                .localizacao("Rua X")
                .tipoCozinha("Italiana")
                .horario(List.of(LocalTime.of(8, 0), LocalTime.of(14, 0)))
                .capacidade(10)
                .build();
    }

    @Dado("que o usuário está na página de reservas de um restaurante")
    public void que_o_usuário_está_na_página_de_reservas_de_um_restaurante() {
        mesa = Mesa.builder()
                .restaurante(restaurante)
                .numero(2)
                .dataHora(List.of(LocalDateTime.of(2024,3,30,12,0)))
                .build();
    }

    @Quando("o usuário escolher a data, o horário e o número de pessoas")
    public void o_usuário_escolher_a_data_o_horário_e_o_número_de_pessoas() {
        response  = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mesa)
                .when()
                .post(ENDPOINT_API_RESERVA);
    }

    @Então("a reserva será confirmada com sucesso")
    public void a_reserva_será_confirmada_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Então("o restaurante será notificado sobre a reserva")
    public void o_restaurante_será_notificado_sobre_a_reserva() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schemas/mesa.schema.json"));
    }

}