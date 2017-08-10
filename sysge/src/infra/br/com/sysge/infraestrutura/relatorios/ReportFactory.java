package br.com.sysge.infraestrutura.relatorios;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportFactory implements Serializable{

	private static final long serialVersionUID = 8776151879565515146L;

	private String reportName;
	
	private Map<String, Object> params;
	
	private InputStream inputStream;
	
	private static final String PDF_PATH_WINDOWS = "C:/SYSGE_WEB/DOCUMENTO.pdf";
	private static final String PDF_PATH_JAVA = "C:\\SYSGE_WEB\\DOCUMENTO.pdf";
	private static final String PDF_PATH_JAVA_IMG = "C:\\SYSGE_WEB\\DOCUMENTO.jpg";
	private static final String DIRETORIO = "C:\\SYSGE_WEB";
	
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
	
	/*private void redirect(String page){
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.redirect(ec.getRequestContextPath() + page);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}*/
	
	public void gerarImagemOS(){
		try {
			byte[] bytes = JasperExportManager.exportReportToPdf((JasperPrint) gerarJasperReport());
			
			PDDocument document = PDDocument.load(bytes);
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			BufferedImage image = pdfRenderer.renderImage(0);
			criarDiretorio();
			ImageIO.write(image, "JPG", new File(PDF_PATH_JAVA_IMG));
			document.close();
		} catch (JRException | IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void gerarPDF(){
		try {
			criarDiretorio();
			exportar();
			Runtime.getRuntime().exec("cmd /c start "+PDF_PATH_WINDOWS);
			File file = new File(PDF_PATH_WINDOWS);
			file.deleteOnExit();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void exportarPDF(){
		criarDiretorio();
		exportar();
	}
	
	private void exportar(){
		try {
			JasperExportManager.exportReportToPdfFile(gerarJasperReport(), PDF_PATH_JAVA);
		} catch (JRException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private JasperPrint gerarJasperReport(){
		try {
			return JasperFillManager.fillReport(getArquivoJRXMLCompilado(), params, new JRBeanCollectionDataSource(list));
		} catch (JRException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void criarDiretorio(){
		File diretorio = new File(DIRETORIO);
		diretorio.mkdir();
	}
	
	private JasperReport getArquivoJRXMLCompilado(){
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("br/com/sysge/relatorios/" + reportName));
			jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			return jasperReport;
		} catch (JRException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public StreamedContent gerarPDFView(String nomeArquivo){
		
		try {
			byte[] bytes = JasperExportManager.exportReportToPdf(gerarJasperReport());
			inputStream = new ByteArrayInputStream(bytes);
			inputStream.read();
			
			return gerar(inputStream, nomeArquivo);
			
		} catch (JRException | IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public StreamedContent gerar(InputStream arquivo, String nomeArquivo){
		return new DefaultStreamedContent(arquivo, "application/pdf" ,nomeArquivo);
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