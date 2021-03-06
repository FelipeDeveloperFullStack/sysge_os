package br.com.sysge.service.conf;

import java.util.List;



import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.conf.Parametro;
import br.com.sysge.model.estoque.Produto;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.service.estoque.ProdutoService;

public class ParametroService extends GenericDaoImpl<Parametro, Long>{

	private static final long serialVersionUID = -4527982637637712859L;
	
	private ProdutoService produtoService = new ProdutoService();
	
	public void salvar(Parametro parametro){
		List<Parametro> parametros = super.findAll();
		if(parametros.isEmpty()){
			super.save(parametro);
		}else{
			if(parametros.size() == 1){
				for(Parametro p : parametros){
					p.setPermitirQtdeNegativaEstoque(parametro.isPermitirQtdeNegativaEstoque());
					p.setMostrarListagemEstoqueNegativoTelaInicial(parametro.isMostrarListagemEstoqueNegativoTelaInicial());
					p.setMostrarListagemOSTelaInicial(parametro.isMostrarListagemOSTelaInicial());
					p.setUnidadeEmpresarialPadrao(parametro.getUnidadeEmpresarialPadrao());
					p.setMostrarContaPagarReceber(parametro.isMostrarContaPagarReceber());
					super.save(p);
				}
			}
		}
	}
	
	public boolean verificarParametroEstoqueNegativo(Produto produto){
		for(Produto prod : produtoService.findBySituation(Situacao.ATIVO)){
			if(prod.getId() == produto.getId()){
				if(prod.getQuantidadeEstoque().doubleValue() <= 0){
					return verificarParametroEstoqueNegativo();
				}
			}
		}
		return false;
		
	}
	
	public boolean verificarParametroEstoqueNegativo(){
		for(Parametro p : super.findAll()){
			if(p.isPermitirQtdeNegativaEstoque()){
				return true;
			}
		}
		return false;
	}

}
