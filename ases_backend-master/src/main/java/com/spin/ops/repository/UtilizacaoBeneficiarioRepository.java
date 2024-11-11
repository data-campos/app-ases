package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.UtilizacaoBenef;
import com.spin.ops.model.Usuario;

public interface UtilizacaoBeneficiarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = 	"select \r\n" + 
			"                nvl((   select  max(proc.ie_tipo_despesa)\r\n" + 
			"                    from    tasy.pls_conta_proc proc,\r\n" + 
			"                            tasy.w_pls_util_benef_item i\r\n" + 
			"                    where   i.nr_seq_util_benef = w.nr_sequencia\r\n" + 
			"                    and     i.nr_seq_conta = proc.nr_seq_conta\r\n" + 
			"                    and     w.cd_procedimento = proc.cd_procedimento\r\n" + 
			"                    and     nvl(w.ie_origem_proced,0) = nvl(proc.ie_origem_proced,nvl(w.ie_origem_proced,0))\r\n" + 
			"                ),\r\n" + 
			"                (select  max(proc.ie_tipo_despesa)\r\n" + 
			"                    from    tasy.pls_conta_proc proc,\r\n" + 
			"                            tasy.pls_conta conta\r\n" + 
			"                    where   conta.cd_guia_ok = w.cd_guia_ok\r\n" + 
			"                    and     conta.nr_sequencia = proc.nr_seq_conta\r\n" + 
			"                    and     w.cd_procedimento = proc.cd_procedimento\r\n" + 
			"                    and     nvl(w.ie_origem_proced,0) = nvl(proc.ie_origem_proced,nvl(w.ie_origem_proced,0))\r\n" + 
			"                )) as ds_tipo_despesa,\r\n" + 
			"                tasy.pls_obter_desc_procedimento(w.cd_procedimento, w.ie_origem_proced) as ds_proc,\r\n" + 
			"                w.qt_utilizacao as qt_proc,\r\n" + 
			"                w.vl_utilizacao as vl_proc,\r\n" + 
			"                w.dt_realizacao as dt_proc,\r\n" + 
			"                nvl(s1.ds_motivo_saida, s2.ds_motivo_saida) as ds_encerra,\r\n" + 
			"                tasy.pls_obter_dados_prestador(w.nr_seq_prestador, 'N') as ds_prest,\r\n" + 
			"                nvl(tasy.pls_obter_dados_prestador(w.nr_seq_prestador, 'CGC'), tasy.pls_obter_dados_prestador(w.nr_seq_prestador, 'CPF')) as ds_cpf_cnpj_prest,\r\n" + 
			"                w.cd_cbo || ' - ' || c.ds_cbo as ds_cbo,\r\n" + 
			"                m.ds_municipio\r\n" + 
			"            from \r\n" + 
			"                tasy.w_pls_utilizacao_benef w\r\n" + 
			"            join   \r\n" + 
			"                tasy.cbo_saude c on (c.cd_cbo = w.cd_cbo)\r\n" + 
			"            left join \r\n" + 
			"                tasy.sus_municipio m on (m.cd_municipio_ibge = w.cd_municipio_ibge)\r\n" + 
			"            left join \r\n" + 
			"                tasy.pls_motivo_saida_sadt s1 on (s1.nr_sequencia = w.nr_seq_saida_spsadt)\r\n" + 
			"            left join \r\n" + 
			"                tasy.pls_motivo_saida s2 on (s2.nr_sequencia = w.nr_seq_saida_int) "+
			" where w.nr_id_transacao = :nr_id_transacao_p "+
			" order by 5, 2 ", nativeQuery = true)
	List<UtilizacaoBenef> buscarUtilizacaoPorPeriodo(@Param("nr_id_transacao_p") String nr_id_transacao_p);
	
	
	
}
