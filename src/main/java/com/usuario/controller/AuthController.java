package com.usuario.controller;

import com.usuario.dto.TokenResponseDTO;
import com.usuario.dto.UsuarioCadastroDTO;
import com.usuario.dto.UsuarioLoginDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints públicos de registro e login de usuários")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    @SecurityRequirements
    @Operation(
        summary = "Registrar cidadão",
        description = "Cria uma nova conta de cidadão (ROLE_CIDADAO). Endpoint público, não requer autenticação."
    )
    @ApiResponse(responseCode = "201", description = "Cidadão registrado com sucesso",
        content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioCadastroDTO dto) {
        UsuarioResponseDTO response = usuarioService.registrarCidadao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(
        summary = "Login",
        description = "Autentica o usuário com e-mail e senha. Retorna um token JWT válido por 24 horas " +
                "que deve ser enviado no header Authorization: Bearer {token} nas demais requisições."
    )
    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado",
        content = @Content(schema = @Schema(implementation = TokenResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UsuarioLoginDTO dto) {
        TokenResponseDTO response = usuarioService.autenticar(dto);
        return ResponseEntity.ok(response);
    }
}
