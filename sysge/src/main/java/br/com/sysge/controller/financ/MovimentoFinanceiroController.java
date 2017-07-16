package br.com.sysge.controller.financ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.sysge.model.financ.LancamentoFinanceiro;
import br.com.sysge.model.financ.MovimentoFinanceiro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.CategoriaLancamentoDespesa;
import br.com.sysge.model.financ.type.CategoriaLancamentoReceita;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.financ.type.TipoAtualizacaoMovimento;
import br.com.sysge.model.financ.type.TipoLancamento;
import br.com.sysge.model.global.Cliente;
import br.com.sysge.model.global.Fornecedor;
import br.com.sysge.model.type.FormaPagamento;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.service.financ.LancamentoFinanceiroService;
import br.com.sysge.service.financ.MovimentoFinanceiroService;
import br.com.sysge.service.financ.ParcelasPagamentoOsService;
import br.com.sysge.service.global.ClienteService;
import br.com.sysge.service.global.FornecedorService;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;


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
	
	private Date dataLancamentoReceita = new Date();
	
	private Date dataLancamentoDespesa = new Date();
	
	private MovimentoFinanceiro movimentoFinanceiro;
	
	@Inject
	private ClienteService clienteService;
	
	@Inject
	private FornecedorService fornecedorService;
	
	public List<CategoriaLancamentoReceita> getCategoriasLancamentoReceita(){
		List<CategoriaLancamentoReceita> receita = new ArrayList<CategoriaLancamentoReceita>();
		receita.add(CategoriaLancamentoReceita.RECEBIMENTOS_DIVERSOS);
		return receita;
	}
	
	public CategoriaLancamentoDespesa[] getCategoriasLancamentoDespesa(){
		return CategoriaLancamentoDespesa.values();
	}
	
	public FormaPagamento[] getFormasPagamento(){
		return FormaPagamento.values();
	}
	
	public StatusFinanceiro[] getStatusFinanceiro(){
		return StatusFinanceiro.values();
	}
	
	public List<Cliente> getClientes(){
		return clienteService.findBySituation(Situacao.ATIVO);
	}
	
	public List<Fornecedor> getFornecedores(){
		return fornecedorService.findBySituation(Situacao.ATIVO);
	}
	
	public void novoLancamento(){
		this.lancamentoFinanceiro = new LancamentoFinanceiro();
	}
	
	public String mudarCorValorMovimento(LancamentoFinanceiro lancamentoFinanceiro){
		if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.DESPESA){
			return "cor_vermelho";
		}else{
			return "cor_azul";
		}
	}
	
	public boolean isHabilitarBotao(LancamentoFinanceiro lancamentoFinanceiro){
		return movimentoFinanceiroService.isHabilitarBotao(lancamentoFinanceiro);
	}
	
	public List<MovimentoFinanceiro> getContasPagarReceber(){
		return movimentoFinanceiroService.listarContasPagarReceber();
	}
	
	public void salvarLancamento(){
		try {
			movimentoFinanceiroService.salvarMovimentoReceita(lancamentoFinanceiro);
			RequestContextUtil.execute("PF('dialog_lancamento_receita').hide();");
			RequestContextUtil.execute("PF('dialog_lancamento_despesa').hide();");
			this.lancamentoFinanceiros = lancamentoFinanceiroService.obterLancamentoFinanceiroPorData(lancamentoFinanceiro.getDataLancamento());
			this.movimentoFinanceiro = movimentoFinanceiroService.setarMovimentoFinanceiro(lancamentoFinanceiro.getDataLancamento());
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void excluirLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		try {
			movimentoFinanceiroService.excluirLancamentoFinanceiro(lancamentoFinanceiro);
			this.lancamentoFinanceiros = lancamentoFinanceiroService.obterLancamentoFinanceiroPorData(lancamentoFinanceiro.getDataLancamento());
			this.movimentoFinanceiro = movimentoFinanceiroService.setarMovimentoFinanceiro(lancamentoFinanceiro.getDataLancamento());
			FacesUtil.mensagemInfo("Título financeiro excluído com sucesso!");
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void pesquisar(){
		try {
			lancamentoFinanceiros = lancamentoFinanceiroService.obterLancamentoFinanceiroPorData(dataMovimento);
			this.movimentoFinanceiro = movimentoFinanceiroService.setarMovimentoFinanceiro(dataMovimento);
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
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.DESPESA){
				this.lancamentoFinanceiro.setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_PAGAR);
			}else{
				this.lancamentoFinanceiro.setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_CONTA_A_RECEBER);
			}
			obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(this.lancamentoFinanceiro);
			lancamentoFinanceiroService.save(this.lancamentoFinanceiro);
			if(lancamentoFinanceiro.getCategoriaLancamentoReceita() == CategoriaLancamentoReceita.ORDEM_SERVICO){
				movimentoFinanceiroService.salvarMovimentoReceita(lancamentoFinanceiro);
			}else{
				movimentoFinanceiroService.salvarMovimentoReceita(lancamentoFinanceiro);
			}
			lancamentoFinanceiros = obterTitulosPelaDataMovimento(this.lancamentoFinanceiro.getDataLancamento());
			this.movimentoFinanceiro = movimentoFinanceiroService.setarMovimentoFinanceiro(dataMovimento);
		}else{
			this.lancamentoFinanceiro = lancamentoFinanceiro;
			this.lancamentoFinanceiro.setStatusRecebimentoReceita(StatusFinanceiro.PAGO);
			if(lancamentoFinanceiro.getTipoLancamento() == TipoLancamento.DESPESA){
				this.lancamentoFinanceiro.setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_PAGO);
			}else{
				this.lancamentoFinanceiro.setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento.ATUALIZADO_P_RECEBIDO);
			}
			obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(this.lancamentoFinanceiro);
			lancamentoFinanceiroService.save(this.lancamentoFinanceiro);
			
			movimentoFinanceiroService.salvarMovimentoReceita(lancamentoFinanceiro);
			lancamentoFinanceiros = obterTitulosPelaDataMovimento(this.lancamentoFinanceiro.getDataLancamento());
			this.movimentoFinanceiro = movimentoFinanceiroService.setarMovimentoFinanceiro(dataMovimento);
		}
	}
	
	private void obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(LancamentoFinanceiro lancamentoFinanceiro){
		if(lancamentoFinanceiro.getCategoriaLancamentoReceita() != null){
			if(lancamentoFinanceiro.getCategoriaLancamentoReceita() == CategoriaLancamentoReceita.ORDEM_SERVICO){
				ParcelasPagamentoOs p = parcelasPagamentoOsService.obterDadosParcelasPagamentoOsPorLancamentoFinanceiro(lancamentoFinanceiro);
				p.setStatusFinanceiro(lancamentoFinanceiro.getStatusRecebimentoReceita());
				parcelasPagamentoOsService.save(p);
			}
		}
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

	public Date getDataLancamentoReceita() {
		return dataLancamentoReceita;
	}

	public void setDataLancamentoReceita(Date dataLancamentoReceita) {
		this.dataLancamentoReceita = dataLancamentoReceita;
	}

	public Date getDataLancamentoDespesa() {
		return dataLancamentoDespesa;
	}

	public void setDataLancamentoDespesa(Date dataLancamentoDespesa) {
		this.dataLancamentoDespesa = dataLancamentoDespesa;
	}

	public MovimentoFinanceiro getMovimentoFinanceiro() {
		return movimentoFinanceiro;
	}

	public void setMovimentoFinanceiro(MovimentoFinanceiro movimentoFinanceiro) {
		this.movimentoFinanceiro = movimentoFinanceiro;
	}

}
