package br.com.alura.codechella.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("vendas")
public class Venda {
    @Id
    private Long id;

    private Long ingressoId;
    private int total;
}
