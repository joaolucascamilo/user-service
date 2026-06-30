package com.usuario.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.url.base}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarVerificacao(String destinatario, String token) {
        String link = baseUrl + "/api/auth/verificar-email?token=" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Confirme seu e-mail — Infra Urbana");
        mensagem.setText(
                "Olá!\n\n" +
                "Clique no link abaixo para confirmar seu endereço de e-mail e ativar sua conta:\n\n" +
                link + "\n\n" +
                "O link expira em 24 horas.\n\n" +
                "Se você não criou uma conta, ignore este e-mail."
        );

        mailSender.send(mensagem);
    }
}
