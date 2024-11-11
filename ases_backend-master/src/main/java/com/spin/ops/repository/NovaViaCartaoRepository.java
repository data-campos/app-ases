package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.MotivoViaAdicionalLista;
import com.spin.ops.model.InterfacesJPA.ViaAdicionalLista;
import com.spin.ops.model.PlsViaAdicCart;

public interface NovaViaCartaoRepository extends JpaRepository<PlsViaAdicCart, Long> {

	@Query(value = 	"select  c.nr_sequencia as nr_seq_carteira, \r\n" + 
			"        c.vl_via_adicional,\r\n" + 
			"        decode(lc.ie_situacao,'D','Definitivo','G','Gerado','P','Provisório') as ds_status,\r\n" + 
			"        v.nr_via_gerada,\r\n" + 
			"        v.dt_atualizacao_nrec as dt_solicitacao,\r\n" + 
			"        c.dt_inicio_vigencia,\r\n" + 
			"        c.dt_validade_carteira,\r\n" + 
			"        v.nr_seq_lote,\r\n" + 
			"        lc.dt_envio\r\n" + 
			"from    tasy.pls_segurado_carteira c,\r\n" + 
			"        tasy.pls_via_adic_cart v,\r\n" + 
			"        tasy.pls_via_adic_cart_lote l,\r\n" + 
			"        tasy.pls_lote_carteira lc\r\n" + 
			"where   c.nr_sequencia = v.nr_seq_carteira\r\n" + 
			"and     v.nr_seq_lote	 = l.nr_sequencia\r\n" + 
			"and     lc.nr_seq_lote_via_adic(+) = l.nr_sequencia\r\n" + 
			"and     v.nr_seq_segurado = :nr_seq_segurado_p \r\n" + 
			"order   by 5,4,3", nativeQuery = true)
	List<ViaAdicionalLista> listar(@Param("nr_seq_segurado_p") Long nr_seq_segurado_p);
	
	@Query(value = 	"select	m.nr_sequencia, m.ds_motivo from tasy.pls_motivo_via_adicional m where m.ie_situacao = 'A' order by 2", nativeQuery = true)
	List<MotivoViaAdicionalLista> obterMotivos();

}
