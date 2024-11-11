package com.spin.ops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.Carteirinha;
import com.spin.ops.model.InterfacesJPA.CarteirinhaDigital;
import com.spin.ops.model.InterfacesJPA.RetornoDsTec;
import com.spin.ops.model.InterfacesJPA.RetornoLogin;
import com.spin.ops.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = "select  w.DS_TEC,   \r\n" + 
			"       w.ds_senha,         \r\n " +  	
			"		w.nr_seq_segurado         \r\n " + 
			"from   tasy.pls_segurado_web w,   \r\n" + 
			"		tasy.pls_segurado_carteira c   \r\n" +  
			"where  w.nr_seq_segurado = c.nr_seq_segurado  \r\n" +  
			"and    c.cd_usuario_plano = :cd_usuario_plano_p ", nativeQuery = true)
	public RetornoDsTec buscarDsTec(@Param("cd_usuario_plano_p") String cd_usuario_plano_p);
		
	@Query(value = "select  p.nm_pessoa_fisica,   \r\n" + 
			"       p.cd_pessoa_fisica, "+
			"		c.cd_usuario_plano as ds_login, "+
			"		w.ds_senha,\r\n" + 
			"       w.ds_tec, \r\n" +
			"		w.nr_seq_segurado as id_segurado,	  \r\n" + 
			"		nvl(w.cd_estabelecimento, s.cd_estabelecimento) as cd_estabelecimento, \r\n" +
			"		(select 	decode(pcp.cd_pessoa_fisica,null,'PJ','PF') \r\n" +
			"			from 	tasy.pls_contrato_pagador pcp \r\n" +
			"			where 	1 = 1 \r\n" +
			"			and 	pcp.ie_tipo_pagador = 'P' \r\n" +
			"			and 	pcp.nr_seq_contrato = s.nr_seq_contrato \r\n" +
			"		) as ie_tipo_pessoa, \r\n" +
			"		s.nr_seq_contrato, \r\n" + 
			"		s.nr_seq_plano, \r\n" + 
			"		p.nr_cpf, \r\n" + 
			"		p.dt_nascimento, \r\n" +
			"       cp.ds_email, "+
			"       c.nr_sequencia as nr_seq_carteira, "+
			"       s.nr_seq_pagador, "+
			"		pl.ie_tipo_contratacao \r\n" +
			"from    tasy.pls_segurado_web w   \r\n" + 
			"inner join	tasy.pls_segurado_carteira c on (w.nr_seq_segurado = c.nr_seq_segurado)   \r\n" + 
			"inner join	tasy.pls_segurado s on (s.nr_sequencia = c.nr_seq_segurado)    \r\n" +
			"inner join	tasy.pls_plano pl on (pl.nr_sequencia = s.nr_seq_plano)    \r\n" +
			"inner join	tasy.pessoa_fisica p  on (p.cd_pessoa_fisica = s.cd_pessoa_fisica)      \r\n" + 
			"inner join (select c.cd_pessoa_fisica, c.ds_email \r\n" + 
                        "from tasy.compl_pessoa_fisica c \r\n" + 
                       "where c.cd_pessoa_fisica = cd_pessoa_fisica \r\n" + 
                         "and c.ie_tipo_complemento = 1) cp on (p.cd_pessoa_fisica = cp.cd_pessoa_fisica) \r\n" +  
			"where     w.ie_situacao = 'A'  \r\n" + 
			"and     c.cd_usuario_plano = :cd_usuario_plano_p    \r\n" + 
			"and     w.ds_senha = :ds_senha_p  ", nativeQuery = true) //:ds_senha_p
	public RetornoLogin loginPorUsuarioeSenha(@Param("cd_usuario_plano_p") String cd_usuario_plano_p, @Param("ds_senha_p") String ds_senha_p);
	
	@Query(value = "SELECT \r\n" +
			"pf.nm_pessoa_fisica, \r\n" +
			"pf.cd_pessoa_fisica, \r\n" +
			"psc.cd_usuario_plano AS ds_login, \r\n" +
			"psw.ds_senha, \r\n" +
			"psw.ds_tec, \r\n" +
			"psw.nr_seq_segurado AS id_segurado, \r\n" +
			"nvl(psw.cd_estabelecimento, ps.cd_estabelecimento) AS cd_estabelecimento, \r\n" +
			"(select 	decode(pcp.cd_pessoa_fisica,null,'PJ','PF') \r\n" +
			"	from 	tasy.pls_contrato_pagador pcp \r\n" +
			"	where 	1 = 1 \r\n" +
			"	and 	pcp.ie_tipo_pagador = 'P' \r\n" +
			"	and 	pcp.nr_seq_contrato = ps.nr_seq_contrato \r\n" +
			") as ie_tipo_pessoa, \r\n" +
			"ps.nr_seq_contrato, \r\n" +
			"ps.nr_seq_plano, \r\n" +
			"pf.nr_cpf, \r\n" +
			"pf.dt_nascimento, \r\n" +
			"compl_pf.ds_email, \r\n" +
			"psc.nr_sequencia AS nr_seq_carteira, \r\n" +
			"ps.nr_seq_pagador, \r\n" +
			"pl.ie_tipo_contratacao \r\n" +
			"	FROM \r\n" +
			"tasy.pls_segurado_web psw, \r\n" +
			"tasy.pls_segurado_carteira psc, \r\n" +
			"tasy.pls_segurado ps, \r\n" +
			"tasy.pessoa_fisica pf, \r\n" +
			"tasy.pls_plano pl, \r\n" +
			"tasy.COMPL_PESSOA_FISICA compl_pf \r\n" +
			"	WHERE \r\n" +
			"1=1 \r\n" +
			"AND psw.nr_seq_segurado = psc.nr_seq_segurado \r\n" +
			"AND ps.nr_sequencia = psc.nr_seq_segurado \r\n" +
			"AND pf.cd_pessoa_fisica = ps.cd_pessoa_fisica \r\n" +
			"AND compl_pf.cd_pessoa_fisica = pf.cd_pessoa_fisica \r\n" +
			"AND pl.nr_sequencia = ps.nr_seq_plano\r\n" +
			"AND compl_pf.ie_tipo_complemento = 1 \r\n" +
			"AND psw.ie_situacao = :ie_situacao \r\n" +
			"AND psc.cd_usuario_plano = :cd_usuario_plano_p", nativeQuery = true)
	public RetornoLogin loginPorUsuario(@Param("cd_usuario_plano_p") String cd_usuario_plano_p, @Param("ie_situacao") String ie_situacao);
	
	@Query(value = "select  p.nm_pessoa_fisica,   \r\n" + 
			"       p.cd_pessoa_fisica, "+
			"		c.nr_seq_segurado as id_segurado,	  \r\n" + 
			"		p.cd_estabelecimento, \r\n" + 
			"		s.nr_seq_contrato, \r\n" + 
			"		s.nr_seq_plano, \r\n" + 
			"		p.nr_cpf, \r\n" + 
			"		p.dt_nascimento, \r\n" +
			"       cp.ds_email, "+
			"       c.nr_sequencia as nr_seq_carteira, "+
			"       s.nr_seq_pagador, "+
			"       (select count(1) from tasy.pls_segurado_web x where x.nr_seq_segurado = c.nr_seq_segurado) qt_web, "+
			"       nvl((select ie_situacao from tasy.pls_segurado_web x where x.nr_seq_segurado = c.nr_seq_segurado), 'N') ieSituacao "+
			"from   tasy.pls_segurado_carteira c \r\n" + 
           "inner join tasy.pls_segurado s on (s.nr_sequencia = c.nr_seq_segurado) \r\n" + 
           "inner join tasy.pessoa_fisica p on (p.cd_pessoa_fisica = s.cd_pessoa_fisica) \r\n" +  
           "inner join (select c.cd_pessoa_fisica, c.ds_email \r\n" + 
                        "from tasy.compl_pessoa_fisica c \r\n" + 
                       "where c.cd_pessoa_fisica = cd_pessoa_fisica \r\n" + 
                         "and c.ie_tipo_complemento = 1) cp on (p.cd_pessoa_fisica = cp.cd_pessoa_fisica) \r\n" +              
			"where  c.cd_usuario_plano = :cd_usuario_plano_p " , nativeQuery = true)
	public Carteirinha beneficiarioPorCarteirinha(@Param("cd_usuario_plano_p") String cd_usuario_plano_p);	
	
	@Query(value = "select  substr(tasy.pls_obter_carteira_mascara(c.nr_seq_segurado),1,255) as nr_carteirinha,\r\n" + 
			"        to_char(p.dt_nascimento, 'dd/MM/yyyy') as dt_nascimento,\r\n" + 
			"        to_char(s.dt_inclusao_operadora, 'dd/MM/yyyy') as dt_adesao,\r\n" + 
			"        to_char(c.dt_validade_carteira, 'dd/MM/yyyy') as dt_validade_carteira,\r\n" + 
			"        tasy.pls_obter_acomodacoes_plano(s.nr_seq_plano) as ds_acomodacao,\r\n" + 
			"        rpad(p.nm_pessoa_fisica,24,' ') as nm_pessoa_fisica,\r\n" + 
			"        substr(tasy.pls_obter_dados_produto(s.nr_seq_plano,'PA'),1,255) as nr_registro_produto,\r\n" + 
			"        rpad(nvl(pl.nm_fantasia, pl.ds_plano),19,' ') as ds_plano,\r\n" + 
			"        substr(tasy.pls_obter_dados_produto(s.nr_seq_plano,'S'),1,50) as nm_segmentacao,\r\n" + 
			"        tasy.pls_obter_dados_segurado(s.nr_seq_contratante_princ, 'N') as nm_empresa, "+
			"        substr(tasy.obter_valor_dominio(1666,pl.ie_tipo_contratacao),1,80) as nm_tipo_contratacao,\r\n" + 
			"        decode(pl.ie_coparticipacao, 'S', 'SIM', 'NÃO') as ds_fator_moderador,\r\n" + 
			"        c.nr_via_solicitacao as nr_via\r\n" + 
			"from    tasy.pls_segurado_web w,     \r\n" + 
			"		 tasy.pls_segurado_carteira c,     \r\n" + 
			"		 tasy.pls_segurado s,     \r\n" + 
			"		 tasy.pessoa_fisica p,\r\n" + 
			"        tasy.pls_plano pl\r\n" + 
			"where   w.nr_seq_segurado = c.nr_seq_segurado    \r\n" + 
			"and     s.nr_sequencia = c.nr_seq_segurado    \r\n" + 
			"and     p.cd_pessoa_fisica = s.cd_pessoa_fisica  \r\n" + 
			"and     pl.nr_sequencia = s.nr_seq_plano\r\n" + 
			"and     w.ie_situacao = 'A'    \r\n" + 
			"and     c.cd_usuario_plano = :cd_usuario_plano_p ", nativeQuery = true)
	public CarteirinhaDigital obterDadosCarteirinhaDigital(@Param("cd_usuario_plano_p") String cd_usuario_plano_p);
}
