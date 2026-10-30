package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 30) String matricula,
        @NotNull TipoUsuario tipo,
        Long setorId
) {
}
