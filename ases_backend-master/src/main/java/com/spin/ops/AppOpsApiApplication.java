package com.spin.ops;

import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AppOpsApiApplication extends SpringBootServletInitializer { 

	@PersistenceContext
	private EntityManager manager;

	public Global global;
	
	String cdEspecialidade = "";
	String dsContato = "";	
	
	String idPrestador = "";
	String nmPrestador = "";
	String dsEndereco = "";
	String nrSeqTipoGuia = "";
	
	public Global getGlobal() {
		return global;
	}

	public void setGlobal(Global global) {
		this.global = global;
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppOpsApiApplication.class);
    }

    public static void main(String[] args) throws JSONException {
        SpringApplication.run(AppOpsApiApplication.class, args);
        
    }
	
	@PostConstruct
	  public void init() throws JSONException{
	    TimeZone.setDefault(TimeZone.getTimeZone(TimeZone.getTimeZone("GMT-3").getID()));
	    
		boolean flFlag = true; // TODO: Definir para true em prod
	    
	    if (flFlag) {
		    montaListasPrestadores();	    				
		}	    
	  }
	
	private void montaListasPrestadores(){
		String sql = montaSelectBuscaPrestadores();
		
		@SuppressWarnings("unchecked")
		List<Object[]> listaTodosPrestadores = manager.createNativeQuery(sql).getResultList();

		alimentaListasGlobaisDePrestadores(listaTodosPrestadores);
	}
	
	private String montaSelectBuscaPrestadores() {
		return "select  p.nr_sequencia, \r\n " +
				"     	decode(NVL(length(tasy.pls_obter_dados_prestador(p.nr_sequencia, 'CPF')), length(tasy.pls_obter_dados_prestador(p.nr_sequencia, 'CGC'))), 14, tasy.pls_obter_dados_prestador(p.nr_sequencia, 'NF'), tasy.pls_obter_dados_prestador(p.nr_sequencia, 'N')) nm_prestador,  \r\n " +
				"        tasy.pls_obter_end_prestador(p.nr_sequencia, null, null) ds_endereco,   \r\n " +
				"        espec.cd_especialidade,  \r\n " +
				"		'(' || substr(tasy.pls_obter_tel_prest(P.cd_pessoa_fisica, p.cd_cgc, 'DDT', p.ie_tipo_endereco, p.nr_seq_compl_pj,p.NR_SEQ_COMPL_PF_TEL_ADIC,p.NR_SEQ_TIPO_COMPL_ADIC), 1, 255) || ')' || substr(tasy.pls_obter_tel_prest(P.cd_pessoa_fisica, p.cd_cgc, 'T', p.ie_tipo_endereco, p.nr_seq_compl_pj,p.NR_SEQ_COMPL_PF_TEL_ADIC,p.NR_SEQ_TIPO_COMPL_ADIC), 1, 255) ds_contato, \r\n " +
				"		(SELECT listagg(NR_SEQ_TIPO_GUIA,',') \r\n " +
				" 		 WITHIN group (ORDER BY PTGMP.NR_SEQ_TIPO_GUIA) NR_SEQ_TIPO_GUIA  \r\n " +
				"         FROM tasy.PLS_TIPO_GUIA_MED_PARTIC ptgmp \r\n " +
				"         WHERE ptgmp.NR_SEQ_PRESTADOR = p.NR_SEQUENCIA) NR_SEQ_TIPO_GUIA \r\n " +
				"        from    tasy.pls_prestador p \r\n " +
				"LEFT JOIN tasy.pls_prestador_med_espec espec on ( \r\n " +
				"    espec.nr_seq_prestador = p.nr_sequencia and \r\n " +
				"    espec.ie_guia_medico = 'S' and \r\n " +
				"    sysdate between nvl(espec.dt_inicio_vigencia,sysdate) and nvl(espec.dt_fim_vigencia,sysdate) \r\n " +
				") \r\n " +
				"where   p.ie_situacao = 'A'  \r\n " +
				"and     tasy.pls_obter_end_prestador(p.nr_sequencia, null, null) is not null \r\n " +
				"and     p.ie_guia_medico = 'S' \r\n " +
				"order by 2, 3, 4"
				;
	}
	
	private void alimentaListasGlobaisDePrestadores(List<Object[]> listaTodosPrestadores) {		
		String idUltimo = "";
		String ultimoEnderecoPrestador = "";
		int localizacao = 0;
		for(Object[] item : listaTodosPrestadores) {													
			String[] retornoInsercaoListaPrestador = inserePrestadorLista(item, localizacao, ultimoEnderecoPrestador, idUltimo);
						
			localizacao = Integer.parseInt(retornoInsercaoListaPrestador[0]);
			
			ultimoEnderecoPrestador = retornoInsercaoListaPrestador[1];
			
			idUltimo = retornoInsercaoListaPrestador[2];						
		}	   			
	}
	
	private String[] inserePrestadorLista(Object[] prestador, int localizacao, String ultimoEnderecoPrestador, String idUltimo) {
		populaVariaveisPrestador(prestador);

		if (!idUltimo.equals(idPrestador)) {
			if ((!(dsEndereco == null)) && (!dsEndereco.isEmpty())) {
				try {
					Global.listaLocaisPrestadoresComEspecialidade.add(Global.criarLocal(nmPrestador, dsEndereco, dsContato, nrSeqTipoGuia));
					
					if (!dsEndereco.equals(ultimoEnderecoPrestador)) {
						Global.listaLocaisPrestadoresSemEspecialidade.add(Global.criarLocal(nmPrestador, dsEndereco, dsContato, nrSeqTipoGuia));
						
						ultimoEnderecoPrestador = dsEndereco;
					}									
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				localizacao = Global.listaLocaisPrestadoresComEspecialidade.size();
			}
		}			
			
		Global.listaLocaisPrestadoresComEspecialidade.get(localizacao-1).setDs_lista_espec(cdEspecialidade);
			
		String[] retorno = {Integer.toString(localizacao), ultimoEnderecoPrestador, idUltimo};
		
		return retorno;
	}					
	
	private void populaVariaveisPrestador(Object[] prestador) {
		cdEspecialidade = "";
		dsContato = "";
		nrSeqTipoGuia = "";
		
		idPrestador = prestador[0].toString();
		nmPrestador = prestador[1].toString();
		dsEndereco = prestador[2].toString();

		if (!(prestador[3] == null)) {
			cdEspecialidade = prestador[3].toString();
		}			
		
		if (!(prestador[4] == null)) {
			dsContato = prestador[4].toString();
		}

		if (!(prestador[5] == null)) {
			nrSeqTipoGuia = prestador[5].toString();
		}
			
	}
}
