package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import com.alessandra.api_sistema_de_chamados_ifrn.models.UsuarioModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;

public interface UsuarioRepository extends JpaRepository<UsuarioModel,Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsBySetorId(Long setorId);

    @Query("""
            SELECT u FROM UsuarioModel u
            WHERE LOWER(u.nome) LIKE :nome
              AND (:tipo IS NULL OR u.tipo = :tipo)
              AND (:setorId IS NULL OR u.setor.id = :setorId)
            """)
    Page<UsuarioModel> filtrar(@Param("nome") String nome,
                               @Param("tipo") TipoUsuario tipo,
                               @Param("setorId") Long setorId,
                               Pageable pageable);

}
