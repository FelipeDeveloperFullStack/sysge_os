package br.com.sysge.service.estoque;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.estoque.Produto;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.service.global.FornecedorService;
import br.com.sysge.util.UsuarioSession;

public class ProdutoService extends GenericDaoImpl<Produto, Long>{

	private static final long serialVersionUID = 1704211895445872913L;
	
	private FornecedorService fornecedorService = new FornecedorService();
	
	public Produto salvar(Produto produto){
		try {
			if(produto.getDescricaoProduto().trim().isEmpty()){
				throw new RuntimeException("A descrição do produto é obrigatório!");
			}
			if(produto.getFornecedor() == null){
				throw new RuntimeException("O fornecedor é obrigatório!");
			}
			return super.save(consistirProduto(produto));
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public boolean verificarSeExisteFornecedorAtivo(){
		if(fornecedorService.findBySituation(Situacao.ATIVO).isEmpty()){
			return true;
		}
		return false;
	}
	
	private Produto consistirProduto(Produto produto){
		if(produto.getId() == null){
			produto.setUsuarioQueCadastrou(UsuarioSession.getSessionUsuario());
			produto.setDataUsuarioCadastro(new Date());
			produto.setSituacao(Situacao.ATIVO);
		}else{
			produto.setUsuarioQueAlterou(UsuarioSession.getSessionUsuario());
			produto.setDataUsuarioAlteracao(new Date());
		}
		return produto;
	}
	
	public List<Produto> pesquisarProduto(Produto produto){
		try {
			if(produto.getDescricaoProduto().trim().isEmpty()){
				return super.findBySituation(produto.getSituacao());
			}else{
				return super.findByParametersForSituation(produto.getDescricaoProduto(), 
						produto.getSituacao(), "descricaoProduto", "LIKE", "%", "%"); 
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	public List<Produto> obterProdutoQuantidadeMinimoEstoque(){
		List<Produto> listaProdutos = new ArrayList<Produto>();
		for(Produto p : super.findBySituation(Situacao.ATIVO)){
			if((p.getQuantidadeEstoque().compareTo(p.getQuantidadeEstoqueMinimo()) <= 0) && p.isMostrarEstoqueMinimoTelaInicial()){
				listaProdutos.add(p);
			}
		}
		return listaProdutos;
	}
	
	
}
