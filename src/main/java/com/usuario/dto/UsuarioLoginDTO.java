package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais para autenticação")
public class UsuarioLoginDTO {

    @Schema(description = "E-mail cadastrado", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Senha de acesso", example = "senhaSegura123")
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
