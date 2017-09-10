package br.com.sysge.infraestrutura.relatorios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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
	
	private void redirect(String page){
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.redirect(ec.getRequestContextPath() + page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getReportStream() {
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
		}

	}
	
}