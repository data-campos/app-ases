package com.spin.ops.controller;

import java.util.List;

import com.spin.ops.model.InterfacesJPA.AutorizacaoLista;
import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;


import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.repository.AutorizacaoDadosRepository;

@CrossOrigin
@RestController
@RequestMapping
public class AutorizacaoController {
	
	@Autowired
	AutorizacaoDadosRepository autorizacaoRepository;
	
	@GetMapping("/autorizacao/buscarautorizacao/{datas}")	
	public List<AutorizacaoLista> retornaAutorizacoes(@RequestHeader (name="Authorization") String token,						
	@PathVariable String datas)throws IOException, ParseException{
		
		String dt_inicio_string = datas.substring(0, datas.indexOf(','));
		String dt_fim_string = datas.substring(datas.indexOf(',') + 1);
		
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		
		Date dt_inicio = formato.parse(dt_inicio_string);
		Date dt_fim = formato.parse(dt_fim_string);
		
		
		//if (filtro.getDt_inicio().after(filtro.getDt_fim())) {
		if (dt_inicio.after(dt_fim)) {			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data inicial não pode ser maior que a data final.");
		}
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
					
			return autorizacaoRepository.retornaAutorizacoes(ret.getIdSegurado(), dt_inicio_string, dt_fim_string);
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
	
	public static class ClasseFiltro{
		Date dt_inicio;
		Date dt_fim;
		String ie_status;
		
		public Date getDt_inicio() {
			return dt_inicio;
		}
		public void setDt_inicio(Date dt_inicio) {
			this.dt_inicio = dt_inicio;
		}
		public Date getDt_fim() {
			return dt_fim;
		}
		public void setDt_fim(Date dt_fim) {
			this.dt_fim = dt_fim;
		}
		
		public String getIe_status() {
			return this.ie_status;
		}
		
		public void setIe_status(String ie_status) {
			this.ie_status = ie_status;
		}
	}
	
	
}

