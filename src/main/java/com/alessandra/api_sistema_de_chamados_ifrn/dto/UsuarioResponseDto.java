package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.UsuarioModel;

public record UsuarioResponseDto(Long id, String nome, String email, String matricula,
                                 TipoUsuario tipo, Long setorId, String setorSigl) {
    public static UsuarioResponseDto from(UsuarioModel u) {
        SetorModel s = u.getSetor();
        return new UsuarioResponseDto(u.getId(), u.getNome(), u.getEmail(), u.getMatricula(),
                u.getTipo(),
                s != null ? s.getId() : null,
                s != null ? s.getSigla() : null);
    }
}
