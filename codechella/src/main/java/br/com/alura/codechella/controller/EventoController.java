package br.com.alura.codechella.controller;

import br.com.alura.codechella.model.Evento;
import br.com.alura.codechella.repository.EventoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/eventos")
@AllArgsConstructor
public class EventoController {

    private EventoRepository repository;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Evento> obterTodos() {
        return repository.findAll();
    }


}
