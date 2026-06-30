package com.usuario.controller;

import com.usuario.dto.EsqueciSenhaDTO;
import com.usuario.dto.RedefinirSenhaDTO;
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
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou e-mail não verificado")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UsuarioLoginDTO dto) {
        TokenResponseDTO response = usuarioService.autenticar(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verificar-email")
    @SecurityRequirements
    @Operation(
        summary = "Verificar e-mail",
        description = "Confirma o endereço de e-mail do usuário através do token enviado por e-mail após o cadastro."
    )
    @ApiResponse(responseCode = "200", description = "E-mail verificado com sucesso")
    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {
        usuarioService.verificarEmail(token);
        return ResponseEntity.ok("E-mail verificado com sucesso! Agora você pode fazer login.");
    }

    @PostMapping("/reenviar-verificacao")
    @SecurityRequirements
    @Operation(
        summary = "Reenviar e-mail de verificação",
        description = "Gera um novo token e reenvia o e-mail de confirmação de cadastro. Retorna 200 mesmo se o e-mail não estiver cadastrado ou já estiver verificado, para não expor quais e-mails existem no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Se o e-mail estiver cadastrado e pendente de verificação, um novo link foi enviado")
    public ResponseEntity<String> reenviarVerificacao(@RequestBody EsqueciSenhaDTO dto) {
        usuarioService.reenviarVerificacao(dto.getEmail());
        return ResponseEntity.ok("Se este e-mail estiver cadastrado e pendente de verificação, você receberá um novo link em breve.");
    }

    @PostMapping("/esqueci-senha")
    @SecurityRequirements
    @Operation(
        summary = "Solicitar redefinição de senha",
        description = "Envia um e-mail com link para redefinição de senha. Retorna 200 mesmo se o e-mail não estiver cadastrado, para não expor quais e-mails existem no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Se o e-mail estiver cadastrado, um link de redefinição foi enviado")
    public ResponseEntity<String> esqueciSenha(@RequestBody EsqueciSenhaDTO dto) {
        usuarioService.solicitarRedefinicaoSenha(dto.getEmail());
        return ResponseEntity.ok("Se este e-mail estiver cadastrado, você receberá as instruções em breve.");
    }

    @PostMapping("/redefinir-senha")
    @SecurityRequirements
    @Operation(
        summary = "Redefinir senha",
        description = "Redefine a senha do usuário usando o token recebido por e-mail. O token expira em 1 hora."
    )
    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso")
    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaDTO dto) {
        usuarioService.redefinirSenha(dto);
        return ResponseEntity.ok("Senha redefinida com sucesso! Agora você pode fazer login com a nova senha.");
    }
}
