package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.models.InteracaoModel;

import java.time.LocalDateTime;

public record InteracaoResponseDto(Long id, String texto, LocalDateTime dataHora,
                                   Long autorId, String autorNome) {
    public static InteracaoResponseDto from(InteracaoModel i) {
        return new InteracaoResponseDto(i.getId(), i.getTexto(), i.getDataHora(),
                i.getAutor().getId(), i.getAutor().getNome());
    }
}
