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

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HistoricoGamificacaoRepository historicoRepository;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, HistoricoGamificacaoRepository historicoRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.historicoRepository = historicoRepository;
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

    @Transactional(readOnly = true)
    public PerfilUsuarioDTO obterMeuPerfil() {
        // 1. Puxa o usuário que foi autenticado pelo SecurityFilter
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Busca no banco de dados para garantir que pegamos os pontos e o histórico mais recentes
        Usuario usuario = repository.findById(usuarioAutenticado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 3. Converte a lista de entidades de histórico para DTOs
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<HistoricoGamificacaoDTO> historicoDTO = usuario.getHistoricoGamificacao().stream()
                .map(h -> new HistoricoGamificacaoDTO(
                        h.getPontosAlterados(),
                        h.getDescricaoEvento(),
                        h.getDataEvento() != null ? h.getDataEvento().format(fmt) : null
                )).toList();

        // 4. Monta e devolve o perfil completo
        return new PerfilUsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name(),
                usuario.getPontosReputacao(),
                historicoDTO
        );
    }

    @Transactional // Garante que as duas operações de banco (update e insert) ocorram juntas
    public void adicionarPontos(PontuacaoRequestDTO dto) {
        // 1. Busca o usuário
        Usuario usuario = repository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Atualiza o saldo total de reputação
        usuario.setPontosReputacao(usuario.getPontosReputacao() + dto.getPontos());
        repository.save(usuario);

        // 3. Cria e salva o registro do histórico
        HistoricoGamificacao historico = new HistoricoGamificacao();
        historico.setUsuario(usuario);
        historico.setPontosAlterados(dto.getPontos());
        historico.setDescricaoEvento(dto.getDescricao());

        historicoRepository.save(historico);
    }

    public UsuarioResponseDTO registrarAgente(UsuarioCadastroDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        Usuario novoAgente = new Usuario();
        novoAgente.setNome(dto.getNome());
        novoAgente.setEmail(dto.getEmail());
        novoAgente.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoAgente.setPerfil(Perfil.ROLE_AGENTE_PREFEITURA); // Define como Agente
        novoAgente.setPontosReputacao(0);

        Usuario salvo = repository.save(novoAgente);

        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getPerfil().name());
    }
}
