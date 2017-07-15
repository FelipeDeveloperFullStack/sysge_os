package br.com.sysge.model.global;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_agenda")
public class Agenda extends GenericDomain{

	private static final long serialVersionUID = 6212176913130076092L;
	
	private String descricao;
	
	private String observacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicial;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFinal;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	

}
