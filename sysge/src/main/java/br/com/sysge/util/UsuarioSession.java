package br.com.sysge.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.conf.Usuario;

public class UsuarioSession extends GenericDomain implements Serializable{
	
	private static final long serialVersionUID = -3874537440027809399L;

	public static Usuario getSessionUsuario(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		return (Usuario) session.getAttribute("usuario");
	}
	
	//Unused
	public Map<Object, Object> setarDataUsuariosAlteracaoOuCadastro(){
		Map<Object, Object> params = new HashMap<Object, Object>();
		
		setUsuarioQueCadastrou(getSessionUsuario());
		setUsuarioQueAlterou(getSessionUsuario());
		
		setDataUsuarioCadastro(new Date());
		setDataUsuarioAlteracao(new Date());
		
		params.put("USUARIO_QUE_CADASTROU", getUsuarioQueCadastrou());
		params.put("USUARIO_QUE_ALTEROU", getUsuarioQueAlterou());
		params.put("DATA_USUARIO_QUE_CADASTROU", getDataUsuarioCadastro());
		params.put("DATA_USUARIO_QUE_ALTEROU", getDataUsuarioAlteracao());
		
		return params;
		
	}

}
