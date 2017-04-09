package br.com.sysge.relatorios.to;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProdutoTO implements Serializable{

	private static final long serialVersionUID = 124344852834454689L;

	private String descricaoProduto;
	
	private Long quantidade = 1L;
	
	private BigDecimal valor = BigDecimal.ZERO;
	
	private BigDecimal subTotal = BigDecimal.ZERO;

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
	
	
}
