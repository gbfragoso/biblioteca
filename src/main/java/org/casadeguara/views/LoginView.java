package org.casadeguara.views;

import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class LoginView implements GenericView {

	private Button btnEntrar;
	private CustomPasswordField senha;
	private CustomTextField login;
	private final VBox view;

	public LoginView() {
		ImageView logo = getImage("/images/logo2.png");

		login = new CustomTextField();
		login.setLeft(getImage("/images/user.png"));
		login.setMaxWidth(260);

		senha = new CustomPasswordField();
		senha.setLeft(getImage("/images/password.png"));
		senha.setMaxWidth(260);

		btnEntrar = new Button("Entrar");
		btnEntrar.setPrefWidth(260);
		btnEntrar.setDefaultButton(true);

		view = new VBox(5);
		view.setAlignment(Pos.CENTER);
		view.getChildren().addAll(logo, login, senha, btnEntrar);
	}

	private ImageView getImage(String source) {
		return new ImageView(getClass().getResource(source).toExternalForm());
	}

	public void acaoBotaoEntrar(EventHandler<ActionEvent> event) {
		btnEntrar.setOnAction(event);
	}

	public String getLogin() {
		return login.getText().toLowerCase();
	}

	public String getSenha() {
		return senha.getText();
	}

	public void limparCampos() {
		login.clear();
		senha.clear();
	}

	@Override
	public VBox getRoot() {
		return view;
	}

}
