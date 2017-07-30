package br.com.sysge.infraestrutura.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.SimpleEmail;
public class EmailFactory {
	
	private final static Logger logger = Logger.getLogger(EmailFactory.class.getName());

	private static List<String> listaEmail = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		listaEmail.add("felipeanalista3@gmail.com");
		sendAttachEmail(listaEmail, "OIe", ""
				+ "Relatório de movimentações financeiras:"
				+ "Por exemplo: Ao realizar uma venda o sistema processa as informações e carrega no relatório, mostrando a origem do movimento como Recebimento e o valor "
				+ "do movimento, entre outras informações."
				+ "", "c:\\teste\\bemalog_22-07-2016.xml");
		
	}
	
	public static void sendAttachEmail(List<String> to, String assunto, String conteudo, String anexo)
	{
		Properties p = getProps();
        Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(p, auth);
		MimeMessage msg = new MimeMessage(session);
		MimeBodyPart mbp = new MimeBodyPart();
		logger.info("Capturando as propriedades!");
		try {
			// "de" e "para"!!
			msg.setFrom(new InternetAddress("felipeanalista3@gmail.com"));
			InternetAddress [] emails = new InternetAddress[to.size()];
			int i = 0;
			for (Iterator<String> iterator = to.iterator(); iterator.hasNext();) {
				String toAddr = (String) iterator.next();
				emails[i] = new InternetAddress(toAddr);
				i++;
			}
			msg.setRecipients(Message.RecipientType.TO, emails);
			msg.setSentDate(new Date());
			msg.setSubject(assunto);
			msg.setText(conteudo);
			//enviando anexo
			DataSource fds = new FileDataSource("C:\\data\\Contrato.pdf");
			mbp.setDisposition(Part.ATTACHMENT);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(fds.getName());
            Multipart mp = new MimeMultipart();   
			mp.addBodyPart(mbp);
			msg.setContent(mp);
			// enviando mensagem
			Transport.send(msg);
			
			logger.info("Anexo "+anexo);
			logger.info("Email enviado com sucesso! para "+to);
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	private static Properties getProps() {
		Properties props = new Properties();		
		props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
  
        
        logger.info("Processando as propriedades!");
        return props;
	}
}
class SMTPAuthenticator extends javax.mail.Authenticator {
	public PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication ("felipe.miguel.santos@hotmail.com", "senha1717");
	}
}


