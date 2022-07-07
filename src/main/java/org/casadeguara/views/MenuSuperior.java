package org.casadeguara.views;

import org.casadeguara.application.Main;
import org.casadeguara.models.UsuarioModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class MenuSuperior {

	private AnchorPane headerPane;
	private TextField pesquisar;
	private ImageView headerLogo;
	private MenuButton menuUsuario;
	private MenuItem menuTrocarSenha;
	private MenuItem menuCadastroUsuario;
	private MenuItem menuPainelAdmin;
	private MenuItem menuSair;

	public MenuSuperior() {
		headerLogo = new ImageView("/images/logo.png");
		headerLogo.setId("headerLogo");

		ImageView icon = new ImageView("images/search-white.png");

		pesquisar = new TextField();
		pesquisar.setId("busca-rapida");
		pesquisar.setPromptText("Busca rápida de livros");

		menuTrocarSenha = new MenuItem("Trocar senha");
		menuCadastroUsuario = new MenuItem("Cadastrar usuário");
		menuPainelAdmin = new MenuItem("Administração");
		menuSair = new MenuItem("Sair");

		menuUsuario = new MenuButton();
		menuUsuario.setId("menu-usuario");
		menuUsuario.setPrefWidth(200);

		menuTrocarSenha.setOnAction(action -> {
			GridPane trocarSenha = new GridPane();
			trocarSenha.setHgap(5);
			trocarSenha.setVgap(5);

			Label senha = new Label("Nova senha:");
			Label repetir = new Label("Repita sua senha:");
			PasswordField senha1 = new PasswordField();
			PasswordField senha2 = new PasswordField();

			trocarSenha.add(senha, 0, 0);
			trocarSenha.add(repetir, 0, 1);
			trocarSenha.add(senha1, 1, 0);
			trocarSenha.add(senha2, 1, 1);

			Alert dialog = new Alert(AlertType.WARNING, null, ButtonType.OK, ButtonType.CANCEL);
			dialog.setHeaderText("Digite sua nova senha:");
			dialog.getDialogPane().setContent(trocarSenha);

			if (dialog.showAndWait().get() == ButtonType.OK) {
				String s1 = senha1.getText();
				String s2 = senha2.getText();

				if (s1.equals(s2)) {
					new UsuarioModel().trocarSenha(s1, Main.getUsuario().getId());
				}
			}
		});

		headerPane = new AnchorPane(headerLogo, pesquisar, menuUsuario, icon);
		headerPane.setId("headerPane");
		headerPane.setPrefHeight(80.0);

		AnchorPane.setTopAnchor(headerLogo, 25.0);
		AnchorPane.setTopAnchor(icon, 30.0);
		AnchorPane.setTopAnchor(pesquisar, 25.0);
		AnchorPane.setTopAnchor(menuUsuario, 32.0);

		AnchorPane.setLeftAnchor(headerLogo, 105.0);
		AnchorPane.setLeftAnchor(icon, 290.0);
		AnchorPane.setLeftAnchor(pesquisar, 320.0);

		AnchorPane.setRightAnchor(pesquisar, 390.0);
		AnchorPane.setRightAnchor(menuUsuario, 80.0);
	}

	public void acaoMenuAdministracao(EventHandler<ActionEvent> event) {
		menuPainelAdmin.setOnAction(event);
	}

	public void acaoMenuCadastroUsuario(EventHandler<ActionEvent> event) {
		menuCadastroUsuario.setOnAction(event);
	}

	public void configurarMenuSair(EventHandler<ActionEvent> event) {
		menuSair.setOnAction(event);
	}

	public void configurarMenuUsuario(String nome, String tipo) {
		menuUsuario.setText("Bem vindo(a), " + nome.split(" ")[0]);
		if (!tipo.equals("Comum")) {
			menuUsuario.getItems().setAll(menuTrocarSenha, menuCadastroUsuario, menuPainelAdmin, menuSair);
		} else {
			menuUsuario.getItems().setAll(menuTrocarSenha, menuSair);
		}
	}

	public void configurarPesquisarRapida(EventHandler<ActionEvent> event) {
		pesquisar.setOnAction(event);
	}

	public String textoBuscaRapida() {
		return pesquisar.getText();
	}

	public AnchorPane getRoot() {
		return headerPane;
	}
}
