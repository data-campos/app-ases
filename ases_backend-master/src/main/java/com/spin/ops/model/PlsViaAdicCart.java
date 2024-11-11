package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.pls_via_adic_cart")
public class PlsViaAdicCart {
	
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
	@SuppressWarnings("unused")
	private String ie_origem_solic = "P";

	private BigDecimal nr_seq_lote;
	private Long nr_seq_carteira;
	private Long nr_seq_segurado;
	private Long nr_seq_motivo_via;
	
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
	public BigDecimal getNr_seq_lote() {
		return nr_seq_lote;
	}
	public void setNr_seq_lote(BigDecimal nr_seq_lote) {
		this.nr_seq_lote = nr_seq_lote;
	}
	public Long getNr_seq_carteira() {
		return nr_seq_carteira;
	}
	public void setNr_seq_carteira(Long nr_seq_carteira) {
		this.nr_seq_carteira = nr_seq_carteira;
	}
	public Long getNr_seq_segurado() {
		return nr_seq_segurado;
	}
	public void setNr_seq_segurado(Long nr_seq_segurado) {
		this.nr_seq_segurado = nr_seq_segurado;
	}
	public String getIe_origem_solic() {
		return "P";
	}
	public void setIe_origem_solic(String ie_origem_solic) {
		this.ie_origem_solic = ie_origem_solic;
	}
	public Long getNr_seq_motivo_via() {
		return nr_seq_motivo_via;
	}
	public void setNr_seq_motivo_via(Long nr_seq_motivo_via) {
		this.nr_seq_motivo_via = nr_seq_motivo_via;
	}
}
