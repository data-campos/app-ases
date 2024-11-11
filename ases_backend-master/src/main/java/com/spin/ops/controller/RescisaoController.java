package com.spin.ops.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spin.ops.Global;
import com.spin.ops.Global.TokenLoginModel;
import com.spin.ops.model.InterfacesJPA.RescisaoLista;
import com.spin.ops.model.PlsSolicRescisaoBenef;
import com.spin.ops.model.PlsSolicitacaoRescisao;
import com.spin.ops.repository.PlsSolicRescisaoBenefRepository;
import com.spin.ops.repository.PlsSolicitacaoRescisaoRepository;

@CrossOrigin
@RestController
@RequestMapping
public class RescisaoController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PlsSolicitacaoRescisaoRepository solics;
	
	@Autowired
	private PlsSolicRescisaoBenefRepository solicItens;
	
	@PostMapping("/rescisao/solicitar")
	public void solicitar(
			@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			PlsSolicitacaoRescisao novo = new PlsSolicitacaoRescisao();
			novo.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.pls_solicitacao_rescisao_seq.nextval from dual").getSingleResult());
			novo.setCd_estabelecimento(ret.getCdEstabelecimento());
			novo.setNr_seq_contrato(ret.getNrSeqContrato());
			solics.save(novo);
			
			PlsSolicRescisaoBenef novoItem = new PlsSolicRescisaoBenef();
			novoItem.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.pls_solic_rescisao_benef_seq.nextval from dual").getSingleResult());
			novoItem.setNr_seq_solicitacao(novo.getNr_sequencia());
			novoItem.setNr_seq_segurado(ret.getIdSegurado());
			solicItens.save(novoItem);
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@GetMapping("/rescisao/consultar")
	public List<RescisaoLista> consultar(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return solics.obter(ret.getIdSegurado());
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	
	
}
