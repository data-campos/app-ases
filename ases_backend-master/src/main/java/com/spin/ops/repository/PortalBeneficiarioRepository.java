package com.spin.ops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.DetalhePortal;
import com.spin.ops.model.Usuario;

public interface PortalBeneficiarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = 	"select x.im_pessoa_fisica "+
					" from tasy.pessoa_fisica_foto x where x.cd_pessoa_fisica = :cd_p ", nativeQuery = true)
	byte[] buscarImagem(@Param("cd_p") String cd_p);

	
	@Query(value = "select   \r\n" + 
			"        p.nm_pessoa_fisica,   \r\n" + 
			"        p.cd_pessoa_fisica,   \r\n" + 
			"        nvl(tasy.pls_obter_email_pessoa(s.cd_estabelecimento, p.cd_pessoa_fisica, null, 'TASY'),'Não informado') as ds_email,\r\n" + 
			"        substr(tasy.pls_obter_dados_produto(s.nr_seq_plano,'N'),1,255) as nm_produto,\r\n" + 

			"        p.dt_nascimento,\r\n" + 
			//"        substr(tasy.pls_obter_carteira_mascara(c.nr_seq_segurado),1,255) as nr_carteirinha, \r\n" + 
			"        c.cd_usuario_plano as nr_carteirinha, \r\n" + 
			"        con.nr_contrato,\r\n" + 
			"        s.nr_sequencia as nr_controle_interno,\r\n" + 
			"        substr(tasy.pls_obter_dados_segurado(s.nr_sequencia,'ES'),1,255) as ds_estipulante,\r\n" + 
			"        o.cd_ans as nr_ans,\r\n" + 

			"        p.nr_cartao_nac_sus,\r\n" + 
			"        c.dt_validade_carteira,\r\n" + 
			"        s.dt_contratacao,\r\n" + 
			"        s.dt_inclusao_operadora,\r\n" + 
			"        substr(tasy.pls_obter_nomes_contrato(s.nr_seq_pagador,'P'),1,255) as nm_pagador,\r\n" + 

			"        nvl(pl.nm_fantasia, pl.ds_plano) ds_plano,\r\n" + 
			"        decode(pl.ie_situacao, 'A', 'Ativo', 'I', 'Inativo','S', 'Ativo com comercialização suspensa') as ds_sit_plano,\r\n" + 
			"        substr(tasy.pls_obter_dados_produto(s.nr_seq_plano,'S'),1,255) as nm_segmentacao,\r\n" + 
			"        decode(pl.ie_regulamentacao,'R','Plano não-regulamentado - celebrado até 1º de janeiro de 1999','Plano regulamentado - celebrado após 1º de janeiro de 1999') as nm_regulamentacao,\r\n" + 
			"        pl.cd_codigo_ant as ds_codigo_anterior,\r\n" + 
			"        pl.nr_protocolo_ans as nr_ans_plano,\r\n" + 
			"        substr(tasy.obter_valor_dominio(1666,pl.ie_tipo_contratacao),1,80) as nm_tipo_contratacao,\r\n" + 
			"        decode(pl.ie_acomodacao,'I', 'Individual','C', 'Coletivo', 'Nenhum') as nm_tipo_acomodacao,\r\n" + 
			"        substr(tasy.pls_obter_dados_produto(s.nr_seq_plano,'P'),1,255) as nm_formacao_preco,\r\n" + 
			"        pl.cd_scpa,\r\n" + 
			"        substr(tasy.obter_valor_dominio(1667,pl.ie_abrangencia),1,80) as ds_abrangencia\r\n" + 

			"from    tasy.pls_segurado_web w,     \r\n" + 
			"        tasy.pls_segurado_carteira c,     \r\n" + 
			"        tasy.pls_segurado s,     \r\n" + 
			"        tasy.pessoa_fisica p,\r\n" + 
			"        tasy.pls_contrato con,\r\n" + 
			"        tasy.pls_plano pl,\r\n" + 
			"        tasy.pls_outorgante o\r\n" + 
			"where   w.nr_seq_segurado = c.nr_seq_segurado    \r\n" + 
			"and     s.nr_sequencia = c.nr_seq_segurado    \r\n" + 
			"and     p.cd_pessoa_fisica = s.cd_pessoa_fisica    \r\n" + 
			"and     con.nr_sequencia = s.nr_seq_contrato\r\n" + 
			"and     pl.nr_sequencia = s.nr_seq_plano\r\n" + 
			"and     o.cd_estabelecimento = con.cd_estabelecimento\r\n" + 
			"and     w.nr_seq_segurado = :nr_seq_segurado_p ", nativeQuery = true) 
	public DetalhePortal buscarDados(@Param("nr_seq_segurado_p") Long nr_seq_segurado_p);
	
	

	
}
