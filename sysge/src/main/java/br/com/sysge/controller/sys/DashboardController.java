package br.com.sysge.controller.sys;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import br.com.sysge.model.conf.Parametro;
import br.com.sysge.service.conf.ParametroService;

@Named
@ViewScoped
public class DashboardController implements Serializable{

	private static final long serialVersionUID = 248574364508549444L;

	private DashboardModel model;
	
	private Parametro parametro;
	
	@Inject
	private ParametroService parametroService;
	
	@PostConstruct
	public void init(){
		model = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();
	    
	    column1.addWidget("osAbertas");
	    column1.addWidget("produtoEstoqMinimo");
	    column1.addWidget("contasPagarEReceberPorData");
	    
	    model.addColumn(column1);
	    
	    parametro = new Parametro();
	}
	
	public void verificarParametroSistema(){
		for(Parametro p : parametroService.findAll()){
			parametro.setMostrarListagemEstoqueNegativoTelaInicial(p.isMostrarListagemEstoqueNegativoTelaInicial());
			parametro.setMostrarListagemOSTelaInicial(p.isMostrarListagemOSTelaInicial());
			parametro.setMostrarContaPagarReceber(p.isMostrarContaPagarReceber());
		}
	}

	public DashboardModel getModel() {
		return model;
	}

	public void setModel(DashboardModel model) {
		this.model = model;
	}

	public Parametro getParametro() {
		return parametro == null ? new Parametro() : this.parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}
	
	
}
