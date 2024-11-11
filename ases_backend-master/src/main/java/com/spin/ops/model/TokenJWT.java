package com.spin.ops.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TokenJWT implements Serializable {
	
	private final String token;
	private final String nomeUsuario;
	private final String dsLogin;
	private final String dsSenha;
	private final Long idSegurado;
	private final String ieTipoContratacao;
	private final Long cdEstabelecimento;
	private final String ieTipoPessoa;
	private final boolean fl_salvar;
	private final byte[] imagem;
	private final List<byte[]> listaBanners;

	public TokenJWT(String token, String nomeUsuario, String dsLogin, String dsSenha, Long idSegurado, String ieTipoContratacao, Long cdEstabelecimento, String ieTipoPessoa,
			boolean fl_salvar, byte[] imagem, List<byte[]> listaBanners) {
		
			this.token = token;
			this.nomeUsuario = nomeUsuario;
			this.dsLogin = dsLogin;
			this.dsSenha = dsSenha;
			this.idSegurado = idSegurado;
			this.ieTipoContratacao = ieTipoContratacao;
			this.fl_salvar = fl_salvar;
			this.cdEstabelecimento = cdEstabelecimento;
			this.ieTipoPessoa = ieTipoPessoa;
			this.imagem = imagem;
			this.listaBanners = listaBanners;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public String getNomeUsuario() {
		return this.nomeUsuario;
	}
	
	public String getDsLogin() {
		return this.dsLogin;
	}

	public String getDsSenha() {
		return this.dsSenha;
	}

	public Long getIdSegurado() {
		return idSegurado;
	}

	public String getIeTipoContratacao() {
		return ieTipoContratacao;
	}

	public String getIeTipoPessoa() {
		return ieTipoPessoa;
	}
	
	public Long getCdEstabelecimento() {
		return cdEstabelecimento;
	}

	public boolean getFl_salvar() {
		return fl_salvar;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public List<byte[]> getListaBanners() {
		return listaBanners;
	}
}