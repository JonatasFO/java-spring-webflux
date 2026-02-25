package br.com.alura.codechella.service;

import br.com.alura.codechella.dto.CompraDto;
import br.com.alura.codechella.dto.IngressoDto;
import br.com.alura.codechella.model.Ingresso;
import br.com.alura.codechella.model.Venda;
import br.com.alura.codechella.repository.IngressoRepository;
import br.com.alura.codechella.repository.VendaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class IngressoService {
    private IngressoRepository repository;
    private VendaRepository vendaRepository;

    public Flux<IngressoDto> obterTodos() {
        return repository.findAll().map(IngressoDto::toDto);
    }

    public Mono<IngressoDto> obterPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(IngressoDto::toDto);
    }

    public Mono<IngressoDto> cadastrar(IngressoDto dto) {
        return repository.save(dto.toModel()).map(IngressoDto::toDto);
    }

    public Mono<Void> excluir(Long id) {
        return repository.findById(id).flatMap(repository::delete);
    }

    public Mono<IngressoDto> alterar(Long id, IngressoDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(ingresso -> atualizaIngresso(ingresso, dto))
                .map(IngressoDto::toDto);
    }

    private Mono<Ingresso> atualizaIngresso(Ingresso ingresso, IngressoDto dto) {
        ingresso.setId(ingresso.getId());
        ingresso.setEventoId(dto.eventoId());
        ingresso.setTipo(dto.tipo());
        ingresso.setValor(dto.valor());
        ingresso.setTotal(dto.total());

        return repository.save(ingresso);
    }

    @Transactional
    public Mono<IngressoDto> comprar(CompraDto dto) {
        return repository.findById(dto.ingressoId())
                .flatMap(ingresso -> {
                    final var venda = new Venda();
                    venda.setIngressoId(ingresso.getId());
                    venda.setTotal(dto.total());
                    return vendaRepository.save(venda).then(Mono.defer(() -> {
                        ingresso.setTotal(ingresso.getTotal() - dto.total());

                        return repository.save(ingresso);
                    }));
                }).map(IngressoDto::toDto);
    }
}
