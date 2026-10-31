package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.Prioridade;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Status;
import com.alessandra.api_sistema_de_chamados_ifrn.models.ChamadoModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.UsuarioModel;

import java.time.LocalDateTime;

public record ChamadoResponseDto(
        Long id, String titulo, String local,
        Prioridade prioridade, Status status,
        LocalDateTime dataAbertura,
        Long categoriaId, String categoriaNome,
        Long solicitanteId, String solicitanteNome,
        Long tecnicoId, String tecnicoNome
) {
    public static ChamadoResponseDto from(ChamadoModel c) {
        UsuarioModel t = c.getTecnico();
        return new ChamadoResponseDto(c.getId(), c.getTitulo(), c.getLocal(),
                c.getPrioridade(), c.getStatus(), c.getDataAbertura(),
                c.getCategoria().getId(), c.getCategoria().getNome(),
                c.getSolicitante().getId(), c.getSolicitante().getNome(),
                t != null ? t.getId() : null,
                t != null ? t.getNome() : null);
    }
}
