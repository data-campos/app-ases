package com.spin.ops.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.spin.ops.RelatoriosPDF;
import com.spin.ops.model.InterfacesJPA.BoletoDados;
import com.spin.ops.model.InterfacesJPA.JurosMultaDados;
import com.spin.ops.repository.MensalidadeRepository;

@CrossOrigin
@RestController
@RequestMapping
public class BoletoController {

	@Autowired
	private MensalidadeRepository mens;

	@PersistenceContext
	private EntityManager manager;
	
	@GetMapping("/boleto/{nr_titulo}/calcular/{dt_vencimento}")
	public RetornoJurosMulta calcular(@RequestHeader (name="Authorization") String token,
			@PathVariable(value = "nr_titulo") String nr_titulo,
			@PathVariable(value = "dt_vencimento") String dt_vencimento) throws IOException, ParseException {
		
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
			Date dtFiltro = f.parse(dt_vencimento);

			JurosMultaDados dados = mens.buscarValores(nr_titulo, dtFiltro);
	
			RetornoJurosMulta ret = new RetornoJurosMulta();
			ret.setVlJuros(dados.getvl_juros());
			ret.setVlMulta(dados.getvl_multa());
			
			return ret;
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}					
	}
	
			
	@Transactional
	@PostMapping("/boleto/{nr_titulo}/gerar/{dt_vencimento}")
	public Retorno gerarBoleto(@RequestHeader (name="Authorization") String token,
			@PathVariable(value = "nr_titulo") String nr_titulo,
			@PathVariable(value = "dt_vencimento") String dt_vencimento) throws IOException, ParseException {
		
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
			Date dtParametro = f.parse(dt_vencimento);
			
			String sql = "\r\n" + 
					"DECLARE\r\n" + 
					"str_w varchar2(4000);\r\n" + 
					"BEGIN\r\n" + 
					"    execute immediate 'ALTER SESSION SET NLS_DATE_FORMAT = ''DD/MM/RR''';\r\n" + 
					"\r\n" + 
					"    tasy.pls_atualizar_mensalidade_web(?, ?, ?, null, 'TASY', null, str_w);\r\n" + 
					"END;";

			manager.createNativeQuery(sql)
				.setParameter(1, nr_titulo)
				.setParameter(2, dtParametro)
				.setParameter(3, 1L)
				.executeUpdate();

			BoletoDados dados = mens.buscarDadosBoleto(nr_titulo);
	
			ByteArrayInputStream bis = RelatoriosPDF.boleto(dados);
			
			String dsRetorno = Global.criarArquivoPdfLocal(bis, "boleto");
			
			Retorno ret = new Retorno();
			ret.setDs_retorno(dsRetorno);
			return ret;
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
	
	public class RetornoJurosMulta{
		BigDecimal vlJuros;
		BigDecimal vlMulta;

		public BigDecimal getVlJuros() {
			return vlJuros;
		}
		public void setVlJuros(BigDecimal vlJuros) {
			this.vlJuros = vlJuros;
		}
		public BigDecimal getVlMulta() {
			return vlMulta;
		}
		public void setVlMulta(BigDecimal vlMulta) {
			this.vlMulta = vlMulta;
		}
	}
	
	
}
