package br.com.sysge.infraestrutura.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sysge.model.conf.Usuario;

@MappedSuperclass
public abstract class GenericDomain implements Serializable{

	private static final long serialVersionUID = -8678526960413727632L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	private Usuario usuarioQueCadastrou;
	
	@OneToOne
	private Usuario usuarioQueAlterou;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUsuarioCadastro;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUsuarioAlteracao; 
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericDomain other = (GenericDomain) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Usuario getUsuarioQueCadastrou() {
		return usuarioQueCadastrou;
	}

	public void setUsuarioQueCadastrou(Usuario usuarioQueCadastrou) {
		this.usuarioQueCadastrou = usuarioQueCadastrou;
	}

	public Usuario getUsuarioQueAlterou() {
		return usuarioQueAlterou;
	}

	public void setUsuarioQueAlterou(Usuario usuarioQueAlterou) {
		this.usuarioQueAlterou = usuarioQueAlterou;
	}

	public Date getDataUsuarioCadastro() {
		return dataUsuarioCadastro;
	}

	public void setDataUsuarioCadastro(Date dataUsuarioCadastro) {
		this.dataUsuarioCadastro = dataUsuarioCadastro;
	}

	public Date getDataUsuarioAlteracao() {
		return dataUsuarioAlteracao;
	}

	public void setDataUsuarioAlteracao(Date dataUsuarioAlteracao) {
		this.dataUsuarioAlteracao = dataUsuarioAlteracao;
	}

	

}
