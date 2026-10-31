package com.alessandra.api_sistema_de_chamados_ifrn.controller;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.AssumirRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.CancelarRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.ChamadoDetalheResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.ChamadoFiltroDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.ChamadoRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.ChamadoResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.InteracaoRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.InteracaoResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.ResolverRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Prioridade;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Status;
import com.alessandra.api_sistema_de_chamados_ifrn.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    // abre um chamado -> 201
    @PostMapping
    public ResponseEntity<ChamadoDetalheResponseDto> abrir(@RequestBody @Valid ChamadoRequestDto req) {
        ChamadoDetalheResponseDto criado = chamadoService.abrir(req);
        return ResponseEntity.created(URI.create("/chamados/" + criado.id())).body(criado);
    }

    // lista paginada com filtros -> 200
    @GetMapping
    public PaginaResponseDto<ChamadoResponseDto> listar(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) Long setorId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long solicitanteId,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(sort = "dataAbertura", direction = Sort.Direction.DESC) Pageable pageable) {

        ChamadoFiltroDto filtro = new ChamadoFiltroDto(status, prioridade, setorId, categoriaId,
                solicitanteId, tecnicoId, dataInicio, dataFim);
        return chamadoService.listar(filtro, pageable);
    }

    // detalha com as interações -> 200
    @GetMapping("/{id}")
    public ChamadoDetalheResponseDto detalhar(@PathVariable Long id) {
        return chamadoService.detalhar(id);
    }

    @PatchMapping("/{id}/assumir")
    public ChamadoDetalheResponseDto assumir(@PathVariable Long id,
                                             @RequestBody @Valid AssumirRequestDto req) {
        return chamadoService.assumir(id, req);
    }

    @PatchMapping("/{id}/resolver")
    public ChamadoDetalheResponseDto resolver(@PathVariable Long id,
                                              @RequestBody @Valid ResolverRequestDto req) {
        return chamadoService.resolver(id, req);
    }

    @PatchMapping("/{id}/cancelar")
    public ChamadoDetalheResponseDto cancelar(@PathVariable Long id,
                                              @RequestBody @Valid CancelarRequestDto req) {
        return chamadoService.cancelar(id, req);
    }

    // registra uma interação -> 201
    @PostMapping("/{id}/interacoes")
    public ResponseEntity<InteracaoResponseDto> registrarInteracao(
            @PathVariable Long id, @RequestBody @Valid InteracaoRequestDto req) {
        InteracaoResponseDto criada = chamadoService.registrarInteracao(id, req);
        return ResponseEntity.created(URI.create("/chamados/" + id)).body(criada);
    }
}