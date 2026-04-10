package com.usuario.service;

import com.usuario.domain.Perfil;
import com.usuario.dto.TokenResponseDTO;
import com.usuario.dto.UsuarioCadastroDTO;
import com.usuario.dto.UsuarioLoginDTO;
import com.usuario.dto.UsuarioResponseDTO;
import com.usuario.entity.Usuario;
import com.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioResponseDTO registrarCidadao(UsuarioCadastroDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado no sistema.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());

        // CRIPTOGRAFIA DA SENHA: O BCrypt gera o hash seguro aqui
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        novoUsuario.setPerfil(Perfil.ROLE_CIDADAO);
        novoUsuario.setPontosReputacao(0); // Começa com 0 pontos na gamificação

        Usuario usuarioSalvo = repository.save(novoUsuario);

        return new UsuarioResponseDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getPerfil().name()
        );
    }

    public TokenResponseDTO autenticar(UsuarioLoginDTO dto) {

        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas.")); // E-mail não encontrado

        // Verificação se a senha enviada bate com a senha criptografada no banco
        // O método matches() do BCrypt faz essa função com segurança
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas."); // Senha errada
        }

        // Gera o Token JWT.
        String token = jwtService.gerarToken(usuario.getEmail(), usuario.getPerfil().name(), usuario.getId());

        // 4. Devolve o token
        return new TokenResponseDTO(token);
    }
}
