package com.minhabiblioteca.biblioteca;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class ViaCepServiceTest {

    private WireMockServer wireMockServer;
    private ViaCepService viaCepService;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        viaCepService = new ViaCepService("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void deveBuscarCepComSucesso() {
        wireMockServer.stubFor(get(urlEqualTo("/ws/01310100/json/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"cep\":\"01310-100\",\"logradouro\":\"Avenida Paulista\",\"bairro\":\"Bela Vista\",\"localidade\":\"São Paulo\",\"uf\":\"SP\"}")));

        Map<String, String> resultado = viaCepService.buscarCep("01310100");

        assertNotNull(resultado);
        assertEquals("01310-100", resultado.get("cep"));
        assertEquals("Avenida Paulista", resultado.get("logradouro"));
        assertEquals("São Paulo", resultado.get("localidade"));
    }

    @Test
    void deveRetornarErroParaCepInvalido() {
        wireMockServer.stubFor(get(urlEqualTo("/ws/00000000/json/"))
                .willReturn(aResponse()
                        .withStatus(400)));

        assertThrows(RuntimeException.class, () -> viaCepService.buscarCep("00000000"));
    }

    @ParameterizedTest
    @CsvSource({
        "01310100, 01310-100, Avenida Paulista,    São Paulo, SP",
        "20040020, 20040-020, Avenida Rio Branco,  Rio de Janeiro, RJ",
        "30130010, 30130-010, Avenida Afonso Pena, Belo Horizonte, MG"
    })
    void deveBuscarMultiplosCepsComSucesso(String cep, String cepFormatado, String logradouro, String cidade, String uf) {
        wireMockServer.stubFor(get(urlEqualTo("/ws/" + cep + "/json/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"cep\":\"" + cepFormatado + "\",\"logradouro\":\"" + logradouro + "\",\"localidade\":\"" + cidade + "\",\"uf\":\"" + uf + "\"}")));

        Map<String, String> resultado = viaCepService.buscarCep(cep);

        assertNotNull(resultado);
        assertEquals(cepFormatado, resultado.get("cep"));
        assertEquals(cidade, resultado.get("localidade"));
    }
}