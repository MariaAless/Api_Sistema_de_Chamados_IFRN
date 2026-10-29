package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import com.alessandra.api_sistema_de_chamados_ifrn.models.SetorModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<SetorModel, Long> {

    // sigla única, ignorando maiúsculas/minúsculas (criação)
    boolean existsBySiglaIgnoreCase(String sigla);

    // mesma checagem na atualização, ignorando o próprio setor
    boolean existsBySiglaIgnoreCaseAndIdNot(String sigla, Long id);
}
