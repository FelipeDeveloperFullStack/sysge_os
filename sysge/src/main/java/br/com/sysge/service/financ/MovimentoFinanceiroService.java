package br.com.sysge.service.financ;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.financ.LancamentoReceita;
import br.com.sysge.model.financ.MovimentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.CategoriaLancamentoReceita;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.financ.type.TipoLancamentoFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.model.type.Pago;

public class MovimentoFinanceiroService extends GenericDaoImpl<MovimentoFinanceiro, Long>{
	
	private static final long serialVersionUID = 3150386706435445711L;
	
	private static final String A_VISTA = "À Vista";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Inject
	private LancamentoReceitaService lancamentoReceitaService;
	
	public void salvarMovimentoFinanceiro(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs){
		
		String condicaoPagamento = ordemServico.getCondicaoPagamento().getDescricao();
		
		parcelasPagamentoOs.getLancamentoReceita().setMovimentoFinanceiro
			(calcularMovimentoReceita(parcelasPagamentoOs.getLancamentoReceita(), parcelasPagamentoOs));
		
		if(condicaoPagamento.equals(A_VISTA)){
			salvarMovimento(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
		}else{
			salvarMovimento(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
		}
		
	}
	
	private void salvarMovimento(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs, String condicaoPagamento, LancamentoReceita lancamentoReceita){
	    
		lancamentoReceita.setCategoriaLancamentoReceita(CategoriaLancamentoReceita.ORDEM_SERVICO);
		lancamentoReceita.setCliente(ordemServico.getCliente());
		lancamentoReceita.setDataLancamento(parcelasPagamentoOs.getDataVencimento());
		lancamentoReceita.setDescricao("Referente a Ordem de Serviço nº "+ordemServico.getId()+" emitida em "+sdf.format(ordemServico.getDataEntrada()));
		lancamentoReceita.setFormaPagamento(ordemServico.getFormaPagamento());
		lancamentoReceita.setMovimentoFinanceiro(parcelasPagamentoOs.getLancamentoReceita().getMovimentoFinanceiro());
		
		if(parcelasPagamentoOs.getPago() == Pago.SIM){
			lancamentoReceita.setStatusRecebimentoReceita(StatusFinanceiro.PAGO);
		}else{
			lancamentoReceita.setStatusRecebimentoReceita(StatusFinanceiro.PENDENTE);
		}
		if(condicaoPagamento.equals(A_VISTA)){
			lancamentoReceita.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_SIMPLES);
			lancamentoReceita.setTitulo("Ordem Serviço nº "+ordemServico.getId());
		}else{
			lancamentoReceita.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_PARCELADO);
			lancamentoReceita.setTitulo("Ordem Serviço nº "+ordemServico.getId()+ " - "+parcelasPagamentoOs.getId()+"º parcela");
		}
		lancamentoReceita.setValor(parcelasPagamentoOs.getValorCobrado());
		lancamentoReceita = lancamentoReceitaService.save(lancamentoReceita);
		parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
	}
	
	private MovimentoFinanceiro calcularMovimentoReceita(LancamentoReceita lancamentoReceita, ParcelasPagamentoOs parcelasPagamentoOs){
		parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
		lancamentoReceita.setMovimentoFinanceiro(lancamentoReceita.getMovimentoFinanceiro());
		
		if(parcelasPagamentoOs.getPago() == Pago.SIM){
			lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido
				(parcelasPagamentoOs.getValorCobrado());
		}
		
		lancamentoReceita.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataVencimento());
		lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(parcelasPagamentoOs.getValorParcela());
		
		lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoOperacional
			(lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().subtract
					(lancamentoReceita.getMovimentoFinanceiro().getTotalPago()));
		
		lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoAtual
			(lancamentoReceita.getMovimentoFinanceiro().getTotalSaldoOperacional().add
					(lancamentoReceita.getMovimentoFinanceiro().getTotalSaldoAnterior()));
		
		lancamentoReceita.setMovimentoFinanceiro(super.save(lancamentoReceita.getMovimentoFinanceiro()));
		
		return lancamentoReceita.getMovimentoFinanceiro();
	}

}
