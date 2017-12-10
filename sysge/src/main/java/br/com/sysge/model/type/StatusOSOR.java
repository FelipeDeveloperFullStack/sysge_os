package br.com.sysge.model.type;

public enum StatusOSOR {
	
	PEDIDO("Pedido"),
	ORDEM_SERVICO("Ordem de Serviço");

	private String statusOSOR;
	
	StatusOSOR(String statusOSOR){
		this.statusOSOR = statusOSOR;
	}
	
	public String getStatusOSOR(){
		return this.statusOSOR;
	}
}
