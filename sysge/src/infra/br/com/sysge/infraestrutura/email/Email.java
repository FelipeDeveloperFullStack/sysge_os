package br.com.sysge.infraestrutura.email;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.sysge.model.conf.Usuario;
import br.com.sysge.service.conf.UsuarioService;

public class Email implements Serializable{
	
	private static final long serialVersionUID = -2034232074129693662L;

	private String emailDestinatario;
	
	private String assunto;
	
	private String mensagem;
	
	@SuppressWarnings("unused")
	private String remetente;
	
	@Inject
	private UsuarioService usuarioService;
	
	private Usuario getSessionUsuario(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		return usuario;
	}
	
	private Usuario getUsuario(){
		return usuarioService.findById(getSessionUsuario().getId());
	}


	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}


	public String getRemetente() {
		return getUsuario().getFuncionario().getEmail();
	}


	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	
	


}
