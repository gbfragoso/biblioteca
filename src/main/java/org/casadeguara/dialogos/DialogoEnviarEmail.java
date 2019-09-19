package org.casadeguara.dialogos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.models.AdministracaoModel;
import org.casadeguara.negocio.Cobranca;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import javafx.collections.FXCollections;
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
	
	private TextField txtProvedor;
	private TextField txtPorta;
	private TextField txtEmail;
	private PasswordField txtSenha;
	private TextField txtAssunto;
	private TextArea texto;

	public DialogoEnviarEmail(AdministracaoModel model, ObservableList<Cobranca> listaCobrancas) {
		DialogPane dialogPane = getDialogPane();
		
		ButtonType enviar = new ButtonType("Enviar", ButtonData.OK_DONE);
		dialogPane.getButtonTypes().addAll(enviar, ButtonType.CANCEL);
		dialogPane.setContent(createContent(model));
		
		setResultConverter(button -> {
			if(button != null && button.equals(enviar)) {
				String provedor = txtProvedor.getText();
				String porta = txtPorta.getText();
				String email = txtEmail.getText();
				String senha = txtSenha.getText();
				
				if(provedor != null && porta != null && email != null && senha != null) {
					Mailer mailer = MailerBuilder
				          .withSMTPServer(provedor, Integer.valueOf(porta), email, senha)
				          .withTransportStrategy(TransportStrategy.SMTP_TLS)
				          .withSessionTimeout(10 * 1000)
				          .clearEmailAddressCriteria()
				          .withDebugLogging(true)
				          .buildMailer();
					
					ObservableList<Email> emails = construirListaEmail(listaCobrancas, texto.getText());
					Task<Void> enviarEmails = new Task<Void>() {
	
						@Override
						protected Void call() throws Exception {
							int size = emails.size();
							updateMessage("Enviando emails");
							for(int i = 0; i < size; i++) {
								mailer.sendMail(emails.get(i), true);
								updateProgress(i+1, size);
							}
							return null;
						}
					};
					new Alerta().progresso(enviarEmails);
					new Thread(enviarEmails).start();
				}
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
		Label tags = new Label("Tags disponívels: <leitor> <listalivros> <quantidade>");
		txtProvedor = new TextField();
		txtPorta = new TextField();
		txtEmail = new TextField();
		txtSenha = new PasswordField();
		txtAssunto = new TextField();
		
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
		AnchorPane.setTopAnchor(tags, 175.0);
		AnchorPane.setTopAnchor(texto, 195.0);
		
		content.getChildren().addAll(provedor, porta, email, senha, assunto,
				txtProvedor, txtPorta, txtEmail, txtSenha, txtAssunto, texto, tags);
		
		return content;
	}
	
	private ObservableList<Email> construirListaEmail(ObservableList<Cobranca> listaCobrancas, String texto) {
		ObservableList<Email> listaEmails = FXCollections.observableArrayList();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Map<String, List<Cobranca>> map = new HashMap<>();
		
		for(Cobranca c : listaCobrancas) {
			String leitor = c.getLeitor();

			if (!map.containsKey(leitor)) {
			    List<Cobranca> list = new ArrayList<>();
			    list.add(c);
			    map.put(leitor, list);
			} else {
			    map.get(leitor).add(c);
			}
		}
		Set<String> listaLeitores = map.keySet();
		
		for(String leitor : listaLeitores) {
			List<Cobranca> debitos = map.get(leitor);
			String email = debitos.get(0).getEmail();
			
			StringBuilder lista = new StringBuilder();
			for(Cobranca c : debitos) {
				lista.append(c.getTombo());
				lista.append(" ");
				lista.append(c.getTitulo());
				lista.append(" Ex: ");
				lista.append(c.getNumero());
				lista.append(" com data para devolução para: ");
				lista.append(simpleDateFormat.format(c.getDataDevolucao()));
				lista.append("\n");
			}
			String resultado = texto;
			resultado = resultado.replace("<leitor>", leitor);
			resultado = resultado.replace("<quantidade>", Integer.toString(debitos.size()));
			resultado = resultado.replace("<listalivros>", lista.toString());
			
			listaEmails.add(
				EmailBuilder.startingBlank()
					.to(leitor, email)
					.withSubject("hey")
					.withPlainText(texto)
					.withHeader("X-Priority", 5)
					.buildEmail()
			);
		}
		
		return listaEmails;
	}

}
