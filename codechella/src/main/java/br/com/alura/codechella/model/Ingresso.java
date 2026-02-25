package br.com.alura.codechella.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ingressos")
public class Ingresso {
    @Id
    private Long id;

    private Long eventoId;
    private TipoIngresso tipo;
    private Double valor;
    private int total;
}
