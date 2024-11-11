package com.spin.ops.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

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
import com.spin.ops.model.InterfacesJPA.Comunicado;
import com.spin.ops.model.PlsComunicExtHistWeb;
import com.spin.ops.repository.ComunicadoRepository;

@CrossOrigin
@RestController
@RequestMapping
public class ComunicadoController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private ComunicadoRepository comunicados;
	
	@GetMapping("/comunicado/listar")
	public List<ComunicadoRet> listar(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			List<Comunicado> lista = comunicados.buscarDados(ret.getNrSeqContrato(), ret.getCdPessoaFisica());
			List<ComunicadoRet> listaPronta = new ArrayList<ComunicadoRet>();
			for (Comunicado item : lista) {
				ComunicadoRet novo = new ComunicadoRet();
				novo.setNr_sequencia(item.getNr_sequencia());
				novo.setDs_titulo(item.getDs_titulo());
				novo.setDt_criacao(item.getDt_criacao());
				novo.setDs_texto(rtfToHtml(item.getDs_texto()));
				
				listaPronta.add(novo);
			}
			
			return listaPronta;
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@GetMapping("/comunicado/possui")
	public boolean consultar(@RequestHeader (name="Authorization") String token) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			return (comunicados.buscarDados(ret.getNrSeqContrato(), ret.getCdPessoaFisica()).size() > 0);
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	@PostMapping("/comunicado/{nr_seq}/ler")
	public void ler(@RequestHeader (name="Authorization") String token, @PathVariable String nr_seq) throws Exception {
		
		try {
			TokenLoginModel ret = Global.TokenLogin.getRegistroPorToken(token);
			
			PlsComunicExtHistWeb leitura = new PlsComunicExtHistWeb();
			leitura.setNr_sequencia((BigDecimal) manager.createNativeQuery("select tasy.pls_comunic_ext_hist_web_seq.nextval from dual").getSingleResult());
			leitura.setCd_pessoa_fisica_resp(ret.getCdPessoaFisica());
			leitura.setNr_seq_comunicado(new BigDecimal(nr_seq));
			
			comunicados.save(leitura);
			
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Global.trataMensagemExceptionBadRequest(e));
		}
	}
	
	public static String rtfToHtml(String rtfText) throws IOException {
		Reader rtf = new StringReader(rtfText);
		JEditorPane p = new JEditorPane();
	    p.setContentType("text/rtf");
	    EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
	    try {
	        kitRtf.read(rtf, p.getDocument(), 0);
	        kitRtf = null;
	        EditorKit kitHtml = p.getEditorKitForContentType("text/html");
	        Writer writer = new StringWriter();
	        kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
	        
	        return writer.toString().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
	    } catch (BadLocationException e) {
	        e.printStackTrace();
	    }
	    return null;
	}	
	
	private static class ComunicadoRet{
		Long nr_sequencia;
		String ds_titulo;
		String ds_texto;
		Date dt_criacao;
		@SuppressWarnings("unused")
		public Long getNr_sequencia() {
			return nr_sequencia;
		}
		public void setNr_sequencia(Long nr_sequencia) {
			this.nr_sequencia = nr_sequencia;
		}
		@SuppressWarnings("unused")
		public String getDs_titulo() {
			return ds_titulo;
		}
		public void setDs_titulo(String ds_titulo) {
			this.ds_titulo = ds_titulo;
		}
		@SuppressWarnings("unused")
		public String getDs_texto() {
			return ds_texto;
		}
		public void setDs_texto(String ds_texto) {
			this.ds_texto = ds_texto;
		}
		@SuppressWarnings("unused")
		public Date getDt_criacao() {
			return dt_criacao;
		}
		public void setDt_criacao(Date dt_criacao) {
			this.dt_criacao = dt_criacao;
		}
	}		
	
}
