package br.com.sysge.model.financ.boleto;

public enum TipoBanco {

	BANCO_DO_BRASIL("Banco do Brasil"),
	BRADESCO("Bradesco"),
	ITAU("Itau"),
	HSBC("HSBC"),
	SANTANDER("Santander"),
	CAIXA_ECONOMICO("Caixa Econômica Federal"),
	SAFRA("Safra");
	
	private String tipoBanco;
	
	TipoBanco(String tipoBanco){
		this.tipoBanco = tipoBanco;
	}
	
	public String getTipoBanco(){
		return this.tipoBanco;
	}
}
