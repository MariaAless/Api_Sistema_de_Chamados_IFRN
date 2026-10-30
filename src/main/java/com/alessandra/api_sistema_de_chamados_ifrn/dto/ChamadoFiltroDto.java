package com.alessandra.api_sistema_de_chamados_ifrn.dto;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.Prioridade;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Status;

import java.time.LocalDate;

public record ChamadoFiltroDto(
        Status status,
        Prioridade prioridade,
        Long setorId,
        Long categoriaId,
        Long solicitanteId,
        Long tecnicoId,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}
