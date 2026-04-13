package com.usuario.controller;

import com.usuario.dto.TokenResponseDTO;
import com.usuario.dto.UsuarioCadastroDTO;
import com.usuario.dto.UsuarioLoginDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioCadastroDTO dto) {
        UsuarioResponseDTO response = usuarioService.registrarCidadao(dto);
        // Retorna o status 201 (Created) e os dados do usuário sem a senha
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UsuarioLoginDTO dto) {
        // Chama o serviço de autenticação
        TokenResponseDTO response = usuarioService.autenticar(dto);

        // Retorna o status 200 (OK) com o token no corpo da resposta
        return ResponseEntity.ok(response);
    }

}
