package br.com.sysge.infraestrutura.relatorios;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;*/

public class ReportFactory {

	private String reportName;
	
	private Map<String, Object> params;
	
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

	public void getReportStream() {

		/*try {
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("br/com/sysge/relatorios/" + reportName));
			jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(list));
			
			JasperViewer view = new JasperViewer(print, false);
			view.setVisible(true);
			
		} catch (JRException ex) {
			Logger.getLogger(ReportFactory.class.getName()).log(Level.SEVERE, null, ex);
		}*/

	}
	
}