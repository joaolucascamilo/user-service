package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos do usuário (sem senha)")
public class UsuarioResponseDTO {

    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Perfil de acesso: ROLE_CIDADAO ou ROLE_AGENTE_PREFEITURA",
            example = "ROLE_CIDADAO", allowableValues = {"ROLE_CIDADAO", "ROLE_AGENTE_PREFEITURA"})
    private String perfil;

    public UsuarioResponseDTO(Long id, String nome, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
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
}
