package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.tasy_solic_alt_campo")
public class TasySolicAltCampo {
	
	@Id
	private BigDecimal nr_sequencia;
	
	@SuppressWarnings("unused")
	private String nm_usuario = "TASY";;
	@SuppressWarnings("unused")
	private String nm_usuario_nrec = "TASY";
	@SuppressWarnings("unused")
	private String ie_status = "P";
	@SuppressWarnings("unused")
	private Date dt_atualizacao = new Date();
	@SuppressWarnings("unused")
	private Date dt_atualizacao_nrec = new Date();

	private String nm_tabela;
	private String nm_atributo;
	private String ds_valor_old;
	private String ds_valor_new;
	private String ds_chave_simples;
	private String ds_chave_composta;

	private BigDecimal nr_seq_solicitacao;
	
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
	public String getNm_tabela() {
		return nm_tabela;
	}
	public void setNm_tabela(String nm_tabela) {
		this.nm_tabela = nm_tabela;
	}
	public String getNm_atributo() {
		return nm_atributo;
	}
	public void setNm_atributo(String nm_atributo) {
		this.nm_atributo = nm_atributo;
	}
	public String getIe_status() {
		return "P";
	}
	public void setIe_status(String ie_status) {
		this.ie_status = ie_status;
	}
	public String getDs_valor_old() {
		return ds_valor_old;
	}
	public void setDs_valor_old(String ds_valor_old) {
		this.ds_valor_old = ds_valor_old;
	}
	public String getDs_valor_new() {
		return ds_valor_new;
	}
	public void setDs_valor_new(String ds_valor_new) {
		this.ds_valor_new = ds_valor_new;
	}
	public BigDecimal getNr_seq_solicitacao() {
		return nr_seq_solicitacao;
	}
	public void setNr_seq_solicitacao(BigDecimal nr_seq_solicitacao) {
		this.nr_seq_solicitacao = nr_seq_solicitacao;
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
	public String getDs_chave_simples() {
		return ds_chave_simples;
	}
	public void setDs_chave_simples(String ds_chave_simples) {
		this.ds_chave_simples = ds_chave_simples;
	}
	public String getDs_chave_composta() {
		return ds_chave_composta;
	}
	public void setDs_chave_composta(String ds_chave_composta) {
		this.ds_chave_composta = ds_chave_composta;
	}
}
