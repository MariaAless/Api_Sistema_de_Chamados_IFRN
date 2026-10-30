package com.alessandra.api_sistema_de_chamados_ifrn.service;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.SetorRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.SetorResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RecursoNaoEncontradoException;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RegraNegocioException;
import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.CategoriaRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.ChamadoRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.SetorRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.UsuarioRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetorService {

    private final SetorRepository setorRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChamadoRepository chamadoRepository;

    public SetorService(SetorRepository setorRepository,
                        CategoriaRepository categoriaRepository,
                        UsuarioRepository usuarioRepository,
                        ChamadoRepository chamadoRepository) {
        this.setorRepository = setorRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.chamadoRepository = chamadoRepository;
    }

    @Transactional(readOnly = true)
    public PaginaResponseDto<SetorResponseDto> listar(String nome, Pageable pageable) {
        return PaginaResponseDto.from(
                setorRepository.filtrar(padraoLike(nome), pageable).map(SetorResponseDto::from));
    }

    @Transactional(readOnly = true)
    public SetorResponseDto buscar(Long id) {
        return SetorResponseDto.from(buscarModel(id));
    }

    @Transactional
    public SetorResponseDto criar(SetorRequestDto req) {
        String sigla = req.sigla().trim().toUpperCase();
        if (setorRepository.existsBySiglaIgnoreCase(sigla)) {
            throw new RegraNegocioException("Já existe um setor com a sigla " + sigla);
        }
        SetorModel setor = new SetorModel();
        setor.setNome(req.nome().trim());
        setor.setSigla(sigla);
        return SetorResponseDto.from(setorRepository.save(setor));
    }

    @Transactional
    public SetorResponseDto atualizar(Long id, SetorRequestDto req) {
        SetorModel setor = buscarModel(id);
        String sigla = req.sigla().trim().toUpperCase();
        if (setorRepository.existsBySiglaIgnoreCaseAndIdNot(sigla, id)) {
            throw new RegraNegocioException("Já existe um setor com a sigla " + sigla);
        }
        setor.setNome(req.nome().trim());
        setor.setSigla(sigla);
        return SetorResponseDto.from(setorRepository.save(setor));
    }

    // Regra 8: não excluir setor com chamados vinculados
    @Transactional
    public void excluir(Long id) {
        SetorModel setor = buscarModel(id);
        if (chamadoRepository.existsByCategoriaSetorId(id)) {
            throw new RegraNegocioException("Setor com chamados vinculados não pode ser excluído");
        }
        // sem estas duas checagens, a FK do banco geraria erro 500
        if (categoriaRepository.existsBySetorId(id)) {
            throw new RegraNegocioException("Setor com categorias não pode ser excluído");
        }
        if (usuarioRepository.existsBySetorId(id)) {
            throw new RegraNegocioException("Setor com técnicos não pode ser excluído");
        }
        setorRepository.delete(setor);
    }

    private SetorModel buscarModel(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Setor não encontrado: " + id));
    }

    // "Coti" -> "%coti%"; vazio ou nulo -> "%" (traz tudo)
    private String padraoLike(String texto) {
        return (texto == null || texto.isBlank()) ? "%" : "%" + texto.trim().toLowerCase() + "%";
    }
}