package com.spin.ops.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Utils;
import com.spin.ops.Global.Local;
import com.spin.ops.model.InterfacesJPA.EspecialidadeLista;
import com.spin.ops.repository.GuiaMedicoRepository;

@CrossOrigin
@RestController
@RequestMapping
public class GuiaMedicoController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private GuiaMedicoRepository guias;
	
	@GetMapping("/guia/lista-especialidade")
	public List<EspecialidadeLista> obterListaEspecialidade() throws Exception {
		
		try {
			return guias.obterLista();
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}			
	}
	
	@PostMapping("/guia/listar")
	public List<Local> obterDados(			
			@RequestBody ClasseFiltro filtro) throws Exception {
		
		try {			
			
			List<Local> listaRetorno = new ArrayList<Local>();
			List<Local> listaPrestadorEmUso = new ArrayList<Local>();
			String cd_espec_filtro = Long.toString(filtro.getCd_especialidade());
			String nm_prestador_filtro = filtro.getNm_prestador();
			String nr_seq_tipo_guia_filtro = filtro.getNr_seq_tipo_guia();
			
			boolean filtrarPrestadoresPorEspecialidade = filtro.getCd_especialidade() > 0L;
			
			listaPrestadorEmUso = retornaListaPrestadorEmUso(filtrarPrestadoresPorEspecialidade);
			
			for(Local item : listaPrestadorEmUso) {
				if ((item.getNr_latitude() != null) && (item.getNr_longitude() != null)) {
					DefineDistanciaEntreLocalAtualLocalPrestador(item, filtro);
				}
				
				boolean deveInserir = true;
									
				//Filtro para especialidade do prestador
				if ((deveInserir) && (filtrarPrestadoresPorEspecialidade)){
					if ((item.getDs_lista_espec() == null) || !(("," + item.getDs_lista_espec() + ",").contains("," + cd_espec_filtro + "," ))) {
						deveInserir = false ; 	
					}
				}
				
				//Filtro para nome do prestador
				if ((deveInserir) && (filtro.getNm_prestador() != "")){						
					if ((item.getNm_prestador() == null) || !((Utils.removeAcentos(item.getNm_prestador().toUpperCase())).contains(Utils.removeAcentos(nm_prestador_filtro.toUpperCase())))) {
						deveInserir = false; 
					}
				}

				//Filtro para tipo de plano
				if ((deveInserir) && (filtro.getNr_seq_tipo_guia() != "")){
					if ((item.getNr_seq_tipo_guia() == null) || !(item.getNr_seq_tipo_guia().contains(nr_seq_tipo_guia_filtro))) {
						deveInserir = false;
					}
				}
				
				if (deveInserir) {
					listaRetorno.add(item);
				}
			}
			
						
			return listaRetorno;
		} catch (RuntimeException e) {
			if (e.getCause() == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						Global.trataMensagemExceptionBadRequest(e));
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						Global.trataMensagemExceptionBadRequest(e));
			}
		}
	}
	
	public List<Local> retornaListaPrestadorEmUso(boolean filtrarPorEspecialidade){
		if (filtrarPorEspecialidade) {
			return Global.listaLocaisPrestadoresComEspecialidade;
		}else {
			return Global.listaLocaisPrestadoresSemEspecialidade;					
		}
	}
	
	public void DefineDistanciaEntreLocalAtualLocalPrestador(Local item, ClasseFiltro filtro) {
		item.setQt_distancia_km(distance(
			Double.parseDouble(filtro.getNr_latitude()),
			Double.parseDouble(item.getNr_latitude()),
			Double.parseDouble(filtro.getNr_longitude()),
			Double.parseDouble(item.getNr_longitude())
			)
		);
	}
	
	public static class ClasseFiltro{
		String nr_latitude;
		String nr_longitude;
		String nm_prestador;
		Long qt_quilometros;
		Long cd_especialidade;
		String nr_seq_tipo_guia;

		public String getNr_latitude() {
			return nr_latitude;
		}
		public void setNr_latitude(String nr_latitude) {
			this.nr_latitude = nr_latitude;
		}
		public String getNr_longitude() {
			return nr_longitude;
		}
		public void setNr_longitude(String nr_longitude) {
			this.nr_longitude = nr_longitude;
		}
		public String getNm_prestador() {
			return nm_prestador.trim();
		}
		public void setNm_prestador(String nm_prestador) {
			this.nm_prestador = nm_prestador;
		}
		public Long getQt_quilometros() {
			return qt_quilometros;
		}
		public void setQt_quilometros(Long qt_quilometros) {
			this.qt_quilometros = qt_quilometros;
		}
		public Long getCd_especialidade() {
			return cd_especialidade;
		}
		public void setCd_especialidade(Long cd_especialidade) {
			this.cd_especialidade = cd_especialidade;
		}
		public String getNr_seq_tipo_guia() {
			return nr_seq_tipo_guia;
		}
		public void setNr_seq_tipo_guia(String nr_seq_tipo_guia) {
			this.nr_seq_tipo_guia = nr_seq_tipo_guia;
		}
	}
	
	public static Long distance(double lat1, double lat2, double lon1,
	        double lon2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c; // convert to meters

	    distance = Math.pow(distance, 2);
	    distance =  Math.sqrt(distance);
	    
	    Double dist = distance;
	    return dist.longValue();
	}
	
}

