package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InteracaoRequestDto(
        @NotNull Long autorId,
        @NotBlank String texto) {
}
