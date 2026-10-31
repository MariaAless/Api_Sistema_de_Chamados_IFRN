package com.alessandra.api_sistema_de_chamados_ifrn.service;

import com.alessandra.api_sistema_de_chamados_ifrn.dto.PaginaResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.UsuarioRequestDto;
import com.alessandra.api_sistema_de_chamados_ifrn.dto.UsuarioResponseDto;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RecursoNaoEncontradoException;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RegraNegocioException;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RequisicaoInvalidaException;
import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.UsuarioModel;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.SetorRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.UsuarioRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SetorRepository setorRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, SetorRepository setorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.setorRepository = setorRepository;
    }

    @Transactional(readOnly = true)
    public PaginaResponseDto<UsuarioResponseDto> listar(String nome, TipoUsuario tipo,
                                                        Long setorId, Pageable pageable) {
        return PaginaResponseDto.from(usuarioRepository
                .filtrar(padraoLike(nome), tipo, setorId, pageable)
                .map(UsuarioResponseDto::from));
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto buscar(Long id) {
        return UsuarioResponseDto.from(buscarModel(id));
    }

    // e-mail duplicado -> 409
    @Transactional
    public UsuarioResponseDto criar(UsuarioRequestDto req) {
        String email = req.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new RegraNegocioException("Já existe um usuário com o e-mail " + email);
        }
        UsuarioModel usuario = new UsuarioModel();
        preencher(usuario, req, email);
        return UsuarioResponseDto.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDto atualizar(Long id, UsuarioRequestDto req) {
        UsuarioModel usuario = buscarModel(id);
        String email = req.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new RegraNegocioException("Já existe um usuário com o e-mail " + email);
        }
        preencher(usuario, req, email);
        return UsuarioResponseDto.from(usuarioRepository.save(usuario));
    }

    private void preencher(UsuarioModel usuario, UsuarioRequestDto req, String email) {
        usuario.setNome(req.nome().trim());
        usuario.setEmail(email);
        usuario.setMatricula(req.matricula().trim());
        usuario.setTipo(req.tipo());

        if (req.tipo() == TipoUsuario.TECNICO) {
            if (req.setorId() == null) {
                throw new RequisicaoInvalidaException("Técnico deve ser vinculado a um setor");
            }
            SetorModel setor = setorRepository.findById(req.setorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Setor não encontrado: " + req.setorId()));
            usuario.setSetor(setor);
        } else {
            usuario.setSetor(null);   // solicitante não pertence a setor
        }
    }

    private UsuarioModel buscarModel(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }

    private String padraoLike(String texto) {
        return (texto == null || texto.isBlank()) ? "%" : "%" + texto.trim().toLowerCase() + "%";
    }
}