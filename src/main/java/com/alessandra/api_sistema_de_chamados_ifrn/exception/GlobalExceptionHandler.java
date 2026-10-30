package com.alessandra.api_sistema_de_chamados_ifrn.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException e,
                                                      HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    // 409: violação de regra de negócio
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException e,
                                                     HttpServletRequest req) {
        return montar(HttpStatus.CONFLICT, e.getMessage(), req);
    }

    // 400: dado inválido detectado no Service
    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErroResponse> requisicaoInvalida(RequisicaoInvalidaException e,
                                                           HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    // 400: falha do @Valid (campo obrigatório vazio, e-mail mal formatado)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e,
                                                  HttpServletRequest req) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return montar(HttpStatus.BAD_REQUEST, mensagem, req);
    }

    // 400: JSON malformado ou enum inexistente (ex.: "prioridade": "URGENTE")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoIlegivel(HttpMessageNotReadableException e,
                                                      HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado", req);
    }

    // 400: parâmetro com tipo errado (ex.: /chamados/abc ou ?status=XYZ)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tipoInvalido(MethodArgumentTypeMismatchException e,
                                                     HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + e.getName() + "'", req);
    }

    // rede de segurança: duas requisições gravando o mesmo e-mail ao mesmo tempo
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException e,
                                                    HttpServletRequest req) {
        return montar(HttpStatus.CONFLICT,
                "Operação viola uma restrição de integridade dos dados", req);
    }


    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem,
                                                HttpServletRequest req) {
        ErroResponse corpo = new ErroResponse(
                LocalDateTime.now(), status.value(), mensagem, req.getRequestURI());
        return ResponseEntity.status(status).body(corpo);
    }
}
