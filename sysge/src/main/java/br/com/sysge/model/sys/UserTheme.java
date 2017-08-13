package br.com.sysge.model.sys;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.conf.Usuario;

@Entity
@Table(name = "tbl_user_theme")
public class UserTheme extends GenericDomain{

	private static final long serialVersionUID = 7494296711335569249L;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Usuario usuario;
	
	private String theme;

	public Usuario getUsuario() {
		return usuario == null ? new Usuario() : usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	

}
