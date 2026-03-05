package br.com.alura.codechella;

import br.com.alura.codechella.dto.EventoDto;
import br.com.alura.codechella.model.TipoEvento;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CodechellaApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(3)
    void cadastraNovoEvento() {
        EventoDto eventoDto = new EventoDto(null, TipoEvento.SHOW, "Kiss", LocalDate.parse("2025-01-01"),
                "Show da melhor banda que existe");

        webTestClient.post().uri("/eventos").bodyValue(eventoDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EventoDto.class)
                .value(response -> {
                    assertNotNull(response.id());
                    assertEquals(eventoDto.tipo(), response.tipo());
                    assertEquals(eventoDto.nome(), response.nome());
                    assertEquals(eventoDto.data(), response.data());
                    assertEquals(eventoDto.descricao(), response.descricao());
                });
    }

    @Test
    @Order(1)
    void buscaEvento() {
        EventoDto eventoDto = new EventoDto(13L, TipoEvento.SHOW, "The Weeknd", LocalDate.parse("2025-11-02"),
                "Um show eletrizante ao ar livre com muitos efeitos especiais.");

        webTestClient.get().uri("/eventos")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(EventoDto.class)
                .value(response -> {
                    EventoDto eventoResponse = response.get(12);
                    assertEquals(eventoDto.id(), eventoResponse.id());
                    assertEquals(eventoDto.tipo(), eventoResponse.tipo());
                    assertEquals(eventoDto.nome(), eventoResponse.nome());
                    assertEquals(eventoDto.data(), eventoResponse.data());
                    assertEquals(eventoDto.descricao(), eventoResponse.descricao());
                });
    }

    @Test
    @Order(2)
    void alteraEvento() {
        EventoDto eventoDto = new EventoDto(5L, TipoEvento.CONCERTO, "Metallica", LocalDate.parse("2025-12-01"),
                "Concerto da banda Metallica");

        webTestClient.put().uri("/eventos/{id}", 5L)
                .bodyValue(eventoDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventoDto.class)
                .value(response -> {
                    assertEquals(eventoDto.id(), response.id());
                    assertEquals(eventoDto.tipo(), response.tipo());
                    assertEquals(eventoDto.nome(), response.nome());
                    assertEquals(eventoDto.data(), response.data());
                    assertEquals(eventoDto.descricao(), response.descricao());
                });
    }

    @Test
    @Order(5)
    void excluiEvento() {
        webTestClient.delete().uri("/eventos/{id}", 10L)
                .exchange()
                .expectStatus().isNoContent();
    }

}
