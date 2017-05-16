package br.com.sysge.controller.financ;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.service.financ.LancamentoFinanceiroService;
import br.com.sysge.service.financ.MovimentoFinanceiroService;
import br.com.sysge.util.FacesUtil;


@Named
@ViewScoped
public class MovimentoFinanceiroController implements Serializable {

	private static final long serialVersionUID = 514967643852507096L;
	
	@Inject
	private MovimentoFinanceiroService movimentoFinanceiroService;
	
	@Inject
	private LancamentoFinanceiroService lancamentoFinanceiroService;
	
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
