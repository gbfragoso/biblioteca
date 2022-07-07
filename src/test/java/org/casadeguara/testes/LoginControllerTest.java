package org.casadeguara.testes;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.casadeguara.controllers.LoginController;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.models.LoginModel;
import org.casadeguara.views.LoginView;
import org.casadeguara.views.MenuLateral;
import org.casadeguara.views.MenuSuperior;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import javafx.scene.Scene;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

	@Mock
	private LoginView view;

	@Mock
	private LoginModel model;

	@Mock
	private MenuLateral menuLateral;

	@Mock
	private MenuSuperior menuSuperior;

	@Mock
	private Scene scene;

	@InjectMocks
	private LoginController controller;

	@Test
	public void autenticacaoPositiva() {
		String login = "login";
		String senha = "senha";
		controller.setReferencias(menuLateral, menuSuperior, scene, null);

		when(view.getLogin()).thenReturn(login);
		when(view.getSenha()).thenReturn(senha);
		when(model.autenticar(login, senha)).thenReturn(new Usuario(1, "nome", login, senha, "tipo", null, true));

		int exitStatus = controller.autenticarUsuario();

		verify(view, atLeast(1)).getLogin();
		verify(view, atLeast(1)).getSenha();
		verify(model, atLeast(1)).autenticar(login, senha);

		Assert.assertEquals(0, exitStatus);
	}

	@Test
	public void autenticacaoNegativa() {
		String login = "login";
		String senha = "senha";

		when(view.getLogin()).thenReturn(login);
		when(view.getSenha()).thenReturn(senha);
		when(model.autenticar(login, senha)).thenReturn(null);

		int exitStatus = controller.autenticarUsuario();

		verify(view, atLeast(1)).getLogin();
		verify(view, atLeast(1)).getSenha();
		verify(model, atLeast(1)).autenticar(login, senha);

		Assert.assertEquals(1, exitStatus);
	}
}
