package br.com.sysge.infraestrutura.email;

import java.io.Serializable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ConfigHotmail implements Serializable {

	private static final long serialVersionUID = -9170218809309849338L;

	private Properties configurarHotmail() {

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

	private Session getSession(Properties props) {
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("sysgeweb@hotmail.com", "fmds1701");
			}
		});

		/** Ativa Debug para sessão */
		// session.setDebug(true);

		return session;
	}

	public boolean enviarEmailHotmail(String remetente, String destinatario, String assunto, String mensagem,
			long numeroOS) {
		boolean retorno = false;
		try {

			Message message = new MimeMessage(getSession(configurarHotmail()));
			message.setFrom(new InternetAddress(remetente)); // Remetente
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); // Destinatário(s)
			message.setSubject(assunto);// Assunto

			Multipart multipart = new MimeMultipart("related");

			// 1º parte- html
			BodyPart messageBodyPart = new MimeBodyPart();
			// String htmlText = "<img src=\"cid:image\">";
			messageBodyPart.setContent(mensagem, "text/html");
			multipart.addBodyPart(messageBodyPart);

			// 2º parte - a imagem
			/*
			 * messageBodyPart = new MimeBodyPart(); DataSource dataSource = new
			 * FileDataSource("C:\\SYSGE_WEB\\DOCUMENTO.jpg");
			 * messageBodyPart.setDataHandler(new DataHandler(dataSource));
			 * messageBodyPart.setHeader("Content-ID","<image>");
			 * 
			 * multipart.addBodyPart(messageBodyPart);
			 */

			message.setContent(multipart);

			// Anexo
			messageBodyPart = new MimeBodyPart();
			String filename = "C:\\SYSGE_WEB\\DOCUMENTO.pdf";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("OS_Nº" + numeroOS + ".pdf");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport.send(message);
			retorno = true;

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		return retorno;
	}

	public void enviarEmailSimples(String remetente, String destinatario, String assunto, String mensagem) {

		Message message = new MimeMessage(getSession(configurarHotmail()));
		try {
			message.setFrom(new InternetAddress(remetente));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			message.setSubject(assunto);

			Multipart multipart = new MimeMultipart("related");

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mensagem, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
		    Transport.send(message);
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 

	}

}
