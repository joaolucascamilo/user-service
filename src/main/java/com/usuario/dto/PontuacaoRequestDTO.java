package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para adicionar pontos de reputação a um usuário")
public class PontuacaoRequestDTO {

    @Schema(description = "ID do usuário que receberá os pontos", example = "1")
    private Long usuarioId;

    @Schema(description = "Quantidade de pontos a adicionar (positivo) ou subtrair (negativo)", example = "10")
    private Integer pontos;

    @Schema(description = "Descrição do evento que gerou a pontuação", example = "Ocorrência reportada e resolvida")
    private String descricao;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
