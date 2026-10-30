package com.alessandra.api_sistema_de_chamados_ifrn.controller;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.CategoriaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.SetorRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.SetorResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.service.CategoriaService;
import com.alessandra.api_sistema_de_chamados_ifrn.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/setores")
public class SetorController {

    private final SetorService setorService;
    private final CategoriaService categoriaService;

    public SetorController(SetorService setorService, CategoriaService categoriaService) {
        this.setorService = setorService;
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<SetorResponseDto> criar(@RequestBody @Valid SetorRequestDto req) {
        SetorResponseDto criado = setorService.criar(req);
        return ResponseEntity.created(URI.create("/setores/" + criado.id())).body(criado);
    }

    @GetMapping
    public PaginaResponseDto<SetorResponseDto> listar(
            @RequestParam(required = false) String nome, Pageable pageable) {
        return setorService.listar(nome, pageable);
    }

    @GetMapping("/{id}")
    public SetorResponseDto buscar(@PathVariable Long id) {
        return setorService.buscar(id);
    }

    @PutMapping("/{id}")
    public SetorResponseDto atualizar(@PathVariable Long id, @RequestBody @Valid SetorRequestDto req) {
        return setorService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        setorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // GET /setores/{id}/categorias
    @GetMapping("/{id}/categorias")
    public PaginaResponseDto<CategoriaResponseDto> listarCategorias(@PathVariable Long id,
                                                                    Pageable pageable) {
        return categoriaService.listarPorSetor(id, pageable);
    }
}