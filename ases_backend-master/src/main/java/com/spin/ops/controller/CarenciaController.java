package com.spin.ops.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;
import com.spin.ops.model.InterfacesJPA.CarenciaLista;
import com.spin.ops.repository.CarenciaRepository;

@CrossOrigin
@RestController
@RequestMapping
public class CarenciaController {

	@Autowired
	private CarenciaRepository carencias;

	@PersistenceContext
	private EntityManager manager;
	
	@GetMapping("/portal/carencia")
	public List<CarenciaLista> obterDados(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return carencias.buscarDados(ret.getIdSegurado(),  ret.getNrSeqPlano(), ret.getNrSeqContrato(), "N");
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@GetMapping("/portal/cpt")
	public List<CarenciaLista> obterDadosCPT(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return carencias.buscarDados(ret.getIdSegurado(),  ret.getNrSeqPlano(), ret.getNrSeqContrato(), "S");
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
}
