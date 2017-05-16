package br.com.sysge.service.financ;

import java.util.Date;
import java.util.List;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.financ.LancamentoFinanceiro;

public class LancamentoFinanceiroService extends GenericDaoImpl<LancamentoFinanceiro, Long>{

	private static final long serialVersionUID = 6994553276942778118L;
	
	public List<LancamentoFinanceiro> obterLancamentoFinanceiroPorData(Date dataMovimento){
		return super.findByData("dataLancamento",dataMovimento);
	}

}
