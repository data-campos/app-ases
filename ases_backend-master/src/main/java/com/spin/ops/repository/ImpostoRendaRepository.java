package com.spin.ops.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.AnosIR;
import com.spin.ops.model.InterfacesJPA.CabecalhoIR;
import com.spin.ops.model.InterfacesJPA.DetalheIR;
import com.spin.ops.model.InterfacesJPA.SumarioIR;
import com.spin.ops.model.PlsViaAdicCart;

public interface ImpostoRendaRepository extends JpaRepository<PlsViaAdicCart, Long> {

	@Query(value = 	"select  x.ds_ano \r\n" + 
			"from    (\r\n" + 
			"    select  distinct extract(year from f.dt_emissao) as ds_ano\r\n" + 
			"    from 	tasy.titulo_receber	f,\r\n" + 
			"            tasy.pls_mensalidade g,\r\n" + 
			"            tasy.titulo_receber_liq	d\r\n" + 
			"    where	g.nr_seq_pagador	= :nr_seq_pagador_p \r\n" + 
			"    and	    f.nr_seq_mensalidade	= g.nr_sequencia\r\n" + 
			"    and	    d.nr_titulo		= f.nr_titulo ) x\r\n" + 
			"where   x.ds_ano <> extract(year from sysdate)\r\n" + 
			"order by 1 desc", nativeQuery = true)
	List<AnosIR> obterAnos(@Param("nr_seq_pagador_p") Long nr_seq_pagador_p);
	
	@Query(value = 	"select 	decode(nvl(c.cd_cgc, '0'), '0', tasy.obter_nome_pf_pj(c.cd_pessoa_fisica, null), tasy.obter_nome_pf_pj(null, c.cd_cgc)) nm_pessoa_fisica,\r\n" + 
			"	nvl(c.cd_cgc, tasy.Obter_Cpf_Pessoa_Fisica(c.cd_pessoa_fisica)) nr_cpf,\r\n" + 
			"   tasy.obter_data_extenso(sysdate,'0') as ds_data_atual, "+
			"   tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'JAN') vl_janeiro,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'FEV') vl_fevereiro,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'MAR') vl_marco,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'ABR') vl_abril,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'MAI') vl_maio,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'JUN') vl_junho,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'JUL') vl_julho,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'AGO') vl_agosto,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'SET') vl_setembro,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'OUT') vl_outubro,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'NOV') vl_novembro,\r\n" + 
			"	tasy.hspb_obter_vl_mens_mes(c.nr_sequencia, :ds_ano_p, 'DEZ') vl_dezembro\r\n" + 
			"from 	tasy.pls_contrato		b,\r\n" + 
			"	tasy.pls_contrato_pagador	c\r\n" + 
			"where	c.nr_seq_contrato			= b.nr_sequencia\r\n" + 
			"and	  c.nr_sequencia	 		= :nr_seq_pagador_p ", nativeQuery = true)
	CabecalhoIR obterCabecalho(@Param("nr_seq_pagador_p") Long nr_seq_pagador_p, @Param("ds_ano_p") String ds_ano_p);
	
	@Query(value = 	"select	\r\n" + 
			"	substr(z.ds_condicao,1,17) ds_condicao,\r\n" + 
			"	z.nm_pessoa_fisica,\r\n" + 
			"	z.nr_cpf,\r\n" + 
			"	sum(z.vl_mensalidade) vl_mensalidade\r\n" + 
			"from	(select c.nr_seq_pagador nr_seq_pag,\r\n" + 
			"		'Titular' ds_condicao,\r\n" + 
			"		a.nm_pessoa_fisica,\r\n" + 
			"		a.nr_cpf,\r\n" + 
			"		sum(nvl(d.vl_lancamento,'0')) vl_mensalidade\r\n" + 
			"	from 	tasy.pessoa_fisica		a,\r\n" + 
			"		tasy.pls_mensalidade_segurado	b,\r\n" + 
			"		tasy.pls_segurado		c,\r\n" + 
			"		tasy.titulo_receber		f,\r\n" + 
			"		tasy.pls_mensalidade		g,\r\n" + 
			"		tasy.titulo_receber_liq	e,\r\n" + 
			"		tasy.pls_titulo_rec_liq_mens d\r\n" + 
			"	where	a.cd_pessoa_fisica	= c.cd_pessoa_fisica\r\n" + 
			"	and	b.nr_seq_segurado	= c.nr_sequencia\r\n" + 
			"	and	c.nr_seq_pagador	= :nr_seq_pagador_p\r\n" + 
			"	and	f.nr_seq_mensalidade	= g.nr_sequencia\r\n" + 
			"	and	b.nr_seq_mensalidade	= g.nr_sequencia\r\n" + 
			"	and	e.nr_titulo		= f.nr_titulo\r\n" + 
			"	and	e.nr_sequencia		= d.nr_seq_baixa\r\n" + 
			"	and	e.nr_titulo		= d.nr_titulo\r\n" + 
			"	and	c.nr_sequencia		= d.nr_seq_segurado\r\n" + 
			"	and	c.nr_seq_titular	is null\r\n" + 
			"	and	trunc(:dt_inicial_p,'yy') = trunc(e.dt_recebimento,'yy')\r\n" + 
			"	and  	e.nr_seq_liq_origem is null\r\n" + 
			"	and	d.ie_tipo_lancamento = 'PR'\r\n" + 
			"	and	nvl(e.vl_perdas,0) = 0\r\n" + 
			"	and  	not exists ( 	select 	1\r\n" + 
			"				from   	tasy.titulo_receber_liq x\r\n" + 
			"				where  	x.nr_titulo  		= e.nr_titulo\r\n" + 
			"				and   	x.nr_seq_liq_origem    	= e.nr_sequencia)\r\n" + 
			"	group by	\r\n" + 
			"		a.nm_pessoa_fisica,\r\n" + 
			"		a.nr_cpf,\r\n" + 
			"		c.nr_seq_pagador, \r\n" + 
			"		d.vl_lancamento\r\n" + 
			"	union all\r\n" + 
			"	select 	c.nr_seq_pagador nr_seq_pag,\r\n" + 
			"		f.ds_parentesco ds_condicao,\r\n" + 
			"		a.nm_pessoa_fisica,\r\n" + 
			"		a.nr_cpf,\r\n" + 
			"		sum(nvl(d.vl_lancamento,'0')) vl_mensalidade\r\n" + 
			"	from 	tasy.pessoa_fisica		a,\r\n" + 
			"		tasy.pls_mensalidade_segurado	b,\r\n" + 
			"		tasy.pls_segurado		c,\r\n" + 
			"		tasy.grau_parentesco		f,\r\n" + 
			"		tasy.titulo_receber		g,\r\n" + 
			"		tasy.pls_mensalidade		h,\r\n" + 
			"		tasy.titulo_receber_liq	e,\r\n" + 
			"		tasy.pls_titulo_rec_liq_mens d\r\n" + 
			"	where	a.cd_pessoa_fisica	= c.cd_pessoa_fisica\r\n" + 
			"	and	b.nr_seq_segurado	= c.nr_sequencia\r\n" + 
			"	and	c.nr_seq_parentesco	= f.nr_sequencia\r\n" + 
			"	and	c.nr_seq_pagador	= :nr_seq_pagador_p \r\n" + 
			"	and	g.nr_seq_mensalidade	= h.nr_sequencia\r\n" + 
			"	and	b.nr_seq_mensalidade	= h.nr_sequencia\r\n" + 
			"	and	e.nr_titulo		= g.nr_titulo\r\n" + 
			"	and	e.nr_sequencia		= d.nr_seq_baixa\r\n" + 
			"	and	e.nr_titulo		= d.nr_titulo\r\n" + 
			"	and	c.nr_sequencia		= d.nr_seq_segurado\r\n" + 
			"	and	c.nr_seq_titular 		is not null\r\n" + 
			"	and	d.ie_tipo_lancamento = 'PR'\r\n" + 
			"	and	nvl(e.vl_perdas,0) = 0\r\n" + 
			"	and	trunc(:dt_inicial_p,'yy') = trunc(e.dt_recebimento,'yy')\r\n" + 
			"	and  	e.nr_seq_liq_origem is null\r\n" + 
			"	and  	not exists ( 	select 	1\r\n" + 
			"				from   	tasy.titulo_receber_liq x\r\n" + 
			"				where  	x.nr_titulo  		= e.nr_titulo\r\n" + 
			"				and   	x.nr_seq_liq_origem    	= e.nr_sequencia)\r\n" + 
			"	group by	\r\n" + 
			"		f.ds_parentesco,\r\n" + 
			"		a.nm_pessoa_fisica,\r\n" + 
			"		a.nr_cpf,\r\n" + 
			"		c.nr_seq_pagador,\r\n" + 
			"		d.vl_lancamento) z\r\n" + 
			"group by	\r\n" + 
			"	z.ds_condicao,\r\n" + 
			"	z.nm_pessoa_fisica,\r\n" + 
			"	z.nr_cpf\r\n" + 
			"order by z.nm_pessoa_fisica ", nativeQuery = true)
	List<DetalheIR> obterDetalhe(@Param("nr_seq_pagador_p") Long nr_seq_pagador_p, @Param("dt_inicial_p") Date dt_inicial_p);
	
	@Query(value = 	"select 	nvl(sum(d.vl_recebido),0.00)  vl_recebido, \r\n" +
	        "  nvl(sum(d.vl_juros),0.00)  vl_juros,"+
	        "  nvl(sum(d.vl_multa),0.00)  vl_multa,"+
	        "  nvl(sum(d.vl_descontos),0.00)  vl_descontos "+
			"from 	tasy.titulo_receber			f,\r\n" + 
			"	tasy.pls_mensalidade			g,\r\n" + 
			"	tasy.titulo_receber_liq		d\r\n" + 
			"where	g.nr_seq_pagador	= :nr_seq_pagador_p \r\n" + 
			"and	f.nr_seq_mensalidade	= g.nr_sequencia\r\n" + 
			"and	f.nr_titulo		= d.nr_titulo\r\n" + 
			"and	trunc(:dt_inicial_p,'yy') = trunc(d.dt_recebimento ,'yy') ", nativeQuery = true)
	SumarioIR obterSumario(@Param("nr_seq_pagador_p") Long nr_seq_pagador_p, @Param("dt_inicial_p") Date dt_inicial_p);
	
}
