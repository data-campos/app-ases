package com.spin.ops;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeInter25;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.spin.ops.model.InterfacesJPA.BoletoDados;
import com.spin.ops.model.InterfacesJPA.CabecalhoIR;
import com.spin.ops.model.InterfacesJPA.DetalheIR;
import com.spin.ops.model.InterfacesJPA.SumarioIR;

@CrossOrigin
@RestController
public class RelatoriosPDF extends PdfPageEventHelper {

	PdfTemplate t;

	Image total;

	static Document document;

	private static Float llx, ury;

	private static PdfContentByte canvas;

	private static ColumnText ct;

	private static Font headFont;

	private static Font catFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	private static Font catFontNove = new Font(Font.FontFamily.HELVETICA, 9);
	
	private static Font catFontSemNeg = new Font(Font.FontFamily.HELVETICA, 8);

	private static Font catFontSub = new Font(Font.FontFamily.HELVETICA, 6);

	private static Font catFontTitulo = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	
	private static Font catFontCabecalho = new Font(Font.FontFamily.HELVETICA, 11);
	
	private static Font catFontCabecalhoNeg = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

	private static Font catFontColuna = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);

	private static Font catFontSubTitulo = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);

	private static Font FontCheck = new Font(Font.FontFamily.ZAPFDINGBATS, 14);

	private static PdfPTable table;

	private static String nmTitulo;

	private static String dsEstilo = "Retrato";
	
	
	
	
	// IR
	public static ByteArrayInputStream IR(String dsAno, CabecalhoIR cabecalho, List<DetalheIR> listaDetalhe, SumarioIR sumario) {
		String dsUsuario = "Tasy";
		dsEstilo = "Retrato";

		Rectangle pageSize = new Rectangle(595, 842);
		pageSize.setBorder(Rectangle.NO_BORDER);
		pageSize.setBorderWidth(0f);
		document = new Document(pageSize, 10, 0, 0, 0);		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, out);
			RelatoriosPDF event = new RelatoriosPDF();
			writer.setPageEvent(event);
			document.open();

			adicionarQuebraLinha();
			canvas = writer.getDirectContent();
			ct = new ColumnText(canvas);

			adicionarPagina(false, dsUsuario, true);
			
			String dsSistemaOperacional = System.getProperty("os.name");
			String path = "";
			if (dsSistemaOperacional.toLowerCase().contains("windows")) {
				path = "storage\\";
			} else {
				path = "/home/srvboot/spinops/storage/";
			}
			
			String pathLogo = path.concat("logo_boleto.jpg");
			
			//-------------------------------------------------
			adicionarLogo(pathLogo, 50f, 80f, 2f, 0f, 0f, 0f, 0f);
			adicionarBox("PLANO DE SAÚDE ASES LTDA.",
						new Rectangle(220, 780, 580, 810), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontSubTitulo, null);
			adicionarBox("Campos dos Goytacazes, ".concat(cabecalho.getds_data_atual()),
					new Rectangle(50, 750, 570, 770), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFontNove, null);
			
			//-------------------------------------
			
			adicionarBox("Senhor(a):",
					new Rectangle(30, 735, 80, 755), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox(cabecalho.getnm_pessoa_fisica(),
					new Rectangle(90, 735, 530, 755), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontColuna, null);
			
			//-------------------------------------
			
			adicionarBox("CPF:",
					new Rectangle(30, 720, 80, 740), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox(cabecalho.getnr_cpf(),
					new Rectangle(90, 720, 530, 740), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontColuna, null);
			
			//-------------------------------------
			
			adicionarBox("Ref: Demonstrativo de pagamentos - ".concat(dsAno),
					new Rectangle(30, 680, 400, 700), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontCabecalho, null);
			
			//-------------------------------------
			
			adicionarBox("Prezado Cliente:",
					new Rectangle(30, 640, 230, 660), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontNove, null);

			String dsTexto = "Abaixo segue demonstrativo dos pagamentos efetuados durante o ano calendário de "
					.concat(dsAno).concat("  à Plano de saúde " + 
							"ASES Ltda., inscrita no CNPJ sob o n. 03.638.220/0001-33, e destinados à manutenção do plano privado de assistência " + 
							"saúde individual/familiar:");
			
			adicionarBox(dsTexto,
					new Rectangle(30, 590, 580, 630), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontNove, null);
			
			//-------------------------------------
			
			adicionarBox("TOTAL MENSAL",
					new Rectangle(30, 570, 200, 585), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontColuna, null);
			
			//-------------------------------------
			
			adicionarBox("Competência",
					new Rectangle(30, 555, 150, 570), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			
			adicionarBox("Valor Mensal R$",
					new Rectangle(150, 555, 270, 570), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			
			float yUm = 540; float yDois = 555;
			
			adicionarBox("Janeiro",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_janeiro(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;
			
			adicionarBox("Fevereiro",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_fevereiro(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Março",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_marco(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Abril",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_abril(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Maio",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_maio(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Junho",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_junho(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Julho",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_julho(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Agosto",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_agosto(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Setembro",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_setembro(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Outubro",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_outubro(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Novembro",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_novembro(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			yUm = yUm - 15; yDois = yDois - 15;

			adicionarBox("Dezembro",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);
			adicionarBox(formatarNumerico(cabecalho.getvl_dezembro(), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontNove, null);

			yUm = yUm - 15; yDois = yDois - 15;
			adicionarBox("Valor Total",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontColuna, null);
			adicionarBox(formatarNumerico(sumario.getvl_recebido().add(sumario.getvl_juros().add(sumario.getvl_multa())), "#,###,###,##0.00"),
					new Rectangle(150, yUm, 270, yDois), Rectangle.BOX, Element.ALIGN_CENTER, catFontColuna, null);
			
			//----------------------------------------------
			
			yUm = yUm - 35; yDois = yDois - 35;
			adicionarBox("COMPOSIÇÃO DO GRUPO FAMILIAR",
					new Rectangle(30, yUm, 200, yDois), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontColuna, null);
			
			//-------------------------------------
			
			yUm = yUm - 20; yDois = yDois - 20;
			adicionarBox("Condição",
					new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox("Nome",
					new Rectangle(150, yUm, 350, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox("CPF",
					new Rectangle(350, yUm, 470, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox("Valor / Beneficiário R$",
					new Rectangle(470, yUm, 570, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna	, null);
			
			for (DetalheIR detalhe: listaDetalhe) {
				yUm = yUm - 15; yDois = yDois - 15;
				adicionarBox(detalhe.getds_condicao(),
						new Rectangle(30, yUm, 150, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontNove, null);
				adicionarBox(detalhe.getnm_pessoa_fisica(),
						new Rectangle(150, yUm, 350, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontNove, null);
				adicionarBox(detalhe.getnr_cpf(),
						new Rectangle(350, yUm, 470, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontNove, null);
				adicionarBox(formatarNumerico(detalhe.getvl_mensalidade(), "#,###,###,##0.00").concat(" _"),
						new Rectangle(470, yUm, 570, yDois), Rectangle.BOX, Element.ALIGN_RIGHT, catFontNove	, null);
			}
			
			yUm = yUm - 20; yDois = yDois - 20;
			adicionarBox("Valor Juros e Multa",
					new Rectangle(330, yUm, 460, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox(formatarNumerico(sumario.getvl_juros().add(sumario.getvl_multa()), "#,###,###,##0.00").concat(" _"),
					new Rectangle(460, yUm, 570, yDois), Rectangle.BOX, Element.ALIGN_RIGHT, catFontColuna, null);
			
			yUm = yUm - 15; yDois = yDois - 15;
			adicionarBox("Valor de Desconto",
					new Rectangle(330, yUm, 460, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox(formatarNumerico(sumario.getvl_descontos(), "#,###,###,##0.00").concat(" _"),
					new Rectangle(460, yUm, 570, yDois), Rectangle.BOX, Element.ALIGN_RIGHT, catFontColuna, null);
			
			yUm = yUm - 15; yDois = yDois - 15;
			adicionarBox("Valor Total",
					new Rectangle(330, yUm, 460, yDois), Rectangle.BOX, Element.ALIGN_LEFT, catFontColuna, null);
			adicionarBox(formatarNumerico(sumario.getvl_recebido().add(sumario.getvl_juros().add(sumario.getvl_multa())), "#,###,###,##0.00").concat(" _"),
					new Rectangle(460, yUm, 570, yDois), Rectangle.BOX, Element.ALIGN_RIGHT, catFontColuna, null);
			
			//-----------------------------------------------------
			
			adicionarBox("Plano de Saúde ASES",
					new Rectangle(15, 45, 460, 60), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontNove, null);
			adicionarBox("CNPJ 03.638.220/0001-33",
					new Rectangle(15, 30, 460, 45), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontNove, null);
			adicionarBox("ANS nº 41158-2",
					new Rectangle(15, 15, 460, 30), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontNove, null);

			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());

	}
			
	// Boleto
	public static ByteArrayInputStream boleto(BoletoDados dados) {

		String dsUsuario = "Tasy";
		dsEstilo = "Retrato";
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

		Rectangle pageSize = new Rectangle(595, 842);
		pageSize.setBorder(Rectangle.NO_BORDER);
		pageSize.setBorderWidth(0f);
		document = new Document(pageSize, 10, 0, 0, 0);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, out);
			RelatoriosPDF event = new RelatoriosPDF();
			writer.setPageEvent(event);
			document.open();

			adicionarQuebraLinha();
			canvas = writer.getDirectContent();
			ct = new ColumnText(canvas);

			adicionarPagina(false, dsUsuario, true);
			
			String dsSistemaOperacional = System.getProperty("os.name");
			String path = "";
			if (dsSistemaOperacional.toLowerCase().contains("windows")) {
				path = "storage\\";
			} else {
				path = "/home/srvboot/spinops/storage/";
			}
			
			String pathBB = path.concat("logo_bb.jpg");
			String pathLogo = path.concat("logo_boleto.jpg");

			//-------------------------------------------------
			adicionarLogo(pathLogo, 50f, 80f, 2f, 0f, 0f, 0f, 0f);
			adicionarBox("ASES - Bloqueto Bancário - BB",
						new Rectangle(250, 810, 580, 755), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontTitulo, null);
			adicionarLinha(new Rectangle(585, 780, 10, 780));

			//-------------------------------------------------
			adicionarLogo(pathBB, 50f, 140f, 20f, 0f, 0f, 0f, 0f);
			adicionarBox("Recibo do Sacado",
					new Rectangle(485, 750, 570, 770), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);

			//-------------------------------------------------
			adicionarBox("Cedente",
					new Rectangle(30, 735, 330, 755), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_cedente().concat(" CNPJ: ").concat(dados.getDs_cedente_cnpj()),
					new Rectangle(30, 735, 330, 748), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			
			adicionarBox("Agência / Código do Cedente",
					new Rectangle(330, 735, 430, 755), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getCd_agencia_conta(),
					new Rectangle(330, 735, 425, 748), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Nosso Número",
					new Rectangle(430, 735, 500, 755), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_nosso_numero(),
					new Rectangle(430, 735, 495, 748), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Vencimento",
					new Rectangle(500, 735, 570, 755), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(f.format(dados.getDt_vencimento_atual()),
					new Rectangle(500, 735, 565, 748), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			//-------------------------------------------------
			
			adicionarBox("Sacado",
					new Rectangle(30, 715, 330, 735), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_sacado(),
					new Rectangle(30, 715, 330, 728), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			
			adicionarBox("Data processamento",
					new Rectangle(330, 715, 430, 735), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(f.format(new Date()),
					new Rectangle(330, 715, 425, 728), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Nº do Documento",
					new Rectangle(430, 715, 500, 735), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_nosso_numero(),
					new Rectangle(430, 715, 495, 728), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Valor Documento",
					new Rectangle(500, 715, 570, 735), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(formatarNumerico(dados.getVl_documento(), "#,###,###,##0.00"),
					new Rectangle(500, 715, 565, 728), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			//-------------------------------------------------
			
			adicionarBox("CPF / CNPJ",
					new Rectangle(30, 695, 330, 715), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_cgc_sacado(),
					new Rectangle(30, 695, 330, 708), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			
			adicionarBox("(-) Desconto / Abatimento",
					new Rectangle(330, 695, 430, 715), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("(-) Outras Deduções",
					new Rectangle(430, 695, 500, 715), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("(+) Mora / Multa",
					new Rectangle(500, 695, 570, 715), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(formatarNumerico(dados.getVl_juros_multa(), "#,###,###,##0.00"),
					new Rectangle(500, 695, 565, 708), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			//-------------------------------------------------
			
			adicionarBox("Observações",
					new Rectangle(30, 620, 570, 695), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_referencia(),
					new Rectangle(40, 620, 570, 688), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			adicionarBox("Autenticação Mecânica",
					new Rectangle(30, 600, 570, 620), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFontSub, null);

			//-------------------------------------------------
			
			String linhaTraco = "-";
			for (int i = 0; i < 156; i++) {
				linhaTraco = linhaTraco.concat(" -");
			}
			
			adicionarBox(linhaTraco,
				new Rectangle(10, 570, 605, 585), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontSub, null);
			
			//-------------------------------------------------
			
			adicionarLogo(pathBB, 50f, 140f, 20f, 300f, 500f, 200f, 400f);
			adicionarBox("",
					new Rectangle(172, 530, 175, 555), Rectangle.LEFT, Element.ALIGN_LEFT, catFontCabecalhoNeg, null);
			adicionarBox(dados.getCd_banco_dig(),
					new Rectangle(175, 530, 210, 550), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontCabecalhoNeg, null);
			
			adicionarBox("",
					new Rectangle(212, 530, 215, 555), Rectangle.LEFT, Element.ALIGN_LEFT, catFontCabecalhoNeg, null);
			adicionarBox(dados.getNr_bloqueto_editado(),
					new Rectangle(0, 530, 570, 550), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFontCabecalho, null);
			adicionarLinha(new Rectangle(30, 530, 570, 530));

			//-------------------------------------------------
			
			adicionarBox("Local de Pagamento",
					new Rectangle(30, 510, 460, 530), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_local(),
					new Rectangle(30, 510, 460, 523), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			
			adicionarBox("Vencimento",
					new Rectangle(460, 510, 570, 530), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(f.format(dados.getDt_vencimento_atual()),
					new Rectangle(460, 510, 565, 523), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);

			
			//-------------------------------------------------
			
			adicionarBox("Cedente",
					new Rectangle(30, 490, 460, 510), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_cedente().concat(" CNPJ: ").concat(dados.getDs_cedente_cnpj()),
					new Rectangle(30, 490, 460, 503), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			
			adicionarBox("Agência / Código do Cedente",
					new Rectangle(460, 490, 570, 510), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getCd_agencia_conta(),
					new Rectangle(460, 490, 565, 503), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);			

			//-------------------------------------------------
			
			adicionarBox("Data documento",
					new Rectangle(30, 470, 110, 490), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(f.format(dados.getDt_emissao()),
					new Rectangle(30, 470, 105, 483), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);

			adicionarBox("Nº do Documento",
					new Rectangle(110, 470, 220, 490), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_nosso_numero(),
					new Rectangle(110, 470, 215, 483), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Esp. Docum.",
					new Rectangle(220, 470, 290, 490), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);

			adicionarBox("Aceite",
					new Rectangle(290, 470, 340, 490), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);

			adicionarBox("Data Processamento",
					new Rectangle(340, 470, 460, 490), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(f.format(new Date()),
					new Rectangle(340, 470, 455, 483), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Nosso Número",
					new Rectangle(460, 470, 570, 490), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_nosso_numero(),
					new Rectangle(460, 470, 565, 483), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);			
			
			//-------------------------------------------------
			
			adicionarBox("Uso do Banco",
					new Rectangle(30, 450, 110, 470), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);

			adicionarBox("Carteira",
					new Rectangle(110, 450, 180, 470), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getCd_carteira(),
					new Rectangle(110, 450, 175, 463), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			adicionarBox("Espécie R$",
					new Rectangle(180, 450, 220, 470), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("Quantidade",
					new Rectangle(220, 450, 340, 470), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);

			adicionarBox("Valor",
					new Rectangle(340, 450, 460, 470), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("Valor Documento",
					new Rectangle(460, 450, 570, 470), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(formatarNumerico(dados.getVl_documento(), "#,###,###,##0.00"),
					new Rectangle(460, 450, 565, 463), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFont, null);
			
			//-------------------------------------------------
			
			adicionarBox("Instruções (Todas informações deste bloqueto são de exclusiva responsabilidade do cedente)",
					new Rectangle(30, 350, 460, 450), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			String dsJust = dados.getDs_instrucao2();
			if (!dados.getDs_instrucao().isEmpty()) {
				//dsJust = dsJust.concat("\n").concat(dados.getDs_instrucao());
			}
			adicionarBox(dsJust,
					new Rectangle(40, 350, 455, 438), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontSemNeg, null);
			
			adicionarBox("(-) Desconto",
					new Rectangle(460, 430, 570, 450), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("(-) Outras Deduções",
					new Rectangle(460, 410, 570, 430), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("(+) Mora / Multa",
					new Rectangle(460, 390, 570, 410), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			
			adicionarBox("(+) Outros Acréscimos",
					new Rectangle(460, 370, 570, 390), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);

			adicionarBox("(=) Valor Cobrado",
					new Rectangle(460, 350, 570, 370), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);

			//-------------------------------------------------
			
			adicionarBox("Sacado",
					new Rectangle(30, 280, 570, 350), Rectangle.BOX, Element.ALIGN_LEFT, catFontSub, null);
			adicionarBox(dados.getDs_sacado(),
					new Rectangle(45, 325, 570, 340), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			adicionarBox(dados.getDs_endereco_sacado(),
					new Rectangle(45, 310, 570, 325), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			adicionarBox(dados.getDs_cidade_sacado(),
					new Rectangle(45, 295, 570, 310), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			adicionarBox("CÓD PF/ PJ: ".concat(dados.getCd_pessoa()),
					new Rectangle(45, 280, 570, 295), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFont, null);
			adicionarBox("Autenticação Mecânica - FICHA DE COMPENSAÇÃO",
					new Rectangle(30, 270, 570, 280), Rectangle.NO_BORDER, Element.ALIGN_RIGHT, catFontSub, null);
				
			createBarcode(dados.getNr_bloqueto_lido(), writer);
			
			adicionarBox(linhaTraco,
					new Rectangle(10, 185, 605, 200), Rectangle.NO_BORDER, Element.ALIGN_LEFT, catFontSub, null);
			
			
			document.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());

	}
	
	
	// --------------------- FUNÇÕES DO RELATORIO ---------------------------

	public static void adicionarTitulo(String titulo, int alinhamento) {
		PdfPCell hcell = new PdfPCell(new Phrase(titulo, headFont));
		hcell.setHorizontalAlignment(alinhamento);
		hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		hcell.setBorderWidth(1);
		table.addCell(hcell);
	}

	public static void adicionarTituloSemBorda(String titulo, int alinhamento) {
		PdfPCell hcell = new PdfPCell(new Phrase(titulo, headFont));
		hcell.setHorizontalAlignment(alinhamento);
		hcell.setBackgroundColor(BaseColor.WHITE);
		hcell.setBorder(Rectangle.NO_BORDER);
		table.addCell(hcell);
	}

	public static String formatarDataOuHora(Date parametro, String mascara) {
		if (parametro == null) {
			return null;
		}

		return new SimpleDateFormat(mascara).format(parametro);
	}

	@SuppressWarnings("unlikely-arg-type")
	public static String formatarNumerico(BigDecimal parametro, String mascara) {
		String vlPronto;
		if ((parametro == null) || (parametro.equals(0))) {
			vlPronto = "0,00";
		} else {
			vlPronto = new DecimalFormat(mascara).format(parametro);
			if (vlPronto.contains(".")) {
				String dsParcial;
				if (vlPronto.length() > 4) {
					dsParcial = vlPronto.substring(vlPronto.length() - 4, vlPronto.length());
				} else {
					dsParcial = vlPronto.substring(vlPronto.length() - 3, vlPronto.length());
				}

				if (dsParcial.contains(".")) {
					vlPronto = vlPronto.replace(",", "V");
					vlPronto = vlPronto.replace(".", "P");

					vlPronto = vlPronto.replace("V", ".");
					vlPronto = vlPronto.replace("P", ",");
				}
			}
		}

		return vlPronto;
	}

	public static void adicionarLinhaResultado(String titulo, int alinhamentoHoriz, int alinhamentoVerti,
			float paddingEsquerda, float paddingDireita, boolean pintarLinhaInferior) {

		PdfPCell hcell = new PdfPCell(new Phrase(titulo, headFont));
		hcell.setHorizontalAlignment(alinhamentoHoriz);
		hcell.setVerticalAlignment(alinhamentoVerti);
		hcell.setPaddingLeft(paddingEsquerda);
		hcell.setPaddingRight(paddingDireita);

		if (pintarLinhaInferior) {
			hcell.setBorder(Rectangle.BOTTOM);
		} else {
			hcell.setBorder(Rectangle.NO_BORDER);
		}
		if ((table.getRows().size() % 2) == 0) {
			hcell.setBackgroundColor(new BaseColor(235, 235, 235));
		}

		table.addCell(hcell);
	}

	public static void adicionarLinhaResultado(String titulo, int alinhamentoHoriz, int alinhamentoVerti,
			float paddingEsquerda, float paddingDireita, boolean pintarLinhaInferior, boolean isNegrito) {

		Font tit = new Font(FontFamily.HELVETICA, 8, isNegrito ? Font.BOLD : Font.NORMAL);

		PdfPCell hcell = new PdfPCell(new Phrase(titulo, tit));
		hcell.setHorizontalAlignment(alinhamentoHoriz);
		hcell.setVerticalAlignment(alinhamentoVerti);
		hcell.setPaddingLeft(paddingEsquerda);
		hcell.setPaddingRight(paddingDireita);

		if (pintarLinhaInferior) {
			hcell.setBorder(Rectangle.BOTTOM);
		} else {
			hcell.setBorder(Rectangle.NO_BORDER);
		}
		if ((table.getRows().size() % 2) == 0) {
			hcell.setBackgroundColor(new BaseColor(235, 235, 235));
		}

		table.addCell(hcell);
	}

	public static void adicionarQuebraLinha() {
		try {
			Paragraph preface = new Paragraph();
			preface.add("\n");
			document.add(preface);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void adicionarLinhaTexto(String dsTexto, int alinhamentoHorizontal, boolean isNegrito) {
		try {
			Font tit = headFont;
			if (isNegrito) {
				tit.setStyle(Font.BOLD);
			} else {
				tit.setStyle(Font.NORMAL);
			}

			Paragraph preface = new Paragraph();
			Paragraph preface2 = new Paragraph(dsTexto, tit);
			preface2.setAlignment(alinhamentoHorizontal);
			preface.add(preface2);
			preface.setAlignment(alinhamentoHorizontal);
			document.add(preface);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	public static void adicionarTitulo(String dsTexto) {
		try {
			Font tit = FontFactory.getFont(FontFactory.HELVETICA);
			tit.setSize(10);
			tit.setStyle(Font.UNDERLINE + Font.BOLD);

			Paragraph preface = new Paragraph();
			preface.add(new Paragraph(dsTexto, tit));
			document.addTitle(nmTitulo);
			document.add(preface);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void adicionarBox(String inf, Rectangle rect, int box, int alinhamento, Font font, BaseColor color)
			throws DocumentException {
		rect.setBorder(box);
		rect.setBorderWidth(0.5f);
		rect.setBackgroundColor(color);
		canvas.rectangle(rect);
		
		if (inf == null) {
			inf = "";
		}

		ct.setSimpleColumn(rect);
		Paragraph par = new Paragraph(" ".concat(inf), font);
		par.setAlignment(alinhamento);
		ct.addElement(par);
		ct.go();
	}

	public static void adicionarBox(String inf, Rectangle rect, int box, int alinhamento, Font font, BaseColor color,
			boolean isAjuste) throws DocumentException {
		rect.setBorder(box);
		rect.setBorderWidth(0.5f);
		rect.setBackgroundColor(color);
		canvas.rectangle(rect);

		if (isAjuste) {
			rect.setTop(rect.getTop() + 5);
			rect.setBottom(rect.getBottom() + 5);
		}
		ct.setSimpleColumn(rect);
		Paragraph par = new Paragraph(" ".concat(inf), font);
		par.setAlignment(alinhamento);
		ct.addElement(par);
		ct.go();
	}

	public static void adicionarLinha(Rectangle rect) throws DocumentException {
		rect.setBorder(Rectangle.TOP);
		rect.setBorderWidth(1);
		canvas.rectangle(rect);
	}

	public static void adicionarChecked(Rectangle rect, boolean isChecado) throws DocumentException {
		rect.setBorder(Rectangle.NO_BORDER);
		canvas.rectangle(rect);

		ct.setSimpleColumn(rect);
		Paragraph par = new Paragraph();
		if (isChecado) {
			Chunk chunk1 = new Chunk("8", FontCheck);
			par.add(chunk1);
		} else {
			Chunk chunk1 = new Chunk("o", FontCheck);
			par.add(chunk1);
		}
		par.setAlignment(Element.ALIGN_CENTER);
		ct.addElement(par);
		ct.go();
	}

	public static void adicionarPagina(boolean isNova, String dsUsuario, boolean isRodape) throws DocumentException {
		Rectangle rectContent;

		if (isNova) {
			document.newPage();
			if ("Retrato".equals(dsEstilo)) {
				rectContent = new Rectangle(10, 10, 585, 833);
				llx = 830f;
				ury = 810f;
			} else {
				rectContent = new Rectangle(10, 10, 833, 585);
				llx = 585f;
				ury = 560f;
			}
		}
		if ("Retrato".equals(dsEstilo)) {
			rectContent = new Rectangle(10, 10, 585, 833);
		} else {
			rectContent = new Rectangle(10, 10, 833, 585);
		}

		rectContent.setBorder(Rectangle.BOX);
		rectContent.setBorderWidth(0.5f);
		canvas.rectangle(rectContent);
	}

	public static void pularLinha(String dsUsuario) throws DocumentException {
		if (ury > 100) {
			llx = llx - 25;
			ury = ury - 25;
		} else {
			adicionarPagina(true, dsUsuario, true);
		}
	}

	public static void pularLinhaTabela(String dsUsuario) throws DocumentException {
		if (ury > 100) {
			llx = llx - 15;
			ury = ury - 15;
		} else {
			adicionarPagina(true, dsUsuario, true);
		}
	}

	public static void adicionarLogo(String url, float altura, float largura, float margem, float bottom, float top, float left, float right) throws DocumentException {
		Image logo;
		try {
			logo = Image.getInstance(String.format(url));
			logo.scaleToFit(largura, altura);
			logo.setScaleToFitHeight(true);
			logo.setScaleToFitLineWhenOverflow(true);
			logo.setIndentationLeft(margem);
			if (top > 0) {
				
				Paragraph preface = new Paragraph();
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add("\n");
				preface.add(logo);
				document.add(preface);

			} else {
				try {
					document.add(logo);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void adicionarRodape(String dsUsuario) throws DocumentException {

		Rectangle rect;
		if ("Retrato".equals(dsEstilo)) {
			rect = new Rectangle(200, 35, 380, 10);
		} else {
			rect = new Rectangle(15, 35, 837, 10);
		}

		canvas.rectangle(rect);

		ct.setSimpleColumn(rect);
		String data = formatarDataOuHora(new Date(), "dd/MM/yyyy HH:mm:ss");
		Paragraph par = new Paragraph(" ".concat(dsUsuario).concat(" - ").concat(data), catFont);
		par.setAlignment(Element.ALIGN_CENTER);
		ct.addElement(par);
		ct.go();
	}

	 public static void createBarcode(String text, PdfWriter writer) throws DocumentException {
		 	BarcodeInter25 code25 = new BarcodeInter25();
	        code25.setCode(text);
//	        code25.setChecksumText(true);
	        code25.setBarHeight(50);
	        code25.setAltText(" ");
	        
	        Image codigoBarras = code25.createImageWithBarcode(writer.getDirectContent(), null, null);
	        codigoBarras.setIndentationLeft(20f);
	        //codigoBarras.scaleToFit(100, 100);
	        codigoBarras.setWidthPercentage(150);
	        
	        
	        Paragraph preface = new Paragraph();
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");
			preface.add("\n");			
			preface.add(codigoBarras);
			document.add(preface);
	        
	 }
}
