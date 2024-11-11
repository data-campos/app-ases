package com.spin.ops.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;
import com.spin.ops.model.InterfacesJPA.AlteracaoDados;
import com.spin.ops.model.InterfacesJPA.ListaAlteracao;
import com.spin.ops.model.TasySolicAltCampo;
import com.spin.ops.model.TasySolicAlteracao;
import com.spin.ops.repository.AlteracaoDadosRepository;
import com.spin.ops.repository.TasySolicAltCampoRepository;
import com.spin.ops.repository.TasySolicAlteracaoRepository;

@CrossOrigin
@RestController
@RequestMapping
public class AlteracaoDadosController {

	@Autowired
	private AlteracaoDadosRepository alts;

	@Autowired
	private TasySolicAlteracaoRepository alteracoes;

	@Autowired
	private TasySolicAltCampoRepository campos;

	@PersistenceContext
	private EntityManager manager;
	
	private BigDecimal idAlteracao = new BigDecimal(0);
	
	@GetMapping("/portal/alteracao/obter")
	public AlteracaoDados obterDados(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return alts.buscarDados(ret.getCdPessoaFisica());
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@PutMapping("/portal/alteracao/solicitar")
	public void solicitar(
			@RequestHeader (name="Authorization") String token, 
			@RequestBody Alteracao dados) throws Exception {
		
		try {
			String dsChaveComposta;
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			AlteracaoDados old = alts.buscarDados(ret.getCdPessoaFisica());
			
			idAlteracao = new BigDecimal(0);
			
			RegistraAlteracao(ret, "PESSOA_FISICA", "NM_PESSOA_FISICA", dados.getNm_pessoa_fisica(), old.getNm_pessoa_fisica(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "DT_NASCIMENTO", dados.getDt_nascimento(), old.getDt_nascimento());
			RegistraAlteracao(ret, "PESSOA_FISICA", "IE_ESTADO_CIVIL", dados.getIe_estado_civil(), old.getIe_estado_civil(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "NR_IDENTIDADE", retiraPontoTraco(dados.getNr_identidade()), old.getNr_identidade(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "NR_CARTAO_NAC_SUS", dados.getNr_cartao_nac_sus(), old.getNr_cartao_nac_sus(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "NR_CPF", retiraPontoTraco(dados.getNr_cpf()), old.getNr_cpf(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "DT_EMISSAO_CI", dados.getDt_emissao_identidade(), old.getDt_emissao_identidade());
			RegistraAlteracao(ret, "PESSOA_FISICA", "SG_EMISSORA_CI", dados.getSg_emissora_ci(), old.getSg_emissora_ci(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "DS_ORGAO_EMISSOR_CI", dados.getDs_orgao_emissor_ci(), old.getDs_orgao_emissor_ci(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "NR_REG_GERAL_ESTRANG", dados.getNr_cartao_estrangeiro(), old.getNr_cartao_estrangeiro(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "NR_CEP_CIDADE_NASC", retiraPontoTraco(dados.getNr_cep_cidade_nasc()), old.getNr_cep_cidade_nasc(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "IE_SEXO", dados.getNr_cep_cidade_nasc(), old.getNr_cep_cidade_nasc(), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "NR_TELEFONE_CELULAR", retiraPontoTraco(dados.getNr_telefone_celular()), retiraPontoTraco(old.getNr_telefone_celular()), ret.getCdPessoaFisica(), null);
			RegistraAlteracao(ret, "PESSOA_FISICA", "CD_DECLARACAO_NASC_VIVO", dados.getCd_declaracao_nasc_vivo(), old.getCd_declaracao_nasc_vivo(), ret.getCdPessoaFisica(), null);
			
			dsChaveComposta = "CD_PESSOA_FISICA=".concat(ret.getCdPessoaFisica()).concat("#@#@IE_TIPO_COMPLEMENTO=1");
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "DS_EMAIL", dados.getDs_email(), old.getDs_email() ,null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "CD_CEP", retiraPontoTraco(dados.getCd_cep()), old.getCd_cep(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "NR_ENDERECO", dados.getNr_endereco(), old.getNr_endereco(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "DS_COMPLEMENTO", dados.getDs_endereco(), old.getDs_endereco(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "DS_BAIRRO", dados.getDs_bairro(), old.getDs_bairro(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "CD_MUNICIPIO_IBGE", dados.getCd_municipio_ibge(), old.getCd_municipio_ibge(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "SG_ESTADO", dados.getSg_estado(), old.getSg_estado(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "NR_DDD_TELEFONE", dados.getNr_ddd_telefone(), old.getNr_ddd_telefone(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "NR_DDI_TELEFONE", dados.getNr_ddi_telefone(), old.getNr_ddi_telefone(),null, dsChaveComposta);
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "NR_TELEFONE", retiraPontoTraco(dados.getNr_telefone()), retiraPontoTraco(old.getNr_telefone()),null, dsChaveComposta);
			
			dsChaveComposta = "CD_PESSOA_FISICA=".concat(ret.getCdPessoaFisica()).concat("#@#@IE_TIPO_COMPLEMENTO=4");
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "NM_CONTATO", dados.getNm_pai(), old.getNm_pai() ,null, dsChaveComposta);
			
			dsChaveComposta = "CD_PESSOA_FISICA=".concat(ret.getCdPessoaFisica()).concat("#@#@IE_TIPO_COMPLEMENTO=5");
			RegistraAlteracao(ret, "COMPL_PESSOA_FISICA", "NM_CONTATO", dados.getNm_mae(), old.getNm_mae() ,null, dsChaveComposta);
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}	
	
	@GetMapping("/portal/alteracao/listar")
	public List<ListaRetorno> listar(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			List<ListaAlteracao> listaBanco = alts.listar(ret.getCdPessoaFisica());
			List<ListaRetorno> listaPronta = new ArrayList<ListaRetorno>();
			
			Long nrSeq = 0L;
			ListaRetorno novo = null;
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			for (ListaAlteracao item : listaBanco) {
				if (!nrSeq.equals(item.getNr_sequencia())) {
					novo = new ListaRetorno();
					novo.setDs("Alteração de: ".concat(f.format(item.getDt_atualizacao_nrec())));
					novo.setDsStatus(item.getDs_status());
					
					listaPronta.add(novo);
				}
				ItemAlteracao novoItem = new ItemAlteracao();
				novoItem.setDsStatus(item.getDs_status_campo());
				novoItem.setNmAtributo(item.getNm_atributo());
				novoItem.setVlAntes(item.getDs_antes());
				novoItem.setVlDepois(item.getDs_depois());
				
				novo.setItem(novoItem);
				
				nrSeq = item.getNr_sequencia();
			}
			
			return listaPronta;
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	
	public String retiraPontoTraco(String dsIn) {
		if (dsIn == null) {
			return "";
		}
		return dsIn.replace(".", "").replace("-", "").replace("(", "").replace(")", "");
	}
	
	public void RegistraAlteracao(TokenLoginModel token, String nmTabela, String nmCampo, String vlAtual, String vlOld, String dsChaveSimples, String dsChaveComposta) {
		if (vlAtual == null) { vlAtual = ""; }
		if (vlOld == null) { vlOld = ""; }

		if (!vlAtual.equals(vlOld)){
			if (idAlteracao.compareTo(new BigDecimal(0)) == 0) {
				TasySolicAlteracao alteracao = new TasySolicAlteracao();
				alteracao.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.tasy_solic_alteracao_seq.nextval from dual").getSingleResult());
				alteracao.setCd_estabelecimento(token.getCdEstabelecimento());
				alteracao.setCd_pessoa_fisica(token.getCdPessoaFisica());
				
				alteracao = alteracoes.save(alteracao);
				
				idAlteracao = alteracao.getNr_sequencia();
			}
			TasySolicAltCampo altCampo = new TasySolicAltCampo();
			altCampo.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.tasy_solic_alt_campo_seq.nextval from dual").getSingleResult());
			altCampo.setDs_valor_new(vlAtual);
			altCampo.setDs_valor_old(vlOld);
			altCampo.setNm_atributo(nmCampo);
			altCampo.setNm_tabela(nmTabela);
			altCampo.setDs_chave_simples(dsChaveSimples);
			altCampo.setDs_chave_composta(dsChaveComposta);
			altCampo.setNr_seq_solicitacao(idAlteracao);
			
			campos.save(altCampo);
		}
	}
	
	public void RegistraAlteracao(TokenLoginModel token, String nmTabela, String nmCampo, Date vlAtual, Date vlOld) {
		if ((vlAtual != null) && (vlOld != null) && (!vlAtual.equals(vlOld))){
			if (idAlteracao.compareTo(new BigDecimal(0)) == 0) {
				TasySolicAlteracao alteracao = new TasySolicAlteracao();
				alteracao.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.tasy_solic_alteracao_seq.nextval from dual").getSingleResult());
				alteracao.setCd_estabelecimento(token.getCdEstabelecimento());
				alteracao.setCd_pessoa_fisica(token.getCdPessoaFisica());
				
				alteracao = alteracoes.save(alteracao);
				idAlteracao = alteracao.getNr_sequencia();
			}
			
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			
			TasySolicAltCampo altCampo = new TasySolicAltCampo();
			altCampo.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.tasy_solic_alt_campo_seq.nextval from dual").getSingleResult());
			altCampo.setDs_valor_new(f.format(vlAtual));
			altCampo.setDs_valor_old(f.format(vlOld));
			altCampo.setNm_atributo(nmCampo);
			altCampo.setNm_tabela(nmTabela);
			altCampo.setDs_chave_simples(token.getCdPessoaFisica());			
			altCampo.setNr_seq_solicitacao(idAlteracao);
			
			campos.save(altCampo);
		}
	}	
	
	public static class Alteracao{
		String Nm_pessoa_fisica;
		Date Dt_nascimento;
		String Ie_estado_civil;
		String Ds_estado_civil;
		String Nr_cpf;
		String Nr_cartao_nac_sus;
		String Ds_email;
		String Nr_identidade;
		Date Dt_emissao_identidade;
		String Sg_emissora_ci;
		String Ds_orgao_emissor_ci;
		String Nr_cartao_estrangeiro;
		String Nr_cep_cidade_nasc;
		String Nm_cidade_nasc;
		String Ie_sexo;
		String Ds_sexo;

		String Cd_cep;
		String Nr_endereco;
		String Ds_endereco;
		String Ds_bairro;
		String Cd_municipio_ibge;
		String Ds_cidade;
		String Sg_estado;
		String Ds_uf;
		String Nr_ddi_telefone;
		String Nr_ddd_telefone;
		String Nr_telefone;
		String Nr_telefone_celular;
		String Cd_declaracao_nasc_vivo;
		String Nm_pai;
		String Nm_mae;
		
		public String getNm_pessoa_fisica() {
			return Nm_pessoa_fisica;
		}
		public void setNm_pessoa_fisica(String nm_pessoa_fisica) {
			Nm_pessoa_fisica = nm_pessoa_fisica;
		}
		public Date getDt_nascimento() {
			return Dt_nascimento;
		}
		public void setDt_nascimento(Date dt_nascimento) {
			Dt_nascimento = dt_nascimento;
		}
		public String getDs_estado_civil() {
			return Ds_estado_civil;
		}
		public void setDs_estado_civil(String ds_estado_civil) {
			Ds_estado_civil = ds_estado_civil;
		}
		public String getNr_cpf() {
			return Nr_cpf;
		}
		public void setNr_cpf(String nr_cpf) {
			Nr_cpf = nr_cpf;
		}
		public String getNr_cartao_nac_sus() {
			return Nr_cartao_nac_sus;
		}
		public void setNr_cartao_nac_sus(String nr_cartao_nac_sus) {
			Nr_cartao_nac_sus = nr_cartao_nac_sus;
		}
		public String getDs_email() {
			return Ds_email;
		}
		public void setDs_email(String ds_email) {
			Ds_email = ds_email;
		}
		public String getNr_identidade() {
			return Nr_identidade;
		}
		public void setNr_identidade(String nr_identidade) {
			Nr_identidade = nr_identidade;
		}
		public Date getDt_emissao_identidade() {
			return Dt_emissao_identidade;
		}
		public void setDt_emissao_identidade(Date dt_emissao_identidade) {
			Dt_emissao_identidade = dt_emissao_identidade;
		}
		public String getSg_emissora_ci() {
			return Sg_emissora_ci;
		}
		public void setSg_emissora_ci(String sg_emissora_ci) {
			Sg_emissora_ci = sg_emissora_ci;
		}
		public String getDs_orgao_emissor_ci() {
			return Ds_orgao_emissor_ci;
		}
		public void setDs_orgao_emissor_ci(String ds_orgao_emissor_ci) {
			Ds_orgao_emissor_ci = ds_orgao_emissor_ci;
		}
		public String getNr_cartao_estrangeiro() {
			return Nr_cartao_estrangeiro;
		}
		public void setNr_cartao_estrangeiro(String nr_cartao_estrangeiro) {
			Nr_cartao_estrangeiro = nr_cartao_estrangeiro;
		}
		public String getNr_cep_cidade_nasc() {
			return Nr_cep_cidade_nasc;
		}
		public void setNr_cep_cidade_nasc(String nr_cep_cidade_nasc) {
			Nr_cep_cidade_nasc = nr_cep_cidade_nasc;
		}
		public String getDs_sexo() {
			return Ds_sexo;
		}
		public void setDs_sexo(String ds_sexo) {
			Ds_sexo = ds_sexo;
		}
		public String getCd_cep() {
			return Cd_cep;
		}
		public void setCd_cep(String cd_cep) {
			Cd_cep = cd_cep;
		}
		public String getNr_endereco() {
			return Nr_endereco;
		}
		public void setNr_endereco(String nr_endereco) {
			Nr_endereco = nr_endereco;
		}
		public String getDs_endereco() {
			return Ds_endereco;
		}
		public void setDs_endereco(String ds_endereco) {
			Ds_endereco = ds_endereco;
		}
		public String getDs_bairro() {
			return Ds_bairro;
		}
		public void setDs_bairro(String ds_bairro) {
			Ds_bairro = ds_bairro;
		}
		public String getDs_cidade() {
			return Ds_cidade;
		}
		public void setDs_cidade(String ds_cidade) {
			Ds_cidade = ds_cidade;
		}
		public String getDs_uf() {
			return Ds_uf;
		}
		public void setDs_uf(String ds_uf) {
			Ds_uf = ds_uf;
		}
		public String getNr_ddi_telefone() {
			return Nr_ddi_telefone;
		}
		public void setNr_ddi_telefone(String nr_ddi_telefone) {
			Nr_ddi_telefone = nr_ddi_telefone;
		}
		public String getNr_ddd_telefone() {
			return Nr_ddd_telefone;
		}
		public void setNr_ddd_telefone(String nr_ddd_telefone) {
			Nr_ddd_telefone = nr_ddd_telefone;
		}
		public String getNr_telefone() {
			return Nr_telefone;
		}
		public void setNr_telefone(String nr_telefone) {
			Nr_telefone = nr_telefone;
		}
		public String getNr_telefone_celular() {
			return Nr_telefone_celular;
		}
		public void setNr_telefone_celular(String nr_telefone_celular) {
			Nr_telefone_celular = nr_telefone_celular;
		}
		public String getCd_declaracao_nasc_vivo() {
			return Cd_declaracao_nasc_vivo;
		}
		public void setCd_declaracao_nasc_vivo(String cd_declaracao_nasc_vivo) {
			Cd_declaracao_nasc_vivo = cd_declaracao_nasc_vivo;
		}
		public String getNm_pai() {
			return Nm_pai;
		}
		public void setNm_pai(String nm_pai) {
			Nm_pai = nm_pai;
		}
		public String getNm_mae() {
			return Nm_mae;
		}
		public void setNm_mae(String nm_mae) {
			Nm_mae = nm_mae;
		}
		public String getIe_estado_civil() {
			return Ie_estado_civil;
		}
		public void setIe_estado_civil(String ie_estado_civil) {
			Ie_estado_civil = ie_estado_civil;
		}		
		public String getIe_sexo() {
			return Ie_sexo;
		}
		public void setIe_sexo(String ie_sexo) {
			Ie_sexo = ie_sexo;
		}
		public String getNm_cidade_nasc() {
			return Nm_cidade_nasc;
		}
		public void setNm_cidade_nasc(String nm_cidade_nasc) {
			Nm_cidade_nasc = nm_cidade_nasc;
		}
		public String getCd_municipio_ibge() {
			return Cd_municipio_ibge;
		}
		public void setCd_municipio_ibge(String cd_municipio_ibge) {
			Cd_municipio_ibge = cd_municipio_ibge;
		}
		public String getSg_estado() {
			return Sg_estado;
		}
		public void setSg_estado(String sg_estado) {
			Sg_estado = sg_estado;
		}
	}
	
	public class ListaRetorno{
		String ds;
		String dsStatus;
		List<ItemAlteracao> listaItens;
		public String getDs() {
			return ds;
		}
		public void setDs(String ds) {
			this.ds = ds;
		}
		public String getDsStatus() {
			return dsStatus;
		}
		public void setDsStatus(String dsStatus) {
			this.dsStatus = dsStatus;
		}
		public List<ItemAlteracao> getListaItens() {
			return listaItens;
		}
		public void setItem(ItemAlteracao item) {
			if (listaItens == null) {
				listaItens = new ArrayList<ItemAlteracao>();
			}
			this.listaItens.add(item);
		}
	}
	
	public class ItemAlteracao{
		String nmAtributo;
		String vlAntes;
		String vlDepois;
		String dsStatus;
		public String getNmAtributo() {
			return nmAtributo;
		}
		public void setNmAtributo(String nmAtributo) {
			this.nmAtributo = nmAtributo;
		}
		public String getVlAntes() {
			return vlAntes;
		}
		public void setVlAntes(String vlAntes) {
			this.vlAntes = vlAntes;
		}
		public String getVlDepois() {
			return vlDepois;
		}
		public void setVlDepois(String vlDepois) {
			this.vlDepois = vlDepois;
		}
		public String getDsStatus() {
			return dsStatus;
		}
		public void setDsStatus(String dsStatus) {
			this.dsStatus = dsStatus;
		}
	}

}
