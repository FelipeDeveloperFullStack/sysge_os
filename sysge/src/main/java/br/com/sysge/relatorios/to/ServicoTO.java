package br.com.sysge.relatorios.to;

import java.io.Serializable;
import java.math.BigDecimal;

public class ServicoTO implements Serializable{
	
	private static final long serialVersionUID = 6534217741573882240L;

	private String nome;
	
	private BigDecimal subTotal;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	
	

}
