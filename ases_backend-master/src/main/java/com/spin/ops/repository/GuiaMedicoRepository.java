package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spin.ops.model.InterfacesJPA.EspecialidadeLista;
import com.spin.ops.model.PlsViaAdicCart;

public interface GuiaMedicoRepository extends JpaRepository<PlsViaAdicCart, Long> {

	@Query(value = 	"select  distinct cd_especialidade,\r\n" + 
			"        tasy.obter_desc_espec_medica(cd_especialidade) as nm_especialidade\r\n" + 
			"from    tasy.pls_prestador_med_espec espec\r\n" + 
			"where   ie_guia_medico = 'S'\r\n" + 
			"and     sysdate between nvl(espec.dt_inicio_vigencia,sysdate) and nvl(espec.dt_fim_vigencia,sysdate)\r\n" + 
			"order   by 2", nativeQuery = true)
	List<EspecialidadeLista> obterLista();
	
}
