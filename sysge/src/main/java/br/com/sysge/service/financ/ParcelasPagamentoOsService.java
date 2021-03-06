package br.com.sysge.service.financ;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.Query;


import br.com.sysge.controller.financ.MovimentoFinanceiroController;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.util.DateUtil;

public class ParcelasPagamentoOsService extends GenericDaoImpl<ParcelasPagamentoOs, Long>{

	private static final long serialVersionUID = 8977519177977425704L;
	
	private static final String A_VISTA = "À Vista";
	
	private static final String CONDICAO_PAGAMENTO = "A condição de pagamento é obrigatório!";
	
	private MovimentoFinanceiroController movimentoFinanceiroController;
	
	
	public ParcelasPagamentoOsService(){
		movimentoFinanceiroController = new MovimentoFinanceiroController();
	}
	
	public List<ParcelasPagamentoOs> salvar(OrdemServico ordemServico, List<ParcelasPagamentoOs> parcelas){
		List<ParcelasPagamentoOs> listaParcelas = procurarParcelasPorOS(ordemServico.getId());
		/*if(!listaParcelas.isEmpty()){
			for(ParcelasPagamentoOs p : listaParcelas){
				super.removeByObject(p);
			}
		}*/
		for(ParcelasPagamentoOs p : parcelas){
			if(p.getStatusFinanceiro() == null){
				p.setStatusFinanceiro(StatusFinanceiro.PENDENTE);
			}
			p.setOrdemServico(ordemServico);
			p = super.save(p);
			listaParcelas.add(p);
		}
		
		return listaParcelas;
	}
	
	public List<ParcelasPagamentoOs> procurarParcelasPorOS(long idOS){
		return super.findByListProperty(idOS, "ordemServico.id");
	}
	
	public boolean verificarSeExistePagamentoRealizado(OrdemServico ordemServico){
		if(ordemServico.getId() != null){
			List<ParcelasPagamentoOs> listaParcelas = procurarParcelasPorOS(ordemServico.getId());
			for(ParcelasPagamentoOs p : listaParcelas){
				if(p.getStatusFinanceiro() == StatusFinanceiro.PAGO){
					return true;
				}
			}
		}
		return false;
	}
	public boolean verificarSeExistePagamentoNAORealizado(OrdemServico ordemServico){
		int contNao = 0;
		if(ordemServico.getId() != null){
			List<ParcelasPagamentoOs> listaParcelas = procurarParcelasPorOS(ordemServico.getId());
			for(ParcelasPagamentoOs p : listaParcelas){
				if(p.getStatusFinanceiro() == StatusFinanceiro.PENDENTE){
					contNao++;
				}
			}
			if(contNao >= 1){
				return true;
			}
			
		}
		return false;
	}
	
	public List<ParcelasPagamentoOs> gerarParcelas(OrdemServico ordemServico, List<ParcelasPagamentoOs> parcelas, ParcelasPagamentoOs parcelasPagamentoOs){
		if(ordemServico.getCondicaoPagamento() == null){
			throw new RuntimeException(CONDICAO_PAGAMENTO);
		}
		if(ordemServico.getTotal() == BigDecimal.ZERO){
			throw new RuntimeException("Não é possível gerar as parcelas, pois o valor total está igual a R$: 0,00");
		}

			//verificarLancamentoFinanceiroParcela(parcelas); //Voltar esse método quando arrumar o financeiro para a OS
		
			String condicaoPagamento = ordemServico.getCondicaoPagamento().getDescricao();
			if(condicaoPagamento == null){
				throw new RuntimeException(CONDICAO_PAGAMENTO);
			}
			
			if(condicaoPagamento.equals(A_VISTA)){
				
				if(parcelas.isEmpty()){
					return setarParcelas(parcelasPagamentoOs, ordemServico, parcelas);
				}else{
					if(parcelas.size() > 1){
						parcelas.removeAll(parcelas);
						return setarParcelas(parcelasPagamentoOs, ordemServico, parcelas);
					}
					for(ParcelasPagamentoOs parcela : parcelas){
						parcela.setQuantidadeParcelas(String.valueOf(1L));
						parcela.setValorParcela(ordemServico.getTotal());
						parcela.setValorCobrado(ordemServico.getTotal());
						parcela.setDataVencimento(DateUtil.asDate(LocalDate.now()));
					}
					return parcelas;
				}
				
			}else{
				return percorrerListaCondicaoPagamento(condicaoPagamento, ordemServico, new ArrayList<ParcelasPagamentoOs>(), parcelasPagamentoOs);
			}
		
	}
	
	private List<ParcelasPagamentoOs> setarParcelas(ParcelasPagamentoOs parcelasPagamentoOs, OrdemServico ordemServico, List<ParcelasPagamentoOs> parcelas){
		parcelasPagamentoOs = new ParcelasPagamentoOs();
		parcelasPagamentoOs.setNumero(1L);
		parcelasPagamentoOs.setOrdemServico(ordemServico);
		parcelasPagamentoOs.setQuantidadeParcelas(String.valueOf(1L));
		parcelasPagamentoOs.setValorParcela(ordemServico.getTotal());
		parcelasPagamentoOs.setValorCobrado(ordemServico.getTotal());
		parcelasPagamentoOs.setDataVencimento(DateUtil.asDate(LocalDate.now()));
		parcelas.add(parcelasPagamentoOs);
		return parcelas;
	}
	
	private void verificarLancamentoFinanceiroParcela(List<ParcelasPagamentoOs> parcelas){
		for(ParcelasPagamentoOs p : parcelas){
			if(p.getStatusFinanceiro() == StatusFinanceiro.PENDENTE){
				throw new RuntimeException("É preciso excluir o(s) titulo(s) financeiro(s) pendente para gerar a(s) nova(s) parcela(s)!");
			}
		}
	}
	
	private List<ParcelasPagamentoOs> percorrerListaCondicaoPagamento(String condicaoPagamento, OrdemServico ordemServico, List<ParcelasPagamentoOs> parcelas, ParcelasPagamentoOs parcelasPagamentoOs){
		condicaoPagamento = condicaoPagamento.trim().replace("dias", "");
		String[] listaCondicaoPagamentoDescricao = condicaoPagamento.trim().split(",");
		for(int i = 1; i <= listaCondicaoPagamentoDescricao.length; i++){
			parcelasPagamentoOs = new ParcelasPagamentoOs();
			parcelasPagamentoOs.setNumero(i);
			parcelasPagamentoOs.setOrdemServico(ordemServico);
			parcelasPagamentoOs.setQuantidadeParcelas(String.valueOf(listaCondicaoPagamentoDescricao.length));
			parcelasPagamentoOs.setValorParcela(ordemServico.getTotal().divide(new BigDecimal(listaCondicaoPagamentoDescricao.length), BigDecimal.ROUND_UP));
			parcelasPagamentoOs.setValorCobrado(ordemServico.getTotal().divide(new BigDecimal(listaCondicaoPagamentoDescricao.length), BigDecimal.ROUND_UP));
			String diaVencimento = listaCondicaoPagamentoDescricao[i-1];
			diaVencimento = diaVencimento.trim();
			parcelasPagamentoOs.setDataVencimento(DateUtil.asDate(LocalDate.now().plusDays(Integer.parseInt(diaVencimento))));
			parcelas.add(parcelasPagamentoOs);
		}
		return parcelas;
	}
	
	public void salvarMovimentoReceitaParcela(ParcelasPagamentoOs parcela){
		try {
			if(parcela.getDataPagamento() == null){
				throw new RuntimeException("A data de pagamento é obrigatória!");
			}
			parcela.setStatusFinanceiro(StatusFinanceiro.PAGO);
			parcela = super.save(parcela);
			parcela.getLancamentoReceita().setDataLancamento(parcela.getDataPagamento());
			parcela.getLancamentoReceita().getMovimentoFinanceiro().setTotalReceita(parcela.getLancamentoReceita().getMovimentoFinanceiro().getTotalReceita().subtract(parcela.getValorCobrado()));
			movimentoFinanceiroController.atualizarMovimentoFinanceiroOrdemServico(parcela.getLancamentoReceita(), parcela);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public ParcelasPagamentoOs obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		Query query = getEntityManager().createQuery("SELECT o FROM "+getEntityClass().getSimpleName()+" o "+ 
				" WHERE o.lancamentoReceita = :lancamentoFinanceiro");
		query.setParameter("lancamentoFinanceiro", lancamentoFinanceiro);
		return (ParcelasPagamentoOs) query.getSingleResult();
	}

}
