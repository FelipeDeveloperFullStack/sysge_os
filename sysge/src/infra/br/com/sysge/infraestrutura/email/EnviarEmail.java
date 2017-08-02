package br.com.sysge.infraestrutura.email;

public class EnviarEmail {
    
    public static void main(String[] args) {
		ConfigHotmail email = new ConfigHotmail();
		if(!email.enviarEmailHotmail()){
			throw new RuntimeException("Não foi possível enviar o email, "
					+ "verifique sua conexão com a internet!");
		}
	}

}