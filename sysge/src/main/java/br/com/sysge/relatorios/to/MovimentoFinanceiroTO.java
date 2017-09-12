package br.com.sysge.relatorios.to;

import java.math.BigDecimal;
import java.util.Date;

public class MovimentoFinanceiroTO {
	
	private Date dataMovimento;
	private BigDecimal totalReceber;
	private BigDecimal totalPagar;
	private BigDecimal totalRecebido;
	private BigDecimal totalPago;
	private BigDecimal saldoMovimentoAnterior;
	private BigDecimal saldoDia;
	private BigDecimal saldoAtual;
	
	public Date getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	public BigDecimal getTotalReceber() {
		return totalReceber;
	}
	public void setTotalReceber(BigDecimal totalReceber) {
		this.totalReceber = totalReceber;
	}
	public BigDecimal getTotalPagar() {
		return totalPagar;
	}
	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}
	public BigDecimal getTotalRecebido() {
		return totalRecebido;
	}
	public void setTotalRecebido(BigDecimal totalRecebido) {
		this.totalRecebido = totalRecebido;
	}
	public BigDecimal getTotalPago() {
		return totalPago;
	}
	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}
	public BigDecimal getSaldoMovimentoAnterior() {
		return saldoMovimentoAnterior;
	}
	public void setSaldoMovimentoAnterior(BigDecimal saldoMovimentoAnterior) {
		this.saldoMovimentoAnterior = saldoMovimentoAnterior;
	}
	public BigDecimal getSaldoDia() {
		return saldoDia;
	}
	public void setSaldoDia(BigDecimal saldoDia) {
		this.saldoDia = saldoDia;
	}
	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}
	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	
	

}
