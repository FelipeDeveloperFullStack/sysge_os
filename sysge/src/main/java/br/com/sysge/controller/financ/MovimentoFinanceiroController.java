package br.com.sysge.controller.financ;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.service.financ.LancamentoFinanceiroService;
import br.com.sysge.service.financ.MovimentoFinanceiroService;
import br.com.sysge.service.financ.ParcelasPagamentoOsService;
import br.com.sysge.util.FacesUtil;


@Named
@ViewScoped
public class MovimentoFinanceiroController implements Serializable {

	private static final long serialVersionUID = 514967643852507096L;
	
	@Inject
	private MovimentoFinanceiroService movimentoFinanceiroService;
	
	@Inject
	private LancamentoFinanceiroService lancamentoFinanceiroService;
	
	@Inject
	private ParcelasPagamentoOsService parcelasPagamentoOsService;
	
	private List<LancamentoFinanceiro> lancamentoFinanceiros;
	
	private LancamentoFinanceiro lancamentoFinanceiro;
	
	private Date dataMovimento = new Date();
	
	public void pesquisar(){
		try {
			lancamentoFinanceiros = lancamentoFinanceiroService.obterLancamentoFinanceiroPorData(dataMovimento);
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void setarDadosFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		this.lancamentoFinanceiro = lancamentoFinanceiro;
	}
	
	public void atualizarStatusFinanceiroTituto(LancamentoFinanceiro lancamentoFinanceiro){
		if(lancamentoFinanceiro.getStatusRecebimentoReceita() == StatusFinanceiro.PAGO){
			this.lancamentoFinanceiro = lancamentoFinanceiro;
			this.lancamentoFinanceiro.setStatusRecebimentoReceita(StatusFinanceiro.PENDENTE);
			obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(this.lancamentoFinanceiro);
			lancamentoFinanceiroService.save(this.lancamentoFinanceiro);
			lancamentoFinanceiros = obterTitulosPelaDataMovimento(this.lancamentoFinanceiro.getDataLancamento());
		}else{
			this.lancamentoFinanceiro = lancamentoFinanceiro;
			this.lancamentoFinanceiro.setStatusRecebimentoReceita(StatusFinanceiro.PAGO);
			obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(this.lancamentoFinanceiro);
			lancamentoFinanceiroService.save(this.lancamentoFinanceiro);
			lancamentoFinanceiros = obterTitulosPelaDataMovimento(this.lancamentoFinanceiro.getDataLancamento());
		}
	}
	
	private void obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		ParcelasPagamentoOs p = parcelasPagamentoOsService.obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(lancamentoFinanceiro);
		p.setStatusFinanceiro(lancamentoFinanceiro.getStatusRecebimentoReceita());
		parcelasPagamentoOsService.save(p);
	}
	
	private List<LancamentoFinanceiro> obterTitulosPelaDataMovimento(Date dataMovimento){
		return lancamentoFinanceiroService.obterLancamentoFinanceiroPorData(dataMovimento);
	}

	public List<LancamentoFinanceiro> getLancamentoFinanceiros() {
		return lancamentoFinanceiros;
	}

	public void setLancamentoFinanceiros(List<LancamentoFinanceiro> lancamentoFinanceiros) {
		this.lancamentoFinanceiros = lancamentoFinanceiros;
	}

	public LancamentoFinanceiro getLancamentoFinanceiro() {
		return lancamentoFinanceiro == null ? new LancamentoFinanceiro() : this.lancamentoFinanceiro;
	}

	public void setLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro) {
		this.lancamentoFinanceiro = lancamentoFinanceiro;
	}

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	
	

}
