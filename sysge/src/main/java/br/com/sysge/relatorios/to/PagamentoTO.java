package br.com.sysge.relatorios.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PagamentoTO implements Serializable{
	
	private static final long serialVersionUID = 8720356673083525518L;

	private String formaPagamento;
	
	private String parcelamento;
	
	private String numero;
	
	private BigDecimal parcela = BigDecimal.ZERO;
	
	private BigDecimal desconto = BigDecimal.ZERO;;
	
	private BigDecimal valorCobrado = BigDecimal.ZERO;;
	
	private String atendente;
	
	private Date vencimento;
	
	private String cliente;
	
	private String nomeFantasiaUnidadeEmpresarial;

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getParcelamento() {
		return parcelamento;
	}

	public void setParcelamento(String parcelamento) {
		this.parcelamento = parcelamento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getParcela() {
		return parcela;
	}

	public void setParcela(BigDecimal parcela) {
		this.parcela = parcela;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorCobrado() {
		return valorCobrado;
	}

	public void setValorCobrado(BigDecimal valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public String getAtendente() {
		return atendente;
	}

	public void setAtendente(String atendente) {
		this.atendente = atendente;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNomeFantasiaUnidadeEmpresarial() {
		return nomeFantasiaUnidadeEmpresarial;
	}

	public void setNomeFantasiaUnidadeEmpresarial(String nomeFantasiaUnidadeEmpresarial) {
		this.nomeFantasiaUnidadeEmpresarial = nomeFantasiaUnidadeEmpresarial;
	}
	
	

}
