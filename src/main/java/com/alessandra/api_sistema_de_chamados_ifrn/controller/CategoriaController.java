package com.alessandra.api_sistema_de_chamados_ifrn.controller;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.CategoriaRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.CategoriaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> criar(@RequestBody @Valid CategoriaRequestDto req) {
        CategoriaResponseDto criada = categoriaService.criar(req);
        return ResponseEntity.created(URI.create("/categorias/" + criada.id())).body(criada);
    }

    @GetMapping
    public PaginaResponseDto<CategoriaResponseDto> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long setorId,
            Pageable pageable) {
        return categoriaService.listar(nome, setorId, pageable);
    }

    @GetMapping("/{id}")
    public CategoriaResponseDto buscar(@PathVariable Long id) {
        return categoriaService.buscar(id);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDto atualizar(@PathVariable Long id,
                                          @RequestBody @Valid CategoriaRequestDto req) {
        return categoriaService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}