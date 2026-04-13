package com.usuario.dto;

import java.time.LocalDateTime;

public class HistoricoGamificacaoDTO {
    private Integer pontosAlterados;
    private String descricaoEvento;
    private LocalDateTime dataEvento;

    public HistoricoGamificacaoDTO(Integer pontosAlterados, String descricaoEvento, LocalDateTime dataEvento) {
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

    public LocalDateTime getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDateTime dataEvento) {
        this.dataEvento = dataEvento;
    }
}
