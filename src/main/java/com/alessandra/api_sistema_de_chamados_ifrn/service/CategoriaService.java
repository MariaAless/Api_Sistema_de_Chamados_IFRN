package com.alessandra.api_sistema_de_chamados_ifrn.service;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.CategoriaRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.CategoriaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RecursoNaoEncontradoException;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RegraNegocioException;
import com.alessandra.api_sistema_de_chamados_ifrn.models.CategoriaModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.CategoriaRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.ChamadoRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final SetorRepository setorRepository;
    private final ChamadoRepository chamadoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            SetorRepository setorRepository,
                            ChamadoRepository chamadoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.setorRepository = setorRepository;
        this.chamadoRepository = chamadoRepository;
    }

    @Transactional(readOnly = true)
    public PaginaResponseDto<CategoriaResponseDto> listar(String nome, Long setorId, Pageable pageable) {
        return PaginaResponseDto.from(categoriaRepository
                .filtrar(padraoLike(nome), setorId, pageable)
                .map(CategoriaResponseDto::from));
    }

    // GET /setores/{id}/categorias
    @Transactional(readOnly = true)
    public PaginaResponseDto<CategoriaResponseDto> listarPorSetor(Long setorId, Pageable pageable) {
        if (!setorRepository.existsById(setorId)) {
            throw new RecursoNaoEncontradoException("Setor não encontrado: " + setorId);
        }
        return listar(null, setorId, pageable);
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDto buscar(Long id) {
        return CategoriaResponseDto.from(buscarModel(id));
    }

    @Transactional
    public CategoriaResponseDto criar(CategoriaRequestDto req) {
        CategoriaModel categoria = new CategoriaModel();
        preencher(categoria, req);
        return CategoriaResponseDto.from(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDto atualizar(Long id, CategoriaRequestDto req) {
        CategoriaModel categoria = buscarModel(id);
        preencher(categoria, req);
        return CategoriaResponseDto.from(categoriaRepository.save(categoria));
    }

    // Regra 8: não excluir categoria com chamados vinculados
    @Transactional
    public void excluir(Long id) {
        CategoriaModel categoria = buscarModel(id);
        if (chamadoRepository.existsByCategoriaId(id)) {
            throw new RegraNegocioException("Categoria com chamados vinculados não pode ser excluída");
        }
        categoriaRepository.delete(categoria);
    }

    private void preencher(CategoriaModel categoria, CategoriaRequestDto req) {
        SetorModel setor = setorRepository.findById(req.setorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Setor não encontrado: " + req.setorId()));
        categoria.setNome(req.nome().trim());
        categoria.setDescricao(req.descricao());
        categoria.setSetor(setor);
    }

    private CategoriaModel buscarModel(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + id));
    }

    private String padraoLike(String texto) {
        return (texto == null || texto.isBlank()) ? "%" : "%" + texto.trim().toLowerCase() + "%";
    }
}
