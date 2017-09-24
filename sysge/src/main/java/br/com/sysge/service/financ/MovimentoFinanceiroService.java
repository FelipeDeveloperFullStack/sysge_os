package br.com.sysge.service.financ;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.infraestrutura.relatorios.ReportFactory;
import br.com.sysge.infraestrutura.relatorios.TiposRelatorio;
import br.com.sysge.model.financ.AuditoriaFinanceiro;
import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.model.financ.MovimentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.CategoriaLancamentoReceita;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.financ.type.TipoAtualizacaoMovimento;
import br.com.sysge.model.financ.type.TipoLancamento;
import br.com.sysge.model.financ.type.TipoLancamentoFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.model.type.StatusOS;
import br.com.sysge.util.DateUtil;
import br.com.sysge.util.UsuarioSession;

public class MovimentoFinanceiroService extends GenericDaoImpl<MovimentoFinanceiro, Long>{
	
	private static final long serialVersionUID = 3150386706435445711L;
	
	private static final String A_VISTA = "À Vista";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private LancamentoFinanceiroService lancamentoFinanceiroService;
	
	@SuppressWarnings("unused")
	
	private ParcelasPagamentoOsService parcelasPagamentoOsService;
	
	private AuditoriaFinanceiroService auditoriaFinanceiroService;
	
	private AuditoriaFinanceiro auditoriaFinanceiro;
	
	private MovimentoFinanceiro movimentoFinanceiro;
	
	public MovimentoFinanceiroService(){
		this.parcelasPagamentoOsService = new ParcelasPagamentoOsService();
		this.auditoriaFinanceiroService = new AuditoriaFinanceiroService();
		this.lancamentoFinanceiroService = new LancamentoFinanceiroService();
	}
	
	public void salvarMovimentoFinanceiroOS(OrdemServico ordemServico, ParcelasPagamentoOs parcelasPagamentoOs){
		
		//Atualizando o objeto
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
			if(ordemServico.getStatusOS() == StatusOS.CANCELADO){
				lancamentoReceita = subtratirTotalRecebidoTotalReceitaCalcularMovimentoFinanceiro(lancamentoReceita, parcelasPagamentoOs);
				lancamentoReceita.setTitulo("(Cancelado) Ordem Serviço nº "+ordemServico.getId());
			}else{
				lancamentoReceita.setTitulo("Ordem Serviço nº "+ordemServico.getId());
			}
		}else{
			lancamentoReceita.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_PARCELADO);
			if(ordemServico.getStatusOS() == StatusOS.CANCELADO){
				lancamentoReceita = subtratirTotalRecebidoTotalReceitaCalcularMovimentoFinanceiro(lancamentoReceita, parcelasPagamentoOs);
				lancamentoReceita.setTitulo("(Cancelado) Ordem Serviço nº "+ordemServico.getId());
			}else{
				lancamentoReceita.setTitulo("Ordem Serviço nº "+ordemServico.getId()+ " - "+parcelasPagamentoOs.getNumero()+"º parcela");
			}
		}
		if(ordemServico.getStatusOS() == StatusOS.CANCELADO){
			lancamentoReceita.setStatusOS(StatusOS.CANCELADO);
			lancamentoReceita.setValor(BigDecimal.ZERO);
		}else{
			lancamentoReceita.setValor(parcelasPagamentoOs.getValorCobrado());
		}
		lancamentoReceita = lancamentoFinanceiroService.save(lancamentoReceita);
		parcelasPagamentoOs.setLancamentoReceita(lancamentoReceita);
	}
	
	private LancamentoFinanceiro subtratirTotalRecebidoTotalReceitaCalcularMovimentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro, ParcelasPagamentoOs parcelasPagamentoOs){
		if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
			if(lancamentoFinanceiro.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalRecebido(subtratirTotalRecebido(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataPagamento());
			}else{
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(subtratirTotalReceita(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataVencimento());
			}
		    lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoFinanceiro, buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento() == null ? parcelasPagamentoOs.getDataVencimento() : parcelasPagamentoOs.getDataPagamento())));
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAnterior(obterSaldoMovimentoAnterior(parcelasPagamentoOs.getDataPagamento() == null ? parcelasPagamentoOs.getDataVencimento() : parcelasPagamentoOs.getDataPagamento()));
			lancamentoFinanceiro.setMovimentoFinanceiro(super.save(lancamentoFinanceiro.getMovimentoFinanceiro()));
		}
		return lancamentoFinanceiro;
	}
	
	private BigDecimal subtratirTotalRecebido(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido().subtract(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalRecebido(m.getTotalRecebido().subtract(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalRecebido();
	}
	
	private BigDecimal subtratirTotalRecebido(BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		for(MovimentoFinanceiro m : listMovimentoFinanceiro){
			m.setTotalRecebido(m.getTotalRecebido().subtract(valor));
			return m.getTotalRecebido();
		}
		return valor;
	}
	
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
	
	private BigDecimal subtratirTotalPago(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalPago(lancamentoReceita.getMovimentoFinanceiro().getTotalPago().subtract(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalPago(m.getTotalPago().subtract(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalPago();
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
	private BigDecimal subtratirTotalReceita(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(lancamentoReceita.getMovimentoFinanceiro().getTotalReceita().subtract(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalReceita(m.getTotalReceita().subtract(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalReceita();
	}
	
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
	
	private BigDecimal subtrairTotalDespesa(LancamentoFinanceiro lancamentoReceita, BigDecimal valor, List<MovimentoFinanceiro> listMovimentoFinanceiro){
		if(listMovimentoFinanceiro.isEmpty()){
			lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(lancamentoReceita.getMovimentoFinanceiro().getTotalDespesa().subtract(valor));
		}else{
			for(MovimentoFinanceiro m : listMovimentoFinanceiro){
				lancamentoReceita.getMovimentoFinanceiro().setTotalDespesa(m.getTotalDespesa().subtract(valor));
			}
		}
		return lancamentoReceita.getMovimentoFinanceiro().getTotalDespesa();
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
	
	
	private LancamentoFinanceiro setarLancamentoFinanceiro(ParcelasPagamentoOs parcelasPagamentoOs){
		parcelasPagamentoOs.getLancamentoReceita().setDataLancamento(parcelasPagamentoOs.getDataPagamento());
		parcelasPagamentoOs.getLancamentoReceita().setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO);
		parcelasPagamentoOs.getLancamentoReceita().setTipoLancamento(TipoLancamento.RECEITA);
		parcelasPagamentoOs.getLancamentoReceita().setMovimentoFinanceiro(criarMovimentoDia(parcelasPagamentoOs.getDataPagamento(), 
																		 parcelasPagamentoOs.getLancamentoReceita().getMovimentoFinanceiro()));
		return parcelasPagamentoOs.getLancamentoReceita();
	}
	
	
	private MovimentoFinanceiro calcularMovimentoParcelasPagamentoOS
			(OrdemServico ordemServico,LancamentoFinanceiro lancamentoFinanceiro, ParcelasPagamentoOs parcelasPagamentoOs){
		
			if(lancamentoFinanceiro.getCategoriaLancamentoDespesa() != null){
				lancamentoFinanceiro.setTipoLancamento(TipoLancamento.DESPESA);
			}else{
				lancamentoFinanceiro.setTipoLancamento(TipoLancamento.RECEITA);
			}
			
			if(parcelasPagamentoOs.getDataPagamento() != null){
				lancamentoFinanceiro.setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO);
				if(parcelasPagamentoOs.getDataPagamento().compareTo(parcelasPagamentoOs.getDataVencimento()) != 0){
					lancamentoFinanceiro = setarLancamentoFinanceiro(parcelasPagamentoOs);
				}
			}else{
				lancamentoFinanceiro.setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER);
				lancamentoFinanceiro.setMovimentoFinanceiro(lancamentoFinanceiro.getMovimentoFinanceiro());
				lancamentoFinanceiro.setMovimentoFinanceiro(setarMovimentoFinanceiroParcelaOS(parcelasPagamentoOs.getDataVencimento() == null 
						? parcelasPagamentoOs.getDataPagamento() : parcelasPagamentoOs.getDataVencimento()));
			}
			
			parcelasPagamentoOs.setLancamentoReceita(lancamentoFinanceiro);
			
			
		if(parcelasPagamentoOs.getStatusFinanceiro() == StatusFinanceiro.PAGO){
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_RECEBIDO){
					if(ordemServico.getStatusOS() != StatusOS.CANCELADO){
						lancamentoFinanceiro.getMovimentoFinanceiro().setTotalRecebido(somarTotalRecebido(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento())));
					}
				}
			}else{
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalPago(somarTotalPago(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento())));
				}
			}
			lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataPagamento());
		}else{
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
				if(ordemServico.getStatusOS() != StatusOS.CANCELADO){
					if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() == null || lancamentoFinanceiro.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER){
						lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(verificarSaldoOSMovimento(buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento()), parcelasPagamentoOs, lancamentoFinanceiro).getTotalReceita());
					}else{
						if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER){
							//lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(somarTotalReceita(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento())));
							lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(verificarSaldoOSMovimento(buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento()), parcelasPagamentoOs, lancamentoFinanceiro).getTotalReceita());
						}
					}
				}
			}else{
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() != TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_PAGAR){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalDespesa(somarTotalDespesa(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataVencimento())));
				}
			}
			lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(parcelasPagamentoOs.getDataVencimento());
		}
		
			if(parcelasPagamentoOs.getDataPagamento() != null){
				verificarSeExisteMovimento(parcelasPagamentoOs.getDataVencimento(), lancamentoFinanceiro);
			}
			
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoFinanceiro, buscarMovimentoFinanceiroByData(parcelasPagamentoOs.getDataPagamento() == null ? parcelasPagamentoOs.getDataVencimento() : parcelasPagamentoOs.getDataPagamento())));
			
			//obter saldo atual
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
			
			//obter saldo dia anterior
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAnterior(obterSaldoMovimentoAnterior(parcelasPagamentoOs.getDataPagamento() == null ? parcelasPagamentoOs.getDataVencimento() : parcelasPagamentoOs.getDataPagamento()));
			
			lancamentoFinanceiro.setMovimentoFinanceiro(super.save(lancamentoFinanceiro.getMovimentoFinanceiro()));
			
			String condicaoPagamento = ordemServico.getCondicaoPagamento().getDescricao();
			if(condicaoPagamento.equals(A_VISTA)){
				salvarMovimentoOS(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
			}else{
				salvarMovimentoOS(ordemServico, parcelasPagamentoOs, condicaoPagamento, parcelasPagamentoOs.getLancamentoReceita());
			}
			
			this.movimentoFinanceiro = lancamentoFinanceiro.getMovimentoFinanceiro();
			
		return lancamentoFinanceiro.getMovimentoFinanceiro();
	}
	
	private MovimentoFinanceiro verificarSaldoOSMovimento(List<MovimentoFinanceiro> listMov, ParcelasPagamentoOs parcelasPagamentoOs, LancamentoFinanceiro lancamentoFinanceiro){
		if(parcelasPagamentoOs.getValorCobrado().compareTo(lancamentoFinanceiro.getMovimentoFinanceiro().getTotalReceita()) != 0){
			if(listMov.isEmpty()){
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(BigDecimal.ZERO);
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(somarTotalReceita(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), listMov));
			}else{
				for(MovimentoFinanceiro m : listMov){
					if(lancamentoFinanceiro.getValor() != null){
						if(parcelasPagamentoOs.getValorCobrado().compareTo(lancamentoFinanceiro.getValor()) == 0){
							m.setTotalReceita(somarTotalReceita(lancamentoFinanceiro, BigDecimal.ZERO, listMov));
						}else{
							m.setTotalReceita(somarTotalReceita(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado().subtract(lancamentoFinanceiro.getValor()), listMov));
						}
					}else{
						m.setTotalReceita(somarTotalReceita(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), listMov));
					}
					lancamentoFinanceiro.setMovimentoFinanceiro(m);
				}
			}
			return lancamentoFinanceiro.getMovimentoFinanceiro();
		}else{
			for(MovimentoFinanceiro m : listMov){
				m.setTotalReceita(somarTotalReceita(lancamentoFinanceiro, parcelasPagamentoOs.getValorCobrado(), listMov));
				lancamentoFinanceiro.setMovimentoFinanceiro(m);
			}
			return lancamentoFinanceiro.getMovimentoFinanceiro();
		}
	}
	
	private void verificarSeExisteMovimento(Date dataVencimento, LancamentoFinanceiro lancamentoFinanceiro){
		MovimentoFinanceiro mov = super.findByData(dataVencimento, "dataMovimento");
		
		if(lancamentoFinanceiroService.findByData("dataLancamento", mov.getDataMovimento()).isEmpty()){
			mov.setTotalRecebido(subtratirTotalRecebido(lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(mov.getDataMovimento())));
			
			mov.setTotalSaldoAnterior(obterSaldoMovimentoAnterior(mov.getDataMovimento()));
			
			mov.setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoFinanceiro, buscarMovimentoFinanceiroByData(mov.getDataMovimento())));
			
			mov.setTotalSaldoAtual(obterSaldoAtual());
			
			super.save(mov);
		}
	}
	
	private void consistiLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		lancamentoFinanceiro.consistirCliente(lancamentoFinanceiro);
		lancamentoFinanceiro.consistirFornecedor(lancamentoFinanceiro);
		lancamentoFinanceiro.consistirData(lancamentoFinanceiro);
		lancamentoFinanceiro.consistirTitulo(lancamentoFinanceiro);
		lancamentoFinanceiro.consistirValor(lancamentoFinanceiro);
	}
	
	public void setarAuditoriaFinanceiro(AuditoriaFinanceiro auditoriaFinanceiro){
		this.auditoriaFinanceiro = auditoriaFinanceiro;
	}
	
	public void consistirAuditoria(){
		auditoriaFinanceiroService.consistir(this.auditoriaFinanceiro);
	}
	
	private void logAuditoria(){
			this.auditoriaFinanceiro.setMensagem("O usuário "+UsuarioSession.getSessionUsuario().getNomeUsuario()+" realizou a exclusão do título financeiro "
					+ this.auditoriaFinanceiro.getTituloFinanceiro()+" no valor de R$: "+this.auditoriaFinanceiro.getValor()+" na categoria "
					+ this.auditoriaFinanceiro.getCategoria()+" no dia "
					+ new SimpleDateFormat("dd/MM/yyyy").format(this.auditoriaFinanceiro.getData())+" "
							+ "as "+new SimpleDateFormat("HH:mm:ss").format(this.auditoriaFinanceiro.getHora()));
			auditoriaFinanceiroService.save(this.auditoriaFinanceiro);
	}
	
	public void excluirLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		try {
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
				
				if(lancamentoFinanceiro.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalRecebido(subtratirTotalRecebido(lancamentoFinanceiro, 
							lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				}else{
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(subtratirTotalReceita(lancamentoFinanceiro, 
							lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				}
			}else{
				if(lancamentoFinanceiro.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalPago(subtratirTotalPago(lancamentoFinanceiro, 
							lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				}else{
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalDespesa(subtrairTotalDespesa(lancamentoFinanceiro, 
							lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				}
			}
			
			//obter saldo dia anterior
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAnterior(
					obterSaldoMovimentoAnterior(lancamentoFinanceiro.getMovimentoFinanceiro().getDataMovimento()));
			
			lancamentoFinanceiro.setMovimentoFinanceiro(super.save(lancamentoFinanceiro.getMovimentoFinanceiro()));
			
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoOperacional(
					somarTotalSaldoOperacional(lancamentoFinanceiro, 
							buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
			
			super.save(lancamentoFinanceiro.getMovimentoFinanceiro());
			
			//obter saldo atual
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
			
			super.save(lancamentoFinanceiro.getMovimentoFinanceiro());
			
			lancamentoFinanceiroService.remove(lancamentoFinanceiro.getId());
			
			logAuditoria();
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public MovimentoFinanceiro salvarMovimentoFinanceiroOrdemServicoPorPagamento(LancamentoFinanceiro lancamentoFinanceiro){
		
		lancamentoFinanceiro.setMovimentoFinanceiro(lancamentoFinanceiro.getMovimentoFinanceiro());
		
		lancamentoFinanceiro.setMovimentoFinanceiro(setarMovimentoFinanceiro(lancamentoFinanceiro));
		
		if(lancamentoFinanceiro.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
				
				lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(lancamentoFinanceiro.getDataLancamento());
				
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalRecebido(somarTotalRecebido(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoFinanceiro, buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				
				lancamentoFinanceiro.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_SIMPLES);
				
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
				
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAnterior(obterSaldoMovimentoAnterior(lancamentoFinanceiro.getDataLancamento()));
				
				lancamentoFinanceiro.setMovimentoFinanceiro(super.save(lancamentoFinanceiro.getMovimentoFinanceiro()));
				
				lancamentoFinanceiroService.save(lancamentoFinanceiro);
				
				this.movimentoFinanceiro = lancamentoFinanceiro.getMovimentoFinanceiro();
			}
		}	
		return lancamentoFinanceiro.getMovimentoFinanceiro();
	}
	
	
	public MovimentoFinanceiro salvarMovimentoReceita(LancamentoFinanceiro lancamentoFinanceiro){
		
		
		if(lancamentoFinanceiro.getCategoriaLancamentoDespesa() != null){
			lancamentoFinanceiro.setTipoLancamento(TipoLancamento.DESPESA);
		}else{
			lancamentoFinanceiro.setTipoLancamento(TipoLancamento.RECEITA);
		}
		consistiLancamentoFinanceiro(lancamentoFinanceiro);//Validar cliente/fornecedor
		lancamentoFinanceiro.setMovimentoFinanceiro(lancamentoFinanceiro.getMovimentoFinanceiro());
		
		lancamentoFinanceiro.setMovimentoFinanceiro(setarMovimentoFinanceiro(lancamentoFinanceiro));
		
		
		if(lancamentoFinanceiro.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalRecebido(somarTotalRecebido(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_RECEBIDO){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(
							lancamentoFinanceiro.getMovimentoFinanceiro().getTotalReceita().signum() == 0 ? BigDecimal.ZERO :
								lancamentoFinanceiro.getMovimentoFinanceiro().getTotalReceita().subtract(lancamentoFinanceiro.getValor()));
				}
			}else{
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalPago(somarTotalPago(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalDespesa(
							lancamentoFinanceiro.getMovimentoFinanceiro().getTotalDespesa().signum() == 0 ? BigDecimal.ZERO : 
								lancamentoFinanceiro.getMovimentoFinanceiro().getTotalDespesa().subtract(lancamentoFinanceiro.getValor()));
				}
			}
			lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(lancamentoFinanceiro.getDataLancamento());
		}else{
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.RECEITA){
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalReceita(somarTotalReceita(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalRecebido(
							lancamentoFinanceiro.getMovimentoFinanceiro().getTotalRecebido().signum() == 0 ? BigDecimal.ZERO : 
								lancamentoFinanceiro.getMovimentoFinanceiro().getTotalRecebido().subtract(lancamentoFinanceiro.getValor()));
				}
			}else{
				lancamentoFinanceiro.getMovimentoFinanceiro().setTotalDespesa(somarTotalDespesa(lancamentoFinanceiro, lancamentoFinanceiro.getValor(), buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
				if(lancamentoFinanceiro.getTipoAtualizacaoMovimento() == TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_PAGAR){
					lancamentoFinanceiro.getMovimentoFinanceiro().setTotalPago(
							lancamentoFinanceiro.getMovimentoFinanceiro().getTotalPago().signum() == 0 ? BigDecimal.ZERO :
								lancamentoFinanceiro.getMovimentoFinanceiro().getTotalPago().subtract(lancamentoFinanceiro.getValor()));
				}
			}
			lancamentoFinanceiro.getMovimentoFinanceiro().setDataMovimento(lancamentoFinanceiro.getDataLancamento());
		}
			
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoOperacional(somarTotalSaldoOperacional(lancamentoFinanceiro, buscarMovimentoFinanceiroByData(lancamentoFinanceiro.getDataLancamento())));
			
			lancamentoFinanceiro.setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro.LANCAMENTO_SIMPLES);
			
			//obter saldo atual
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAtual(obterSaldoAtual());
			
			//obter saldo dia anterior
			lancamentoFinanceiro.getMovimentoFinanceiro().setTotalSaldoAnterior(obterSaldoMovimentoAnterior(lancamentoFinanceiro.getMovimentoFinanceiro().getDataMovimento()));
			
			lancamentoFinanceiro.setMovimentoFinanceiro(super.save(lancamentoFinanceiro.getMovimentoFinanceiro()));
			lancamentoFinanceiroService.save(lancamentoFinanceiro);
			
			this.movimentoFinanceiro = lancamentoFinanceiro.getMovimentoFinanceiro();
		
		return lancamentoFinanceiro.getMovimentoFinanceiro();
	}
	
	@SuppressWarnings({ "unchecked"})
	private List<MovimentoFinanceiro> buscarMovimentoFinanceiroByData(Date dataMovimentoFinanceiro){
		try {
			Query query = getEntityManager().createQuery("SELECT m FROM "+getEntityClass().getSimpleName() + " m "
					+ "WHERE m.dataMovimento = :dataMovimento");
			query.setParameter("dataMovimento", dataMovimentoFinanceiro);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
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
	
	private BigDecimal obterSaldoMovimentoAnterior(Date dataMov){
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
		MovimentoFinanceiro m = super.findByData(dataMov, "dataMovimento");
		for(MovimentoFinanceiro mov : buscarMovimentoFinanceiroPorData(dataMov)){
			m.setDataMovimento(dataMov);
			m.setTotalDespesa(mov.getTotalDespesa());
			m.setTotalPago(mov.getTotalPago());
			m.setTotalRecebido(mov.getTotalRecebido());
			m.setTotalReceita(mov.getTotalReceita());
			m.setTotalSaldoAnterior(obterSaldoMovimentoAnterior(dataMov));
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
	
	
	private MovimentoFinanceiro criarMovimentoDia(Date dataLancamento, MovimentoFinanceiro movimentoFinanceiro){
		if(buscarMovimentoFinanceiroByData(dataLancamento).isEmpty()){
			MovimentoFinanceiro mov = new MovimentoFinanceiro();
			mov.setDataMovimento(dataLancamento);
			return super.save(mov);
		}
		return movimentoFinanceiro;
	}
	
	public boolean isHabilitarBotao(LancamentoFinanceiro lancamentoFinanceiro){
		if(lancamentoFinanceiro.getCategoriaLancamentoReceita() == CategoriaLancamentoReceita.ORDEM_SERVICO){
			return false;
		}else{
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiro> obterMovimentoPorPeriodo(Date dataInicial, Date dataFinal){
		Query query = getEntityManager().createQuery("SELECT m FROM "+getEntityClass().getSimpleName() + " m "
				+ "WHERE m.dataMovimento >= :dataInicial and m.dataMovimento <= :dataFinal order by m.dataMovimento ASC");
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		return query.getResultList();
	}
	
	public void gerarRelatorioMovimentoFinanceiroPorPeriodo(Date dataInicial, Date dataFinal){
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(dataInicial == null){
			throw new RuntimeException("A data inicial é obrigatória");
		}
		
		if(dataFinal == null){
			throw new RuntimeException("A data final é obrigatória");
		}
		
		try {
			params.put("dataInicial", dataInicial);
			params.put("dataFinal", dataFinal);
			params.put("saldoAtual", obterSaldoAtual());
			
			ReportFactory reportFactory = new ReportFactory("r_movimento_financeiro.jasper", params, 
					TiposRelatorio.PDF, obterMovimentoPorPeriodo(dataInicial, dataFinal));
			
			reportFactory.gerarPDFView("Relatório de Movimento Financeiro");
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
