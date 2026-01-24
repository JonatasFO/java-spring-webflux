package br.com.alura.codechella.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("eventos")
public class Evento {
    @Id
    private Long id;

    private TipoEvento tipo;
    private String nome;
    private LocalDate data;
    private String descricao;
}
