package br.com.sysge.relatorios.to;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemOrdemServicoTO implements Serializable{

	private static final long serialVersionUID = -7896461367131969057L;
	
	private String descricao;
	
	private String quantidade;
	
	private BigDecimal valor;
	
	private BigDecimal valorUnit;
	
	private BigDecimal total;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValorUnit() {
		return valorUnit;
	}

	public void setValorUnit(BigDecimal valorUnit) {
		this.valorUnit = valorUnit;
	}

}
