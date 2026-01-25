package br.com.alura.codechella.controller;

import br.com.alura.codechella.dto.EventoDto;
import br.com.alura.codechella.service.EventoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/eventos")
@AllArgsConstructor
public class EventoController {

    private EventoService service;

    @GetMapping//(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> obterTodos() {
        return service.obterTodos();
    }

    @GetMapping("/{id}")
    public Mono<EventoDto> obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @PostMapping
    public Mono<EventoDto> cadastrar(@RequestBody EventoDto eventoDto) {
        return service.cadastar(eventoDto);
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
