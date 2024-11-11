package com.spin.ops.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;
import com.spin.ops.model.InterfacesJPA.DetalhePortal;
import com.spin.ops.model.InterfacesJPA.UtilizacaoBenef;
import com.spin.ops.repository.UtilizacaoBeneficiarioRepository;

@CrossOrigin
@RestController
@RequestMapping
public class UtilizacaoBenefficiarioController {

	@Autowired
	private UtilizacaoBeneficiarioRepository utils;

	@PersistenceContext
	private EntityManager manager;
	
	public List<Agrupamento> listaAgrup; 
	 
	@Transactional
	@PostMapping("/portal/utilizacao")
	public List<Agrupamento> obterDados(
			@RequestHeader (name="Authorization") String token, 
			@RequestBody ClasseFiltro filtro) throws Exception {
		
 		if (filtro.getDt_inicio().after(filtro.getDt_fim())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data inicial não pode ser após a data final.");
		}
		
		TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
		
		try {

			String nrTransacao = "0";
			String nrTransacaoBefore = manager.createNativeQuery("select nvl(max(nr_id_transacao),'0') from tasy.w_pls_utilizacao_benef "+
					" where nr_seq_segurado = ?1 ").setParameter(1, ret.getIdSegurado()).getSingleResult().toString();

			String sql = "\r\n" + 
					"DECLARE\r\n" + 
					"id_w tasy.w_pls_utilizacao_benef.nr_sequencia%type;\r\n" + 
					"BEGIN\r\n" + 
					"    execute immediate 'ALTER SESSION SET NLS_DATE_FORMAT = ''DD/MM/RR''';\r\n" + 
					"\r\n" + 
					"    tasy.pls_gerar_utilizacao_benef( to_number( ? ), ? , ? , 'TASY', to_number( ? ), id_w);\r\n" + 
					"END;";

			
			manager.createNativeQuery(sql)
				.setParameter(1, ret.getIdSegurado().toString())
				.setParameter(2, filtro.getDt_inicio())
				.setParameter(3, filtro.getDt_fim())
				.setParameter(4, ret.getCdEstabelecimento().toString())
				.executeUpdate();
				 
			String nrTransacaoAfter = manager.createNativeQuery("select nvl(max(nr_id_transacao),'0') from tasy.w_pls_utilizacao_benef "+
					" where nr_seq_segurado = ?1 ").setParameter(1, ret.getIdSegurado()).getSingleResult().toString();
			
			if (!nrTransacaoBefore.equals(nrTransacaoAfter)) {
				nrTransacao = nrTransacaoAfter;
			}
			
			listaAgrup = new ArrayList<Agrupamento>();
			List<UtilizacaoBenef> lista = utils.buscarUtilizacaoPorPeriodo(nrTransacao); //chamada.getString(6)
			for (int i = 0; i < lista.size(); i++) {
				Agrupamento agrup = addAgrupamento(lista.get(i).getDs_tipo_despesa());
				agrup.setLista(lista.get(i));
				agrup.setVlTotal(agrup.getVlTotal().add(lista.get(i).getVl_proc()));
				agrup.setQtTotal();
			}
			return listaAgrup;
			
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	public Agrupamento addAgrupamento(String dsAgrupamento) {
		for (int y = 0; y < listaAgrup.size(); y++) {
			if (listaAgrup.get(y).getDsAgrupamento().equals(dsAgrupamento)) {
				return listaAgrup.get(y);
			}
		}
		
		Agrupamento agrup = new Agrupamento();
		agrup.setDsAgrupamento(dsAgrupamento);
		switch(dsAgrupamento) {
			case "1": agrup.setDsAgrupamentoLista("Consultas"); break;
			case "2": agrup.setDsAgrupamentoLista("Outras Despesas"); break;
			case "3": agrup.setDsAgrupamentoLista("Outras Despesas"); break;
			case "4": agrup.setDsAgrupamentoLista("Outras Despesas"); break;
		}
		
		listaAgrup.add(agrup);
		
		return agrup;
	}
	
	public class Retorno{
		
		public Retorno(
				DetalhePortal detalhe,
				byte[] imagem) {
			this.detalhe = detalhe;
			this.imagem = imagem;
		}
				
		DetalhePortal detalhe;
		byte[] imagem;
		
		public DetalhePortal getDetalhe() {
			return detalhe;
		}
		public void setDetalhe(DetalhePortal detalhe) {
			this.detalhe = detalhe;
		}
		public byte[] getImagem() {
			return imagem;
		}
		public void setImagem(byte[] imagem) {
			this.imagem = imagem;
		}
	}
	
	private static class Agrupamento {
		String dsAgrupamento;
		String dsAgrupamentoLista;
		BigDecimal vlTotal;
		Long qtTotal;
		List<UtilizacaoBenef> lista;
		public String getDsAgrupamento() {
			return dsAgrupamento;
		}
		public void setDsAgrupamento(String dsAgrupamento) {
			this.dsAgrupamento = dsAgrupamento;
		}
		public BigDecimal getVlTotal() {
			if (vlTotal == null) {
				return new BigDecimal(0);
			}
			return vlTotal;
		}
		public void setVlTotal(BigDecimal vlTotal) {
			this.vlTotal = vlTotal;
		}
		@SuppressWarnings("unused")
		public List<UtilizacaoBenef> getLista() {
			return lista;
		}
		public void setLista(UtilizacaoBenef util) {
			if (lista == null) {
				lista = new ArrayList<UtilizacaoBenef>();
			}
			this.lista.add(util);
		}
		@SuppressWarnings("unused")
		public String getDsAgrupamentoLista() {
			return dsAgrupamentoLista;
		}
		public void setDsAgrupamentoLista(String dsAgrupamentoLista) {
			this.dsAgrupamentoLista = dsAgrupamentoLista;
		}
		@SuppressWarnings("unused")
		public Long getQtTotal() {
			return qtTotal;
		}
		public void setQtTotal() {
			if (qtTotal == null) { qtTotal = 0L; }
			this.qtTotal = qtTotal + 1L;
		}
		
	}
	
	
	public static class ClasseFiltro{
		Date dt_inicio;
		Date dt_fim;
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
	}
	
}
