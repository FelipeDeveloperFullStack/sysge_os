package br.com.sysge.service.global;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.global.Agenda;

public class AgendaService extends GenericDaoImpl<Agenda, Long>{

	private static final long serialVersionUID = 5194682334477155014L;
	
	public void salvar(Agenda agenda){
		if(agenda.getDescricao().isEmpty()){
			throw new RuntimeException("A descrição para a agenda é obrigatória!");
		}
		if(agenda.getDataInicial() == null){
			throw new RuntimeException("A data inicial é obrigatória!");
		}
		if(agenda.getDataFinal() == null){
			throw new RuntimeException("A data final é obrigatória!");
		}
		super.save(agenda);
	}

}
