package br.com.sysge.model.conf;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_parametros")
public class Parametro extends GenericDomain{

	private static final long serialVersionUID = -7327414672365504907L;
	
	private boolean permitirQtdeNegativaEstoque;
	
	private boolean mostrarListagemEstoqueNegativoTelaInicial;
	
	private boolean mostrarListagemOSTelaInicial;

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
	
	

}
