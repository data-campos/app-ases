package com.spin.ops.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;
import com.spin.ops.model.InterfacesJPA.MensalidadeDetalheLista;
import com.spin.ops.model.InterfacesJPA.MensalidadeLista;
import com.spin.ops.repository.MensalidadeRepository;

@CrossOrigin
@RestController
@RequestMapping
public class MensalidadeController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private MensalidadeRepository mensalidades;
	
	@GetMapping("/mensalidade/listar/{ie_sit}")
	public List<MensalidadeLista> consultar(@RequestHeader (name="Authorization") String token, 
			@PathVariable String ie_sit) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return mensalidades.buscarDados(ret.getIdSegurado(), ie_sit.toLowerCase());
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@GetMapping("/mensalidade/{nr_sequencia}/detalhe")
	public List<MensalidadeDetalheLista> consultarDetalhe(@RequestHeader (name="Authorization") String token, 
			@PathVariable Long nr_sequencia) throws Exception {
		
		try {
			return mensalidades.buscarDadosDetalhe(nr_sequencia);
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	
}
