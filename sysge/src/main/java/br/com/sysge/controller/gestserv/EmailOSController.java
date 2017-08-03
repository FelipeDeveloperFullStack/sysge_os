package br.com.sysge.controller.gestserv;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.sysge.infraestrutura.email.Email;
import br.com.sysge.infraestrutura.email.Hotmail;
import br.com.sysge.model.conf.Usuario;
import br.com.sysge.service.conf.UsuarioService;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;

@Named
@ViewScoped
public class EmailOSController implements Serializable{

	private static final long serialVersionUID = 7411624959358133535L;
	
	private Email email;
	
	private Hotmail hotmail;
	
	
	public void novoEmail(){
		this.email = new Email();
		this.email.setRemetente(getUsuario().getFuncionario().getEmail());
	}
	
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
	
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	
	public void enviarEmail(){
		
		try {
			this.hotmail = new Hotmail();
			hotmail.enviarEmail(
					email.getRemetente(), 
					email.getEmailDestinatario(), 
					email.getAssunto(),
					email.getMensagem());
			FacesUtil.mensagemInfo("Email enviado com sucesso");
			RequestContextUtil.execute("PF('dialog_envio_email').hide();");
			
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
		
		
	}
	

}
