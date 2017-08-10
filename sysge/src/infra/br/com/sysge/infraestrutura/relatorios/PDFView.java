package br.com.sysge.infraestrutura.relatorios;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.service.gestserv.OrdemServicoService;
import br.com.sysge.util.FacesUtil;

@Named
@SessionScoped
public class PDFView implements Serializable{
	
	private static final long serialVersionUID = 3061575851336719141L;
	
	@Inject
	private OrdemServicoService ordemServicoService;
	
	private StreamedContent content;

	public StreamedContent getContent() {
		try {
			if(content != null){
				content.getStream().reset();
			}		
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return content;
	}

	public void setContent(StreamedContent content) {
		this.content = content;
	}
	
	public void gerarNotaRecebimento(OrdemServico ordemServico){
		try {
			content = ordemServicoService.gerarNotaRecebimento(ordemServico);
			//RequestContextUtil.execute("PF('pdfViewNotaRecebimento').show();");
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}finally {
			ordemServico = new OrdemServico();
		}
	}

}
