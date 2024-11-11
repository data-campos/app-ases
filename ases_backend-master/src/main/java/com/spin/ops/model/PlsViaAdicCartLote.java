package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.pls_via_adic_cart_lote")
public class PlsViaAdicCartLote {
	
	@Id
	private BigDecimal nr_sequencia;
	
	@SuppressWarnings("unused")
	private String nm_usuario = "TASY";;
	@SuppressWarnings("unused")
	private String nm_usuario_nrec = "TASY";
	@SuppressWarnings("unused")
	private Date dt_atualizacao = new Date();
	@SuppressWarnings("unused")
	private Date dt_atualizacao_nrec = new Date();

	private Long nr_seq_contrato;
	@SuppressWarnings("unused")
	private Date dt_geracao = new Date();
	@SuppressWarnings("unused")
	private String ie_processo = "W";
	@SuppressWarnings("unused")
	private String ie_limite_dependencia = "N";
	@SuppressWarnings("unused")
	private Date dt_solicitacao = new Date();
	
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
	public Long getNr_seq_contrato() {
		return nr_seq_contrato;
	}
	public void setNr_seq_contrato(Long nr_seq_contrato) {
		this.nr_seq_contrato = nr_seq_contrato;
	}
	public Date getDt_geracao() {
		return new Date();
	}
	public void setDt_geracao(Date dt_geracao) {
		this.dt_geracao = dt_geracao;
	}
	public String getIe_processo() {
		return "W";
	}
	public void setIe_processo(String ie_processo) {
		this.ie_processo = ie_processo;
	}
	public String getIe_limite_dependencia() {
		return "N";
	}
	public void setIe_limite_dependencia(String ie_limite_dependencia) {
		this.ie_limite_dependencia = ie_limite_dependencia;
	}
	public Date getDt_solicitacao() {
		return new Date();
	}
	public void setDt_solicitacao(Date dt_solicitacao) {
		this.dt_solicitacao = dt_solicitacao;
	}
	
}
