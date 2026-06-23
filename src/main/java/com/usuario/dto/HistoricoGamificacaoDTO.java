package com.usuario.dto;

public class HistoricoGamificacaoDTO {
    private Integer pontosAlterados;
    private String descricaoEvento;
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
