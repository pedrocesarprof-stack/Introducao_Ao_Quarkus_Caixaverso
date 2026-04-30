package com.ada.emprestimo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class EmprestimoResourceTest {

    @Test
    void deveRetornar400QuandoClienteIdAusente() {
        String body = """
                {
                    "valorTotal": 5000.00,
                    "quantidadeParcelas": 12,
                    "tipoAmortizacao": "SAC"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/emprestimos")
                .then()
                .statusCode(400)
                .body("violations", not(empty()));
    }

    @Test
    void deveRetornar400QuandoValorTotalAbaixoDoMinimo() {
        String body = """
                {
                    "clienteId": "123e4567-e89b-12d3-a456-426614174000",
                    "valorTotal": 50.00,
                    "quantidadeParcelas": 12,
                    "tipoAmortizacao": "SAC"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/emprestimos")
                .then()
                .statusCode(400)
                .body("violations", not(empty()));
    }

    @Test
    void deveRetornar400QuandoValorTotalAcimaDoMaximo() {
        String body = """
                {
                    "clienteId": "123e4567-e89b-12d3-a456-426614174000",
                    "valorTotal": 99999999.00,
                    "quantidadeParcelas": 12,
                    "tipoAmortizacao": "SAC"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/emprestimos")
                .then()
                .statusCode(400)
                .body("violations", not(empty()));
    }

    @Test
    void deveRetornar400QuandoQuantidadeParcelasAbaixoDoMinimo() {
        String body = """
                {
                    "clienteId": "123e4567-e89b-12d3-a456-426614174000",
                    "valorTotal": 5000.00,
                    "quantidadeParcelas": 0,
                    "tipoAmortizacao": "SAC"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/emprestimos")
                .then()
                .statusCode(400)
                .body("violations", not(empty()));
    }

    @Test
    void deveRetornar400QuandoQuantidadeParcelasAcimaDoMaximo() {
        String body = """
                {
                    "clienteId": "123e4567-e89b-12d3-a456-426614174000",
                    "valorTotal": 5000.00,
                    "quantidadeParcelas": 481,
                    "tipoAmortizacao": "SAC"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/emprestimos")
                .then()
                .statusCode(400)
                .body("violations", not(empty()));
    }

    @Test
    void deveRetornar400QuandoTipoAmortizacaoAusente() {
        String body = """
                {
                    "clienteId": "123e4567-e89b-12d3-a456-426614174000",
                    "valorTotal": 5000.00,
                    "quantidadeParcelas": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/emprestimos")
                .then()
                .statusCode(400)
                .body("violations", not(empty()));
    }
}

