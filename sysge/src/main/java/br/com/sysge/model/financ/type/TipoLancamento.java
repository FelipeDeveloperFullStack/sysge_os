package br.com.sysge.model.financ.type;

public enum TipoLancamento {

	RECEITA("Receita"),
	DESPESA("Despesa");
	
	private String tipoLancamento;
	
	TipoLancamento(String tipoLancamento){
		this.tipoLancamento = tipoLancamento;
	}
	
	public String getTipoLancamento(){
		return this.tipoLancamento;
	}
}
