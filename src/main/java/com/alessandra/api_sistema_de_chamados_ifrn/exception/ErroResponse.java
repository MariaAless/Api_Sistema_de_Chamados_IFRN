package com.alessandra.api_sistema_de_chamados_ifrn.exception;

import java.time.LocalDateTime;

public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String mensagem,
        String caminho)
{ }


