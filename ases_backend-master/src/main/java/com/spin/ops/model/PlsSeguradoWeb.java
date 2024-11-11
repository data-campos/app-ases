package com.spin.ops.model;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.google.common.hash.Hashing;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasy.pls_segurado_web")
public class PlsSeguradoWeb {
	
	@Id
	private BigDecimal nr_sequencia;
	
	private String nm_usuario = "TASY";;
	private String nm_usuario_nrec = "TASY";
	private Date dt_atualizacao = new Date();
	private Date dt_atualizacao_nrec = new Date();
	
	private Long nr_seq_segurado;
	private String ds_tec;
	private String ds_senha;
	private String ie_situacao;
	private String ds_hash;
	private Long cd_estabelecimento;
	private String ie_origem_login;
	public BigDecimal getNr_sequencia() {
		return nr_sequencia;
	}
	public void setNr_sequencia(BigDecimal nr_sequencia) {
		this.nr_sequencia = nr_sequencia;
	}
	public String getNm_usuario() {
		return nm_usuario;
	}
	public void setNm_usuario(String nm_usuario) {
		this.nm_usuario = nm_usuario;
	}
	public String getNm_usuario_nrec() {
		return nm_usuario_nrec;
	}
	public void setNm_usuario_nrec(String nm_usuario_nrec) {
		this.nm_usuario_nrec = nm_usuario_nrec;
	}
	public Date getDt_atualizacao() {
		return dt_atualizacao;
	}
	public void setDt_atualizacao(Date dt_atualizacao) {
		this.dt_atualizacao = dt_atualizacao;
	}
	public Date getDt_atualizacao_nrec() {
		return dt_atualizacao_nrec;
	}
	public void setDt_atualizacao_nrec(Date dt_atualizacao_nrec) {
		this.dt_atualizacao_nrec = dt_atualizacao_nrec;
	}
	public Long getNr_seq_segurado() {
		return nr_seq_segurado;
	}
	public void setNr_seq_segurado(Long nr_seq_segurado) {
		this.nr_seq_segurado = nr_seq_segurado;
	}
	public String getDs_senha() {
		return ds_senha;
	}
	public void setDs_senha(String ds_senha) {
		this.ds_senha = ds_senha;
	}
	
	public String getIe_situacao() {
		return ie_situacao;
	}
	public void setIe_situacao(String ie_situacao) {
		this.ie_situacao = ie_situacao;
	}
	public String getDs_hash() {
		return ds_hash;
	}
	public void setDs_hash(String ds_hash) {
		this.ds_hash = ds_hash;
	}
	public Long getCd_estabelecimento() {
		return cd_estabelecimento;
	}
	public void setCd_estabelecimento(Long cd_estabelecimento) {
		this.cd_estabelecimento = cd_estabelecimento;
	}
	public String getIe_origem_login() {
		return ie_origem_login;
	}
	public void setIe_origem_login(String ie_origem_login) {
		this.ie_origem_login = ie_origem_login;
	}
	public String getDs_Tec() {
		return ds_tec;
	}
	public void setDs_Tec(String ds_tec) {
		this.ds_tec = ds_tec;
	}
}
