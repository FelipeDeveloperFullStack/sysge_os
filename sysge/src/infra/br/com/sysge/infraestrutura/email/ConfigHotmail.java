package br.com.sysge.infraestrutura.email;

import java.io.Serializable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ConfigHotmail implements Serializable{
	
	private static final long serialVersionUID = -9170218809309849338L;
	
	private Properties configurarHotmail(){
		
		Properties props = new Properties();
	        
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        
        return props;
        
	}
	
	private Session getSession(Properties props){
		 Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                     protected PasswordAuthentication getPasswordAuthentication() 
                     {
                           return new PasswordAuthentication("felipe.miguel.santos@hotmail.com", "senha1717");
                     }
                });
    
        /** Ativa Debug para sessão */
        session.setDebug(true);
        
        return session;
	}
	
	public boolean enviarEmailHotmail(){
		boolean retorno = false; 
		 try {
			 
            Message message = new MimeMessage(getSession(configurarHotmail()));
            message.setFrom(new InternetAddress("felipe.miguel.santos@hotmail.com")); //Remetente

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("felipeanalista3@gmail.com, felipe.miguel.santos@hotmail.com")); //Destinatário(s)
            message.setSubject("Enviando email com JavaMail");//Assunto
            message.setText("Enviei este email utilizando JavaMail com minha conta Hotmail!");
            
            DataSource ax = new FileDataSource("C:\\backup_sysge\\backup_14-03-17-08-04-56.sql");
            message.setDataHandler(new DataHandler(ax));
            message.setFileName(ax.getName());
            
            /**Método para enviar a mensagem criada*/
            Transport.send(message);
            retorno = true;
            
       } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
      
      return retorno;
	}

}
