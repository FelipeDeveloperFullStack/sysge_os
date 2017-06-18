package br.com.sysge.model.financ;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.financ.type.CategoriaLancamentoDespesa;
import br.com.sysge.model.financ.type.CategoriaLancamentoReceita;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.financ.type.TipoAtualizacaoMovimento;
import br.com.sysge.model.financ.type.TipoLancamento;
import br.com.sysge.model.financ.type.TipoLancamentoFinanceiro;
import br.com.sysge.model.global.Cliente;
import br.com.sysge.model.global.Fornecedor;
import br.com.sysge.model.type.FormaPagamento;

@Entity
@Table(name = "tbl_lancamento_financeiro")
public class LancamentoFinanceiro extends GenericDomain{
	
	private static final long serialVersionUID = -4992132456364102520L;

	@Enumerated(EnumType.STRING)
	private TipoLancamentoFinanceiro tipoLancamentoFinanceiro;

	@Temporal(TemporalType.DATE)
	private Date dataLancamento;
	
	private String titulo;
	
	private String descricao;
	
	private BigDecimal valor;
	
	@Enumerated(EnumType.STRING)
	private TipoAtualizacaoMovimento tipoAtualizacaoMovimento;
	
	@Enumerated(EnumType.STRING)
	private CategoriaLancamentoReceita categoriaLancamentoReceita;
	
	@Enumerated(EnumType.STRING)
	private CategoriaLancamentoDespesa categoriaLancamentoDespesa;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Cliente cliente;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Fornecedor fornecedor;
	
	@Enumerated(EnumType.STRING)
	private FormaPagamento formaPagamento;
	
	@Enumerated(EnumType.STRING)
	private StatusFinanceiro statusRecebimentoReceita;
	
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipoLancamento;
	
	@OneToOne(fetch = FetchType.EAGER)
	private MovimentoFinanceiro movimentoFinanceiro;

	public TipoLancamentoFinanceiro getTipoLancamentoFinanceiro() {
		return tipoLancamentoFinanceiro;
	}

	public void setTipoLancamentoFinanceiro(TipoLancamentoFinanceiro tipoLancamentoFinanceiro) {
		this.tipoLancamentoFinanceiro = tipoLancamentoFinanceiro;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public CategoriaLancamentoReceita getCategoriaLancamentoReceita() {
		return categoriaLancamentoReceita;
	}

	public void setCategoriaLancamentoReceita(CategoriaLancamentoReceita categoriaLancamentoReceita) {
		this.categoriaLancamentoReceita = categoriaLancamentoReceita;
	}

	public Cliente getCliente() {
		return cliente == null ? new Cliente() : cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public StatusFinanceiro getStatusRecebimentoReceita() {
		return statusRecebimentoReceita;
	}

	public void setStatusRecebimentoReceita(StatusFinanceiro statusRecebimentoReceita) {
		this.statusRecebimentoReceita = statusRecebimentoReceita;
	}

	public MovimentoFinanceiro getMovimentoFinanceiro() {
		return movimentoFinanceiro == null ? new MovimentoFinanceiro() : this.movimentoFinanceiro;
	}

	public void setMovimentoFinanceiro(MovimentoFinanceiro movimentoFinanceiro) {
		this.movimentoFinanceiro = movimentoFinanceiro == null ? new MovimentoFinanceiro() : movimentoFinanceiro;
	}

	public Fornecedor getFornecedor() {
		return fornecedor == null ? new Fornecedor() : this.fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public CategoriaLancamentoDespesa getCategoriaLancamentoDespesa() {
		return categoriaLancamentoDespesa;
	}

	public void setCategoriaLancamentoDespesa(CategoriaLancamentoDespesa categoriaLancamentoDespesa) {
		this.categoriaLancamentoDespesa = categoriaLancamentoDespesa;
	}

	public TipoAtualizacaoMovimento getTipoAtualizacaoMovimento() {
		return tipoAtualizacaoMovimento;
	}

	public void setTipoAtualizacaoMovimento(TipoAtualizacaoMovimento tipoAtualizacaoMovimento) {
		this.tipoAtualizacaoMovimento = tipoAtualizacaoMovimento;
	}
	
	
}
