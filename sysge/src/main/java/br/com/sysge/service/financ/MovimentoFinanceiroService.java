package br.com.sysge.service.financ;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.model.financ.MovimentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.CategoriaLancamentoReceita;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.financ.type.TipoAtualizacaoMovimento;
import br.com.sysge.model.financ.type.TipoLancamento;
import br.com.sysge.model.financ.type.TipoLancamentoFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.util.DateUtil;

public class MovimentoFinanceiroService extends GenericDaoImpl<MovimentoFinanceiro, Long>{
	
	private static final long serialVersionUID = 3150386706435445711L;
	
	private static final String A_VISTA = "À Vista";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Inject
	private LancamentoFinanceiroService lancamentoFinanceiroService;
	
	@Inject
	private ParcelasPagamentoOsService parcelasPagamentoOsService;
	
	private MovimentoFinanceiro movimentoFinanceiro;
	
	public void salvarMovimentoFinanceiroOS(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs){
		
		parcelasPagamentoOs.setLancamentoReceita(parcelasPagamentoOs.getLancamentoReceita());
		if(parcelasPagamentoOs.getLancamentoReceita().getId() != null){
			parcelasPagamentoOs.setLancamentoReceita(lancamentoFinanceiroService.findById(parcelasPagamentoOs.getLancamentoReceita().getId()));
		}
		
		parcelasPagamentoOs.getLancamentoReceita().setMovimentoFinanceiro
			(calcularMovimentoParcelasPagamentoOS(ordemServico,parcelasPagamentoOs.getLancamentoReceita(), parcelasPagamentoOs));
		
	}
	
	private void salvarMovimentoOS(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs, String condicaoPagamento, LancamentoFinanceiro lancamentoReceita){
	    
		lancamentoReceita.setCategoriaLancamentoReceita(CategoriaLancamentoReceita.ORDEM_SERVICO);
		lancamentoReceita.setCliente(ordemServico.getCliente());
		lancamentoReceita.setDataLancamento(parcelasPagamentoOs.getDataVencimento());
		lancamentoReceita.setDescricao("Referente a Ordem de Serviço nº "+ordemServico.getId()+" emitida em "+sdf.format(ordemServico.getDataEntrada()));
		lancamentoReceita.setFormaPagamento(ordemServico.getFormaPagamento());
		lancamentoReceita.setMovimentoFinanceiro(parcelasPagamentoOs.getLancamentoReceita().getMovimentoFinanceiro());
		lancamentoReceita.setTipoLancamento(TipoLancamento.RECEITA);
		
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
		lancamentoReceita = lancamentoFinanceiroService.save(lancamentoReceita);
		parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
	}
	
	
	//Somar total recebido
	private BigDecimal somarTotalRecebido(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().add(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(m.getTotalRecebido().add(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido();
	}
	
	private BigDecimal somarTotalPago(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalPago(lancamentoReceita.getMovimentoFinanceiro().getTotalPago().add(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalPago(m.getTotalPago().add(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalPago();
	}
	
	//Somar total receita
	private BigDecimal somarTotalReceita(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(lancamentoReceita.getMovimentoFinanceiro().getTotalReceita().add(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(m.getTotalReceita().add(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalReceita();
	}
	
	private BigDecimal somarTotalDespesa(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(lancamentoReceita.getMovimentoFinanceiro().getTotalDespesa().add(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(m.getTotalDespesa().add(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalDespesa();
	}
	
	//Somar saldo operacional
	private BigDecimal somarTotalSaldoOperacional(LancamentoFinanceiro lancamentoReceita, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		BigDecimal saldo = BigDecimal.ZERO;
		if(listMovimentoFinanceiro.isEmpty()){
			saldo = lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().subtract(lancamentoReceita.getMovimentoFinanceiro().getTotalPago());
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				saldo = m.getTotalRecebido().subtract(m.getTotalPago());
			}
		}
		return saldo;
			
	}
	
	private MovimentoFinanceiro setarMovimentoFinanceiro(LancamentoFinanceiro lancamentoReceita){
		 for(MovimentoFinanceiro mov : buscarMovimentoFinanceiroByData(lancamentoReceita.getDataLancamento())){
			return mov;
		 }
		 return new MovimentoFinanceiro();
	}
	private MovimentoFinanceiro setarMovimentoFinanceiroParcelaOS(Date data){
		for(MovimentoFinanceiro mov : buscarMovimentoFinanceiroByData(data)){
			return mov;
		}
		return new MovimentoFinanceiro();
	}
	
	private MovimentoFinanceiro calcularMovimentoParcelasPagamentoOS
			(OrdemServico ordemServico,LancamentoFinanceiro lancamentoReceita, ParcelasPagamentoOs parcelasPagamentoOs){
		
			if(lancamentoReceita.getCategoriaLancamentoDespesa() != null){
				lancamentoReceita.setTipoLancamento(TipoLancamento.DESPESA);
			}else{
				lancamentoReceita.setTipoLancamento(TipoLancamento.RECEITA);
			}
			
			if(buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento()).isEmpty()){
				lancamentoReceita.setMovimentoFinanceiro(new MovimentoFinanceiro());
			}else{
				lancamentoReceita.setMovimentoFinanceiro(lancamentoReceita.getMovimentoFinanceiro());
				lancamentoReceita.setMovimentoFinanceiro(setarMovimentoFinanceiroParcelaOS(parcelasPagamentoOs.getDataVencimento() == null 
						? parcelasPagamentoOs.getDataPagamento() : parcelasPagamentoOs.getDataVencimento()));
			}
			
			parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
			
		
		if(parcelasPagamentoOs.getStatusFinanceiro() == StatusFinanceiro.PAGO){
			if(lancamentoReceita.getTipoLancamento() == TipoLancamento.RECEITA){
				if(lancamentoReceita.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_RECEBIDO){
					lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(somarTotalRecebido(lancamentoReceita, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento())));
				}
			}else{
				if(lancamentoReceita.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO){
					lancamentoReceita.getMovimentoFinanceiro().setTotalPago(somarTotalPago(lancamentoReceita, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento())));
				}
			}
			lancamentoReceita.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataPagamento());
		}else{
			if(lancamentoReceita.getTipoLancamento() == TipoLancamento.RECEITA){
				if(lancamentoReceita.getTipoAtualizacaoMovimento() == null){
					lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(somarTotalReceita(lancamentoReceita, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento())));
				}else{
					if(lancamentoReceita.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER){
						lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(somarTotalReceita(lancamentoReceita, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento())));
					}
				}
			}else{
				if(lancamentoReceita.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_PAGAR){
					lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(somarTotalDespesa(lancamentoReceita, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento())));
				}
			}
			lancamentoReceita.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataVencimento());
		}
			
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoReceita, buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento() == null ? parcelasPagamentoOs.getDataVencimento() : parcelasPagamentoOs.getDataPagamento())));
			
			//obter saldo atual
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
			
			//obter saldo dia anterior
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoAnterior(obterSaldoDiaAnterior(parcelasPagamentoOs.getDataPagamento() == null ? parcelasPagamentoOs.getDataVencimento() : parcelasPagamentoOs.getDataPagamento()));
			
			lancamentoReceita.setMovimentoFinanceiro(super.save(lancamentoReceita.getMovimentoFinanceiro()));
			lancamentoReceita = lancamentoFinanceiroService.save(lancamentoReceita);
			
			String condicaoPagamento = ordemServico.getCondicaoPagamento().getDescricao();
			if(condicaoPagamento.equals(A_VISTA)){
				salvarMovimentoOS(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
			}else{
				salvarMovimentoOS(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
			}
			
			this.movimentoFinanceiro = lancamentoReceita.getMovimentoFinanceiro();
			
		return lancamentoReceita.getMovimentoFinanceiro();
	}
	
	public MovimentoFinanceiro salvarMovimentoReceita(LancamentoFinanceiro lancamentoReceita){
		
		if(lancamentoReceita.getCategoriaLancamentoDespesa() != null){
			lancamentoReceita.setTipoLancamento(TipoLancamento.DESPESA);
		}else{
			lancamentoReceita.setTipoLancamento(TipoLancamento.RECEITA);
		}
		lancamentoReceita.setMovimentoFinanceiro(lancamentoReceita.getMovimentoFinanceiro());
		
		lancamentoReceita.setMovimentoFinanceiro(setarMovimentoFinanceiro(lancamentoReceita));
		
		
		if(lancamentoReceita.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
			if(lancamentoReceita.getTipoLancamento() == TipoLancamento.RECEITA){
				lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(somarTotalRecebido(lancamentoReceita, lancamentoReceita.getValor(), buscarMovimentoFinanceiroByData(lancamentoReceita.getDataLancamento())));
				if(lancamentoReceita.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_RECEBIDO){
					lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(
							lancamentoReceita.getMovimentoFinanceiro().getTotalReceita().signum() == 0 ? BigDecimal.ZERO :
								lancamentoReceita.getMovimentoFinanceiro().getTotalReceita().subtract(lancamentoReceita.getValor()));
				}
			}else{
				lancamentoReceita.getMovimentoFinanceiro().setTotalPago(somarTotalPago(lancamentoReceita, lancamentoReceita.getValor(), buscarMovimentoFinanceiroByData(lancamentoReceita.getDataLancamento())));
				if(lancamentoReceita.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO){
					lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(
							lancamentoReceita.getMovimentoFinanceiro().getTotalDespesa().signum() == 0 ? BigDecimal.ZERO : 
								lancamentoReceita.getMovimentoFinanceiro().getTotalDespesa().subtract(lancamentoReceita.getValor()));
				}
			}
			lancamentoReceita.getMovimentoFinanceiro().setDataMovimento(lancamentoReceita.getDataLancamento());
		}else{
			if(lancamentoReceita.getTipoLancamento() == TipoLancamento.RECEITA){
				lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(somarTotalReceita(lancamentoReceita, lancamentoReceita.getValor(), buscarMovimentoFinanceiroByData(lancamentoReceita.getDataLancamento())));
				if(lancamentoReceita.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER){
					lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(
							lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().signum() == 0 ? BigDecimal.ZERO : 
								lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().subtract(lancamentoReceita.getValor()));
				}
			}else{
				lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(somarTotalDespesa(lancamentoReceita, lancamentoReceita.getValor(), buscarMovimentoFinanceiroByData(lancamentoReceita.getDataLancamento())));
				if(lancamentoReceita.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_PAGAR){
					lancamentoReceita.getMovimentoFinanceiro().setTotalPago(
							lancamentoReceita.getMovimentoFinanceiro().getTotalPago().signum() == 0 ? BigDecimal.ZERO :
								lancamentoReceita.getMovimentoFinanceiro().getTotalPago().subtract(lancamentoReceita.getValor()));
				}
			}
			lancamentoReceita.getMovimentoFinanceiro().setDataMovimento(lancamentoReceita.getDataLancamento());
		}
			
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoReceita, buscarMovimentoFinanceiroByData(lancamentoReceita.getDataLancamento())));
			
			lancamentoReceita.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_SIMPLES);
			
			//obter saldo atual
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
			
			//obter saldo dia anterior
			lancamentoReceita.getMovimentoFinanceiro().setTotalSaldoAnterior(obterSaldoDiaAnterior(lancamentoReceita.getMovimentoFinanceiro().getDataMovimento()));
			
			lancamentoReceita.setMovimentoFinanceiro(super.save(lancamentoReceita.getMovimentoFinanceiro()));
			lancamentoFinanceiroService.save(lancamentoReceita);
			
			this.movimentoFinanceiro = lancamentoReceita.getMovimentoFinanceiro();
		
		return lancamentoReceita.getMovimentoFinanceiro();
	}
	
	@SuppressWarnings("unchecked")
	private List<MovimentoFinanceiro> buscarMovimentoFinanceiroByData(Date dataMovimentoFinanceiro){
		Query query = getEntityManager().createQuery("SELECT m FROM "+getEntityClass().getSimpleName() + " m "
				+ "WHERE m.dataMovimento = :dataMovimento");
		query.setParameter("dataMovimento", dataMovimentoFinanceiro);
		return query.getResultList();
	}
	
	public List<MovimentoFinanceiro> buscarMovimentoFinanceiroPorData(Date dataMov){
		return super.findByData("dataMovimento", dataMov);
	}
	
	private BigDecimal obterSaldoAtual(){
		BigDecimal valorTotal = BigDecimal.ZERO;
		for(MovimentoFinanceiro m : super.findAll()){
			valorTotal = valorTotal.add(m.getTotalSaldoOperacional());
		}
		return valorTotal;
	}
	
	private BigDecimal obterSaldoDiaAnterior(Date dataMov){
		LocalDate dataMovimento = DateUtil.asLocalDate(dataMov);
		List<MovimentoFinanceiro> movList = super.findAll();
		LocalDate dataMovimentoDiaAnterior = null;
		BigDecimal saldoAnterior = BigDecimal.ZERO;
		for(int i = 0; i < 1825; i++){
			dataMovimentoDiaAnterior = dataMovimento.minusDays(i+1);
			for(MovimentoFinanceiro mov2 : movList){
				if(mov2.getDataMovimento().compareTo(DateUtil.asDate(dataMovimentoDiaAnterior)) == 0){
					for(MovimentoFinanceiro m : buscarMovimentoFinanceiroPorData(DateUtil.asDate(dataMovimentoDiaAnterior))){
						return m.getTotalSaldoOperacional();
					}
				}
			}
		}
		
		return saldoAnterior;
		
	}

	public MovimentoFinanceiro getMovimentoFinanceiro() {
		return movimentoFinanceiro == null ? new MovimentoFinanceiro() : this.movimentoFinanceiro;
	}
	
	public MovimentoFinanceiro setarMovimentoFinanceiro(Date dataMov) {
		MovimentoFinanceiro m = new MovimentoFinanceiro();
		for(MovimentoFinanceiro mov : buscarMovimentoFinanceiroPorData(dataMov)){
			m.setDataMovimento(dataMov);
			m.setTotalDespesa(mov.getTotalDespesa());
			m.setTotalPago(mov.getTotalPago());
			m.setTotalRecebido(mov.getTotalRecebido());
			m.setTotalReceita(mov.getTotalReceita());
			m.setTotalSaldoAnterior(obterSaldoDiaAnterior(dataMov));
			m.setTotalSaldoAtual(obterSaldoAtual());
			m.setTotalSaldoOperacional(mov.getTotalSaldoOperacional());
			return m;
		}
		return m;
	}
	
	public List<MovimentoFinanceiro> listarContasPagarReceber(){
		List<MovimentoFinanceiro> movs = new ArrayList<MovimentoFinanceiro>();
		for(MovimentoFinanceiro mov : super.findAll()){
			if(mov.getTotalReceita().signum() != 0 || mov.getTotalDespesa().signum() != 0){
				movs.add(mov);
			}
		}
		return movs;
	}

}
