package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.Prioridade;
import com.alessandra.api_sistema_de_chamados_ifrn.enums.Status;
import com.alessandra.api_sistema_de_chamados_ifrn.models.ChamadoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

public interface ChamadoRepository extends JpaRepository<ChamadoRepository, Long> {

    // regra 8 (categoria): "categoria.id" -> existsByCategoriaId
    boolean existsByCategoriaId(Long categoriaId);

    // regra 8 (setor): navega chamado.categoria.setor.id -> existsByCategoriaSetorId
    boolean existsByCategoriaSetorId(Long setorId);

    // GET /chamados: 8 filtros opcionais e combináveis
    // :inicio e :fim nunca são nulos (o Service usa datas limite quando não informados)
    @Query("""
            SELECT c FROM ChamadoModel c
            WHERE (:status IS NULL OR c.status = :status)
              AND (:prioridade IS NULL OR c.prioridade = :prioridade)
              AND (:setorId IS NULL OR c.categoria.setor.id = :setorId)
              AND (:categoriaId IS NULL OR c.categoria.id = :categoriaId)
              AND (:solicitanteId IS NULL OR c.solicitante.id = :solicitanteId)
              AND (:tecnicoId IS NULL OR c.tecnico.id = :tecnicoId)
              AND c.dataAbertura >= :inicio
              AND c.dataAbertura < :fim
            """)
    Page<ChamadoModel> filtrar(@Param("status") Status status,
                               @Param("prioridade") Prioridade prioridade,
                               @Param("setorId") Long setorId,
                               @Param("categoriaId") Long categoriaId,
                               @Param("solicitanteId") Long solicitanteId,
                               @Param("tecnicoId") Long tecnicoId,
                               @Param("inicio") LocalDateTime inicio,
                               @Param("fim") LocalDateTime fim,
                               Pageable pageable);

    // GET /usuarios/{id}/chamados: abertos pelo usuário OU atribuídos a ele
    @Query("""
            SELECT c FROM ChamadoModel c
            WHERE (c.solicitante.id = :usuarioId OR c.tecnico.id = :usuarioId)
              AND (:status IS NULL OR c.status = :status)
            """)
    Page<ChamadoModel> doUsuario(@Param("usuarioId") Long usuarioId,
                                 @Param("status") Status status,
                                 Pageable pageable);

}
