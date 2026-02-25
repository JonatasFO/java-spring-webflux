package br.com.alura.codechella.controller;

import br.com.alura.codechella.dto.EventoDto;
import br.com.alura.codechella.service.EventoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;
    private final Sinks.Many<EventoDto> eventoSink;

    public EventoController(EventoService service) {
        this.service = service;
        this.eventoSink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @GetMapping
    public Flux<EventoDto> obterTodos() {
        return service.obterTodos();
    }

    @GetMapping(value = "/categoria/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> obterPorTipo(@PathVariable String tipo) {
        return Flux.merge(service.obterPorTipo(tipo), eventoSink.asFlux())
                .delayElements(Duration.ofSeconds(4));
    }

    @GetMapping("/{id}")
    public Mono<EventoDto> obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @PostMapping
    public Mono<EventoDto> cadastrar(@RequestBody EventoDto eventoDto) {
        return service.cadastar(eventoDto).doOnSuccess(eventoSink::tryEmitNext);
    }

    @PutMapping("/{id}")
    public Mono<EventoDto> atualizar(@PathVariable Long id, @RequestBody EventoDto eventoDto) {
        return service.atualizar(id, eventoDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> excluir(@PathVariable Long id) {
        return service.excluir(id);
    }

}
