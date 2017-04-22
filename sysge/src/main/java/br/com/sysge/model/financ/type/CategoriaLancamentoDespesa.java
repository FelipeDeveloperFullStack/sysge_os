package br.com.sysge.model.financ.type;

public enum CategoriaLancamentoDespesa {
	
	ENERGIA("Conta de energia"),
	AGUA("Conta de água"),
	TELEFONE("Conta de telefone"),
	GAS("Conta de gás"),
	IMPOSTOS("Impostos"),
	INTERNET("Conta de internet"),
	ALUGUEL("Aluguel"),
	EQUIPAMENTO("Equipamento"),
	COMPUTADOR("Computador"),
	JUROS_BACANRIOS("Juros bancários"),
	DEMAIS_CONTAS("Demais contas"),
	TAXAS_DIVERSAS("Taxas diversas");
	
	private String tipo;
	
	CategoriaLancamentoDespesa(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return this.tipo;
	}

}
