package com.spin.ops.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;
import com.spin.ops.RelatoriosPDF;
import com.spin.ops.model.InterfacesJPA.AnosIR;
import com.spin.ops.model.InterfacesJPA.CabecalhoIR;
import com.spin.ops.model.InterfacesJPA.DetalheIR;
import com.spin.ops.model.InterfacesJPA.SumarioIR;
import com.spin.ops.repository.ImpostoRendaRepository;

@CrossOrigin
@RestController
@RequestMapping
public class ImpostoRendaController {

	@Autowired
	private ImpostoRendaRepository impos;

	@PersistenceContext
	private EntityManager manager;
	
	@GetMapping("/imposto-renda/listar-anos")
	public List<AnosIR> listar(@RequestHeader (name="Authorization") String token) {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return impos.obterAnos(ret.getNrSeqPagador());
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@Transactional
	@PostMapping("/imposto-renda/{ds_ano}/gerar")
	public Retorno gerarIR(@RequestHeader (name="Authorization") String token,
			@PathVariable(value = "ds_ano") String ds_ano) throws ParseException, IOException {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			Date dataIni = f.parse("01/01/".concat(ds_ano));
			
			manager.createNativeQuery("ALTER SESSION SET NLS_DATE_FORMAT = 'DD/MM/RR'").executeUpdate();
			
			CabecalhoIR cabecalho = impos.obterCabecalho(ret.getNrSeqPagador(), ds_ano);
			List<DetalheIR> listaDetalhe = impos.obterDetalhe(ret.getNrSeqPagador(), dataIni);
			SumarioIR sumario = impos.obterSumario(ret.getNrSeqPagador(), dataIni);
			
			ByteArrayInputStream bis = RelatoriosPDF.IR(ds_ano, cabecalho, listaDetalhe, sumario);
			
			String dsRetorno = Global.criarArquivoPdfLocal(bis, "demonstrativo_ir");
			
			Retorno rett = new Retorno();
			rett.setDs_retorno(dsRetorno);
			return rett;
			        
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}		
		
		
	}	
	
	public class Retorno{
		String ds_retorno;

		public String getDs_retorno() {
			return ds_retorno;
		}

		public void setDs_retorno(String ds_retorno) {
			this.ds_retorno = ds_retorno;
		}
	}	
	
}
