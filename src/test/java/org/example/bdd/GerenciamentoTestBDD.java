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
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GerenciamentoTestBDD {

    Response response;
    Restaurante restaurante;
    Mesa mesa;
    String ENDPOINT_API_FAZER_RESERVA = "http://localhost:8080/mesas/reservar";
    String ENDPOINT_API_ATUALIZAR_RESERVA = "http://localhost:8080/mesas/atualizar";
    String ENDPOINT_API_RESERVAS_DO_RESTAURANTE = "http://localhost:8080/restaurantes/restaurante";

    @Dado("que o restaurante tem reservas para o dia")
    public void que_o_restaurante_tem_reservas_para_o_dia() {
        restaurante = Restaurante.builder()
                .nome("Teste")
                .localizacao("Rua X")
                .tipoCozinha("Italiana")
                .horario(List.of(LocalTime.of(8, 0), LocalTime.of(14, 0)))
                .capacidade(10)
                .build();

        mesa = Mesa.builder()
                .restaurante(restaurante)
                .numero(2)
                .dataHora(List.of(LocalDateTime.of(2024,3,30,12,0)))
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mesa)
                .when()
                .post(ENDPOINT_API_FAZER_RESERVA);
    }

    @Quando("o restaurante acessar a lista de reservas")
    public void o_restaurante_acessar_a_lista_de_reservas() {
        response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(restaurante)
                    .when()
                    .get(ENDPOINT_API_RESERVAS_DO_RESTAURANTE+"/{id}",1);
    }

    @Então("ele verá todas as reservas para a data e horário específicos")
    public void ele_verá_todas_as_reservas_para_a_data_e_horário_específicos() {
        response.then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Dado("que o restaurante tem uma reserva confirmada")
    public void que_o_restaurante_tem_uma_reserva_confirmada() {
        mesa.getConfirmacao().add(Boolean.TRUE);
    }

    @Quando("ele mudar o status da reserva para Confirmada ou Cancelada")
    public void ele_mudar_o_status_da_reserva_para_ou_Confirmada_ou_Cancelada() {
        mesa = Mesa.builder()
                .restaurante(restaurante)
                .numero(2)
                .dataHora(List.of(LocalDateTime.of(2024,3,30,12,0)))
                .confirmacao(Collections.singletonList(Boolean.FALSE))
                .build();

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mesa)
                .when()
                .put(ENDPOINT_API_ATUALIZAR_RESERVA);
    }

    @Então("o status da reserva será atualizado no sistema")
    public void o_status_da_reserva_será_atualizado_no_sistema() {
        response.then()
                .statusCode(HttpStatus.CREATED.value());    }
}
