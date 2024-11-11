package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.tasy_solic_alteracao")
public class TasySolicAlteracao {
	
	@Id
	private BigDecimal nr_sequencia;
	
	@SuppressWarnings("unused")
	private String nm_usuario = "TASY";;
	@SuppressWarnings("unused")
	private String nm_usuario_nrec = "TASY";
	@SuppressWarnings("unused")
	private String ie_processo = "M";
	@SuppressWarnings("unused")
	private String ie_status = "A";
	@SuppressWarnings("unused")
	private Date dt_atualizacao = new Date();
	@SuppressWarnings("unused")
	private Date dt_atualizacao_nrec = new Date();
	@SuppressWarnings("unused")
	private Long cd_funcao = 120L;
	
	private String cd_pessoa_fisica;
	private Long cd_estabelecimento;
	
	public BigDecimal getNr_sequencia() {
		return nr_sequencia;
	}
	public void setNr_sequencia(BigDecimal nr_sequencia) {
		this.nr_sequencia = nr_sequencia;
	}
	public String getNm_usuario() {
		return "TASY";
	}
	public void setNm_usuario(String nm_usuario) {
		this.nm_usuario = nm_usuario;
	}
	public String getNm_usuario_nrec() {
		return "TASY";
	}
	public void setNm_usuario_nrec(String nm_usuario_nrec) {
		this.nm_usuario_nrec = nm_usuario_nrec;
	}
	public String getIe_processo() {
		return "M";
	}
	public void setIe_processo(String ie_processo) {
		this.ie_processo = ie_processo;
	}
	public String getIe_status() {
		return "A";
	}
	public void setIe_status(String ie_status) {
		this.ie_status = ie_status;
	}
	public String getCd_pessoa_fisica() {
		return cd_pessoa_fisica;
	}
	public void setCd_pessoa_fisica(String cd_pessoa_fisica) {
		this.cd_pessoa_fisica = cd_pessoa_fisica;
	}
	public Long getCd_estabelecimento() {
		return cd_estabelecimento;
	}
	public void setCd_estabelecimento(Long cd_estabelecimento) {
		this.cd_estabelecimento = cd_estabelecimento;
	}
	public Date getDt_atualizacao() {
		return new Date();
	}
	public void setDt_atualizacao(Date dt_atualizacao) {
		this.dt_atualizacao = dt_atualizacao;
	}
	public Date getDt_atualizacao_nrec() {
		return new Date();
	}
	public void setDt_atualizacao_nrec(Date dt_atualizacao_nrec) {
		this.dt_atualizacao_nrec = dt_atualizacao_nrec;
	}
	public Long getCd_funcao() {
		return 120L;
	}
	public void setCd_funcao(Long cd_funcao) {
		this.cd_funcao = cd_funcao;
	}
}
