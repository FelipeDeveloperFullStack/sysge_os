package br.com.sysge.model.financ.type;

public enum TipoLancamentoFinanceiro {

	LANCAMENTO_SIMPLES("Lançamento simples"),
	LANCAMENTO_PARCELADO("Lançamento parcelado");
	
	private String tipo;
	
	TipoLancamentoFinanceiro(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return this.tipo;
	}
}
