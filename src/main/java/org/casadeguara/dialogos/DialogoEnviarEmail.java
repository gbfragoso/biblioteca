package org.casadeguara.dialogos;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.casadeguara.models.AdministracaoModel;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class DialogoEnviarEmail extends Dialog<Boolean> {
	
	private TextField txtProvedor;
	private TextField txtPorta;
	private TextField txtEmail;
	private PasswordField txtSenha;
	private TextField txtAssunto;
	private CheckBox chkRepetir;
	private TextArea texto;

	public DialogoEnviarEmail(AdministracaoModel model) {
		DialogPane dialogPane = getDialogPane();
		
		ButtonType enviar = new ButtonType("Enviar", ButtonData.OK_DONE);
		dialogPane.getButtonTypes().addAll(enviar, ButtonType.CANCEL);
		dialogPane.setContent(createContent(model));
		
		setResultConverter(button -> {
			if(button != null && button.equals(enviar)) {
				System.out.println("Enviado com sucesso");
			}
			return true;
		});
	}
	
	public AnchorPane createContent(AdministracaoModel model) {
		Label provedor = new Label("Provedor:");
		Label porta = new Label("Porta:");
		Label email = new Label("Email:");
		Label senha = new Label("Senha:");
		Label assunto = new Label("Assunto:");
		Label tags = new Label("Tags disponívels: <leitor>,<listalivros>,<quantidade>");
		txtProvedor = new TextField();
		txtPorta = new TextField();
		txtEmail = new TextField();
		txtSenha = new PasswordField();
		txtAssunto = new TextField();
		chkRepetir = new CheckBox("Repetir cobranças");
		
		texto = new TextArea();
		texto.setWrapText(true);
		texto.setText(model.getTextoCobranca());
		
		AnchorPane content = new AnchorPane();
		AnchorPane.setLeftAnchor(provedor, 5.0);
		AnchorPane.setLeftAnchor(porta, 5.0);
		AnchorPane.setLeftAnchor(email, 5.0);
		AnchorPane.setLeftAnchor(senha, 5.0);
		AnchorPane.setLeftAnchor(assunto, 5.0);
		AnchorPane.setLeftAnchor(tags, 5.0);
		AnchorPane.setLeftAnchor(texto, 5.0);
		AnchorPane.setLeftAnchor(txtProvedor, 60.0);
		AnchorPane.setLeftAnchor(txtPorta, 60.0);
		AnchorPane.setLeftAnchor(txtEmail, 60.0);
		AnchorPane.setLeftAnchor(txtSenha, 60.0);
		AnchorPane.setLeftAnchor(txtAssunto, 60.0);
		AnchorPane.setLeftAnchor(chkRepetir, 5.0);
		AnchorPane.setRightAnchor(provedor, 5.0);
		AnchorPane.setRightAnchor(porta, 5.0);
		AnchorPane.setRightAnchor(email, 5.0);
		AnchorPane.setRightAnchor(senha, 5.0);
		AnchorPane.setRightAnchor(texto, 5.0);
		AnchorPane.setRightAnchor(txtProvedor, 5.0);
		AnchorPane.setRightAnchor(txtPorta, 5.0);
		AnchorPane.setRightAnchor(txtEmail, 5.0);
		AnchorPane.setRightAnchor(txtSenha, 5.0);
		AnchorPane.setRightAnchor(txtAssunto, 5.0);
		AnchorPane.setTopAnchor(provedor, 5.0);
		AnchorPane.setTopAnchor(txtProvedor, 5.0);
		AnchorPane.setTopAnchor(porta, 35.0);
		AnchorPane.setTopAnchor(txtPorta, 35.0);
		AnchorPane.setTopAnchor(email, 65.0);
		AnchorPane.setTopAnchor(txtEmail, 65.0);
		AnchorPane.setTopAnchor(senha, 95.0);
		AnchorPane.setTopAnchor(txtSenha, 95.0);
		AnchorPane.setTopAnchor(assunto, 125.0);
		AnchorPane.setTopAnchor(txtAssunto, 125.0);
		AnchorPane.setTopAnchor(chkRepetir, 155.0);
		AnchorPane.setTopAnchor(tags, 175.0);
		AnchorPane.setTopAnchor(texto, 195.0);
		
		content.getChildren().addAll(provedor, porta, email, senha, assunto, chkRepetir,
				txtProvedor, txtPorta, txtEmail, txtSenha, txtAssunto, texto, tags);
		
		return content;
	}
	
	private Session configurarEmail() {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.mailtrap.io");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
		
		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(txtEmail.getText(), txtSenha.getText());
			}
		});
		return session;
	}
	
	private int enviarMensagem(Session session, String from, String to, String assunto, String msg) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(assunto);
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
		 
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
