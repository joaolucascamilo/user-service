package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Perfil completo do usuário autenticado com dados de gamificação")
public class PerfilUsuarioDTO {

    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Perfil de acesso", example = "ROLE_CIDADAO",
            allowableValues = {"ROLE_CIDADAO", "ROLE_AGENTE_PREFEITURA"})
    private String perfil;

    @Schema(description = "Total de pontos de reputação acumulados", example = "150")
    private Integer pontosReputacao;

    @Schema(description = "Histórico de eventos de gamificação em ordem cronológica decrescente")
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
