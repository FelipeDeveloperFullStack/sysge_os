package br.com.sysge.relatorios.to;

import java.io.Serializable;

public class ItemOrdemServicoTO implements Serializable{

	private static final long serialVersionUID = -7896461367131969057L;
	
	private String descricao;
	
	private String dados;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDados() {
		return dados;
	}

	public void setDados(String dados) {
		this.dados = dados;
	}
	
	

}
