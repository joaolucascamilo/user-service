package com.usuario.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestClient restClient;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.remetente.email}")
    private String remetenteEmail;

    @Value("${brevo.remetente.nome:Infra Urbana}")
    private String remetenteNome;

    @Value("${app.url.base}")
    private String baseUrl;

    @Value("${app.url.frontend}")
    private String frontendUrl;

    public EmailService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(BREVO_API_URL).build();
    }

    public void enviarVerificacao(String destinatario, String token) {
        String link = baseUrl + "/api/auth/verificar-email?token=" + token;

        String texto =
                "Ola!\n\n" +
                "Clique no link abaixo para confirmar seu endereco de e-mail e ativar sua conta:\n\n" +
                link + "\n\n" +
                "O link expira em 24 horas.\n\n" +
                "Se voce nao criou uma conta, ignore este e-mail.";

        enviar(destinatario, "Confirme seu e-mail -- SOMAR", texto);
    }

    public void enviarRedefinicaoSenha(String destinatario, String token) {
        String link = frontendUrl + "/redefinir-senha?token=" + token;

        String texto =
                "Ola!\n\n" +
                "Recebemos uma solicitacao para redefinir a senha da sua conta.\n\n" +
                "Clique no link abaixo para criar uma nova senha:\n\n" +
                link + "\n\n" +
                "O link expira em 1 hora.\n\n" +
                "Se voce nao solicitou a redefinicao de senha, ignore este e-mail. Sua senha permanece a mesma.";

        enviar(destinatario, "Redefinicao de senha -- SOMAR", texto);
    }

    private void enviar(String destinatario, String assunto, String texto) {
        Map<String, Object> corpo = Map.of(
                "sender", Map.of("name", remetenteNome, "email", remetenteEmail),
                "to", java.util.List.of(Map.of("email", destinatario)),
                "subject", assunto,
                "textContent", texto
        );

        try {
            restClient.post()
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .body(corpo)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            logger.error("Falha ao enviar e-mail para {}", destinatario, e);
            throw e;
        }
    }
}
