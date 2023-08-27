package org.casadeguara.controllers;

import org.casadeguara.application.App;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.models.LoginModel;
import org.casadeguara.views.LoginView;
import org.casadeguara.views.MenuLateral;
import org.casadeguara.views.MenuSuperior;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class LoginController implements GenericController {

	private LoginView view;
	private LoginModel model;
	private MenuLateral menuLateral;
	private MenuSuperior menuSuperior;
	private BorderPane telaPrincipal;
	private Scene scene;

	public LoginController(LoginView view) {
		this.view = view;
		this.model = new LoginModel();

		configureView();
	}

	@Override
	public void configureView() {
		view.acaoBotaoEntrar(event -> autenticarUsuario());
	}

	public int autenticarUsuario() {
		String login = view.getLogin();
		String senha = view.getSenha();

		Usuario usuario = model.autenticar(login, senha);
		if (usuario != null) {
			int id = usuario.getId();
			String nome = usuario.getNome();
			String tipo = usuario.getTipo();

			Usuario usuarioLogado = App.getUsuario();
			usuarioLogado.setId(id);
			usuarioLogado.setNome(nome);
			usuarioLogado.setTipo(tipo);

			concluirLogin(id, nome, tipo);
			view.limparCampos();

			return 0;
		} else {
			view.mensagemInformativa("Usuário ou senha inválidos");
		}
		return 1;
	}

	public void setReferencias(MenuLateral menuLateral, MenuSuperior menuSuperior, Scene scene,
			BorderPane telaPrincipal) {
		this.menuLateral = menuLateral;
		this.menuSuperior = menuSuperior;
		this.scene = scene;
		this.telaPrincipal = telaPrincipal;
	}

	private int concluirLogin(int idusuario, String nome, String tipo) {
		menuLateral.configurarAcesso(model.controleAcesso(idusuario));
		menuSuperior.configurarMenuUsuario(nome, tipo);
		scene.setRoot(telaPrincipal);
		return 0;
	}
}
