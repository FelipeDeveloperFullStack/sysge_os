package br.com.sysge.model.financ.type;

public enum CategoriaLancamentoReceita {
	
	ORDEM_SERVICO("Ordem de Serviços"),
	RECEBIMENTOS_DIVERSOS("Recebimentos Diversos");
	
	private String tipo;
	
	CategoriaLancamentoReceita(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return this.tipo;
	}

}
