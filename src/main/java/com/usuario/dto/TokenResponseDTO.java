package com.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo o token JWT")
public class TokenResponseDTO {

    @Schema(description = "Token JWT assinado com HS256, válido por 24 horas",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvQGVtYWlsLmNvbSIsInBlcmZpbCI6IlJPTEVfQ0lEQURBTyIsImlkIjoxfQ.abc123")
    private String token;

    public TokenResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
