package br.com.sysge.infraestrutura.email;

import java.io.Serializable;

public class Email implements Serializable{
	
	private static final long serialVersionUID = -2034232074129693662L;

	private String emailDestinatario;
	
	private String assunto;
	
	private String mensagem;
	
	private String remetente;
	
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
		return remetente;
	}


	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	
	


}
