package org.example.bdd;

import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.restassured.response.Response;
import org.example.entities.Restaurante;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BuscaTesteBDD {

    private Response response;

    private Restaurante restaurante;

    private final String ENDPOINT_API_RESTAURANTE = "http://localhost:8080/restaurantes";

    @Dado("que o usuário está na página de busca de restaurantes pelo nome")
    public void que_o_usuário_está_na_página_de_busca_de_restaurantes_pelo_nome() {
        restaurante = Restaurante.builder()
                .nome("Teste")
                .localizacao("Rua X")
                .tipoCozinha("Italiana")
                .horario(List.of(LocalTime.of(8,0), LocalTime.of(14,0)))
                .capacidade(10)
                .build();
    }
    @Quando("ele digitar o nome do restaurante")
    public void ele_digitar_o_nome_do_restaurante() {
        response = given()
                .pathParam("nome", restaurante.getNome())
                .when()
                .get(ENDPOINT_API_RESTAURANTE + "/nome/");
    }
    @Então("ele verá os resultados que correspondem ao nome fornecido")
    public void ele_verá_os_resultados_que_correspondem_ao_nome_fornecido() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

    @Dado("que o usuário está na página de busca de restaurantes pela localizacao")
    public void que_o_usuário_está_na_página_de_busca_de_restaurantes_pela_localizacao() {
        restaurante = Restaurante.builder()
                .nome("Teste")
                .localizacao("Rua X")
                .tipoCozinha("Italiana")
                .horario(List.of(LocalTime.of(8,0), LocalTime.of(14,0)))
                .capacidade(10)
                .build();
    }
    @Quando("ele fornecer a localização desejada")
    public void ele_fornecer_a_localização_desejada() {
        response = given()
                .pathParam("localizacao", restaurante.getLocalizacao())
                .when()
                .get(ENDPOINT_API_RESTAURANTE + "/localizacao/");
    }
    @Então("ele verá os restaurantes disponíveis naquela localização")
    public void ele_verá_os_restaurantes_disponíveis_naquela_localização() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

    @Dado("que o usuário está na página de busca de restaurantes pelo tipo cozinha")
    public void que_o_usuário_está_na_página_de_busca_de_restaurantes_pelo_tipo_cozinha() {
        restaurante = Restaurante.builder()
                .nome("Teste")
                .localizacao("Rua X")
                .tipoCozinha("Italiana")
                .horario(List.of(LocalTime.of(8,0), LocalTime.of(14,0)))
                .capacidade(10)
                .build();
    }
    @Quando("ele selecionar o tipo de cozinha desejado")
    public void ele_selecionar_o_tipo_de_cozinha_desejado() {
        response = given()
                .pathParam("tipoCozinha", restaurante.getLocalizacao())
                .when()
                .get(ENDPOINT_API_RESTAURANTE + "/tipoCozinha/");
    }
    @Então("ele verá os restaurantes que oferecem aquele tipo de cozinha")
    public void ele_verá_os_restaurantes_que_oferecem_aquele_tipo_de_cozinha() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

}
