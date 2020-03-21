package org.casadeguara.dialogos;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.models.AdministracaoModel;
import org.casadeguara.negocio.Cobranca;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class DialogoEnviarEmail extends Dialog<Boolean> {
	
	private TextField txtEmail;
	private PasswordField txtSenha;
	private TextField txtAssunto;
	private TextArea texto;
	
	public DialogoEnviarEmail(AdministracaoModel model, ObservableList<Cobranca> lista) {
		ButtonType enviar = new ButtonType("Enviar", ButtonData.OK_DONE);

		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(enviar, ButtonType.CANCEL);
		dialogPane.setContent(createContent(model.getTextoCobranca()));
		
		setResizable(false);
		
		setResultConverter(button -> {
			if(button != null && button.equals(enviar)) {
				String temail = txtEmail.getText();
				String senha = txtSenha.getText();
				
				if(temail != null && senha != null) {
					Properties prop = new Properties();
					prop.put("mail.smtp.auth", true);
					prop.put("mail.smtp.starttls.enable", "true");
					prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
					prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
					prop.put("mail.imap.ssl.enable", "true"); // required for Gmail
					prop.put("mail.smtp.host", "smtp.gmail.com");
					prop.put("mail.smtp.port", "465");
					prop.put("mail.imap.auth.mechanisms", "XOAUTH2");
					
					try {
						Session session = Session.getInstance(prop, new Authenticator() {
						    @Override
						    protected PasswordAuthentication getPasswordAuthentication() {
						        return new PasswordAuthentication(txtEmail.getText(), txtSenha.getText());
						    }
						});
						
						if(session != null) {
							model.setTextoCobranca(texto.getText());
							enviarEmails(session, lista, texto.getText());
						}
					} catch (Exception e) {
						new Alerta().erro("Não conseguimos realizar login no email.");
					}
				}
			}
			return true;
		});
	}

	private AnchorPane createContent(String textoCobranca) {
		Label lblEmail = new Label("Email:");
		Label lblSenha = new Label("Senha:");
		Label lblAssunto = new Label("Assunto:");
		Label tags = new Label("Tags disponívels: <leitor> <listalivros> <quantidade>");
		
		txtEmail = new TextField("bibliotecabatuira@gmail.com");
		txtSenha = new PasswordField();
		txtSenha.setText("guara123456");
		txtAssunto = new TextField("Atraso na devolução de livros");
		
		texto = new TextArea();
		texto.setWrapText(true);
		texto.setPrefHeight(200);
		texto.setText(textoCobranca);
		
		AnchorPane content = new AnchorPane();
		AnchorPane.setLeftAnchor(lblEmail, 5.0);
		AnchorPane.setLeftAnchor(lblSenha, 5.0);
		AnchorPane.setLeftAnchor(lblAssunto, 5.0);
		AnchorPane.setLeftAnchor(tags, 5.0);
		AnchorPane.setLeftAnchor(texto, 5.0);
		AnchorPane.setLeftAnchor(txtEmail, 5.0);
		AnchorPane.setLeftAnchor(txtSenha, 5.0);
		AnchorPane.setLeftAnchor(txtAssunto, 5.0);
		AnchorPane.setRightAnchor(lblEmail, 5.0);
		AnchorPane.setRightAnchor(lblSenha, 5.0);
		AnchorPane.setRightAnchor(texto, 5.0);
		AnchorPane.setRightAnchor(txtEmail, 5.0);
		AnchorPane.setRightAnchor(txtSenha, 5.0);
		AnchorPane.setRightAnchor(txtAssunto, 5.0);
		AnchorPane.setTopAnchor(lblEmail, 5.0);
		AnchorPane.setTopAnchor(txtEmail, 5.0);
		AnchorPane.setTopAnchor(lblSenha, 35.0);
		AnchorPane.setTopAnchor(txtSenha, 35.0);
		AnchorPane.setTopAnchor(lblAssunto, 65.0);
		AnchorPane.setTopAnchor(txtAssunto, 65.0);
		AnchorPane.setTopAnchor(tags, 95.0);
		AnchorPane.setTopAnchor(texto, 125.0);
		AnchorPane.setBottomAnchor(texto, 5.0);
		
		content.getChildren().addAll(lblEmail, lblSenha, lblAssunto,
				txtEmail, txtSenha, txtAssunto, texto, tags);
		return content;
	}

	private void enviarEmails(Session session, ObservableList<Cobranca> listaCobrancas, String texto) {
		Task<Void> enviartask = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					int listaCobrancasSize = listaCobrancas.size();
					
					for(int i = 0; i <= listaCobrancasSize; i++) {
						Cobranca debito = listaCobrancas.get(i);
						Message message = montarCorpoMensagem(session, texto, debito);
						updateMessage("Enviando mensagens ("+(i+1)+" / "+listaCobrancasSize+")");
						updateProgress(i, listaCobrancasSize);
						Transport.send(message);
						Thread.sleep(500);
					}
				} catch (MessagingException e) {
					updateMessage(e.getLocalizedMessage());
					cancel();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(enviartask).start();
		new Alerta().progresso(enviartask);
	}

	private Message montarCorpoMensagem(Session session, String texto, Cobranca debito)
			throws MessagingException, AddressException {
		String resultado = texto;
		resultado = resultado.replace("<leitor>", debito.getLeitor());
		resultado = resultado.replace("<quantidade>", Integer.toString(debito.getLivros().length));
		resultado = resultado.replace("<listalivros>", String.join("\n", debito.getLivros()));
		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(txtEmail.getText()));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(debito.getEmail()));
		message.setSubject(txtAssunto.getText());
		message.setText(resultado); 
		return message;
	}
}
