package com.spin.ops.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name="tasy.pessoa_fisica")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(max=50)
	@Column(name="ds_login")
	private String ds_login;
	
	@NotEmpty
	@Size(max=50)
	@Column(name="ds_email")
	private String ds_email;
	
	@NotEmpty
	@Size(max=50)
	@Column(name="ds_nome")
	@OrderBy
	private String ds_nome;
	
	private Boolean fl_ativo;
	
	private String ds_senha;
	
	private String ds_senha_digitada;
	
	@Size(max=400)
	private String ds_imagem_avatar;
	
	@Size(max=25)
	private String nr_celular;
	
	@Column(name="id_empresa_login")
	private Long id_empresa_login;

	@Size(max=2)
	@Column(name="ds_estado")
	private String ds_estado;
	
	@Size(max=25)
	private String nr_cnpj_cpf;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDs_login() {
		return ds_login;
	}

	public void setDs_login(String ds_login) {
		this.ds_login = ds_login;
	}

	public String getDs_email() {
		return ds_email;
	}
	
	public void setDs_email(String ds_email) {
		this.ds_email = ds_email;
	}
	
	public String getDs_nome() {
		return ds_nome;
	}

	public void setDs_nome(String ds_nome) {
		this.ds_nome = ds_nome;
	}

	public Boolean getFl_ativo() {
		return fl_ativo;
	}

	public void setFl_ativo(Boolean fl_ativo) {
		this.fl_ativo = fl_ativo;
	}

	public String getDs_senha() {
		return ds_senha;
	}

	public void setDs_senha(String ds_senha) {
		this.ds_senha = ds_senha;
	}

	public String getDs_imagem_avatar() {
		return ds_imagem_avatar;
	}

	public void setDs_imagem_avatar(String ds_imagem_avatar) {
		this.ds_imagem_avatar = ds_imagem_avatar;
	}

	public String getNr_celular() {
		return nr_celular;
	}

	public void setNr_celular(String nr_celular) {
		this.nr_celular = nr_celular;
	}

	public String getDs_estado() {
		return ds_estado;
	}

	public void setDs_estado(String ds_estado) {
		this.ds_estado = ds_estado;
	}

	public String getNr_cnpj_cpf() {
		return nr_cnpj_cpf;
	}

	public void setNr_cnpj_cpf(String nr_cnpj_cpf) {
		this.nr_cnpj_cpf = nr_cnpj_cpf;
	}


	public String getDs_senha_digitada() {
		return ds_senha_digitada;
	}

	public void setDs_senha_digitada(String ds_senha_digitada) {
		this.ds_senha_digitada = ds_senha_digitada;
	}

	public Long getId_empresa_login() {
		return id_empresa_login;
	}

	public void setId_empresa_login(Long id_empresa_login) {
		this.id_empresa_login = id_empresa_login;
	}

}
