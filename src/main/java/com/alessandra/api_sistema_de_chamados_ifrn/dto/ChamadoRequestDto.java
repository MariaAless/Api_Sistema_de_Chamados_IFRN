package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChamadoRequestDto (
        @NotBlank @Size(max = 150) String titulo,
        @NotBlank String descricao,
        @NotBlank @Size(max = 150) String local,
        Prioridade prioridade,
        @NotNull Long categoriaId,
        @NotNull Long solicitanteId
){
}
