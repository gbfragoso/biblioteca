package org.casadeguara.dialogos;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
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

import org.casadeguara.alertas.Alerta;
import org.casadeguara.models.AdministracaoModel;
import org.casadeguara.negocio.Cobranca;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

@SuppressWarnings("unchecked")
public class DialogoEnviarEmail extends Dialog<Boolean> {
	
	private TableView<Cobranca> table;
	private TextField txtProvedor;
	private TextField txtPorta;
	private TextField txtEmail;
	private PasswordField txtSenha;
	private TextField txtAssunto;
	private TextArea texto;
	private ProgressBar progressBar;
	
	public DialogoEnviarEmail(AdministracaoModel model, ObservableList<Cobranca> lista) {
		Label lblProvedor = new Label("Provedor:");
		Label lblPorta = new Label("Porta:");
		Label lblEmail = new Label("Email:");
		Label lblSenha = new Label("Senha:");
		Label lblAssunto = new Label("Assunto:");
		Label tags = new Label("Tags disponívels: <leitor> <listalivros> <quantidade>");
		txtProvedor = new TextField("smtp.gmail.com");
		txtPorta = new TextField("465");
		txtEmail = new TextField("bibliotecabatuira@gmail.com");
		txtSenha = new PasswordField();
		txtAssunto = new TextField();
		progressBar = new ProgressBar(0);
		
		texto = new TextArea();
		texto.setWrapText(true);
		texto.setText(model.getTextoCobranca());
		
		table = new TableView<Cobranca>(lista);
		
		TableColumn<Cobranca, String> leitor = new TableColumn<>("Leitor");
		TableColumn<Cobranca, String> email = new TableColumn<>("Email");
		TableColumn<Cobranca, String> tombo = new TableColumn<>("Tombo");
		TableColumn<Cobranca, String> titulo = new TableColumn<>("Titulo");
		TableColumn<Cobranca, Integer> numero = new TableColumn<>("N°");
		TableColumn<Cobranca, Date> dataEmprestimo = new TableColumn<>("Data empréstimo");
		TableColumn<Cobranca, Date> dataDevolucao = new TableColumn<>("Data devolução");
		TableColumn<Cobranca, Timestamp> cobranca = new TableColumn<>("Cobrança");
		
		vincularColunaAtributo(leitor, "leitor");
		vincularColunaAtributo(email, "email");
		vincularColunaAtributo(tombo, "tombo");
		vincularColunaAtributo(titulo, "titulo");
		vincularColunaAtributo(numero, "numero");
		vincularColunaAtributo(dataEmprestimo, "dataEmprestimo");
		vincularColunaAtributo(dataDevolucao, "dataDevolucao");
		vincularColunaAtributo(cobranca, "cobranca");
		
		table.getColumns().addAll(leitor, email, tombo, titulo, numero, dataEmprestimo, dataDevolucao, cobranca);
		table.setPrefWidth(600);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
			if(newvalue.getEmail() == null || newvalue.getEmail().isEmpty()) {
				new Alerta().informacao("Selecionar um leitor sem email ocasionará um erro no envio de mensagens.");
			}
		});
		
		AnchorPane content = new AnchorPane();
		AnchorPane.setLeftAnchor(table, 5.0);
		AnchorPane.setLeftAnchor(lblProvedor, 615.0);
		AnchorPane.setLeftAnchor(lblPorta, 615.0);
		AnchorPane.setLeftAnchor(lblEmail, 615.0);
		AnchorPane.setLeftAnchor(lblSenha, 615.0);
		AnchorPane.setLeftAnchor(lblAssunto, 615.0);
		AnchorPane.setLeftAnchor(tags, 615.0);
		AnchorPane.setLeftAnchor(texto, 615.0);
		AnchorPane.setLeftAnchor(txtProvedor, 670.0);
		AnchorPane.setLeftAnchor(txtPorta, 670.0);
		AnchorPane.setLeftAnchor(txtEmail, 670.0);
		AnchorPane.setLeftAnchor(txtSenha, 670.0);
		AnchorPane.setLeftAnchor(txtAssunto, 670.0);
		AnchorPane.setRightAnchor(lblProvedor, 5.0);
		AnchorPane.setRightAnchor(lblPorta, 5.0);
		AnchorPane.setRightAnchor(lblEmail, 5.0);
		AnchorPane.setRightAnchor(lblSenha, 5.0);
		AnchorPane.setRightAnchor(texto, 5.0);
		AnchorPane.setRightAnchor(txtProvedor, 5.0);
		AnchorPane.setRightAnchor(txtPorta, 5.0);
		AnchorPane.setRightAnchor(txtEmail, 5.0);
		AnchorPane.setRightAnchor(txtSenha, 5.0);
		AnchorPane.setRightAnchor(txtAssunto, 5.0);
		AnchorPane.setRightAnchor(progressBar, 5.0);
		AnchorPane.setTopAnchor(table, 5.0);
		AnchorPane.setTopAnchor(lblProvedor, 5.0);
		AnchorPane.setTopAnchor(txtProvedor, 5.0);
		AnchorPane.setTopAnchor(lblPorta, 35.0);
		AnchorPane.setTopAnchor(txtPorta, 35.0);
		AnchorPane.setTopAnchor(lblEmail, 65.0);
		AnchorPane.setTopAnchor(txtEmail, 65.0);
		AnchorPane.setTopAnchor(lblSenha, 95.0);
		AnchorPane.setTopAnchor(txtSenha, 95.0);
		AnchorPane.setTopAnchor(lblAssunto, 125.0);
		AnchorPane.setTopAnchor(txtAssunto, 125.0);
		AnchorPane.setTopAnchor(tags, 175.0);
		AnchorPane.setTopAnchor(texto, 195.0);
		AnchorPane.setBottomAnchor(texto, 25.0);
		AnchorPane.setBottomAnchor(progressBar, 5.0);
		AnchorPane.setBottomAnchor(table, 5.0);
		
		content.getChildren().addAll(table,lblProvedor, lblPorta, lblEmail, lblSenha, lblAssunto,
				txtProvedor, txtPorta, txtEmail, txtSenha, txtAssunto, texto, tags, progressBar);
		
		ButtonType enviar = new ButtonType("Enviar", ButtonData.OK_DONE);

		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(enviar, ButtonType.CANCEL);
		dialogPane.setContent(content);
		
		setResizable(false);
		
		setResultConverter(button -> {
			if(button != null && button.equals(enviar)) {
				ObservableList<Cobranca> cobrancas = table.getSelectionModel().getSelectedItems();
				String provedor = txtProvedor.getText();
				String porta = txtPorta.getText();
				String temail = txtEmail.getText();
				String senha = txtSenha.getText();
				
				if(provedor != null && porta != null && temail != null && senha != null) {
					Properties prop = new Properties();
					prop.put("mail.smtp.auth", true);
					prop.put("mail.smtp.starttls.enable", "true");
					prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
					prop.put("mail.smtp.host", txtProvedor.getText());
					prop.put("mail.smtp.port", txtPorta.getText());
					prop.put("mail.smtp.ssl.trust", txtProvedor.getText());
					prop.put("mail.imap.ssl.enable", "true"); // required for Gmail
					prop.put("mail.imap.auth.mechanisms", "XOAUTH2");
					
					Session session = Session.getInstance(prop, new Authenticator() {
					    @Override
					    protected PasswordAuthentication getPasswordAuthentication() {
					        return new PasswordAuthentication(txtEmail.getText(), txtSenha.getText());
					    }
					});
					
					model.atualizarDataCobranca(cobrancas.stream().map(Cobranca::getIdemprestimo).toArray());
					enviarEmails(session, cobrancas, texto.getText());
				}
			}
			return true;
		});
	}
	
	private void vincularColunaAtributo(TableColumn<?, ?> coluna, String atributo) {
        coluna.setCellValueFactory(new PropertyValueFactory<>(atributo));
    }

	private void enviarEmails(Session session, ObservableList<Cobranca> listaCobrancas, String texto) {
		Task<Void> enviartask = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					Map<String, List<Cobranca>> map = new HashMap<>();
					int count = 0;
					
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
						updateProgress(count++, map.size());
						List<Cobranca> debitos = map.get(leitor);
						String email = debitos.get(0).getEmail();
						
						Message message = montarCorpoMensagem(session, texto, leitor, debitos, email);
						 
						Transport.send(message);
					}
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		progressBar.progressProperty().bind(enviartask.progressProperty());
		new Thread(enviartask).start();
	}

	private Message montarCorpoMensagem(Session session, String texto, String leitor, 
			List<Cobranca> debitos, String email) throws MessagingException, AddressException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(txtEmail.getText()));
		message.setRecipients( Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject(txtAssunto.getText());
		 
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(resultado, "text/html");
		 
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		 
		message.setContent(multipart);
		return message;
	}
}
