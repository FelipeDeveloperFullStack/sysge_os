package br.com.sysge.util;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.sysge.model.conf.Usuario;

public class UsuarioSession implements Serializable{
	
	private static final long serialVersionUID = -3874537440027809399L;

	public static Usuario getSessionUsuario(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		return (Usuario) session.getAttribute("usuario");
	}

}
