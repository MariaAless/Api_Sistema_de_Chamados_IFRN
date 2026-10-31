package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.Prioridade;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Status;
import com.alessandra.api_sistema_de_chamados_ifrn.models.ChamadoModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.UsuarioModel;

import java.time.LocalDateTime;
import java.util.List;

public record ChamadoDetalheResponseDto(Long id, String titulo, String descricao, String local,
                                       Prioridade prioridade, Status status,
                                       LocalDateTime dataAbertura, LocalDateTime dataFechamento,
                                       String solucao,
                                       Long categoriaId, String categoriaNome,
                                       Long solicitanteId, String solicitanteNome,
                                       Long tecnicoId, String tecnicoNome,
                                       List<InteracaoResponseDto> interacoes) {

    public static ChamadoDetalheResponseDto from(ChamadoModel c) {
        UsuarioModel t = c.getTecnico();
        return new ChamadoDetalheResponseDto(c.getId(), c.getTitulo(), c.getDescricao(), c.getLocal(),
                c.getPrioridade(), c.getStatus(), c.getDataAbertura(), c.getDataFechamento(),
                c.getSolucao(),
                c.getCategoria().getId(), c.getCategoria().getNome(),
                c.getSolicitante().getId(), c.getSolicitante().getNome(),
                t != null ? t.getId() : null,
                t != null ? t.getNome() : null,
                c.getInteracoes().stream().map(InteracaoResponseDto::from).toList());
    }
}
