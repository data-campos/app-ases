package com.spin.ops.controller;

import static com.spin.ops.security.SecurityConstants.TOKEN_PREFIX;
import static java.util.Collections.emptyList;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.spin.ops.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.hash.Hashing;
import com.spin.ops.Global;
import com.spin.ops.model.InterfacesJPA.RetornoDsTec;
import com.spin.ops.model.InterfacesJPA.RetornoLogin;
import com.spin.ops.model.TokenJWT;
import com.spin.ops.repository.PortalBeneficiarioRepository;
import com.spin.ops.repository.UsuarioRepository;
import com.spin.ops.security.JwtTokenUtil;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioRepository usuarios;

	@Autowired
	private PortalBeneficiarioRepository portal;

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody ClasseLogin login) throws Exception {


			RetornoDsTec retDsTec = usuarios.buscarDsTec(login.getDs_login());
			String sha256hex = "";
			if (retDsTec != null) {
				if (retDsTec.getDs_tec() != null) {
					sha256hex = (Hashing.sha256().hashString(login.getDs_senha().toUpperCase() + retDsTec.getDs_tec(), StandardCharsets.UTF_8)).toString().toUpperCase();
				} else {
					sha256hex = Hashing.sha256().hashString(login.getDs_senha(), StandardCharsets.UTF_8).toString().toUpperCase();
				}
			}
			RetornoLogin ret = usuarios.loginPorUsuarioeSenha(login.getDs_login(), sha256hex);
			UserDetails userDetails;

			if (ret == null) {
				ret = usuarios.loginPorUsuarioeSenha(login.getDs_login(), login.getDs_senha());
				if (ret == null) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INVALID_CREDENTIALS");
				} else {
					authenticate(login.getDs_login(), login.getDs_senha());
					userDetails = new User(login.getDs_login(), login.getDs_senha(), emptyList());
					if (retDsTec.getDs_tec() == null) {
						processaDsTec(ret);
					}
				}
			} else {

				authenticate(login.getDs_login(), sha256hex);

				userDetails = new User(login.getDs_login(), sha256hex, emptyList());
				if (retDsTec.getDs_tec() == null) {
					processaDsTec(ret);
				}
			}

		try {

			final String token = TOKEN_PREFIX.concat("")
					.concat(jwtTokenUtil.generateToken(userDetails));

			Global.TokenLogin.setToken(token, ret.getId_segurado(), ret.getNm_pessoa_fisica(), ret.getCd_estabelecimento(),
					ret.getCd_pessoa_fisica(), ret.getNr_seq_contrato(), ret.getNr_seq_plano(), ret.getNr_seq_carteira(),
					ret.getNr_Seq_pagador());

			List<byte[]> listaBanners = new ArrayList<byte[]>();

			String path = Utils.pathToStorage();

			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			if (!(listOfFiles == null)) {
				for (File file : listOfFiles) {
				    if ((file.isFile()) && (file.getName().contains("banner")) && (FilenameUtils.getExtension(file.getName()).equals("jpg"))) {
				    	listaBanners.add(Files.readAllBytes(file.toPath()));
				    }
				}
			}

			byte[] imagem = portal.buscarImagem(ret.getCd_pessoa_fisica());

			if (imagem != null && imagem.length > 1500000) {
				imagem = null;
			}

			return ResponseEntity.ok(
					new TokenJWT(
						token,
						ret.getNm_pessoa_fisica(),
						ret.getDs_login(),
						ret.getDs_senha(),
						ret.getId_segurado(),
						ret.getIe_tipo_contratacao(),
						ret.getCd_estabelecimento(),
						ret.getIe_tipo_pessoa(),
						login.isFl_salvar(),
						imagem,
						listaBanners
					)
			);
		}catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}

	private void processaDsTec(RetornoLogin login) {
		String ds_Tec = Global.generateDsTec(login.getDt_nascimento().toString(), login.getNr_cpf());
		String sha256 = (Hashing.sha256().hashString(login.getDs_senha().toUpperCase() + ds_Tec, StandardCharsets.UTF_8)).toString().toUpperCase();

		StringBuilder sb = new StringBuilder();
		sb.append("update tasy.pls_segurado_web w set w.ds_tec = :ds_tec, w.ds_senha = :ds_senha where w.nr_seq_segurado = :nr_seq_segurado");

		Query query = manager.createNativeQuery(sb.toString());

		query.setParameter("ds_tec", ds_Tec)
		 .setParameter("ds_senha", sha256)
		 .setParameter("nr_seq_segurado", login.getId_segurado());

		query.executeUpdate();
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INVALID_CREDENTIALS");
		}
	}

	public static class ClasseLogin{
		String ds_login;
		String ds_senha;
		boolean fl_salvar;
		public String getDs_login() {
			return ds_login;
		}
		public void setDs_login(String ds_login) {
			this.ds_login = ds_login;
		}
		public String getDs_senha() {
			return ds_senha;
		}
		public void setDs_senha(String ds_senha) {
			this.ds_senha = ds_senha;
		}
		public boolean isFl_salvar() {
			return fl_salvar;
		}
		public void setFl_salvar(boolean fl_salvar) {
			this.fl_salvar = fl_salvar;
		}
	}
}
