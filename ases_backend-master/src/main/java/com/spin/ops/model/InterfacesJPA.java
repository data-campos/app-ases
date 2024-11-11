package com.spin.ops.model;

import java.math.BigDecimal;
import java.util.Date;

public class InterfacesJPA{

	public interface RetornoDsTec{
		String getDs_tec();		
		String getDs_senha();
		String getnr_seq_segurado();
	}
	
	public interface RetornoLogin{
		String getNm_pessoa_fisica();
		String getCd_pessoa_fisica();
		String getDs_login();
		String getIe_tipo_pessoa();
		String getDs_senha();
		String getDs_tec();
		Long getId_segurado();
		Long getCd_estabelecimento();
		Long getNr_seq_contrato();
		Long getNr_seq_plano();

		String getNr_cpf();
		Date getDt_nascimento();
		String getDs_email();
		
		Long getNr_seq_carteira();
		
		Long getNr_Seq_pagador();
		String getIe_tipo_contratacao();
	}
	
	public interface Carteirinha{
		String getNm_pessoa_fisica();
		String getCd_pessoa_fisica();
		Long getId_segurado();
		Long getCd_estabelecimento();
		Long getNr_seq_contrato();
		Long getNr_seq_plano();
		
		
		String getNr_cpf();
		Date getDt_nascimento();
		String getDs_email();
		
		Long getNr_seq_carteira();
		
		Long getNr_Seq_pagador();
		Long getQt_web();
		String getieSituacao();
	}	
	
	public interface DetalhePortal{
		String getNm_pessoa_fisica();
		String getCd_pessoa_fisica();
		String getDs_email();
		String getNm_produto();
		Date getDt_nascimento();
		String getNr_carteirinha();
		String getNr_contrato();
		String getNr_controle_interno();
		String getDs_estipulante();
		String getNr_ans();
		String getNr_cartao_nac_sus();
		Date getDt_validade_carteira();
		Date getDt_contratacao();
		Date getDt_inclusao_operadora();
		String getNm_pagador();
		String getDs_plano();
		String getDs_sit_plano();
		String getNm_segmentacao();
		String getNm_regulamentacao();
		String getDs_codigo_anterior();
		String getNr_ans_plano();
		String getNm_tipo_contratacao();
		String getNm_tipo_acomodacao();
		String getNm_formacao_preco();
		String getCd_scpa();
		String getDs_abrangencia();
	}
	
	public interface CarteirinhaDigital{
		String getNr_carteirinha();
		String getDt_nascimento();
		String getDt_adesao();
		String getDt_validade_carteira();
		String getDs_acomodacao();
		String getNm_pessoa_fisica();
		String getNr_registro_produto();
		String getDs_plano();
		String getNm_segmentacao();
		String getNm_empresa();
		String getNm_tipo_contratacao();
		String getDs_fator_moderador();
		String getNr_via();
	}
	
	public interface UtilizacaoBenef{
		String getDs_tipo_despesa();
		String getDs_proc();
		BigDecimal getQt_proc();
		BigDecimal getVl_proc();
		Date getDt_proc();
		String getDs_encerra();
		String getDs_prest();
		String getDs_cpf_cnpj_prest();
		String getDs_cbo();
		String getDs_municipio();
	}	
	
	public interface MensalidadeLista{
		Long getNr_sequencia();
		String getDt_mensalidade();
		Date getDt_referencia();
		Long getNr_parcela();
		Date getDt_vencimento();
		String getNm_pagador();
		Long getNr_seq_lote();
		String getNr_titulo();
		String getNr_nota_fiscal();
		BigDecimal getVl_mensalidade();
		BigDecimal getVl_coparticipacao();
		String getDs_situacao();
		BigDecimal getVl_multa_juros();
		BigDecimal getVl_atual();
		String getNr_boleto();
	}
	
	public interface AutorizacaoLista{
		String getcdGuia();
		Date getdtSolicitacao();
		Date getdtAutorizacao();
		String getNmBeneficiario();
		String getDsProcedimento();
		Long getQtSolicitada();
		Long getAutorizada();
		String getDsObservacao();
		Long getCdProcedimento();
		String getIeTipoGuia();	
		String getdsStatus();
		String getnmMedico();
		String getnmPrestador();		
	}
	
	public interface MensalidadeDetalheLista{
		Long getNr_sequencia();
		String getDs_carteirinha();
		String getNm_segurado();
		BigDecimal getVl_mensalidade();
		BigDecimal getVl_coparticipacao();
		BigDecimal getVl_adicionais();
	}
	
	public interface JurosMultaDados{
		BigDecimal getvl_juros();
		BigDecimal getvl_multa();
	}
		
	public interface BoletoDados{
		String getDs_cedente();
		String getDs_cedente_cnpj();
		String getCd_agencia_conta();
		String getDs_nosso_numero();
		Date getDt_vencimento_atual();
		String getCd_pessoa();
		String getDs_nome_sacado();
		String getDs_sacado();
		String getDs_endereco_sacado();
		String getDs_cidade_sacado();
		Date getDt_emissao();
		BigDecimal getVl_juros_multa();
		BigDecimal getVl_documento();
		BigDecimal getVl_cobrado();
		String getDs_cgc_sacado();
		String getDs_referencia();
		String getNr_bloqueto_editado();
		String getNr_bloqueto_lido();
		String getDs_local();
		String getCd_carteira();
		String getDs_instrucao();
		String getDs_instrucao2();
		String getCd_banco_dig();
	}
	
	public interface Comunicado{
		Long getNr_sequencia();
		String getDs_titulo();
		String getDs_texto();
		Date getDt_criacao();
	}		
	
	public interface CarenciaLista{
		String getDs_carencia();
		String getDt_validade();
	}	
	
	public interface EspecialidadeLista{
		Long getCd_especialidade();
		String getNm_especialidade();
	}
	
	public interface RescisaoLista{
		Date getData();
		String getDsStatus();
	}
	
	public interface ViaAdicionalLista{
		Long getNr_sequencia();
		BigDecimal getVl_via_adicional();
		String getDs_status();
		Long getNr_via_gerada();
		Date getDt_solicitacao();
		Date getDt_inicio_vigencia();
		Date getDt_validade_carteira();
		Long getNr_seq_lote();
		Date getDt_envio();
	}
	
	public interface MotivoViaAdicionalLista{
		Long getNr_sequencia();
		String getDs_motivo();
	}
	
	public interface AnosIR{
		String getDs_ano();
	}
	
	public interface CabecalhoIR{
		String getnm_pessoa_fisica();
		String getnr_cpf();
		String getds_data_atual();
		BigDecimal getvl_janeiro();
		BigDecimal getvl_fevereiro();
		BigDecimal getvl_marco();
		BigDecimal getvl_abril();
		BigDecimal getvl_maio();
		BigDecimal getvl_junho();
		BigDecimal getvl_julho();
		BigDecimal getvl_agosto();
		BigDecimal getvl_setembro();
		BigDecimal getvl_outubro();
		BigDecimal getvl_novembro();
		BigDecimal getvl_dezembro();
	}
	
	public interface DetalheIR{
		String getds_condicao();
		String getnm_pessoa_fisica();
		String getnr_cpf();
		BigDecimal getvl_mensalidade();
	}
	
	public interface SumarioIR{
		BigDecimal getvl_recebido();
		BigDecimal getvl_juros();
		BigDecimal getvl_descontos();
		BigDecimal getvl_multa();
	}
	
	
	public interface AlteracaoDados{
		String getNm_pessoa_fisica();
		Date getDt_nascimento();
		String getIe_estado_civil();
		String getDs_estado_civil();
		String getNr_cpf();
		String getNr_cartao_nac_sus();
		String getDs_email();
		String getNr_identidade();
		Date getDt_emissao_identidade();
		String getSg_emissora_ci();
		String getDs_orgao_emissor_ci();
		String getNr_cartao_estrangeiro();
		String getNr_cep_cidade_nasc();
		String getNm_cidade_nasc();
		String getIe_sexo();
		String getDs_sexo();
		
		String getCd_cep();
		String getNr_endereco();
		String getDs_endereco();
		String getDs_bairro();
		String getCd_municipio_ibge();
		String getDs_cidade();
		String getSg_estado();
		String getDs_uf();
		String getNr_ddi_telefone();
		String getNr_ddd_telefone();
		String getNr_telefone();
		String getNr_telefone_celular();
		String getCd_declaracao_nasc_vivo();
		String getNm_pai();
		String getNm_mae();
	}
	
	public interface ListaAlteracao{
		Long getNr_sequencia();
		Date getDt_atualizacao_nrec();
		String getDs_status();
		String getNm_atributo();
		String getDs_antes();
		String getDs_depois();
		String getDs_status_campo();
	}
	
}
