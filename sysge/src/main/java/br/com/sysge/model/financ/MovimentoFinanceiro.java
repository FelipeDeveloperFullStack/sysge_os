package br.com.sysge.model.financ;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_movimento_financeiro")
public class MovimentoFinanceiro extends GenericDomain{

	private static final long serialVersionUID = 5840204305542662751L;
	
	@Temporal(TemporalType.DATE)
	private Date dataMovimento;
	
	private BigDecimal totalReceita = BigDecimal.ZERO;
	
	private BigDecimal totalDespesa = BigDecimal.ZERO;
	
	private BigDecimal totalRecebido = BigDecimal.ZERO;
	
	private BigDecimal totalPago = BigDecimal.ZERO;
	
	private BigDecimal totalSaldoOperacional = BigDecimal.ZERO;
	
	private BigDecimal totalSaldoAnterior = BigDecimal.ZERO;
	
	private BigDecimal totalSaldoAtual = BigDecimal.ZERO;
	
	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public BigDecimal getTotalReceita() {
		return totalReceita;
	}

	public void setTotalReceita(BigDecimal totalReceita) {
		this.totalReceita = totalReceita;
	}

	public BigDecimal getTotalDespesa() {
		return totalDespesa;
	}

	public void setTotalDespesa(BigDecimal totalDespesa) {
		this.totalDespesa = totalDespesa;
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

	public BigDecimal getTotalSaldoOperacional() {
		return totalSaldoOperacional;
	}

	public void setTotalSaldoOperacional(BigDecimal totalSaldoOperacional) {
		this.totalSaldoOperacional = totalSaldoOperacional;
	}

	public BigDecimal getTotalSaldoAnterior() {
		return totalSaldoAnterior;
	}

	public void setTotalSaldoAnterior(BigDecimal totalSaldoAnterior) {
		this.totalSaldoAnterior = totalSaldoAnterior;
	}

	public BigDecimal getTotalSaldoAtual() {
		return totalSaldoAtual;
	}

	public void setTotalSaldoAtual(BigDecimal totalSaldoAtual) {
		this.totalSaldoAtual = totalSaldoAtual;
	}
	
}
