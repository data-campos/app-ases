package com.spin.ops;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.hash.Hashing;

@CrossOrigin
@RestController
@RequestMapping
public class Global{

	public static List<TokenLoginModel> listaTokenLoginModel;
	public static List<TokenRecuperarCadastrarModel> listaTokenRecuperarCadastrarModel = new ArrayList<TokenRecuperarCadastrarModel>();
	public static List<TokenExcluirContaModel> listaTokenExcluirContaModel = new ArrayList<TokenExcluirContaModel>();

	@Data
	public static class TokenLoginModel {
		Long idSegurado;		
		Long cdEstabelecimento;		
		Long nrSeqContrato;
		Long nrSeqPlano;
		Long nrSeqCarteira;
		Long nrSeqPagador;
		String token;
		String cdPessoaFisica;
		String nmSegurado;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TokenRecuperarCadastrarModel {
		String token;
		String email;
		String nrCarteirinha;
		char tpSolicitacao;
		Date dateSolicitacao;
		boolean isUtilizada; //Caso true, quer dizer que o usuário ja trocou de senha usando o código desta solicitação, portanto terá q gerar outro código
		String newPassword; // Caso tpSolicitacao = R, usará essa senha para trocar de senha

		public boolean isExpired(int qtDays) {
			return dateSolicitacao.after(new Date(new Date().getTime() + (qtDays * Utils.day)));
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TokenExcluirContaModel {
		String token;
		String email;
		String nrCarteirinha;
		boolean isUtilizada; //Caso true, quer dizer que o usuário ja trocou de senha usando o código desta solicitação, portanto terá q gerar outro código
		Date dateSolicitacao;

		public boolean isExpired(int qtDays) {
			return dateSolicitacao.after(new Date(new Date().getTime() + (qtDays * Utils.day)));
		}
	}

	public static Long seqBoleto = 0L;
	public static Long seqIR = 0L;

	public class PrestadorListaw{
		String nm_prestador;
		String ds_endereco;
		public String getNm_prestador() {
			return nm_prestador;
		}
		public void setNm_prestador(String nm_prestador) {
			this.nm_prestador = nm_prestador;
		}
		public String getDs_endereco() {
			return ds_endereco;
		}
		public void setDs_endereco(String ds_endereco) {
			this.ds_endereco = ds_endereco;
		}		
	}

	public static String trataMensagemExceptionBadRequest(Exception e) {
		if (e.getMessage() != null) return e.getMessage();

		if (e.getCause() != null && e.getCause().getMessage() != null) return e.getCause().getMessage();

		if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause().getMessage() != null) return e.getCause().getCause().getMessage();

		return "Erro ao processar a requisição";
	}
	
	public static String generateDsTec(String dtNascimento, String cpf) {
		return (Hashing.sha256().hashString(
					   dtNascimento 
					 + cpf.substring(1, 8), StandardCharsets.UTF_8).toString().substring(0, 15));
	}
	
	public static List<Local> listaLocaisPrestadoresComEspecialidade = new ArrayList<Local>();	
	public static List<Local> listaLocaisPrestadoresSemEspecialidade = new ArrayList<Local>();	

	public static class Local {
		String nm_prestador;
		String ds_local;
		String nr_latitude;
		String nr_longitude;
		Long qt_distancia_km;
		String ds_lista_espec;
		String ds_contato;
		String nr_seq_tipo_guia;
		public String getDs_local() {
			return ds_local;
		}
		public void setDs_local(String ds_local) {
			this.ds_local = ds_local;
		}
		public String getNr_latitude() {
			return nr_latitude;
		}
		public void setNr_latitude(String nr_latitude) {
			this.nr_latitude = nr_latitude;
		}
		public String getNr_longitude() {
			return nr_longitude;
		}
		public void setNr_longitude(String nr_longitude) {
			this.nr_longitude = nr_longitude;
		}
		public String getNm_prestador() {
			return nm_prestador;
		}
		public void setNm_prestador(String nm_prestador) {
			this.nm_prestador = nm_prestador;
		}
		public Long getQt_distancia_km() {
			return qt_distancia_km;
		}
		public void setQt_distancia_km(Long qt_distancia_km) {
			this.qt_distancia_km = qt_distancia_km;
		}
		public String getDs_lista_espec() {
			return ds_lista_espec;
		}
		public void setDs_lista_espec(String ds_lista_espec) {
			if (!ds_lista_espec.isEmpty()) {
				if (this.ds_lista_espec == null) {
					this.ds_lista_espec = ds_lista_espec;
				} else {
					this.ds_lista_espec = this.ds_lista_espec.concat(",").concat(ds_lista_espec);
				}
			}
		}
		public String getDs_contato() {
			return ds_contato;
		}
		public void setDs_contato(String ds_contato) {
			this.ds_contato = ds_contato;
		}
		public String getNr_seq_tipo_guia() {
			return nr_seq_tipo_guia;
		}
		public void setNr_seq_tipo_guia(String nr_seq_tipo_guia) {
			this.nr_seq_tipo_guia = nr_seq_tipo_guia;
		}
	}	
	
	public static Local criarLocal(String nmPrestador, String dsEndereco, String dsContato, String nr_seq_tipo_guia) throws JSONException {
		Local novo = new Local();
		novo.setNm_prestador(nmPrestador);
		novo.setDs_local(dsEndereco);
		novo.setQt_distancia_km(0L);
		novo.setDs_contato(dsContato);
		novo.setNr_seq_tipo_guia(nr_seq_tipo_guia);
		
		try 
        {
            String dsUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=".concat(dsEndereco.replace(" ", "+")).
            		concat("&key=AIzaSyBC4gsHpJ85QXh-H2N8ybMIVu4QRNrA1IE");

            URL url = new URL(dsUrl);
            URLConnection conn = url.openConnection();                                                                    
            conn.connect();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            StringBuffer sbLocation = new StringBuffer();

            for (int i=0; i != -1; i = isr.read())
            {   
                sbLocation.append((char)i);
            }
            String getContent = sbLocation.toString().trim();   
            if(getContent.contains("results"))
            {
                String temp = getContent.substring(getContent.indexOf("["));
                JSONArray JSONArrayForAll = new JSONArray(temp);
                novo.setNr_latitude(JSONArrayForAll.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString());
                novo.setNr_longitude(JSONArrayForAll.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString());
            }
        }
		catch (Exception e) { 
			System.out.println(nmPrestador.concat("\n").concat(dsEndereco).concat("\n").concat(e.getMessage()));
		}
		return novo;		
	}	
	
	public static String criarArquivoPdfLocal(ByteArrayInputStream bis, String dsNome) throws IOException {
		String path = Utils.pathToStorage();
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		if (!(listOfFiles == null)) {
			for (File file : listOfFiles) {
			    if ((file.isFile()) && (FilenameUtils.getExtension(file.getName()).equals("pdf"))) {
			    	if ((file.getName().contains("boleto")) || (file.getName().contains("demonstrativo_ir"))){
			    		file.delete();
			    	}
			    }
			}		
		}		
		
		String dsNomeArquivo = dsNome.concat("_").concat(getSeqArquivo(dsNome).concat(".pdf"));
		String dsLocal = path.concat(dsNomeArquivo);
		
		File newFile=new File(dsLocal);
		FileOutputStream fos = new FileOutputStream(newFile);
		int data;
		while((data=bis.read())!=-1){
			char ch = (char)data;
			fos.write(ch);
		}
		fos.flush();
		fos.close();
		
		return dsNomeArquivo;
				
	}

	public Date BuscarDataAtual() throws ParseException {
		
		Date dataHoraAtual = new Date();
		String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHoraAtual);
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		Date dataFormatada = f.parse(data);
		
		return dataFormatada;
	}
	
	public static String getSeqArquivo(String dsNome) {
		if (dsNome.equals("boleto")) {
			seqBoleto = seqBoleto + 1L;
			return seqBoleto.toString();
		}
		if (dsNome.equals("demonstrativo_ir")) {
			seqIR = seqIR + 1L;
			return seqIR.toString();
		}
		return "";
	}
	
	@Scheduled(fixedRate = (2 * Utils.day))
	public static void limpaLista() {
		for (TokenRecuperarCadastrarModel tok : listaTokenRecuperarCadastrarModel) {
			if (tok.isExpired(2)){ //Remover apenas se fazer 2 dias desde que a solicitação foi gerada
				listaTokenRecuperarCadastrarModel.remove(tok);
			}
		}

		for (TokenExcluirContaModel tok : listaTokenExcluirContaModel) {
			if (tok.isExpired(2)){ //Remover apenas se fazer 2 dias desde que a solicitação foi gerada
				listaTokenExcluirContaModel.remove(tok);
			}
		}
	}
	
	public static class TokenLogin {

		public static void setToken(String token, Long idSegurado, String nmSegurado, Long cdEstabelecimento, String cdPessoaFisica,
				Long nrSeqContrato, Long nrSeqPlano, Long nrSeqCarteira, Long nrSeqPagador) {
			if (listaTokenLoginModel == null) {
				listaTokenLoginModel = new ArrayList<TokenLoginModel>();
			}
			boolean achou = false;
			for (TokenLoginModel tok : listaTokenLoginModel) {
				if (tok.getToken().equals(token)) {
					achou = true;
				}
			}
			if (!achou) {
				TokenLoginModel tok = new TokenLoginModel();
				tok.setToken(token);
				tok.setIdSegurado(idSegurado);
				tok.setNmSegurado(nmSegurado);
				tok.setCdEstabelecimento(cdEstabelecimento);
				tok.setNrSeqContrato(nrSeqContrato);
				tok.setNrSeqPlano(nrSeqPlano);
				tok.setCdPessoaFisica(cdPessoaFisica);
				tok.setNrSeqCarteira(nrSeqCarteira);
				tok.setNrSeqPagador(nrSeqPagador);
				
				listaTokenLoginModel.add(tok);
			}
			
		}

		public static TokenLoginModel getRegistroPorToken(String token) {
			try {
				if ((listaTokenLoginModel == null) || (listaTokenLoginModel.size() == 0)) {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
				}
				for (TokenLoginModel tok : listaTokenLoginModel) {
					if (tok.getToken().equals(token)) {
						return tok;
					}
				}
			} catch(RuntimeException e) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
			}
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");	
		}
	}

	public static class TokenExcluirConta {
		public static boolean hashEmUso(String hash) {
			if (listaTokenExcluirContaModel == null) {
				listaTokenExcluirContaModel = new ArrayList<TokenExcluirContaModel>();
			}
			for (TokenExcluirContaModel tok : listaTokenExcluirContaModel) {
				if (tok.token.trim().equalsIgnoreCase(hash.trim())) {
					return true;
				}
			}

			return false;
		}

		public static void removeSolicitacaoPorToken(String token) {
			if (listaTokenExcluirContaModel == null) {
				listaTokenExcluirContaModel = new ArrayList<TokenExcluirContaModel>();
			}

			listaTokenExcluirContaModel.removeIf(tok -> tok.token.trim().equalsIgnoreCase(token.trim()));
		}

		public static String getSolicitacaoNrCarteirinhaPorToken(String token ) {
			if (listaTokenExcluirContaModel == null) {
				listaTokenExcluirContaModel = new ArrayList<TokenExcluirContaModel>();
			}

			String nrCarteirinhaRetorno = "NEncontrado";

			for (TokenExcluirContaModel tok : listaTokenExcluirContaModel) {
				if (tok.token.trim().equalsIgnoreCase(token.trim())) {
					if (tok.isExpired(1)) return "Vencido";

					return tok.nrCarteirinha;
				}
			}

			return nrCarteirinhaRetorno;
		}

		public static String getNrCarteirinhaPorTokenSolicitacao(String token ) {
			if (listaTokenExcluirContaModel == null) {
				listaTokenExcluirContaModel = new ArrayList<TokenExcluirContaModel>();
			}

			String nrCarteirinhaRetorno = "NEncontrado";

			for (TokenExcluirContaModel tok : listaTokenExcluirContaModel) {
				if ((tok.token.trim().equalsIgnoreCase(token.trim()))) {
					if (tok.isExpired(1)) return "Vencido";
					if (tok.isUtilizada) return "Utilizado";

					tok.isUtilizada = true;
					return tok.nrCarteirinha;
				}
			}

			return nrCarteirinhaRetorno;
		}

		public static TokenExcluirContaModel getTokenPorHash(String hash){
			if (listaTokenExcluirContaModel == null) {
				listaTokenExcluirContaModel = new ArrayList<TokenExcluirContaModel>();
			}

			for (TokenExcluirContaModel tok : listaTokenExcluirContaModel) {
				if (tok.token.trim().equalsIgnoreCase(hash.trim())) {
					return tok;
				}
			}

			return null;
		}

		public static void gerarNovaSolicitacao(TokenExcluirContaModel tokenExcluirContaModel) {
			if (tokenExcluirContaModel != null) {
				listaTokenExcluirContaModel.add(tokenExcluirContaModel);
			}
		}
	}

	public static class TokenRecuperarCadastrarConta {
		public static boolean hashEmUso(String hash) {
			if (listaTokenRecuperarCadastrarModel == null) {
				listaTokenRecuperarCadastrarModel = new ArrayList<TokenRecuperarCadastrarModel>();
			}
			for (TokenRecuperarCadastrarModel tok : listaTokenRecuperarCadastrarModel) {
				if (tok.token.trim().equalsIgnoreCase(hash.trim())) {
					return true;
				}
			}

			return false;
		}

		public static void removeSolicitacaoPorToken(String token) {
			if (listaTokenRecuperarCadastrarModel == null) {
				listaTokenRecuperarCadastrarModel = new ArrayList<TokenRecuperarCadastrarModel>();
			}

			listaTokenRecuperarCadastrarModel.removeIf(tok -> tok.token.trim().equalsIgnoreCase(token.trim()));
		}
		
		public static String getNrCarteirinhaPorTokenSolicitacao(String token, char tpSolicitacao ) {
			if (listaTokenRecuperarCadastrarModel == null) {
				listaTokenRecuperarCadastrarModel = new ArrayList<TokenRecuperarCadastrarModel>();
			}
			
			String nrCarteirinhaRetorno = "NEncontrado";
	
			for (TokenRecuperarCadastrarModel tok : listaTokenRecuperarCadastrarModel) {
				if ((tok.token.trim().equalsIgnoreCase(token.trim())) && tok.tpSolicitacao == tpSolicitacao) {
					if (tok.isExpired(1)) return "Vencido";
					if (tok.isUtilizada) return "Utilizado";
	
					tok.isUtilizada = true;
					return tok.nrCarteirinha;
				}
			}
	
			return nrCarteirinhaRetorno;
		}

		public static void gerarNovaSolicitacaoCadastroRecSenha(TokenRecuperarCadastrarModel tokenCadRecSenha) {
			if (tokenCadRecSenha != null) {
				listaTokenRecuperarCadastrarModel.add(tokenCadRecSenha);
			}						
		}

		public static TokenRecuperarCadastrarModel getSolicitacaoPorNrCarteirinha(String nrCarteirinha, char tpSolicitacao) {
			if (listaTokenRecuperarCadastrarModel == null) {
				listaTokenRecuperarCadastrarModel = new ArrayList<TokenRecuperarCadastrarModel>();
			}
	
			for (TokenRecuperarCadastrarModel tok : listaTokenRecuperarCadastrarModel) {
				if ((tok.nrCarteirinha.trim().equalsIgnoreCase(nrCarteirinha.trim())) && tok.tpSolicitacao == tpSolicitacao) {
					if (!tok.isExpired(1)) {
						return tok;
					}
				}
			}
	
			return null;
		}

		public static TokenRecuperarCadastrarModel getTokenPorHash(String hash){
			if (listaTokenRecuperarCadastrarModel == null) {
				listaTokenRecuperarCadastrarModel = new ArrayList<TokenRecuperarCadastrarModel>();
			}
	
			for (TokenRecuperarCadastrarModel tok : listaTokenRecuperarCadastrarModel) {
				if (tok.token.trim().equalsIgnoreCase(hash.trim())) {
					return tok;
				}
			}
	
			return null;
		}
	}
	
}
