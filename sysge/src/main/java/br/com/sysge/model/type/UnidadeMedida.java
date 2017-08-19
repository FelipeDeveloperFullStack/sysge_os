package br.com.sysge.model.type;

public enum UnidadeMedida {
	
	UNIDADE("UN"),
	QUILOGRAMA("KG"),
	LITRO("LT"),
	CAIXA("CX"),
	MILILITRO("ML"),
	PACOTE("PC"),
	FARDO("FD"),
	FRASCO("FR"),
	GRAMA("G");

	private String unidadeMedida;
	
	UnidadeMedida(String unidadeMedida){
		this.unidadeMedida = unidadeMedida;
	}
	
	public String getUnidadeMedida(){
		return this.unidadeMedida;
	}
}
