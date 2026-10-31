package com.alessandra.api_sistema_de_chamados_ifrn.models;


import com.alessandra.api_sistema_de_chamados_ifrn.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TB_SETOR")
public class SetorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String sigla;

    @OneToMany(mappedBy = "setor")
    private List<CategoriaModel> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "setor")
    private List<UsuarioModel> tecnicos = new ArrayList<>();
}
