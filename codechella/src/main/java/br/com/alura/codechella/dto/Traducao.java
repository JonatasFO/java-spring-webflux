package br.com.alura.codechella.dto;

import java.util.List;

public record Traducao(List<Texto> translations) {

    public String getTexto() {
        return translations.get(0).text();
    }
}
