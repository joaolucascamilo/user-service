package com.usuario.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historico_gamificacao")
public class HistoricoGamificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "pontos_alterados", nullable = false)
    private Integer pontosAlterados;

    @Column(name = "descricao_evento", nullable = false)
    private String descricaoEvento;

    @Column(name = "data_evento")
    private LocalDateTime dataEvento = LocalDateTime.now();
}
