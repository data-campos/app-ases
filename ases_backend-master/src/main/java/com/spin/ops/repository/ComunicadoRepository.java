package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.Comunicado;
import com.spin.ops.model.PlsComunicExtHistWeb;

public interface ComunicadoRepository extends JpaRepository<PlsComunicExtHistWeb, Long> {

	@Query(value = 	"select    a.nr_sequencia, a.ds_titulo, a.ds_texto, a.dt_criacao \r\n" + 
			"from    tasy.pls_comunic_externa_web    a\r\n" + 
			"where   a.ie_situacao    = 'A'\r\n" + 
			"and     a.dt_liberacao is not null\r\n" + 
			"and     sysdate    between    a.dt_liberacao and (tasy.fim_dia(nvl(a.dt_fim_liberacao, sysdate)))\r\n" + 
			"and    a.ie_tipo_login = 'B' \r\n" + 
			"and    ((a.nr_seq_contrato is null or a.nr_seq_contrato = :nr_seq_contrato_p )\r\n" + 
			"and     not exists     (select  1\r\n" + 
			"            from    tasy.pls_comunic_ext_hist_web y\r\n" + 
			"            where   a.nr_sequencia = y.nr_seq_comunicado\r\n" + 
			"            and     y.cd_pessoa_fisica_resp    = :cd_pessoa_fisica_p ))" , nativeQuery = true)
	List<Comunicado> buscarDados(@Param("nr_seq_contrato_p") Long nr_seq_contrato_p, @Param("cd_pessoa_fisica_p") String cd_pessoa_fisica_p);
	
	
	
}
