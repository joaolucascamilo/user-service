package com.usuario.controller;

import com.usuario.dto.PerfilUsuarioDTO;
import com.usuario.dto.PontuacaoRequestDTO;
import com.usuario.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/meu-perfil")
    public ResponseEntity<PerfilUsuarioDTO> obterMeuPerfil() {
        PerfilUsuarioDTO perfil = usuarioService.obterMeuPerfil();
        return ResponseEntity.ok(perfil);
    }

    @PostMapping("/pontuar")
    public ResponseEntity<String> pontuarUsuario(@RequestBody PontuacaoRequestDTO dto) {
        usuarioService.adicionarPontos(dto);
        return ResponseEntity.ok("Pontuação registrada com sucesso!");
    }
}
