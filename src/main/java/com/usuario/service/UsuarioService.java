package com.usuario.service;

import com.usuario.domain.Perfil;
import com.usuario.dto.*;
import com.usuario.entity.HistoricoGamificacao;
import com.usuario.entity.Usuario;
import com.usuario.repository.HistoricoGamificacaoRepository;
import com.usuario.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HistoricoGamificacaoRepository historicoRepository;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
                          HistoricoGamificacaoRepository historicoRepository, EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.historicoRepository = historicoRepository;
        this.emailService = emailService;
    }

    public UsuarioResponseDTO registrarCidadao(UsuarioCadastroDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado no sistema.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setPerfil(Perfil.ROLE_CIDADAO);
        novoUsuario.setPontosReputacao(0);
        novoUsuario.setEmailVerificado(false);
        novoUsuario.setTokenVerificacao(UUID.randomUUID().toString());
        novoUsuario.setTokenExpiracao(LocalDateTime.now().plusHours(24));

        Usuario usuarioSalvo = repository.save(novoUsuario);

        emailService.enviarVerificacao(usuarioSalvo.getEmail(), usuarioSalvo.getTokenVerificacao());

        return new UsuarioResponseDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getPerfil().name()
        );
    }

    @Transactional
    public void verificarEmail(String token) {
        Usuario usuario = repository.findByTokenVerificacao(token)
                .orElseThrow(() -> new RuntimeException("Token de verificação inválido."));

        if (usuario.getTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token de verificação expirado. Solicite um novo cadastro.");
        }

        usuario.setEmailVerificado(true);
        usuario.setTokenVerificacao(null);
        usuario.setTokenExpiracao(null);
        repository.save(usuario);
    }

    public TokenResponseDTO autenticar(UsuarioLoginDTO dto) {
        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas."));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas.");
        }

        if (!usuario.getEmailVerificado()) {
            throw new RuntimeException("E-mail não verificado. Verifique sua caixa de entrada e clique no link de confirmação.");
        }

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.getPerfil().name(), usuario.getId());
        return new TokenResponseDTO(token);
    }

    @Transactional(readOnly = true)
    public PerfilUsuarioDTO obterMeuPerfil() {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuario = repository.findById(usuarioAutenticado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<HistoricoGamificacaoDTO> historicoDTO = usuario.getHistoricoGamificacao().stream()
                .map(h -> new HistoricoGamificacaoDTO(
                        h.getPontosAlterados(),
                        h.getDescricaoEvento(),
                        h.getDataEvento() != null ? h.getDataEvento().format(fmt) : null
                )).toList();

        return new PerfilUsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name(),
                usuario.getPontosReputacao(),
                historicoDTO
        );
    }

    @Transactional
    public void adicionarPontos(PontuacaoRequestDTO dto) {
        Usuario usuario = repository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuario.setPontosReputacao(usuario.getPontosReputacao() + dto.getPontos());
        repository.save(usuario);

        HistoricoGamificacao historico = new HistoricoGamificacao();
        historico.setUsuario(usuario);
        historico.setPontosAlterados(dto.getPontos());
        historico.setDescricaoEvento(dto.getDescricao());

        historicoRepository.save(historico);
    }

    @Transactional
    public void solicitarRedefinicaoSenha(String email) {
        // Sempre retorna sucesso para nao revelar quais emails estao cadastrados
        repository.findByEmail(email).ifPresent(usuario -> {
            usuario.setTokenRedefinicaoSenha(UUID.randomUUID().toString());
            usuario.setTokenRedefinicaoExpiracao(LocalDateTime.now().plusHours(1));
            repository.save(usuario);
            emailService.enviarRedefinicaoSenha(usuario.getEmail(), usuario.getTokenRedefinicaoSenha());
        });
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaDTO dto) {
        Usuario usuario = repository.findByTokenRedefinicaoSenha(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Token de redefinicao invalido."));

        if (usuario.getTokenRedefinicaoExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado. Solicite uma nova redefinicao de senha.");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.setTokenRedefinicaoSenha(null);
        usuario.setTokenRedefinicaoExpiracao(null);
        repository.save(usuario);
    }

    public UsuarioResponseDTO registrarAgente(UsuarioCadastroDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        Usuario novoAgente = new Usuario();
        novoAgente.setNome(dto.getNome());
        novoAgente.setEmail(dto.getEmail());
        novoAgente.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoAgente.setPerfil(Perfil.ROLE_AGENTE_PREFEITURA);
        novoAgente.setPontosReputacao(0);
        novoAgente.setEmailVerificado(false);
        novoAgente.setTokenVerificacao(UUID.randomUUID().toString());
        novoAgente.setTokenExpiracao(LocalDateTime.now().plusHours(24));

        Usuario salvo = repository.save(novoAgente);

        emailService.enviarVerificacao(salvo.getEmail(), salvo.getTokenVerificacao());

        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getPerfil().name());
    }
}
