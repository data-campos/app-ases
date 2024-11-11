package com.spin.ops.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.BoletoDados;
import com.spin.ops.model.InterfacesJPA.JurosMultaDados;
import com.spin.ops.model.InterfacesJPA.MensalidadeDetalheLista;
import com.spin.ops.model.InterfacesJPA.MensalidadeLista;
import com.spin.ops.model.Usuario;

public interface MensalidadeRepository extends JpaRepository<Usuario, Long> {

	@Query(value = 	"select  me.nr_sequencia, to_char(me.dt_referencia,'MM/yyyy') as dt_mensalidade, me.dt_referencia,\r\n" + 
			"                    me.nr_parcela,\r\n" + 
			"                    t.dt_vencimento,\r\n" + 
			"                    tasy.pls_obter_dados_pagador(me.nr_seq_pagador, 'N') as nm_pagador,\r\n" + 
			"                    me.nr_seq_lote,\r\n" + 
			"                    t.nr_titulo,\r\n" + 
			"                    t.nr_nota_fiscal,\r\n" + 
			"                    t.vl_titulo vl_mensalidade,\r\n" + 
			"                    me.vl_coparticipacao,\r\n" + 
			"                    substr(tasy.obter_valor_dominio(710,t.ie_situacao),1,80) as ds_situacao,\r\n" + 
			"                    nvl(tasy.obter_juros_multa_titulo(t.nr_titulo,nvl(t.dt_liquidacao, sysdate),'R','M'),0) vl_multa_juros,\r\n" + 
			"                    nvl(tasy.obter_juros_multa_titulo(t.nr_titulo,nvl(t.dt_liquidacao, sysdate),'R','M'),0) + nvl(t.vl_saldo_titulo,0) vl_atual,\r\n" + 
			"                    nvl((select nr_bloqueto_lido from tasy.titulo_receber_bloq_v x where x.nr_titulo = t.nr_titulo),'') as nr_boleto\r\n" + 
			"            from    tasy.pls_mensalidade me,\r\n" + 
			"                    tasy.titulo_receber t\r\n" + 
			"            where   t.nr_seq_mensalidade = me.nr_sequencia\r\n" + 
			"            and     me.dt_referencia > trunc(sysdate) - 180 "+
			"            and     exists (select  1\r\n" + 
			"                            from    tasy.pls_mensalidade_segurado m,\r\n" + 
			"                                    tasy.pls_segurado s\r\n" + 
			"                            where   s.nr_sequencia    = m.nr_seq_segurado\r\n" + 
			"                            and     m.nr_seq_mensalidade = me.nr_sequencia\r\n" + 
			"                            and     m.nr_seq_segurado = :nr_seq_segurado_p )\r\n" + 
			"            and     (( :ie_sit_p = 't') or ((:ie_sit_p = 'a') and (nvl(t.vl_saldo_titulo,0) > 0)) or ((:ie_sit_p = 'l') and (nvl(t.vl_saldo_titulo,0) = 0)))\r\n" + 
			"            and     me.ie_cancelamento is null\r\n" + 
			"            order   by me.dt_referencia desc", nativeQuery = true)
	List<MensalidadeLista> buscarDados(@Param("nr_seq_segurado_p") Long nr_seq_segurado_p, @Param("ie_sit_p") String ie_sit_p);
	
	@Query(value = 	"select  m.nr_sequencia,\r\n" + 
			"    c.cd_usuario_plano as ds_carteirinha,\r\n" + 
			"    tasy.pls_obter_dados_segurado(m.nr_seq_segurado,'N') nm_segurado,\r\n" + 
			"    m.vl_mensalidade,\r\n" + 
			"    m.vl_coparticipacao,\r\n" + 
			"    m.vl_adicionais\r\n" + 
			"from tasy.pls_mensalidade_segurado m,\r\n" + 
			"tasy.pls_segurado_carteira c\r\n" + 
			"where m.nr_seq_mensalidade = :nr_seq_mensalidade_p\r\n" + 
			"and m.nr_seq_segurado = c.nr_seq_segurado\r\n" + 
			"order by 3 ", nativeQuery = true)
	List<MensalidadeDetalheLista> buscarDadosDetalhe(@Param("nr_seq_mensalidade_p") Long nr_seq_mensalidade_p);
	
	@Query(value = 	"select  'Plano de Saúde Ases LTDA' as ds_cedente,\r\n" +
			"		 '03.638.220/0001-33' as ds_cedente_cnpj,\r\n" +
			"        a.cd_agencia_conta,\r\n" + 
			"        nvl(a.nr_nota_fiscal, a.nr_titulo) ds_nosso_numero,\r\n" + 
			"        a.dt_vencimento_atual,\r\n" + 
			"        a.cd_pessoa,\r\n" + 
			"        a.ds_nome_sacado,\r\n" + 
			"        a.ds_sacado,\r\n" + 
			"        a.ds_endereco_sacado, "+
			"        a.ds_cidade_sacado,\r\n" + 
			"        a.dt_Emissao,\r\n" + 
			"    	(a.vl_saldo_juros + a.vl_saldo_multa) vl_juros_multa,\r\n" + 
			"    	(a.vl_saldo_juros + a.vl_saldo_multa + a.vl_saldo_titulo) vl_documento,\r\n" + 
			"    	(vl_saldo_titulo + tasy.obter_juros_multa_titulo(a.nr_titulo,a.dt_pagamento_previsto,'R','A')) vl_cobrado,\r\n" + 
			"        nvl(a.CD_CGC, tasy.obter_cpf_pessoa_fisica(a.CD_PESSOA_FISICA)) ds_cgc_sacado,\r\n" + 
			"        'Documento original: ' || to_char(c.dt_referencia,'mm/yy') ds_referencia,\r\n" + 
			"        a.nr_bloqueto_editado,\r\n" + 
			"        a.nr_bloqueto as nr_bloqueto_lido,\r\n" + 
			"        b.ds_local_pag_bloqueto as ds_local,\r\n" +
			"        a.cd_carteira,\r\n" + 
			"        a.ds_instrucao,\r\n" + 
			"        'Valido para pagamento até ' || TO_CHAR(a.dt_pagamento_previsto,'dd/mm/yyyy') || CHR(13) || \r\n" + 
			"		'Boleto reemitido com data de vencimento e valor atualizados ' || CHR(13) ||\r\n" + 
			"		'(Valor Original + Encargos)' || CHR(13) ||\r\n" + 
			"		'Vencimento original '|| TO_CHAR(a.dt_vencimento,'dd/mm/yyyy') || CHR(13) || \r\n" + 
			"		'Valor original...: '|| REPLACE(REPLACE(REPLACE(TO_CHAR(NVL(a.vl_saldo_titulo,0),'9,999,999,999,990.00'),'.','x'),',','.'),'x',',') || CHR(13) ||\r\n" + 
			"		'Encargos........: ' || REPLACE(REPLACE(REPLACE(TO_CHAR((NVL(a.vl_saldo_juros,0) + NVL(a.vl_saldo_multa,0)),'9,999,999,999,990.00'),'.','x'),',','.'),'x',',') ds_instrucao2,        \r\n" +
			"        a.cd_banco_dig "+
			"from  	tasy.pls_mensalidade c,\r\n" + 
			"        tasy.parametro_contas_receber b,\r\n" + 
			"      	tasy.titulo_receber_bloq_v a\r\n" + 
			"where 	a.nr_titulo             = :nr_titulo_p \r\n" + 
			"and 	a.cd_estabelecimento 	= b.cd_estabelecimento\r\n" + 
			"and	c.nr_sequencia		= a.nr_seq_mensalidade(+) ", nativeQuery = true)
	BoletoDados buscarDadosBoleto(@Param("nr_titulo_p") String nr_titulo_p);	
	
	
	@Query(value = 	"select  	tasy.obter_juros_multa_titulo(:nr_titulo_p, :dt_p, 'R', 'J') vl_juros,\r\n" + 
					"        			tasy.obter_juros_multa_titulo(:nr_titulo_p, :dt_p, 'R', 'M') vl_multa\r\n" + 
					"from    dual ", nativeQuery = true)
	JurosMultaDados buscarValores(@Param("nr_titulo_p") String nr_titulo_p, @Param("dt_p") Date dt_p);	
	
}
