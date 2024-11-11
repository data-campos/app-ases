package com.spin.ops.controller;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.hash.Hashing;
import com.spin.ops.Utils;
import com.spin.ops.Global;
import com.spin.ops.Global.TokenRecuperarCadastrarModel;
import com.spin.ops.ServicoEmail;
import com.spin.ops.model.InterfacesJPA.Carteirinha;
import com.spin.ops.model.InterfacesJPA.RetornoLogin;
import com.spin.ops.model.PlsSeguradoWeb;
import com.spin.ops.repository.PlsSeguradoWebRepository;
import com.spin.ops.repository.UsuarioRepository;

@CrossOrigin
@Controller
@RequestMapping
public class AlteracaoNovoBeneficiarioController {

    static final String dsEmail = "portal@ases.com.br";
    static final String dsSenha = "Tbt@3231";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlsSeguradoWebRepository webRepository;

    @PersistenceContext
    private EntityManager manager;

    // Método para solicitar a troca de senha via e-mail
    @Transactional
    @ResponseBody
    @PutMapping("/beneficiario/solicitar-alteracao-senha")
    public RetornoMensagem solicitarAlteracaoSenha(@RequestBody UsuarioRecSenha usuarioRecSenha) throws Exception {

        RetornoLogin login;
        try {
            login = usuarioRepository.loginPorUsuario(usuarioRecSenha.getNrCarteirinha(), "A");

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Global.trataMensagemExceptionBadRequest(e));
        }

        if (login == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiário não cadastrado.");
        } else if (!(sanitizeString(usuarioRecSenha.getNrCpf()).equals(sanitizeString(login.getNr_cpf())))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CPF informado não está de acordo com o CPF do beneficiário.");
        } else if (login.getDs_email() == null || login.getDs_email().trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não existe um e-mail vinculado na conta deste beneficiário. Favor entrar em contato com a administração do plano ASES");
        }

        try {
            String hashRecSenha = GerarHashString();

            String emailEnviar = login.getDs_email();

            String newPassword = Utils.humanReadablePassword(8, 2);

            Global.TokenRecuperarCadastrarConta.gerarNovaSolicitacaoCadastroRecSenha(
                    new TokenRecuperarCadastrarModel(
                            hashRecSenha,
                            emailEnviar,
                            usuarioRecSenha.getNrCarteirinha(),
                            'R',
                            new Date(),
                            false,
                            newPassword
                    )
            );

            ServicoEmail servEmail = new ServicoEmail();
            servEmail.enviaEmail(emailEnviar, hashRecSenha, 'R');

            return new RetornoMensagem("Enviamos um e-mail com a nova senha e um link de confirmação.", emailEnviar);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Global.trataMensagemExceptionBadRequest(e));
        }
    }

    // Método para trocar de senha quando já está logado no sistema
    @Transactional
    @ResponseBody
    @PutMapping("/beneficiario/alterar-senha")
    public ResponseEntity<String> alterarSenha(@RequestBody FormAlterarSenha form) throws Exception {
        if (!(form.getDsSenhaNova().equals(form.getDsSenhaConfirmacao()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A confirmação da senha não está de acordo com a senha informada.");
        }

        RetornoLogin login;
        try {
            login = usuarioRepository.loginPorUsuario(form.getDsLogin(), "A");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    Global.trataMensagemExceptionBadRequest(e)
            );
        }

        if (login == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi encontrado um beneficiário com este número de carteirinha.");
        }

        boolean existeDsTec = login.getDs_tec() != null;

        String ds_tec = existeDsTec ? login.getDs_tec() : Global.generateDsTec(login.getDt_nascimento().toString(), login.getNr_cpf());

        String dsSenhaAtualCriptografada = (Hashing.sha256().hashString(
                form.getDsSenhaAtual().toUpperCase() + ds_tec,
                StandardCharsets.UTF_8
        )).toString().toUpperCase();


        if (!dsSenhaAtualCriptografada.equals(login.getDs_senha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha atual informada não confere com a senha cadastrada.");
        }

        String dsSenhaNovaCriptografada = (Hashing.sha256().hashString(
                form.getDsSenhaNova().toUpperCase() + ds_tec,
                StandardCharsets.UTF_8
        )).toString().toUpperCase();

        StringBuilder sb = new StringBuilder();
        sb.append("update tasy.pls_segurado_web ");
        sb.append("set ds_senha = :ds_senha");
        if (!existeDsTec) {
            sb.append(", ds_tec = :ds_tec");
        }
        sb.append(" where nr_seq_segurado = :nr_seq_segurado and ie_situacao = 'A' ");

        Query query = manager.createNativeQuery(sb.toString());

        query.setParameter("ds_senha", dsSenhaNovaCriptografada);
        query.setParameter("nr_seq_segurado", login.getId_segurado());
        if (!existeDsTec) {
            query.setParameter("ds_tec", ds_tec);
        }

        query.executeUpdate();
        return ResponseEntity.ok("{ \"message\": \"Senha alterada com sucesso!\" }");
    }

    // Método para criar usuário
    @Transactional
    @ResponseBody
    @PostMapping("/beneficiario/novo")
    public RetornoMensagem novo(@RequestBody Usuario usuario) throws Exception {
        if (!(usuario.getDsSenha().equals(usuario.getDsSenhaConfirmacao()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A confirmação da senha não está de acordo com a senha informada.");
        }

        if (!(Utils.trimLowerCase(usuario.getDsEmail()).equals(Utils.trimLowerCase(usuario.getDsEmailConfirmacao())))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A confirmação do e-mail não está de acordo com o e-mail informado.");
        }

        Carteirinha carteirinha = usuarioRepository.beneficiarioPorCarteirinha(usuario.getNrCarteirinha());
        String hashSolicitacao = GerarHashString();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        if (carteirinha == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiário não encontrado pelo número da carteirinha.");
        }
        if (!(usuario.getNrCpf().equals(carteirinha.getNr_cpf()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Cpf informado não está de acordo com o Cpf do beneficiário.");
        }
        if (!(f.format(usuario.getDtNascimento()).equals(f.format(carteirinha.getDt_nascimento())))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de nascimento informada não está de acordo com a data do beneficiário.");
        }

        // TODO: ATIVAR E INATIVAR CONTA

        // Verifica se já foi enviado o e-mail de ativação
        if (!carteirinha.getQt_web().equals(0L)) {
            if (carteirinha.getieSituacao().trim().equalsIgnoreCase("A")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe acesso configurado para este beneficiário.");
            }

            String emailEnviar = carteirinha.getDs_email().trim().toLowerCase();
            TokenRecuperarCadastrarModel solicitacaoCadRecSenha = Global.TokenRecuperarCadastrarConta.getSolicitacaoPorNrCarteirinha(usuario.getNrCarteirinha(), 'C');

            if (solicitacaoCadRecSenha == null) {
                Global.TokenRecuperarCadastrarConta.gerarNovaSolicitacaoCadastroRecSenha(
                        new TokenRecuperarCadastrarModel(
                                hashSolicitacao,
                                emailEnviar,
                                usuario.getNrCarteirinha(),
                                'C',
                                new Date(),
                                false,
                                ""
                        )
                );

                ServicoEmail servEmail = new ServicoEmail();
                servEmail.enviaEmail(emailEnviar, hashSolicitacao, 'C');
            } else {
                emailEnviar = solicitacaoCadRecSenha.getEmail(); // pega o e-mail para mostrar para o usuário
            }

            return new RetornoMensagem("Foi identificada uma conta aguardando ativação vinculada nesta carteirinha. Favor verificar o e-mail da conta para obter o link de confirmação necessário para ativa-la", emailEnviar);
        }

        try {
            String ds_tec = Global.generateDsTec(usuario.getDtNascimento().toString(), usuario.getNrCpf());

            String sha256hex = (Hashing.sha256().hashString(usuario.getDsSenha().toUpperCase() + ds_tec, StandardCharsets.UTF_8)).toString().toUpperCase();

            PlsSeguradoWeb web = new PlsSeguradoWeb();
            web.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.pls_segurado_web_seq.nextval from dual").getSingleResult());
            web.setCd_estabelecimento(carteirinha.getCd_estabelecimento());
            web.setDs_hash(sha256hex.substring(0, 31));
            web.setDs_senha(sha256hex);
            web.setDs_Tec(ds_tec);
            web.setDt_atualizacao(new Date());
            web.setDt_atualizacao_nrec(new Date());
            web.setIe_origem_login("W");
            web.setIe_situacao("P"); //Setar para ativo posteriormente quando for ativado via e-mail
            web.setNr_seq_segurado(carteirinha.getId_segurado());

            webRepository.save(web);

            String emailEnviar = usuario.getDsEmail().trim().toLowerCase();

            Global.TokenRecuperarCadastrarConta.gerarNovaSolicitacaoCadastroRecSenha(
                    new TokenRecuperarCadastrarModel(
                            hashSolicitacao,
                            emailEnviar,
                            usuario.getNrCarteirinha(),
                            'C',
                            new Date(),
                            false,
                            ""
                    )
            );

            Utils.atualizaComplPessoaFisicaResidencial(manager, "ds_Email", usuario.getDsEmail(), carteirinha.getCd_pessoa_fisica());
            ServicoEmail servEmail = new ServicoEmail();
            servEmail.enviaEmail(usuario.getDsEmail(), hashSolicitacao, 'C');

            return new RetornoMensagem("Foi enviado um e-mail com informações e um link para ativar seu cadastro!", emailEnviar);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Global.trataMensagemExceptionBadRequest(e));
        }
    }

    // Método para validar Hash (RETORNA HTML)
    @Transactional
    @GetMapping("/beneficiario/validar-hash")
    public String validarHash(Model model, @RequestParam String hash, @RequestParam char tpEmail) throws Exception {
        String errorPage = "custom-render-error.html";
        String successPage = "custom-render-success.html";

        String nrCarteirinhaSolicitante = "";

        nrCarteirinhaSolicitante = Global.TokenRecuperarCadastrarConta.getNrCarteirinhaPorTokenSolicitacao(hash, tpEmail);

        switch (nrCarteirinhaSolicitante) {
            case "Vencido":
                model.addAttribute("message", "O código digitado corresponde à uma solicitação para cadastro que expirou o prazo de 24 horas.");
                break;
            case "NEncontrado":
                model.addAttribute("message", "O código digitado não foi identificado no sistema. Digite outro código ou refaça a solicitação");
                break;
            case "Utilizado":
                model.addAttribute("message", "O código digitado já foi utilizado. Favor refaça a solicitação para ter acesso a um novo código");
                break;
            case "":
                model.addAttribute("message", "Ocorreu um erro desconhecido ao processar a troca de senha. Favor refazer a solicitação");
                break;
        }

        if (model.containsAttribute("message")) {
            return errorPage;
        }

        try {
            if (tpEmail == 'C') {
                RetornoLogin login = usuarioRepository.loginPorUsuario(nrCarteirinhaSolicitante, "P");

                StringBuilder sb = new StringBuilder();

                sb.append("update tasy.pls_segurado_web psw ");
                sb.append("set psw.ie_situacao = 'A' ");
                sb.append("where psw.nr_seq_segurado = :nr_seq_segurado");

                Query query = manager.createNativeQuery(sb.toString());

                query.setParameter("nr_seq_segurado", login.getId_segurado());

                query.executeUpdate();

                model.addAttribute("message", "Cadastro ativado com sucesso! Agora você já pode aproveitar todas as funcionalidades do aplicativo");

                return successPage;
            }

            if (tpEmail == 'R') {
                RetornoLogin login = usuarioRepository.loginPorUsuario(nrCarteirinhaSolicitante, "A");

                TokenRecuperarCadastrarModel token = Global.TokenRecuperarCadastrarConta.getTokenPorHash(hash);

                if (token == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ocorreu um erro desconhecido ao processar a troca de senha. Favor refazer a solicitação");
                }

                boolean existeDsTec = login.getDs_tec() != null;

                String ds_tec = existeDsTec ? login.getDs_tec() : Global.generateDsTec(login.getDt_nascimento().toString(), login.getNr_cpf());

                String sha256hex = (Hashing.sha256().hashString(token.getNewPassword().toUpperCase() + ds_tec, StandardCharsets.UTF_8)).toString().toUpperCase();

                StringBuilder sb = new StringBuilder();
                sb.append("update tasy.pls_segurado_web ");
                sb.append("set ds_senha = :ds_senha");
                if (!existeDsTec) {
                    sb.append(", ds_tec = :ds_tec");
                }
                sb.append(" where nr_seq_segurado = :nr_seq_segurado and ie_situacao = 'A' ");

                Query query = manager.createNativeQuery(sb.toString());

                query.setParameter("ds_senha", sha256hex);
                query.setParameter("nr_seq_segurado", login.getId_segurado());
                if (!existeDsTec) {
                    query.setParameter("ds_tec", ds_tec);
                }

                query.executeUpdate();

                Global.TokenRecuperarCadastrarConta.removeSolicitacaoPorToken(hash);

                model.addAttribute("message", "Sua senha foi redefinida com sucesso!");

                return successPage;
            }

            model.addAttribute("message", "Ocorreu um erro desconhecido ao processar a troca de senha. Favor refazer a solicitação");
            return errorPage;
        } catch (RuntimeException e) {
            model.addAttribute("message", Global.trataMensagemExceptionBadRequest(e));
            return errorPage;
        }
    }

    public String GerarHashString() {
        String hashRetorno = Utils.randomString(12);

        if (!Global.TokenRecuperarCadastrarConta.hashEmUso(hashRetorno)) {
            return hashRetorno;
        } else {
            return GerarHashString();
        }
    }

    public String sanitizeString(String str) {
        return str.replace(".", "").replace("-", "").replace("(", "").replace(")", "");
    }

    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("email-ssl.com.br");
        mailSender.setPort(587);
        mailSender.setUsername(dsEmail);
        mailSender.setPassword(dsSenha);

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.connectiontimeout", true);

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    public static class ConfirmacaoUsuarioNovo {
        public String Hash;
        public String nrCarteirinha;

        public String getHash() {
            return Hash;
        }

        public void setHash(String dsHash) {
            this.Hash = Hash;
        }

        public String getNrCarteirinha() {
            return nrCarteirinha;
        }

        public void setNrCarteirinha(String nrCarteirinha) {
            this.nrCarteirinha = nrCarteirinha;
        }
    }

    public class RetornoMensagem {
        public String mensagem;
        public String dsEmail;

        public RetornoMensagem() {
        }

        public RetornoMensagem(String mensagem, String dsEmail) {
            super();
            this.mensagem = mensagem;
            this.dsEmail = dsEmail;
        }

        public String getDsEmail() {
            return dsEmail;
        }

        public void setDsEmail(String dsEmail) {
            this.dsEmail = dsEmail;
        }

        public RetornoMensagem(String mensagem) {
            this.mensagem = mensagem;
        }

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }
    }

    public static class UsuarioRecSenha {
        String nrCarteirinha;
        String nrCpf;

        public String getNrCarteirinha() {
            return nrCarteirinha;
        }

        public void setNrCarteirinha(String nrCarteirinha) {
            this.nrCarteirinha = nrCarteirinha;
        }

        public String getNrCpf() {
            return nrCpf;
        }

        public void setNrCpf(String nrCpf) {
            this.nrCpf = nrCpf;
        }
    }

    public static class UsuarioCredenciais {
        String dsSenha;
        String dsSenhaConfirmacao;
        String hash;

        public String getDsSenha() {
            return dsSenha;
        }

        public void setDsSenha(String dsSenha) {
            this.dsSenha = dsSenha;
        }

        public String getDsSenhaConfirmacao() {
            return dsSenhaConfirmacao;
        }

        public void setDsSenhaConfirmacao(String dsSenhaConfirmacao) {
            this.dsSenhaConfirmacao = dsSenhaConfirmacao;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }

    public static class Usuario {
        String nrCarteirinha;
        String dsEmail;
        String dsEmailConfirmacao;

        Date dtNascimento;
        String nrCpf;

        String dsSenha;
        String dsSenhaConfirmacao;

        public String getNrCarteirinha() {
            return nrCarteirinha;
        }

        public void setNrCarteirinha(String nrCarteirinha) {
            this.nrCarteirinha = nrCarteirinha;
        }

        public String getDsEmail() {
            return dsEmail;
        }

        public void setDsEmail(String dsEmail) {
            this.dsEmail = dsEmail;
        }

        public String getDsEmailConfirmacao() {
            return dsEmailConfirmacao;
        }

        public void setDsEmailConfirmacao(String dsEmailConfirmacao) {
            this.dsEmailConfirmacao = dsEmailConfirmacao;
        }

        public Date getDtNascimento() {
            return dtNascimento;
        }

        public void setDtNascimento(Date dtNascimento) {
            this.dtNascimento = dtNascimento;
        }

        public String getNrCpf() {
            return nrCpf;
        }

        public void setNrCpf(String nrCpf) {
            this.nrCpf = nrCpf;
        }

        public String getDsSenha() {
            return dsSenha;
        }

        public void setDsSenha(String dsSenha) {
            this.dsSenha = dsSenha;
        }

        public String getDsSenhaConfirmacao() {
            return dsSenhaConfirmacao;
        }

        public void setDsSenhaConfirmacao(String dsSenhaConfirmacao) {
            this.dsSenhaConfirmacao = dsSenhaConfirmacao;
        }
    }

    public static class FormAlterarSenha {
        String dsLogin;
        String dsSenhaAtual;
        String dsSenhaNova;
        String dsSenhaConfirmacao;

        public String getDsLogin() {
            return dsLogin;
        }

        public String getDsSenhaAtual() {
            return dsSenhaAtual;
        }

        public String getDsSenhaNova() {
            return dsSenhaNova;
        }

        public String getDsSenhaConfirmacao() {
            return dsSenhaConfirmacao;
        }
    }
}
