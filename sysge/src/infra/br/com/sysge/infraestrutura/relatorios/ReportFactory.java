package br.com.sysge.infraestrutura.relatorios;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportFactory {

	private String reportName;
	
	private Map<String, Object> params;
	
	private static final String PDF_PATH_WINDOWS = "C:/IMAGENS_SYSGE/ORDEM_SERVICO.pdf";
	private static final String PDF_PATH_JAVA = "C:\\IMAGENS_SYSGE\\ORDEM_SERVICO.pdf";
	private static final String PDF_PATH_JAVA_IMG = "C:\\IMAGENS_SYSGE\\ORDEM_SERVICO.jpg";
	private static final String DIRETORIO = "C:\\IMAGENS_SYSGE";
	
	@SuppressWarnings("unused")
	private TiposRelatorio tipoRelatorio;
	
	private List<?> list;

	public ReportFactory(String ReportName, Map<String, Object> params, TiposRelatorio tipoRelatorio, List<?> list) {
		this.reportName = ReportName;
		this.params = params;
		this.tipoRelatorio = tipoRelatorio;
		this.list = list;
	}

	public ReportFactory(String ReportName, TiposRelatorio tipoRelatorio) {
		this.reportName = ReportName;
		this.tipoRelatorio = tipoRelatorio;
	}
	
	public ReportFactory(String ReportName, Map<String, Object> params, TiposRelatorio tipoRelatorio) {
		this.reportName = ReportName;
		this.params = params;
		this.tipoRelatorio = tipoRelatorio;
	}
	
	private void redirect(String page){
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.redirect(ec.getRequestContextPath() + page);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void gerarImagemOS(){
		try {
			byte[] bytes = JasperExportManager.exportReportToPdf((JasperPrint) gerarJasperReport());
			
			PDDocument document = PDDocument.load(bytes);
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			BufferedImage image = pdfRenderer.renderImage(0);
			criarDiretorio();
			ImageIO.write(image, "JPG", new File(PDF_PATH_JAVA_IMG));
			
		} catch (JRException | IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void gerarPDF(){
		try {
			criarDiretorio();
			JasperExportManager.exportReportToPdfFile(gerarJasperReport(), PDF_PATH_JAVA);
			Runtime.getRuntime().exec("cmd /c start "+PDF_PATH_WINDOWS);
			File file = new File(PDF_PATH_JAVA);
			file.deleteOnExit();
			
		} catch (JRException | IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private JasperPrint gerarJasperReport(){
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("br/com/sysge/relatorios/" + reportName));
			jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			return JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(list));
		} catch (JRException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void criarDiretorio(){
		File diretorio = new File(DIRETORIO);
		diretorio.mkdir();
	}

	public void getReportStream() {
		gerarPDF();
		
		/**
		 * 1º Colocar essa linha no form da ordem de servico:
		 * 	<h:form id="formGeralServico" target="_blank">
		 * 
		 * 2º Colocar a propriedade ajax="false" no botão que irá executar a ação de gerar o PDF
		 */
		
		/*
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		if(params == null && list == null && reportName == null){
			throw new RuntimeException("Nenhum registro encontrado para "
					+ "geração do relatório, tente novamente!");
		}else{
			session.setAttribute("params", params);
			session.setAttribute("list", list == null ? new ArrayList<>() : list);
			session.setAttribute("reportName", reportName);
			
			redirect("/ReportServlet");
		}*/

	}
	
}