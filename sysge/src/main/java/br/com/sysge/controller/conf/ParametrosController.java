package br.com.sysge.controller.conf;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.sysge.model.conf.Parametro;
import br.com.sysge.model.global.UnidadeEmpresarial;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.service.conf.ParametroService;
import br.com.sysge.service.global.UnidadeEmpresarialService;
import br.com.sysge.util.FacesUtil;


@ViewScoped
@ManagedBean
public class ParametrosController implements Serializable{

	private static final long serialVersionUID = -3967688189576379494L;
	
	private ParametroService parametroService = new ParametroService();
	
	private Parametro parametro;
	
	private UnidadeEmpresarialService unidadeEmpresarialService = new UnidadeEmpresarialService();
	
	@PostConstruct
	public void init(){
		parametro = new Parametro();
		setarDadosParametro();
	}
	
	public void salvar(){
		try {
			parametroService.salvar(parametro);
			FacesUtil.mensagemInfo("Parâmetro(s) salvo(s) com sucesso!");
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	private void setarDadosParametro(){
		for(Parametro p : parametroService.findAll()){
			this.parametro.setPermitirQtdeNegativaEstoque(p.isPermitirQtdeNegativaEstoque());
			this.parametro.setMostrarListagemEstoqueNegativoTelaInicial(p.isMostrarListagemEstoqueNegativoTelaInicial());
			this.parametro.setMostrarListagemOSTelaInicial(p.isMostrarListagemOSTelaInicial());
			this.parametro.setUnidadeEmpresarialPadrao(p.getUnidadeEmpresarialPadrao());
			this.parametro.setMostrarContaPagarReceber(p.isMostrarContaPagarReceber());
		}
	}

	public Parametro getParametro() {
		return parametro == null ? new Parametro() : this.parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}
	
	public List<UnidadeEmpresarial> getUnidadesEmpresariais(){
		return unidadeEmpresarialService.findBySituation(Situacao.ATIVO);
	}
	
	public void uploadImage(FileUploadEvent event){
		UploadedFile file = event.getFile();
		
		try {
			Path tempFile = Files.createTempFile(null, null);
			Files.copy(file.getInputstream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
			
			FacesUtil.mensagemInfo("Imagem salva com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.mensagemErro(e.getMessage());
		}
		
	}
	

}
