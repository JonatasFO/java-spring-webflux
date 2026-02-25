package br.com.alura.codechella.service;

import br.com.alura.codechella.dto.EventoDto;
import br.com.alura.codechella.integration.TraducaoDeTextos;
import br.com.alura.codechella.model.Evento;
import br.com.alura.codechella.model.TipoEvento;
import br.com.alura.codechella.repository.EventoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EventoService {
    private EventoRepository repository;

    public Flux<EventoDto> obterTodos() {
        return repository.findAll().map(EventoDto::toDto);
    }

    public Mono<EventoDto> obterPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDto::toDto);
    }


    public Mono<EventoDto> cadastar(EventoDto eventoDto) {
        return repository.save(eventoDto.toModel()).map(EventoDto::toDto);
    }

    public Mono<EventoDto> atualizar(Long id, EventoDto eventoDto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Id do evento não encontrado.")))
                .flatMap(evento -> atualizarEvento(evento, eventoDto))
                .map(EventoDto::toDto);
    }

    private Mono<Evento> atualizarEvento(Evento evento, EventoDto eventoDto) {
        evento.setTipo(eventoDto.tipo());
        evento.setNome(eventoDto.nome());
        evento.setDescricao(eventoDto.descricao());
        evento.setData(eventoDto.data());

        return repository.save(evento);
    }

    public Mono<Void> excluir(Long id) {
        return repository.findById(id).flatMap(repository::delete);
    }

    public Flux<EventoDto> obterPorTipo(String tipo) {
        final var tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());

        return repository.findByTipo(tipoEvento).map(EventoDto::toDto);
    }

    public Mono<String> obterTraducao(Long id, String idioma) {
        return repository.findById(id)
                .flatMap(evento -> TraducaoDeTextos.obterTraducao(evento.getDescricao(), idioma));
    }
}
