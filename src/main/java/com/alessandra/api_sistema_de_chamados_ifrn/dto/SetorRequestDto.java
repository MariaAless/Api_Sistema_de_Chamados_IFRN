package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record SetorRequestDto(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Size(max = 20) String sigla) {
}