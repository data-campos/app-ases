package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.PlsSolicitacaoRescisao;
import com.spin.ops.model.InterfacesJPA.RescisaoLista;

public interface PlsSolicitacaoRescisaoRepository extends JpaRepository<PlsSolicitacaoRescisao, Long> {

	@Query(value = 	"select s.dt_solicitacao as data, "+
					"       substr(tasy.obter_valor_dominio(8259,s.ie_status),1,80) as dsStatus \r\n" +
					" from tasy.pls_solicitacao_rescisao s, tasy.pls_solic_rescisao_benef b "+
					" where s.nr_sequencia = b.nr_seq_solicitacao "+
					" and b.nr_seq_segurado = :nr_seq_segurado_p "+
					" order by 1 desc", nativeQuery = true)
	List<RescisaoLista> obter(@Param("nr_seq_segurado_p") Long nr_seq_segurado_p);

}
