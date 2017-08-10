package br.com.sysge.model.financ;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.conf.Usuario;

@Entity
@Table(name = "tbl_auditoria_financeiro")
public class AuditoriaFinanceiro extends GenericDomain{

	private static final long serialVersionUID = 7338347390820396275L;
	
	@OneToOne
	private Usuario usuario;
	
	private String justificativa;
	
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@Temporal(TemporalType.TIME)
	private Date hora;
	
	private String mensagem;
	
	@Transient
	private String senha;
	
	private String tituloFinanceiro;
	
	private String categoria;
	
	private BigDecimal valor;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getTituloFinanceiro() {
		return tituloFinanceiro;
	}

	public void setTituloFinanceiro(String tituloFinanceiro) {
		this.tituloFinanceiro = tituloFinanceiro;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	

}
