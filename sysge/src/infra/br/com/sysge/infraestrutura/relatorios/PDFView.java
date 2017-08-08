package br.com.sysge.infraestrutura.relatorios;

import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class PDFView {
	
	private StreamedContent content;

	public StreamedContent getContent() {
		return content;
	}

	public void gerar(InputStream arquivo, String nomeArquivo){
		content = new DefaultStreamedContent(arquivo, nomeArquivo);
	}
	
	public void setContent(StreamedContent content) {
		this.content = content;
	}

}
