package br.com.sysge.controller.conf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sysge.model.conf.Parametro;
import br.com.sysge.service.conf.ParametroService;
import br.com.sysge.util.FacesUtil;

@Named
@ViewScoped
public class ParametrosController implements Serializable{

	private static final long serialVersionUID = -3967688189576379494L;
	
	@Inject
	private ParametroService parametroService;
	
	private Parametro parametro;
	
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
		}
	}

	public Parametro getParametro() {
		return parametro == null ? new Parametro() : this.parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}
	
	

}
