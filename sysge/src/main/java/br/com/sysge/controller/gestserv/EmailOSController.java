package br.com.sysge.controller.gestserv;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import br.com.sysge.infraestrutura.email.Email;
import br.com.sysge.infraestrutura.email.Hotmail;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;


@ViewScoped
@ManagedBean
public class EmailOSController implements Serializable{

	private static final long serialVersionUID = 7411624959358133535L;
	
	private Email email;
	
	private Hotmail hotmail;
	
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	
	public void enviarEmail(long numeroOS, Email email){
		this.email = email;
		try {
			this.hotmail = new Hotmail();
			consistirEmailDestinatario();
			consistirRemetente();
			consistirAssunto();
			hotmail.enviarEmail(
					email.getRemetente(), 
					email.getEmailDestinatario(), 
					email.getAssunto(),
					email.getMensagem(),
					numeroOS);
			FacesUtil.mensagemInfo("Email enviado com sucesso");
			RequestContextUtil.execute("PF('dialog_envio_email').hide();");
			
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
		
		
	}
	
	private void consistirEmailDestinatario(){
		if(email.getEmailDestinatario().isEmpty()){
			throw new RuntimeException("O email do destinatário é obrigatório!");
		}
	}
	
	private void consistirRemetente(){
		if(email.getRemetente().isEmpty()){
			throw new RuntimeException("O email do remetente é obrigatório!");
		}
	}
	
	private void consistirAssunto(){
		if(email.getAssunto().isEmpty()){
			throw new RuntimeException("O assunto do email é obrigatório!");
		}
	}
	

}
