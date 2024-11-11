package com.spin.ops;

import java.util.Date;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.mail.MailException;
import org.springframework.web.util.UriComponentsBuilder;

public class ServicoEmail {
    static final String senderMail = "portal@ases.com.br";
    static final String senderPassword = "Tbt@3231";

    public void enviaEmail(String email, String hash, char tpEmail) throws MailException, MessagingException {
        MailBuilder mailBuilder;
        JavaMailSender emailSender = mailSender();
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String baseUrl = "http://149.78.4.53:8080/spinops";

        StringBuilder SBUrl = new StringBuilder().append(baseUrl);

        switch (tpEmail) {
            case 'R':
            case 'C':
                SBUrl.append("/beneficiario/validar-hash");
                break;
            case 'E':
                SBUrl.append("/usuario/excluir-conta/validar-hash");
                break;
            default:
                throw new Error("Tipo de email não identificado");
        }

        String url = UriComponentsBuilder
                .fromUriString(SBUrl.toString())
                .queryParam("hash", hash.toUpperCase())
                .queryParam("tpEmail", tpEmail)
                .build().toUriString();

        switch (tpEmail) {
            case 'R':
                mailBuilder = enviaEmailTrocaSenha(url, hash);
                break;
            case 'E':
                mailBuilder = enviaEmailExclusaoConta(url, hash);
                break;
            case 'C':
                mailBuilder = enviaEmailConfirmaCadastro(url);
                break;
            default:
                throw new Error("Tipo de email não identificado");
        }

        helper.setFrom(senderMail);
        helper.setTo(email);
        helper.setSentDate(new Date());
        helper.setSubject(mailBuilder.assunto);
        helper.setText(mailBuilder.mensagem, true);

        System.out.println(url);

        try {
            emailSender.send(message);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("email-ssl.com.br");
        mailSender.setPort(587);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setUsername(senderMail);
        mailSender.setPassword(senderPassword);

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.connectiontimeout", true);

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    private MailBuilder enviaEmailTrocaSenha(String url, String hash) {
        Global.TokenRecuperarCadastrarModel item =  Global.TokenRecuperarCadastrarConta.getTokenPorHash(hash);

        if (item == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hash não encontrado");
        }

        String assunto = "ASES - Recuperação de senha";
        String mensagem = "<html><body>"
                .concat("Olá, tudo bem com você?")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Sua nova senha é: ")
                .concat("<b>" + item.newPassword + "</b>")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Clique no link para confirmar sua troca de senha: ")
                .concat("<br/>")
                .concat("<br/>")
                .concat("<a href='" + url + "'>Confirmar Troca de Senha</a>")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Atenciosamente,")
                .concat("<br/>")
                .concat("Plano ASES.")
                .concat("<br/>")
                .concat("Obs.: Essa mensagem é gerada automaticamente e não deverá ser respondida.")
                .concat("</body></html>");

        return new MailBuilder(assunto, mensagem);
    }

    private MailBuilder enviaEmailExclusaoConta(String url, String hash) {
        Global.TokenExcluirContaModel item =  Global.TokenExcluirConta.getTokenPorHash(hash);

        if (item == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hash não encontrado");
        }

        String assunto = "ASES - Exclusão de conta";
        String mensagem = "<html><body>"
                .concat("Olá, tudo bem com você?")
                .concat("<br/>")
                .concat("<br/>")
                .concat("<b>Essa é uma solicitação para excluir sua conta!</b>")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Clique no link para confirmar a exclusão da sua conta: ")
                .concat("<br/>")
                .concat("<br/>")
                .concat("<a href='" + url + "'>Confirmar Exclusão da Conta</a>")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Atenciosamente,")
                .concat("<br/>")
                .concat("Plano ASES.")
                .concat("<br/>")
                .concat("Obs.: Essa mensagem é gerada automaticamente e não deverá ser respondida.")
                .concat("</body></html>");

        return new MailBuilder(assunto, mensagem);
    }

    private MailBuilder enviaEmailConfirmaCadastro(String url) {
        String assunto = "ASES - Confirmação de cadastro";
        String mensagem = "<html><body>"
                .concat("Olá, tudo bem com você?")
                .concat("<br/>")
                .concat("Seja bem-vindo ao aplicativo do Plano ASES!")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Clique no link para confirmar seu cadastro: ")
                .concat("<br/>")
                .concat("<a href='" + url + "'>Validar Cadastro</a>")
                .concat("<br/>")
                .concat("<br/>")
                .concat("Atenciosamente,")
                .concat("<br/>")
                .concat("Plano ASES.")
                .concat("<br/>")
                .concat("Obs.: Essa mensagem é gerada automaticamente e não deverá ser respondida.")
                .concat("</body></html>");

        return new MailBuilder(assunto, mensagem);
    }

    private static class MailBuilder {
        public String assunto;
        public String mensagem;

        public MailBuilder(String assunto, String mensagem) {
            super();
            this.mensagem = mensagem;
            this.assunto = assunto;
        }
    }
}
