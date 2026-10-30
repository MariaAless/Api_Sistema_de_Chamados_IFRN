package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

public interface SetorRepository extends JpaRepository<SetorModel, Long> {

    // sigla única, ignorando maiúsculas/minúsculas (criação)
    boolean existsBySiglaIgnoreCase(String sigla);

    // mesma checagem na atualização, ignorando o próprio setor
    boolean existsBySiglaIgnoreCaseAndIdNot(String sigla, Long id);


    @Query("""
            SELECT s FROM SetorModel s
            WHERE LOWER(s.nome) LIKE :nome
            """)
    Page<SetorModel> filtrar(@Param("nome") String nome, Pageable pageable);
}
