package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.pls_comunic_ext_hist_web")
public class PlsComunicExtHistWeb {
	
	@Id
	private BigDecimal nr_sequencia;
	
	@SuppressWarnings("unused")
	private String nm_usuario = "TASY";;
	@SuppressWarnings("unused")
	private String nm_usuario_nrec = "TASY";
	@SuppressWarnings("unused")
	private String nm_usuario_leitura = "APP";

	@SuppressWarnings("unused")
	private Date dt_atualizacao = new Date();
	@SuppressWarnings("unused")
	private Date dt_atualizacao_nrec = new Date();
	@SuppressWarnings("unused")
	private Date dt_leitura = new Date();

	private BigDecimal nr_seq_comunicado;
	private String cd_pessoa_fisica_resp;
	
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
	public String getNm_usuario_leitura() {
		return "APP";
	}
	public void setNm_usuario_leitura(String nm_usuario_leitura) {
		this.nm_usuario_leitura = nm_usuario_leitura;
	}
	public Date getDt_leitura() {
		return new Date();
	}
	public void setDt_leitura(Date dt_leitura) {
		this.dt_leitura = dt_leitura;
	}
	public BigDecimal getNr_seq_comunicado() {
		return nr_seq_comunicado;
	}
	public void setNr_seq_comunicado(BigDecimal nr_seq_comunicado) {
		this.nr_seq_comunicado = nr_seq_comunicado;
	}
	public String getCd_pessoa_fisica_resp() {
		return cd_pessoa_fisica_resp;
	}
	public void setCd_pessoa_fisica_resp(String cd_pessoa_fisica_resp) {
		this.cd_pessoa_fisica_resp = cd_pessoa_fisica_resp;
	}

}
