package br.com.sysge.service.financ;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.financ.AuditoriaFinanceiro;
import br.com.sysge.util.UsuarioSession;

public class AuditoriaFinanceiroService extends GenericDaoImpl<AuditoriaFinanceiro, Long>{

	private static final long serialVersionUID = -9064562135452305070L;
	
	public void consistir(AuditoriaFinanceiro auditoriaFinanceiro){
		if(auditoriaFinanceiro.getJustificativa().isEmpty()){
			throw new RuntimeException("A justificativa é obrigatória!");
		}
		if(auditoriaFinanceiro.getSenha().isEmpty()){
			throw new RuntimeException("A senha é obrigatória!");
		}
		if(!auditoriaFinanceiro.getSenha().equals(UsuarioSession.getSessionUsuario().getSenhaUsuario())){
			throw new RuntimeException("A senha não confere com o usuário logado, tente novamente!");
		}
	}

}
