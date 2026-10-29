package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository extends JpaRepository<ChamadoRepository, Long> {

    // regra 8 (categoria): "categoria.id" -> existsByCategoriaId
    boolean existsByCategoriaId(Long categoriaId);

    // regra 8 (setor): navega chamado.categoria.setor.id -> existsByCategoriaSetorId
    boolean existsByCategoriaSetorId(Long setorId);

}
