package br.com.sysge.infraestrutura.relatorios;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportFactory {

	private String reportName;
	private Map<String, Object> params;
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

	public InputStream getReportStream() {

		InputStream input = null;

		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(getClass().getClassLoader().getResourceAsStream("br/com/sysge/relatorios/" + reportName));
			jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(list);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, params, datasource);
			JRExporter exporter = null;
			
			if (tipoRelatorio == TiposRelatorio.PDF)
				exporter = new JRPdfExporter();

			if (tipoRelatorio == TiposRelatorio.HTML) {
				exporter = new JRHtmlExporter();
				exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
			}

			if (tipoRelatorio == TiposRelatorio.XLS)
				exporter = new JRXlsExporter();

			if (tipoRelatorio == TiposRelatorio.CVS)
				exporter = new JRCsvExporter();

			if (tipoRelatorio == TiposRelatorio.TXT)
				exporter = new JRTextExporter();

			if (tipoRelatorio == TiposRelatorio.RTF)
				exporter = new JRRtfExporter();

			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.exportReport();
			
			input = new ByteArrayInputStream(output.toByteArray());
			
		} catch (JRException ex) {
			Logger.getLogger(ReportFactory.class.getName()).log(Level.SEVERE, null, ex);
		}

		return input;

	}

}