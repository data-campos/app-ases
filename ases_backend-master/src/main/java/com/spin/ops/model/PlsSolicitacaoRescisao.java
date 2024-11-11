package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.pls_solicitacao_rescisao")
public class PlsSolicitacaoRescisao {
	
	@Id
	private BigDecimal nr_sequencia;
	
	@SuppressWarnings("unused")
	private String nm_usuario = "TASY";;
	@SuppressWarnings("unused")
	private String nm_usuario_nrec = "TASY";
	@SuppressWarnings("unused")
	private String ie_origem_solicitacao = "P";
	@SuppressWarnings("unused")
	private String ie_status = "1";
	@SuppressWarnings("unused")
	private Date dt_atualizacao = new Date();
	@SuppressWarnings("unused")
	private Date dt_atualizacao_nrec = new Date();
	private Date dt_solicitacao = new Date();

	private Long cd_estabelecimento;
	private Long nr_seq_contrato;
	
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
	public String getIe_origem_solicitacao() {
		return "P";
	}
	public void setIe_origem_solicitacao(String ie_origem_solicitacao) {
		this.ie_origem_solicitacao = ie_origem_solicitacao;
	}
	public String getIe_status() {
		return "1";
	}
	public void setIe_status(String ie_status) {
		this.ie_status = ie_status;
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
	public Date getDt_solicitacao() {
		return dt_solicitacao;
	}
	public void setDt_solicitacao(Date dt_solicitacao) {
		this.dt_solicitacao = dt_solicitacao;
	}
	public Long getNr_seq_contrato() {
		return nr_seq_contrato;
	}
	public void setNr_seq_contrato(Long nr_seq_contrato) {
		this.nr_seq_contrato = nr_seq_contrato;
	}
}
