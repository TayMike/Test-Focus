package org.example.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.restassured.response.Response;
import org.example.entities.Restaurante;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestauranteTestBDD {

    private Response response;

    private Restaurante restaurante;

    private final String ENDPOINT_API_RESTAURANTE = "http://localhost:8080/restaurantes/cadastrar";

    @Dado("que o restaurante deseja se cadastrar")
    public void que_o_restaurante_deseja_se_cadastrar() {
        restaurante = Restaurante.builder()
                .nome("Teste")
                .localizacao("Rua X")
                .tipoCozinha("Italiana")
                .horario(List.of(LocalTime.of(8,0), LocalTime.of(14,0)))
                .capacidade(10)
                .build();
    }

    @Quando("ele fornecer as informações de nome, localização, tipo de cozinha, horários de funcionamento e capacidade")
    public void ele_fornecer_as_informações_de_nome_localização_tipo_de_cozinha_horários_de_funcionamento_e_capacidade() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restaurante)
                .when()
                .post(ENDPOINT_API_RESTAURANTE);
    }
    @Então("o restaurante será cadastrado com sucesso no sistema")
    public void o_restaurante_será_cadastrado_com_sucesso_no_sistema() {
        response.then()
                .statusCode(HttpStatus.CREATED.value());
    }
    @Então("as informações do restaurante serão armazenadas corretamente")
    public void as_informações_do_restaurante_serão_armazenadas_corretamente() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

}
