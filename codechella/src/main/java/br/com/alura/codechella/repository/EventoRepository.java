package br.com.alura.codechella.repository;

import br.com.alura.codechella.model.Evento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventoRepository extends ReactiveCrudRepository<Evento, Long> {
}
