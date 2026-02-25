package br.com.alura.codechella.repository;

import br.com.alura.codechella.model.Venda;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface VendaRepository extends ReactiveCrudRepository<Venda, Long> {
}
