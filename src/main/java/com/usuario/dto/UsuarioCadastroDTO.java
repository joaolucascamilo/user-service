package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de um novo usuário")
public class UsuarioCadastroDTO {

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail único utilizado para login", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Senha de acesso (será armazenada com BCrypt)", example = "senhaSegura123")
    private String senha;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
