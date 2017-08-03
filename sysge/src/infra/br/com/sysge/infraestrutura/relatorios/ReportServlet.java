package br.com.sysge.infraestrutura.relatorios;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

@WebServlet(urlPatterns = {"/ReportServlet"})
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReportServlet() {
        super();
    }
    
    @SuppressWarnings("unchecked")
	protected void processarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JRException{
    	response.setHeader("application/pdf", "Content-Type");  
        response.setContentType("application/pdf");
        
        ServletOutputStream ouputStream = null;
        
		HttpSession session = request.getSession(true);
		Map<String, Object> params = (Map<String, Object>) session.getAttribute("params");
		List<Object> list = (List<Object>) session.getAttribute("list");
		String reportName = session.getAttribute("reportName").toString();
		
		if(list instanceof List<?>){
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("br/com/sysge/relatorios/" + reportName));
			jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			
			JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(list));
			try {
				byte[] bytes = JasperExportManager.exportReportToPdf((JasperPrint) print);
				response.setContentLength(bytes.length);                   
				ouputStream = response.getOutputStream();  
				ouputStream.write(bytes, 0, bytes.length);               
				ouputStream.flush();
			} catch (JRException e) {
				e.printStackTrace();
			}finally {
				if (ouputStream != null)
					ouputStream.close();
			}
		}
		
		
		
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processarRequest(request, response);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
