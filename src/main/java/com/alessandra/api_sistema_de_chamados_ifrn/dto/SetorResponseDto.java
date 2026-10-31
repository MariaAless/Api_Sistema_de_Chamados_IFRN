package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;

public record SetorResponseDto(Long id, String nome, String sigla) {

    // utilizado para converter um objeto SetorModel em um objeto SetorResponse
    public static SetorResponseDto from(SetorModel s) {
        return new SetorResponseDto(s.getId(), s.getNome(), s.getSigla());
    }

}
