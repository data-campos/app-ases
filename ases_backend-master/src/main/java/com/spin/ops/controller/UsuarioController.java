package com.spin.ops.controller;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.spin.ops.ServicoEmail;
import com.spin.ops.Utils;
import com.spin.ops.repository.PlsSeguradoWebRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.hash.Hashing;
import com.spin.ops.Global;
import com.spin.ops.model.InterfacesJPA.CarteirinhaDigital;
import com.spin.ops.model.InterfacesJPA.RetornoLogin;
import com.spin.ops.repository.UsuarioRepository;

@CrossOrigin
@Controller
@RequestMapping
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PlsSeguradoWebRepository plsSeguradoWebRepository;

	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	@ResponseBody
	@PutMapping("/usuario/alterar-senha")
	public String createAuthenticationToken(@RequestBody AlterarSenha alt) throws Exception {
		
		if (alt.getDs_senha_atual().equalsIgnoreCase(alt.getDs_senha_nova())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"A nova senha deve ser diferente a senha atual.");
		}

		String sha256hex = Hashing.sha256().hashString(alt.getDs_senha_atual(), StandardCharsets.UTF_8).toString().toUpperCase();
		RetornoLogin ret = usuarioRepository.loginPorUsuarioeSenha(alt.getDs_login(), sha256hex);
		if (ret == null) {
			ret = usuarioRepository.loginPorUsuarioeSenha(alt.getDs_login(), alt.getDs_senha_atual());
		}
		if (ret == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha atual não está correta.");
		}

		String sha256hexNova = Hashing.sha256().hashString(alt.getDs_senha_nova(), StandardCharsets.UTF_8).toString().toUpperCase();
		
		try {
			manager.createNativeQuery(" update tasy.pls_segurado_web set ds_senha = ?1 where nr_seq_segurado = ?2 and ie_situacao = 'A' ")
			.setParameter(1, sha256hexNova)
			.setParameter(2, alt.getId_segurado())
			.executeUpdate();
			return "ok";
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}

	// Método para solicitar a exclusão de conta via e-mail
    @Transactional
    @ResponseBody
    @PostMapping("/usuario/excluir-conta")
    public String solicitarExcluirConta(@RequestBody ExcluirConta excluirConta) throws Exception {
		try {
        	RetornoLogin login = usuarioRepository.loginPorUsuario(excluirConta.getDs_login(), "A");

			if (login == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiário não cadastrado.");
			} else if (login.getDs_email() == null || login.getDs_email().trim().equals("")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não existe um e-mail vinculado na conta deste beneficiário. Favor entrar em contato com a administração do plano ASES");
			}

            String hashExcluirConta = GerarHashExcluirConta();

            String emailEnviar = login.getDs_email();

			Global.TokenExcluirContaModel solicitacao = new Global.TokenExcluirContaModel();

			solicitacao.setToken(hashExcluirConta);
			solicitacao.setEmail(emailEnviar);
			solicitacao.setNrCarteirinha(login.getDs_login());
			solicitacao.setUtilizada(false);
			solicitacao.setDateSolicitacao(new Date());

            Global.TokenExcluirConta.gerarNovaSolicitacao(solicitacao);

            ServicoEmail servEmail = new ServicoEmail();
            servEmail.enviaEmail(emailEnviar, hashExcluirConta, 'E');

            return "{\"message\":\"Enviamos um e-mail para confirmar a exclusão da conta.\"}";
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Global.trataMensagemExceptionBadRequest(e));
        }
    }

	// Método para validar Hash (RETORNA HTML)
    @GetMapping("/usuario/excluir-conta/validar-hash")
	@Transactional
    public String validarHash(Model model, @RequestParam String hash) throws Exception {
        String errorPage = "custom-render-error.html";
        String successPage = "custom-render-success.html";

		String nrCarteirinhaSolicitante = Global.TokenExcluirConta.getNrCarteirinhaPorTokenSolicitacao(hash);

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
                model.addAttribute("message", "Ocorreu um erro desconhecido ao processar a exclusão da conta. Favor refazer a solicitação");
                break;
        }

        if (model.containsAttribute("message")) {
            return errorPage;
        }

        try {
            RetornoLogin login = usuarioRepository.loginPorUsuario(nrCarteirinhaSolicitante, "A");

            Global.TokenExcluirContaModel token = Global.TokenExcluirConta.getTokenPorHash(hash);

            if (token == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ocorreu um erro desconhecido ao processar a troca de senha. Favor refazer a solicitação");
            }

			Query sql = manager.createNativeQuery("delete from tasy.pls_segurado_web where nr_seq_segurado = :nr_seq_segurado");
			sql.setParameter("nr_seq_segurado", login.getId_segurado());
			sql.executeUpdate();

            Global.TokenExcluirConta.removeSolicitacaoPorToken(hash);

            model.addAttribute("message", "Sua conta foi excluída com sucesso!");

            return successPage;
        } catch (RuntimeException e) {
            model.addAttribute("message", Global.trataMensagemExceptionBadRequest(e));
            return errorPage;
        }
    }

	@GetMapping("/usuario/{cd_carteirinha}/carteira-digital")
	@ResponseBody
	public CarteirinhaDigital consultar(@RequestHeader (name="Authorization") String token, 
			@PathVariable String cd_carteirinha) throws Exception {
		
		try {
			return usuarioRepository.obterDadosCarteirinhaDigital(cd_carteirinha);
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}

	public String GerarHashExcluirConta() {
		String hashRetorno = Utils.randomString(12);

		if (!Global.TokenExcluirConta.hashEmUso(hashRetorno)) {
			return hashRetorno;
		} else {
			return GerarHashExcluirConta();
		}
	}

	public String sanitizeString(String str) {
		return str.replace(".", "").replace("-", "").replace("(", "").replace(")", "");
	}

	@Data
	public static class AlterarSenha {
		Long id_segurado;
		String ds_login;
		String ds_senha_atual;
		String ds_senha_nova;
	}

	@Data
	public static class ExcluirConta {
		String ds_login;
	}
	
}
