package com.spin.ops.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import com.spin.ops.model.InterfacesJPA.MotivoViaAdicionalLista;
import com.spin.ops.model.InterfacesJPA.ViaAdicionalLista;
import com.spin.ops.model.PlsViaAdicCart;
import com.spin.ops.model.PlsViaAdicCartLote;
import com.spin.ops.repository.NovaViaCartaoLoteRepository;
import com.spin.ops.repository.NovaViaCartaoRepository;

@CrossOrigin
@RestController
@RequestMapping
public class NovaViaCartaoController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private NovaViaCartaoRepository novas;
	
	@Autowired
	private NovaViaCartaoLoteRepository lotes;

	@GetMapping("/nova-via/listar")
	public Retorno listar(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			Retorno retorno = new Retorno();
			retorno.setListaMotivo(novas.obterMotivos());
			retorno.setListaSolicitacao(novas.listar(ret.getIdSegurado()));
			
			return retorno;
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@PostMapping("/nova-via/{nr_seq_motivo}/solicitar")
	public void ler(@RequestHeader (name="Authorization") String token, @PathVariable Long nr_seq_motivo) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			PlsViaAdicCartLote lote = new PlsViaAdicCartLote();
			lote.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.pls_via_adic_cart_lote_seq.nextval from dual").getSingleResult());
			lote.setNr_seq_contrato(ret.getNrSeqContrato());
			
			lotes.save(lote);
			
			PlsViaAdicCart solic = new PlsViaAdicCart();
			solic.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.pls_via_adic_cart_seq.nextval from dual").getSingleResult());
			solic.setNr_seq_segurado(ret.getIdSegurado());
			solic.setNr_seq_motivo_via(nr_seq_motivo);
			solic.setNr_seq_carteira(ret.getNrSeqCarteira());
			solic.setNr_seq_lote(lote.getNr_sequencia());
			
			novas.save(solic);
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	public class Retorno{
		List<ViaAdicionalLista> listaSolicitacao;
		List<MotivoViaAdicionalLista> listaMotivo;
		public List<ViaAdicionalLista> getListaSolicitacao() {
			return listaSolicitacao;
		}
		public void setListaSolicitacao(List<ViaAdicionalLista> listaSolicitacao) {
			this.listaSolicitacao = listaSolicitacao;
		}
		public List<MotivoViaAdicionalLista> getListaMotivo() {
			return listaMotivo;
		}
		public void setListaMotivo(List<MotivoViaAdicionalLista> listaMotivo) {
			this.listaMotivo = listaMotivo;
		}
	}	
}
