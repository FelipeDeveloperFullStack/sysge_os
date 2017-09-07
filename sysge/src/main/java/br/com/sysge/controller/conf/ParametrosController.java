package br.com.sysge.controller.conf;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


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
	

}
