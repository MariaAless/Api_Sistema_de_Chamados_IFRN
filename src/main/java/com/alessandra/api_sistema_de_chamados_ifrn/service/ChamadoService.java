package com.alessandra.api_sistema_de_chamados_ifrn.service;

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
import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RecursoNaoEncontradoException;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RegraNegocioException;
import com.alessandra.api_sistema_de_chamados_ifrn.exception.RequisicaoInvalidaException;
import com.alessandra.api_sistema_de_chamados_ifrn.models.CategoriaModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.ChamadoModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.InteracaoModel;
import com.alessandra.api_sistema_de_chamados_ifrn.models.UsuarioModel;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.CategoriaRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.ChamadoRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.InteracaoRepository;
import com.alessandra.api_sistema_de_chamados_ifrn.repository.UsuarioRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ChamadoService {

    // datas limite usadas quando o filtro de período não é informado
    private static final LocalDateTime DATA_MINIMA = LocalDateTime.of(1900, 1, 1, 0, 0);
    private static final LocalDateTime DATA_MAXIMA = LocalDateTime.of(9999, 12, 31, 0, 0);

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final InteracaoRepository interacaoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          UsuarioRepository usuarioRepository,
                          CategoriaRepository categoriaRepository,
                          InteracaoRepository interacaoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.interacaoRepository = interacaoRepository;
    }



    @Transactional(readOnly = true)
    public PaginaResponseDto<ChamadoResponseDto> listar(ChamadoFiltroDto f, Pageable pageable) {
        if (f.dataInicio() != null && f.dataFim() != null && f.dataInicio().isAfter(f.dataFim())) {
            throw new RequisicaoInvalidaException("dataInicio não pode ser posterior a dataFim");
        }
        LocalDateTime inicio = f.dataInicio() != null ? f.dataInicio().atStartOfDay() : DATA_MINIMA;
        // até o fim do dia final: antes das 00:00 do dia seguinte
        LocalDateTime fim = f.dataFim() != null ? f.dataFim().plusDays(1).atStartOfDay() : DATA_MAXIMA;

        return PaginaResponseDto.from(chamadoRepository
                .filtrar(f.status(), f.prioridade(), f.setorId(), f.categoriaId(),
                        f.solicitanteId(), f.tecnicoId(), inicio, fim, pageable)
                .map(ChamadoResponseDto::from));
    }

    // GET /usuarios/{id}/chamados: abertos pelo solicitante ou atribuídos ao técnico
    @Transactional(readOnly = true)
    public PaginaResponseDto<ChamadoResponseDto> listarDoUsuario(Long usuarioId, Status status,
                                                                 Pageable pageable) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado: " + usuarioId);
        }
        return PaginaResponseDto.from(chamadoRepository
                .doUsuario(usuarioId, status, pageable)
                .map(ChamadoResponseDto::from));
    }

    @Transactional(readOnly = true)
    public ChamadoDetalheResponseDto detalhar(Long id) {
        return ChamadoDetalheResponseDto.from(buscarModel(id));
    }

    // regra 1 e 2 :abrir

    @Transactional
    public ChamadoDetalheResponseDto abrir(ChamadoRequestDto req) {
        UsuarioModel solicitante = buscarUsuario(req.solicitanteId());
        if (solicitante.getTipo() != TipoUsuario.SOLICITANTE) {
            throw new RegraNegocioException("Apenas usuários do tipo SOLICITANTE podem abrir chamados");
        }
        CategoriaModel categoria = categoriaRepository.findById(req.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada: " + req.categoriaId()));

        ChamadoModel chamado = new ChamadoModel();
        chamado.setTitulo(req.titulo().trim());
        chamado.setDescricao(req.descricao().trim());
        chamado.setLocal(req.local().trim());
        chamado.setPrioridade(req.prioridade() != null ? req.prioridade() : Prioridade.MEDIA);
        chamado.setStatus(Status.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setSolicitante(solicitante);
        chamado.setCategoria(categoria);
        return ChamadoDetalheResponseDto.from(chamadoRepository.save(chamado));
    }

    // regra 3: assumir

    @Transactional
    public ChamadoDetalheResponseDto assumir(Long id, AssumirRequestDto req) {
        ChamadoModel chamado = buscarModel(id);
        UsuarioModel tecnico = buscarUsuario(req.tecnicoId());

        exigirStatus(chamado, Status.ABERTO, "assumido");
        if (tecnico.getTipo() != TipoUsuario.TECNICO) {
            throw new RegraNegocioException("Apenas técnicos podem assumir chamados");
        }
        if (tecnico.getSetor() == null
                || !tecnico.getSetor().getId().equals(chamado.getCategoria().getSetor().getId())) {
            throw new RegraNegocioException("A categoria do chamado não pertence ao setor do técnico");
        }
        chamado.setTecnico(tecnico);
        chamado.setStatus(Status.EM_ATENDIMENTO);
        return ChamadoDetalheResponseDto.from(chamadoRepository.save(chamado));
    }

    // regra 4: resolver

    @Transactional
    public ChamadoDetalheResponseDto resolver(Long id, ResolverRequestDto req) {
        ChamadoModel chamado = buscarModel(id);

        exigirStatus(chamado, Status.EM_ATENDIMENTO, "resolvido");
        if (chamado.getTecnico() == null || !chamado.getTecnico().getId().equals(req.tecnicoId())) {
            throw new RegraNegocioException("Somente o técnico atribuído pode resolver o chamado");
        }
        if (req.solucao() == null || req.solucao().isBlank()) {
            throw new RequisicaoInvalidaException("A solução é obrigatória");
        }
        chamado.setSolucao(req.solucao().trim());
        chamado.setDataFechamento(LocalDateTime.now());
        chamado.setStatus(Status.RESOLVIDO);
        return ChamadoDetalheResponseDto.from(chamadoRepository.save(chamado));
    }

    //regra 5: cancelar

    @Transactional
    public ChamadoDetalheResponseDto cancelar(Long id, CancelarRequestDto req) {
        ChamadoModel chamado = buscarModel(id);

        exigirStatus(chamado, Status.ABERTO, "cancelado");
        if (!chamado.getSolicitante().getId().equals(req.solicitanteId())) {
            throw new RegraNegocioException("Somente o solicitante do chamado pode cancelá-lo");
        }
        chamado.setStatus(Status.CANCELADO);
        return ChamadoDetalheResponseDto.from(chamadoRepository.save(chamado));
    }

    // regras 6 e 7: interações

    @Transactional
    public InteracaoResponseDto registrarInteracao(Long chamadoId, InteracaoRequestDto req) {
        ChamadoModel chamado = buscarModel(chamadoId);
        UsuarioModel autor = buscarUsuario(req.autorId());

        // regra 6
        if (chamado.getStatus() == Status.RESOLVIDO || chamado.getStatus() == Status.CANCELADO) {
            throw new RegraNegocioException(
                    "Chamado " + chamado.getStatus() + " não aceita novas interações");
        }
        // regra 7
        boolean ehSolicitante = chamado.getSolicitante().getId().equals(autor.getId());
        boolean ehTecnico = chamado.getTecnico() != null
                && chamado.getTecnico().getId().equals(autor.getId());
        if (!ehSolicitante && !ehTecnico) {
            throw new RegraNegocioException(
                    "Apenas o solicitante e o técnico atribuído podem registrar interações");
        }

        InteracaoModel interacao = new InteracaoModel();
        interacao.setTexto(req.texto().trim());
        interacao.setDataHora(LocalDateTime.now());
        interacao.setChamado(chamado);
        interacao.setAutor(autor);
        return InteracaoResponseDto.from(interacaoRepository.save(interacao));
    }



    // Ciclo de vida: ABERTO -> EM_ATENDIMENTO -> RESOLVIDO e ABERTO -> CANCELADO.
    // Cobre também a regra 6: um chamado finalizado nunca está no status exigido.
    private void exigirStatus(ChamadoModel chamado, Status esperado, String acao) {
        if (chamado.getStatus() != esperado) {
            throw new RegraNegocioException("Chamado com status " + chamado.getStatus()
                    + " não pode ser " + acao + " (exige status " + esperado + ")");
        }
    }

    private ChamadoModel buscarModel(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado: " + id));
    }

    private UsuarioModel buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }
}