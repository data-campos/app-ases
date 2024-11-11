package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.CarenciaLista;
import com.spin.ops.model.Usuario;

public interface CarenciaRepository extends JpaRepository<Usuario, Long> {

	@Query(value = 	"select \r\n" + 
			"upper(substr(tasy.obter_descricao_padrao('PLS_TIPO_CARENCIA','DS_CARENCIA',x.nr_seq_tipo_carencia),1,200)) ds_carencia,\r\n" + 
			"'Isento' dt_validade\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_segurado = :nr_seq_segurado_p\r\n" + 
			"and substr(tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia),1,1) = :ie_cpt_p\r\n" + 
			"and (nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0)) < trunc(sysdate,'dd')\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N'\r\n" + 
			"union\r\n" + 
			"select \r\n" + 
			"upper(substr(tasy.obter_descricao_padrao('PLS_TIPO_CARENCIA','DS_CARENCIA',x.nr_seq_tipo_carencia),1,200)) ds_carencia,\r\n" + 
			"to_char((nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0))) dt_validade\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_segurado = :nr_seq_segurado_p\r\n" + 
			"and substr(tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia),1,1) = :ie_cpt_p\r\n" + 
			"and (nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0)) >= trunc(sysdate,'dd')\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N'\r\n" + 
			"union\r\n" + 
			"select \r\n" + 
			"upper(substr(tasy.obter_descricao_padrao('PLS_TIPO_CARENCIA','DS_CARENCIA',x.nr_seq_tipo_carencia),1,255)) ds_carencia,\r\n" + 
			"'Isento' dt_validade\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_contrato = :nr_seq_contrato_p\r\n" + 
			"and substr(tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia),1,1) = :ie_cpt_p\r\n" + 
			"and not exists (select 1\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_segurado = :nr_seq_segurado_p)\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N')\r\n" + 
			"and (nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0)) < trunc(sysdate,'dd')\r\n" + 
			"union\r\n" + 
			"select \r\n" + 
			"upper(substr(tasy.obter_descricao_padrao('PLS_TIPO_CARENCIA','DS_CARENCIA',x.nr_seq_tipo_carencia),1,255)) ds_carencia,\r\n" + 
			"to_char((nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0))) dt_validade\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_contrato = :nr_seq_contrato_p\r\n" + 
			"and substr(tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia),1,1) = :ie_cpt_p\r\n" + 
			"and not exists (select 1\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_segurado = :nr_seq_segurado_p)\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N')\r\n" + 
			"and (nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0)) >= trunc(sysdate,'dd')\r\n" + 
			"union\r\n" + 
			"select  \r\n" + 
			"upper(substr(tasy.obter_descricao_padrao('PLS_TIPO_CARENCIA','DS_CARENCIA',x.nr_seq_tipo_carencia),1,255)) ds_carencia,\r\n" + 
			"'Isento' dt_validade\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where nr_seq_plano = :nr_seq_plano_p\r\n" + 
			"and substr(tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia),1,1) = :ie_cpt_p\r\n" + 
			"and not exists (select 1\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_segurado = :nr_seq_segurado_p)\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N')\r\n" + 
			"and not exists (select 1\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_contrato = :nr_seq_contrato_p)\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N')\r\n" + 
			"and (nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0)) < trunc(sysdate,'dd')\r\n" + 
			"union\r\n" + 
			"select  \r\n" + 
			"upper(substr(tasy.obter_descricao_padrao('PLS_TIPO_CARENCIA','DS_CARENCIA',x.nr_seq_tipo_carencia),1,255)) ds_carencia,\r\n" + 
			"to_char((nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0))) dt_validade\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where nr_seq_plano = :nr_seq_plano_p\r\n" + 
			"and substr(tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia),1,1) = :ie_cpt_p\r\n" + 
			"and not exists (select 1\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_segurado = :nr_seq_segurado_p)\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N')\r\n" + 
			"and not exists (select 1\r\n" + 
			"from tasy.pls_carencia x\r\n" + 
			"where x.nr_seq_contrato = :nr_seq_contrato_p)\r\n" + 
			"--and tasy.pls_obter_se_cpt(x.nr_seq_tipo_carencia) = 'N')\r\n" + 
			"and (nvl(x.dt_inicio_vigencia,to_date(substr(tasy.pls_obter_dados_segurado(:nr_seq_segurado_p,'D'),1,10),'dd/mm/yyyy')) + nvl(x.qt_dias,0)) >= trunc(sysdate,'dd')\r\n" + 
			"order by 1", nativeQuery = true)
	List<CarenciaLista> buscarDados(@Param("nr_seq_segurado_p") Long nr_seq_segurado_p, @Param("nr_seq_plano_p") Long nr_seq_plano_p, @Param("nr_seq_contrato_p") Long nr_seq_contrato_p, @Param("ie_cpt_p") String ie_cpt_p);
	
}
