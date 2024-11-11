package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.AutorizacaoLista;
import com.spin.ops.model.Usuario;

public interface AutorizacaoDadosRepository extends JpaRepository<Usuario, Long> {
	@Query(value = "select pls.cd_guia cdGuia, \r\n" +
			"			   pls.dt_solicitacao dtSolicitacao, \r\n" +
			"			   pls.dt_autorizacao dtAutorizacao, \r\n" +
			"			   substr(tasy.pls_obter_nome_beneficiario(pls.nr_seq_segurado), 1, 255) nmBeneficiario, \r\n" +
			"			   substr(tasy.obter_descricao_procedimento(pgpc.cd_procedimento, pgpc.ie_origem_proced), 1, 255) dsProcedimento, \r\n" +
			"			   pgpc.qt_solicitada qtSolicitada, \r\n" +
			"			   pgpc.qt_autorizada qtAutorizada, \r\n" +
			"			   nvl(pls.ds_observacao, '  ') dsObservacao, \r\n" +
			"			   pgpc.cd_procedimento cdProcedimento, \r\n" +
			"			   substr(tasy.obter_valor_dominio(1746,pls.ie_tipo_guia),1,255) ieTipoGuia, \r\n" +
			"			   decode(pgpc.ie_status, 'P', 'AUTORIZADO', 'L', 'AUTORIZADO', 'S', 'AUTORIZADO', 'N', 'NEGADO', 'M', 'NEGADO', 'D', 'NEGADO', 'A', 'EM ANÁLISE', 'U', 'EM ANÁLISE', 'N/A') dsStatus, \r\n" +
			"			   substr(tasy.obter_nome_pessoa_fisica(pls.cd_medico_solicitante, null),1,40) nmMedico, \r\n" +
			"			   substr(tasy.pls_obter_dados_prestador(pls.nr_seq_prestador,'N'),1,255) nmPrestador \r\n" +
			"			from tasy.pls_guia_plano pls, \r\n" +
			"			     tasy.pls_guia_plano_proc pgpc \r\n" +
			"			where pgpc.nr_Seq_guia = pls.nr_sequencia \r\n" +
			"			  and pls.nr_seq_segurado = :nr_seq_segurado \r\n" +
			"			  and pls.ie_status = 1 \r\n" +
			"			  and trunc(pls.dt_autorizacao) between to_date(:dt_inicio, 'DD/MM/RRRR') and to_date(:dt_fim, 'DD/MM/RRRR') \r\n" +
			"			group by pls.cd_guia, \r\n" +
			"					   pls.dt_solicitacao, \r\n" +
			"					   pls.dt_autorizacao, \r\n" +
			"					   pls.dt_cancelamento, \r\n" +
			"					   substr(tasy.pls_obter_nome_beneficiario(pls.nr_seq_segurado), 1, 255), \r\n" +
			"					   substr(tasy.obter_descricao_procedimento(pgpc.cd_procedimento, pgpc.ie_origem_proced), 1, 255), \r\n" +
			"					   pgpc.qt_solicitada, \r\n" +
			"					   pgpc.qt_autorizada, \r\n" +
			"					   pls.ds_observacao, \r\n" +
			"					   pgpc.cd_procedimento, \r\n" +
			"					   pls.ie_tipo_guia, \r\n" +
			"					   decode(pgpc.ie_status, 'P', 'AUTORIZADO', 'L', 'AUTORIZADO', 'S', 'AUTORIZADO', 'N', 'NEGADO', 'M', 'NEGADO', 'D', 'NEGADO', 'A', 'EM ANÁLISE', 'U', 'EM ANÁLISE', 'N/A'), \r\n" +
			"					   pls.cd_medico_solicitante, \r\n" +
			"					   pls.nr_seq_prestador \r\n" +
			"            order by  pls.cd_guia", nativeQuery = true)
	public List<AutorizacaoLista> retornaAutorizacoes(@Param("nr_seq_segurado")Long nr_seq_segurado, @Param("dt_inicio") String dt_inicio, @Param("dt_fim") String dt_fim);
}
