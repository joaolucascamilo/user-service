package com.usuario.entity;

import com.usuario.domain.Perfil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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

    @Column(name = "email_verificado", nullable = false, columnDefinition = "boolean not null default false")
    private Boolean emailVerificado = false;

    @Column(name = "token_verificacao")
    private String tokenVerificacao;

    @Column(name = "token_expiracao")
    private LocalDateTime tokenExpiracao;

    @Column(name = "token_redefinicao_senha")
    private String tokenRedefinicaoSenha;

    @Column(name = "token_redefinicao_expiracao")
    private LocalDateTime tokenRedefinicaoExpiracao;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HistoricoGamificacao> historicoGamificacao;
}