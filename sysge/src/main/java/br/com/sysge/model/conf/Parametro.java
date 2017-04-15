package br.com.sysge.model.conf;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.global.UnidadeEmpresarial;

@Entity
@Table(name = "tbl_parametros")
public class Parametro extends GenericDomain{

	private static final long serialVersionUID = -7327414672365504907L;
	
	private boolean permitirQtdeNegativaEstoque;
	
	private boolean mostrarListagemEstoqueNegativoTelaInicial;
	
	private boolean mostrarListagemOSTelaInicial;
	
	@OneToOne(fetch = FetchType.EAGER)
	private UnidadeEmpresarial unidadeEmpresarialPadrao;
	
	public boolean isPermitirQtdeNegativaEstoque() {
		return permitirQtdeNegativaEstoque;
	}

	public void setPermitirQtdeNegativaEstoque(boolean permitirQtdeNegativaEstoque) {
		this.permitirQtdeNegativaEstoque = permitirQtdeNegativaEstoque;
	}

	public boolean isMostrarListagemEstoqueNegativoTelaInicial() {
		return mostrarListagemEstoqueNegativoTelaInicial;
	}

	public void setMostrarListagemEstoqueNegativoTelaInicial(boolean mostrarListagemEstoqueNegativoTelaInicial) {
		this.mostrarListagemEstoqueNegativoTelaInicial = mostrarListagemEstoqueNegativoTelaInicial;
	}

	public boolean isMostrarListagemOSTelaInicial() {
		return mostrarListagemOSTelaInicial;
	}

	public void setMostrarListagemOSTelaInicial(boolean mostrarListagemOSTelaInicial) {
		this.mostrarListagemOSTelaInicial = mostrarListagemOSTelaInicial;
	}

	public UnidadeEmpresarial getUnidadeEmpresarialPadrao() {
		return unidadeEmpresarialPadrao == null ? new UnidadeEmpresarial() : this.unidadeEmpresarialPadrao;
	}

	public void setUnidadeEmpresarialPadrao(UnidadeEmpresarial unidadeEmpresarialPadrao) {
		this.unidadeEmpresarialPadrao = unidadeEmpresarialPadrao;
	}
	
	

}
