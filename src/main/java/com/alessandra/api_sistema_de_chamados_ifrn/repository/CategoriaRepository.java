package com.alessandra.api_sistema_de_chamados_ifrn.repository;

import com.alessandra.api_sistema_de_chamados_ifrn.models.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
    // e-mail único (criação)
    boolean existsByEmailIgnoreCase(String email);

    // e-mail único na atualização, ignorando o próprio usuário
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // usado ao excluir setor: existem técnicos nele?
    boolean existsBySetorId(Long setorId);
}
