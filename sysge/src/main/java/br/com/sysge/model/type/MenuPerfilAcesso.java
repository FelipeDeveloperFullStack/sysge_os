package br.com.sysge.model.type;

public enum MenuPerfilAcesso {
	
	//COMPRAS("Compras"), 
			PRODUTO("Produto"),
			SERVICO("Serviços"),
		ORDEM_DE_SERVICO("Ordem de Serviço"),
		ORCAMENTO("Orçamento"),	
		FUNCIONARIO("Funcionário"),
			LANCAMENTO_FINANCEIRO("Lançamento financeiro"),
		CONDICAO_PAGAMENTO("Condição de pagamento"),
		AUDITORIA_FINANCEIRO("Auditoria Financeiro"),
		//CONTAS_A_PAGAR("Contas a pagar"),	
		//CONTAS_A_RECEBER("Contas a receber"),	
			CLIENTE("Cliente"),
			FORNECEDOR("Fornecedor"),
			UNIDADE_EMPRESARIAL("Unidade Empresarial"),
			AGENDA("Agenda de compromisso"),
		USUARIO("Usuário"),
		PERFIL_ACESSO("Perfil de acesso"),
		PARAMETROS("Parâmetros"),
		BACKUP("Backup");
	
	private String menu;
	
	MenuPerfilAcesso(String menu){
		this.menu = menu;
	}
	
	public String getMenu(){
		return this.menu;
	}

}
