package br.com.sysge.infraestrutura.email;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable{
	
	private static final long serialVersionUID = -2034232074129693662L;

	private ArrayList<String> destinatarios;
	
	private String nomeDestinatario;
	
	private String assunto;
	
	private String mensagem;

	public ArrayList<String> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(ArrayList<String> destinatarios) {
		this.destinatarios = destinatarios;
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

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}
	
	


}
