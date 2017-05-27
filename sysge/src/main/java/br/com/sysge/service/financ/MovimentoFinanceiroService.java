package br.com.sysge.service.financ;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.model.financ.MovimentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.CategoriaLancamentoReceita;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.financ.type.TipoLancamentoFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;

public class MovimentoFinanceiroService extends GenericDaoImpl<MovimentoFinanceiro, Long>{
	
	private static final long serialVersionUID = 3150386706435445711L;
	
	private static final String A_VISTA = "À Vista";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Inject
	private LancamentoFinanceiroService lancamentoReceitaService;
	
	public void salvarMovimentoFinanceiroOS(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs){
		
		String condicaoPagamento = ordemServico.getCondicaoPagamento().getDescricao();
		
		parcelasPagamentoOs.getLancamentoReceita().setMovimentoFinanceiro
			(calcularMovimentoReceita(parcelasPagamentoOs.getLancamentoReceita(), parcelasPagamentoOs));
		
		if(condicaoPagamento.equals(A_VISTA)){
			salvarMovimentoOS(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
		}else{
			salvarMovimentoOS(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
		}
		
	}
	
	private void salvarMovimentoOS(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs, String condicaoPagamento, LancamentoFinanceiro lancamentoReceita){
	    
		lancamentoReceita.setCategoriaLancamentoReceita(CategoriaLancamentoReceita.ORDEM_SERVICO);
		lancamentoReceita.setCliente(ordemServico.getCliente());
		lancamentoReceita.setDataLancamento(parcelasPagamentoOs.getDataVencimento());
		lancamentoReceita.setDescricao("Referente a Ordem de Serviço nº "+ordemServico.getId()+" emitida em "+sdf.format(ordemServico.getDataEntrada()));
		lancamentoReceita.setFormaPagamento(ordemServico.getFormaPagamento());
		lancamentoReceita.setMovimentoFinanceiro(parcelasPagamentoOs.getLancamentoReceita().getMovimentoFinanceiro());
		
		if(parcelasPagamentoOs.getStatusFinanceiro() == StatusFinanceiro.PAGO){
			lancamentoReceita.setStatusRecebimentoReceita(StatusFinanceiro.PAGO);
			lancamentoReceita.setDataLancamento(parcelasPagamentoOs.getDataPagamento());
		}else{
			lancamentoReceita.setStatusRecebimentoReceita(StatusFinanceiro.PENDENTE);
		}
		if(condicaoPagamento.equals(A_VISTA)){
			lancamentoReceita.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_SIMPLES);
			lancamentoReceita.setTitulo("Ordem Serviço nº "+ordemServico.getId());
		}else{
			lancamentoReceita.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_PARCELADO);
			lancamentoReceita.setTitulo("Ordem Serviço nº "+ordemServico.getId()+ " - "+parcelasPagamentoOs.getNumero()+"º parcela");
		}
		lancamentoReceita.setValor(parcelasPagamentoOs.getValorCobrado());
		lancamentoReceita = lancamentoReceitaService.save(lancamentoReceita);
		parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
	}
	
	/*public LancamentoReceita salvarMovimentoReceita(LancamentoReceita lancamentoReceita){
		return lancamentoReceitaService.save(lancamentoReceita);
	}*/
	
	private MovimentoFinanceiro calcularMovimentoReceita
			(LancamentoFinanceiro lancamentoReceita, ParcelasPagamentoOs parcelasPagamentoOs){
		parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
		lancamentoReceita.setMovimentoFinanceiro(lancamentoReceita.getMovimentoFinanceiro());
		
		if(parcelasPagamentoOs.getStatusFinanceiro() == StatusFinanceiro.PAGO){
			lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido
				(lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().add
						(parcelasPagamentoOs.getValorCobrado()));
			
			lancamentoReceita.getMovimentoFinanceiro().setDataMovimento
				(parcelasPagamentoOs.getDataPagamento());
		}else{
			lancamentoReceita.getMovimentoFinanceiro().setDataMovimento
				(parcelasPagamentoOs.getDataVencimento());
		}
			
			lancamentoReceita.getMovimentoFinanceiro().setTotalReceita
				(lancamentoReceita.getMovimentoFinanceiro().getTotalReceita().add
						(parcelasPagamentoOs.getValorParcela()));
			
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoOperacional
			(lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().subtract
					(lancamentoReceita.getMovimentoFinanceiro().getTotalPago()));
			
			//buscar saldo atual do dia anterior
			
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoAtual
			(lancamentoReceita.getMovimentoFinanceiro().getTotalSaldoOperacional().add
					(lancamentoReceita.getMovimentoFinanceiro().getTotalSaldoAnterior()));
			
			lancamentoReceita.setMovimentoFinanceiro(super.save(lancamentoReceita.getMovimentoFinanceiro()));
		
		
		
		return lancamentoReceita.getMovimentoFinanceiro();
	}

}
