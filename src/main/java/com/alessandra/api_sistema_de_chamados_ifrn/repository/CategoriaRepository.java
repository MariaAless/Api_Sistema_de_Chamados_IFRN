package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import com.alessandra.api_sistema_de_chamados_ifrn.models.CategoriaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
    boolean existsBySetorId(Long setorId);

    @Query("""
            SELECT c FROM CategoriaModel c
            WHERE LOWER(c.nome) LIKE :nome
              AND (:setorId IS NULL OR c.setor.id = :setorId)
            """)
    Page<CategoriaModel> filtrar(@Param("nome") String nome,
                                 @Param("setorId") Long setorId,
                                 Pageable pageable);


}
