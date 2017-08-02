package br.com.sysge.infraestrutura.email;

import java.io.Serializable;

public class Hotmail implements Serializable{
    
	private static final long serialVersionUID = 2553405782932275797L;

	public void enviarEmail(String remetente, String destinatario, String assunto, String mensagem) {
		ConfigHotmail email = new ConfigHotmail();
		if(!email.enviarEmailHotmail(remetente, destinatario, assunto, mensagem)){
			throw new RuntimeException("Não foi possível enviar o email, "
					+ "verifique sua conexão com a internet!");
		}
	}

}