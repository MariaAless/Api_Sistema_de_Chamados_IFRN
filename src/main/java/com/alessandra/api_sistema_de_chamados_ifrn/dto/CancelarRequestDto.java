package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import jakarta.validation.constraints.NotNull;

public record CancelarRequestDto(
        @NotNull Long solicitanteId
) {
}
