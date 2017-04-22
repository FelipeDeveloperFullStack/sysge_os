package br.com.sysge.model.financ.type;

public enum StatusFinanceiro {
	
	PAGO("Pago"),
	PENDENTE("Pendente");
	
	private String status;
	
	StatusFinanceiro(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return this.status;
	}

}
