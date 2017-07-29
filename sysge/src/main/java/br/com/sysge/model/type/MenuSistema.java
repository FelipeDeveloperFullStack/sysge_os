package br.com.sysge.model.type;

public enum MenuSistema {
	
	COMPRAS("Compras"), 
	ESTOQUE("Estoque"),
		CADASTRO_ESTQ("Cadastros"),
			PRODUTO("Produto"),
	GESTAO_SERVICO("Gestão de Serviço"),
		CADASTROS_GS("Cadastros"),
			SERVICO("Serviços"),
		ORDEM_DE_SERVICO("Ordem de Serviço"),
	RH("RH"),
		CADASTRO_RH("Cadastros"),
			FUNCIONARIO("Funcionário"),
	FINANCEIRO("Financeiro"),
		CADASTROS("Cadastros"),
		CAIXA("Caixa"),
			LANCAMENTO_FINANCEIRO("Lançamento financeiro"),
		BANCO("Banco"),
			LANCAMENTO_BANCO("Lançamento de banco"),
		CONDICAO_PAGAMENTO("Condição de pagamento"),	
		CONTAS_A_PAGAR("Contas a pagar"),	
		CONTAS_A_RECEBER("Contas a receber"),	
		RELATORIOS("Relatórios"),
	GLOBAL("Global"),
		CADASTROS_GL("Cadastros"),
			CLIENTE("Cliente"),
			FORNECEDOR("Fornecedor"),
			UNIDADE_EMPRESARIAL("Unidade Empresarial"),
			//AGENDA("Agenda de compromisso"),
	CONFIGURACAO("Configuracao"),
		USUARIO("Usuário"),
		PERFIL_ACESSO("Perfil de acesso"),
		PARAMETROS("Parâmetros"),
		BACKUP("Backup");
	
	private String menu;
	
	MenuSistema(String menu){
		this.menu = menu;
	}
	
	public String getMenu(){
		return this.menu;
	}

}
