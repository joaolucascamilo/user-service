package com.usuario.config;

import com.usuario.entity.Usuario;
import com.usuario.repository.UsuarioRepository;
import com.usuario.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component // Gerido pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Tenta recuperar o token do cabeçalho da requisição
        String token = recuperarToken(request);

        // 2. Se o token existir, vamos validá-lo e autenticar o usuário
        if (token != null) {
            // Extrai o email do token validado
            String email = jwtService.extrairEmail(token);

            Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Cria a permissão (ROLE) que o Spring Security entende
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(usuario.getPerfil().name()));

            // Cria o "Crachá" de autenticação
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);

            // Diz ao Spring Security: "Este usuário está autenticado para esta requisição!"
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. Continua o fluxo normal da requisição (passa para o próximo filtro ou controller)
        filterChain.doFilter(request, response);
    }

    // Método para extrair o token do cabeçalho HTTP
    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        // Retorna apenas a string do token, removendo a palavra "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}
