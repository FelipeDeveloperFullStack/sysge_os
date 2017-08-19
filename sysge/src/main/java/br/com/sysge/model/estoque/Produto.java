package br.com.sysge.model.estoque;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.global.Fornecedor;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.UnidadeMedida;

@Entity
@Table(name = "tbl_produto")
public class Produto extends GenericDomain{

	private static final long serialVersionUID = -7328127398997221454L;

	private String descricaoProduto;
	
	private BigDecimal valorCusto = BigDecimal.ZERO;
	
	private BigDecimal valorVenda = BigDecimal.ZERO;
	
	private BigDecimal quantidadeEstoque = BigDecimal.ZERO;
	
	private BigDecimal quantidadeEstoqueMinimo = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidadeMedida;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Fornecedor fornecedor;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;
	
	private boolean mostrarEstoqueMinimoTelaInicial = Boolean.TRUE;

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public BigDecimal getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public BigDecimal getQuantidadeEstoqueMinimo() {
		return quantidadeEstoqueMinimo;
	}

	public void setQuantidadeEstoqueMinimo(BigDecimal quantidadeEstoqueMinimo) {
		this.quantidadeEstoqueMinimo = quantidadeEstoqueMinimo;
	}

	public Fornecedor getFornecedor() {
		if(fornecedor == null){
			fornecedor = new Fornecedor();
		}
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public boolean isMostrarEstoqueMinimoTelaInicial() {
		return mostrarEstoqueMinimoTelaInicial;
	}

	public void setMostrarEstoqueMinimoTelaInicial(boolean mostrarEstoqueMinimoTelaInicial) {
		this.mostrarEstoqueMinimoTelaInicial = mostrarEstoqueMinimoTelaInicial;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	
	
}
