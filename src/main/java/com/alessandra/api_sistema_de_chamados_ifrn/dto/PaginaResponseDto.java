package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginaResponseDto<T>(List<T> itens, int pagina, int tamanho,
                                  long totalElementos, int totalPaginas) {

    public static <T> PaginaResponseDto<T> from(Page<T> page) {
        return new PaginaResponseDto<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
