package com.usuario.dto;

import java.util.List;

public class PerfilUsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String perfil;
    private Integer pontosReputacao;
    private List<HistoricoGamificacaoDTO> historico;

    public PerfilUsuarioDTO(Long id, String nome, String email, String perfil, Integer pontosReputacao, List<HistoricoGamificacaoDTO> historicoDTO) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.pontosReputacao = pontosReputacao;
        this.historico = historicoDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Integer getPontosReputacao() {
        return pontosReputacao;
    }

    public void setPontosReputacao(Integer pontosReputacao) {
        this.pontosReputacao = pontosReputacao;
    }

    public List<HistoricoGamificacaoDTO> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoGamificacaoDTO> historico) {
        this.historico = historico;
    }
}
