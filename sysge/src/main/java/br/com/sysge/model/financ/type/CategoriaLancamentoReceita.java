package br.com.sysge.model.financ.type;

public enum CategoriaLancamentoReceita {
	
	ORDEM_SERVICO("Ordem de Serviços", 1),
	RECEBIMENTOS_DIVERSOS("Recebimentos Diversos", 2);
	
	private String tipo;
	private int index;
	
	CategoriaLancamentoReceita(String tipo, int index){
		this.tipo = tipo;
		this.index = index;
	}
	
	public String getTipo(){
		return this.tipo;
	}
	
	public int getIndex(){
		return this.index;
	}

}
