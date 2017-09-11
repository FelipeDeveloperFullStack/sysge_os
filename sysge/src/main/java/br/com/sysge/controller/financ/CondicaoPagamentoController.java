package br.com.sysge.controller.financ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;



import br.com.sysge.model.financ.CondicaoPagamento;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.service.financ.CondicaoPagamentoService;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;


@ViewScoped
@ManagedBean
public class CondicaoPagamentoController implements Serializable{

	private static final long serialVersionUID = 8114651297348114528L;
	
	private CondicaoPagamento condicaoPagamento;
	
	private static final String A_VISTA = "À Vista";
	
	private String descricao = A_VISTA;
	
	private List<CondicaoPagamento> condicoesPagamento;
	
	private CondicaoPagamentoService condicaoPagamentoService = new CondicaoPagamentoService();
	
	@PostConstruct
	public void init() {
		novaListaFormaPagamento();
	}
	
	public void novaListaFormaPagamento(){
		condicoesPagamento = new ArrayList<CondicaoPagamento>();
	}

	public void novo() {
		descricao = A_VISTA;
		this.condicaoPagamento = new CondicaoPagamento();
	}
	
	public void pesquisar(){
		this.condicoesPagamento = new ArrayList<CondicaoPagamento>();
		this.condicoesPagamento = condicaoPagamentoService.pesquisarCondicaoPagamento(condicaoPagamento);
	}

	public void salvar() {
		try {
			condicaoPagamento.setDescricao(descricao);
			verificarSeExisteCondicaoPagamentoCadastradoComMesmaDescricao(condicaoPagamento);
	        condicaoPagamentoService.salvar(condicaoPagamento);
			FacesUtil.mensagemInfo("Condição de pagamento salvo com sucesso!");
			novaListaFormaPagamento();
			fecharDialogs();
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	private void verificarSeExisteCondicaoPagamentoCadastradoComMesmaDescricao(CondicaoPagamento condicaoPagamento){
		condicaoPagamentoService.verificarSeExisteCondicaoPagamentoCadastradoComMesmaDescricao(condicaoPagamento);
	}
	
	public void calcularCondicaoPagamento(){
		descricao = condicaoPagamentoService.calcularCondicaoPagamento(condicaoPagamento, descricao);
	}
	
	public void editarCondicaoPagamento(CondicaoPagamento condicaoPagamento){
		this.condicaoPagamento = condicaoPagamento;
		RequestContextUtil.execute("PF('confirmarAtualizacaoStatus').show();");
	}
	
	public void editarCondicaoPagamento(){
		try {
			if(this.condicaoPagamento.getSituacao() == Situacao.ATIVO){
				this.condicaoPagamento.setSituacao(Situacao.INATIVO);
			}else{
				this.condicaoPagamento.setSituacao(Situacao.ATIVO);
			}
			condicaoPagamentoService.salvar(this.condicaoPagamento);
			novaListaFormaPagamento();
			this.condicaoPagamento = new CondicaoPagamento();
			RequestContextUtil.execute("PF('confirmarAtualizacaoStatus').hide();");
			FacesUtil.mensagemInfo("Status atualizado com sucesso!");
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoServico').hide();");
	}


	public Situacao[] getSituacoes() {
		return Situacao.values();
	}

	public CondicaoPagamento getCondicaoPagamento() {
		if(this.condicaoPagamento == null){
			this.condicaoPagamento = new CondicaoPagamento();
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public List<CondicaoPagamento> getCondicoesPagamento() {
		return condicoesPagamento;
	}

	public void setCondicoesPagamento(List<CondicaoPagamento> condicoesPagamento) {
		this.condicoesPagamento = condicoesPagamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
	
}
