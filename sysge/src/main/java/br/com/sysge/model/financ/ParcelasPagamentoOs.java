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
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.model.type.FormaPagamento;

@Entity
@Table(name = "tbl_parcelas_pagamento_os")
public class ParcelasPagamentoOs extends GenericDomain{
	
	private static final long serialVersionUID = 124094786371637282L;

	@OneToOne(fetch = FetchType.EAGER)
	private OrdemServico ordemServico;
	
	private long numero;
	
	private BigDecimal valorParcela = BigDecimal.ZERO;
	
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	
	private BigDecimal valorCobrado = BigDecimal.ZERO;
	
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;
	
	@Enumerated(EnumType.STRING)
	private StatusFinanceiro statusFinanceiro;
	
	@Enumerated(EnumType.STRING)
	private FormaPagamento formaPagamento;
	
	private String quantidadeParcelas;
	
	@OneToOne(fetch = FetchType.EAGER)
	private LancamentoFinanceiro lancamentoReceita;

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorCobrado() {
		return valorCobrado;
	}

	public void setValorCobrado(BigDecimal valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public StatusFinanceiro getStatusFinanceiro() {
		return statusFinanceiro;
	}

	public void setStatusFinanceiro(StatusFinanceiro statusFinanceiro) {
		this.statusFinanceiro = statusFinanceiro;
	}


	public String getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(String quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public OrdemServico getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public LancamentoFinanceiro getLancamentoReceita() {
		return lancamentoReceita == null ? new LancamentoFinanceiro() : this.lancamentoReceita;
	}

	public void setLancamentoReceita(LancamentoFinanceiro lancamentoReceita) {
		this.lancamentoReceita = lancamentoReceita == null ? new LancamentoFinanceiro() : lancamentoReceita;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	
}
