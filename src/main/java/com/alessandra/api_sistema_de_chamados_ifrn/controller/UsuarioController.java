package com.alessandra.api_sistema_de_chamados_ifrn.controller;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.ChamadoResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.UsuarioRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.UsuarioResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Status;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import com.alessandra.api_sistema_de_chamados_ifrn.service.ChamadoService;
import com.alessandra.api_sistema_de_chamados_ifrn.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ChamadoService chamadoService;

    public UsuarioController(UsuarioService usuarioService, ChamadoService chamadoService) {
        this.usuarioService = usuarioService;
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criar(@RequestBody @Valid UsuarioRequestDto req) {
        UsuarioResponseDto criado = usuarioService.criar(req);
        return ResponseEntity.created(URI.create("/usuarios/" + criado.id())).body(criado);
    }

    @GetMapping
    public PaginaResponseDto<UsuarioResponseDto> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) TipoUsuario tipo,
            @RequestParam(required = false) Long setorId,
            Pageable pageable) {
        return usuarioService.listar(nome, tipo, setorId, pageable);
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto atualizar(@PathVariable Long id,
                                        @RequestBody @Valid UsuarioRequestDto req) {
        return usuarioService.atualizar(id, req);
    }

    // GET /usuarios/{id}/chamados
    @GetMapping("/{id}/chamados")
    public PaginaResponseDto<ChamadoResponseDto> listarChamados(
            @PathVariable Long id,
            @RequestParam(required = false) Status status,
            Pageable pageable) {
        return chamadoService.listarDoUsuario(id, status, pageable);
    }
}