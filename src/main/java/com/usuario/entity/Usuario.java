package com.usuario.entity;

import com.usuario.domain.Perfil;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    @Column(name = "pontos_reputacao", nullable = false)
    private Integer pontosReputacao = 0;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    // Relacionamento 1:N com o histórico
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HistoricoGamificacao> historicoGamificacao;
}