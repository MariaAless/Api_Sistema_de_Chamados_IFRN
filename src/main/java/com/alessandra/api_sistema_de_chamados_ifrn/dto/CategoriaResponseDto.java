package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.models.CategoriaModel;

public record CategoriaResponseDto(Long id, String nome, String descricao,
                                   Long setorId, String setorSigla) {

    public static CategoriaResponseDto from(CategoriaModel c) {
        return new CategoriaResponseDto(c.getId(), c.getNome(), c.getDescricao(),
                c.getSetor().getId(), c.getSetor().getSigla());
    }
}
