package br.com.sysge.infraestrutura.relatorios;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PDF")
public class PDFServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public PDFServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		byte[] bytes = (byte[]) request.getSession().getAttribute("documentoPDF");
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
