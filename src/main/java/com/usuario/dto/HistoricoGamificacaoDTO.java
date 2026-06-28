package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Registro de um evento de gamificação do usuário")
public class HistoricoGamificacaoDTO {

    @Schema(description = "Pontos adicionados (positivo) ou removidos (negativo) neste evento", example = "10")
    private Integer pontosAlterados;

    @Schema(description = "Descrição do evento que originou a alteração de pontos", example = "Ocorrência reportada e resolvida")
    private String descricaoEvento;

    @Schema(description = "Data e hora do evento no formato ISO 8601", example = "2024-06-15T14:30:00")
    private String dataEvento;

    public HistoricoGamificacaoDTO(Integer pontosAlterados, String descricaoEvento, String dataEvento) {
        this.pontosAlterados = pontosAlterados;
        this.descricaoEvento = descricaoEvento;
        this.dataEvento = dataEvento;
    }

    public Integer getPontosAlterados() {
        return pontosAlterados;
    }

    public void setPontosAlterados(Integer pontosAlterados) {
        this.pontosAlterados = pontosAlterados;
    }

    public String getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(String descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }
}
