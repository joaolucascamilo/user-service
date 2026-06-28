package com.usuario.controller;

import com.usuario.dto.PerfilUsuarioDTO;
import com.usuario.dto.PontuacaoRequestDTO;
import com.usuario.dto.UsuarioCadastroDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de perfil e gamificação — requer autenticação JWT")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/meu-perfil")
    @Operation(
        summary = "Obter perfil do usuário autenticado",
        description = "Retorna os dados completos do usuário identificado pelo token JWT, " +
                "incluindo pontos de reputação e histórico de gamificação."
    )
    @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso",
        content = @Content(schema = @Schema(implementation = PerfilUsuarioDTO.class)))
    @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    public ResponseEntity<PerfilUsuarioDTO> obterMeuPerfil() {
        PerfilUsuarioDTO perfil = usuarioService.obterMeuPerfil();
        return ResponseEntity.ok(perfil);
    }

    @PostMapping("/pontuar")
    @Operation(
        summary = "Adicionar pontos de reputação",
        description = "Registra pontos de reputação para um usuário e cria um evento no histórico de gamificação. " +
                "Chamado por outros microserviços (ex: serviço de ocorrências) após ações do cidadão."
    )
    @ApiResponse(responseCode = "200", description = "Pontuação registrada com sucesso")
    @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<String> pontuarUsuario(@RequestBody PontuacaoRequestDTO dto) {
        usuarioService.adicionarPontos(dto);
        return ResponseEntity.ok("Pontuação registrada com sucesso!");
    }

    @PostMapping("/registrar-agente")
    @Operation(
        summary = "Registrar agente da prefeitura",
        description = "Cria uma conta com perfil ROLE_AGENTE_PREFEITURA. " +
                "Restrito a usuários com perfil ROLE_AGENTE_PREFEITURA (administradores)."
    )
    @ApiResponse(responseCode = "201", description = "Agente registrado com sucesso",
        content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    @ApiResponse(responseCode = "403", description = "Perfil sem permissão — requer ROLE_AGENTE_PREFEITURA")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
    public ResponseEntity<UsuarioResponseDTO> registrarAgente(@RequestBody UsuarioCadastroDTO dto) {
        UsuarioResponseDTO response = usuarioService.registrarAgente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
