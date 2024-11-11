package com.spin.ops.controller;

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
import com.spin.ops.model.InterfacesJPA.DetalhePortal;
import com.spin.ops.repository.PortalBeneficiarioRepository;

@CrossOrigin
@RestController
@RequestMapping
public class PortalBeneficiarioController {

	@Autowired
	private PortalBeneficiarioRepository portal;

	@PersistenceContext
	private EntityManager manager;
	
	@GetMapping("/portal/basico")
	public Retorno createAuthenticationToken(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			DetalhePortal detalhe = portal.buscarDados( Global.TokenLogin.getRegistroPorToken(token) .getIdSegurado() );
			byte[] data = portal.buscarImagem(detalhe.getCd_pessoa_fisica());
			
			return new Retorno(detalhe, data);
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
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
	
}
