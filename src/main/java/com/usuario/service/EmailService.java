package com.usuario.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.url.base}")
    private String baseUrl;

    @Value("${app.url.frontend}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarVerificacao(String destinatario, String token) {
        String link = baseUrl + "/api/auth/verificar-email?token=" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Confirme seu e-mail -- Infra Urbana");
        mensagem.setText(
                "Ola!\n\n" +
                "Clique no link abaixo para confirmar seu endereco de e-mail e ativar sua conta:\n\n" +
                link + "\n\n" +
                "O link expira em 24 horas.\n\n" +
                "Se voce nao criou uma conta, ignore este e-mail."
        );

        try {
            mailSender.send(mensagem);
        } catch (MailException e) {
            logger.error("Falha ao enviar e-mail de verificacao para {}", destinatario, e);
            throw e;
        }
    }

    public void enviarRedefinicaoSenha(String destinatario, String token) {
        String link = frontendUrl + "/redefinir-senha?token=" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Redefinicao de senha -- Infra Urbana");
        mensagem.setText(
                "Ola!\n\n" +
                "Recebemos uma solicitacao para redefinir a senha da sua conta.\n\n" +
                "Clique no link abaixo para criar uma nova senha:\n\n" +
                link + "\n\n" +
                "O link expira em 1 hora.\n\n" +
                "Se voce nao solicitou a redefinicao de senha, ignore este e-mail. Sua senha permanece a mesma."
        );

        try {
            mailSender.send(mensagem);
        } catch (MailException e) {
            logger.error("Falha ao enviar e-mail de redefinicao de senha para {}", destinatario, e);
            throw e;
        }
    }
}
