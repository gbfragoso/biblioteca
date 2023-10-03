package org.casadeguara.application;

import java.time.LocalDateTime;
import java.util.Locale;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.controllers.AdministracaoController;
import org.casadeguara.controllers.CadastroAutorController;
import org.casadeguara.controllers.CadastroEditoraController;
import org.casadeguara.controllers.CadastroLeitorController;
import org.casadeguara.controllers.CadastroLivroController;
import org.casadeguara.controllers.CadastroPalavrasController;
import org.casadeguara.controllers.CadastroUsuarioController;
import org.casadeguara.controllers.ConsultaEmprestimoController;
import org.casadeguara.controllers.ConsultaExemplarController;
import org.casadeguara.controllers.LoginController;
import org.casadeguara.controllers.MovimentacaoController;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.utilitarios.Backup;
import org.casadeguara.utilitarios.Formatador;
import org.casadeguara.views.AdministracaoView;
import org.casadeguara.views.CadastroAutorView;
import org.casadeguara.views.CadastroEditoraView;
import org.casadeguara.views.CadastroLeitorView;
import org.casadeguara.views.CadastroLivroView;
import org.casadeguara.views.CadastroPalavrasView;
import org.casadeguara.views.CadastroUsuarioView;
import org.casadeguara.views.ConsultaEmprestimoView;
import org.casadeguara.views.ConsultaExemplarView;
import org.casadeguara.views.GenericView;
import org.casadeguara.views.LoginView;
import org.casadeguara.views.MenuLateral;
import org.casadeguara.views.MenuSuperior;
import org.casadeguara.views.MovimentacaoView;
import org.casadeguara.views.MuralAvisos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Essa classe é a classe principal da aplicação.
 * 
 * @author Gustavo
 * @version 4.0
 */
public class App extends Application {

	private static Usuario usuarioLogado = new Usuario();

	private Scene scene;

	// Tela Principal
	private BorderPane telaPrincipal;
	private MenuSuperior menuSuperior;
	private MenuLateral menuLateral;
	private MuralAvisos muralAvisos;

	private AdministracaoView administracaoView;
	private CadastroAutorView cadastroAutorView;
	private CadastroEditoraView cadastroEditoraView;
	private CadastroLeitorView cadastroLeitorView;
	private CadastroLivroView cadastroLivroView;
	private CadastroPalavrasView cadastroPalavrasView;
	private CadastroUsuarioView cadastroUsuarioView;
	private ConsultaEmprestimoView consultaEmprestimoView;
	private ConsultaExemplarView consultaExemplarView;
	private LoginView loginView;
	private MovimentacaoView movimentacaoView;

	private ConsultaExemplarController consultaController;
	private LoginController loginController;

	private void initViews() {
		administracaoView = new AdministracaoView();
		cadastroAutorView = new CadastroAutorView();
		cadastroEditoraView = new CadastroEditoraView();
		cadastroLeitorView = new CadastroLeitorView();
		cadastroLivroView = new CadastroLivroView();
		cadastroPalavrasView = new CadastroPalavrasView();
		cadastroUsuarioView = new CadastroUsuarioView();
		consultaEmprestimoView = new ConsultaEmprestimoView();
		consultaExemplarView = new ConsultaExemplarView();
		loginView = new LoginView();
		movimentacaoView = new MovimentacaoView();
		menuLateral = new MenuLateral();
		menuSuperior = new MenuSuperior();
		muralAvisos = new MuralAvisos();
	}

	private void initControllers() {
		new AdministracaoController(administracaoView);
		new CadastroAutorController(cadastroAutorView);
		new CadastroEditoraController(cadastroEditoraView);
		new CadastroLeitorController(cadastroLeitorView);
		new CadastroLivroController(cadastroLivroView);
		new CadastroPalavrasController(cadastroPalavrasView);
		new CadastroUsuarioController(cadastroUsuarioView);
		new ConsultaEmprestimoController(consultaEmprestimoView);
		consultaController = new ConsultaExemplarController(consultaExemplarView);
		loginController = new LoginController(loginView);
		new MovimentacaoController(movimentacaoView);
	}

	private void configurarLogin() {
		loginController.setReferencias(menuLateral, menuSuperior, scene, telaPrincipal);
	}

	private void configurarMargem(GenericView target) {
		BorderPane.setMargin(target.getRoot(), new Insets(40.0));
	}

	private void configurarMargensViews() {
		configurarMargem(administracaoView);
		configurarMargem(cadastroAutorView);
		configurarMargem(cadastroEditoraView);
		configurarMargem(cadastroLeitorView);
		configurarMargem(cadastroLivroView);
		configurarMargem(cadastroPalavrasView);
		configurarMargem(cadastroUsuarioView);
		configurarMargem(consultaEmprestimoView);
		configurarMargem(consultaExemplarView);
		configurarMargem(movimentacaoView);
	}

	private void configurarRedirecionamento() {
		menuLateral.acaoBotaoMovimentacao(event -> redirecionarPara(movimentacaoView));
		menuLateral.acaoBotaoConsultaEmprestimos(event -> redirecionarPara(consultaEmprestimoView));
		menuLateral.acaoBotaoConsultaExemplares(event -> redirecionarPara(consultaExemplarView));
		menuLateral.acaoBotaoCadastroAutor(event -> redirecionarPara(cadastroAutorView));
		menuLateral.acaoBotaoCadastroEditora(event -> redirecionarPara(cadastroEditoraView));
		menuLateral.acaoBotaoCadastroLeitor(event -> redirecionarPara(cadastroLeitorView));
		menuLateral.acaoBotaoCadastroLivro(event -> redirecionarPara(cadastroLivroView));
		menuLateral.acaoBotaoCadastroPalavras(event -> redirecionarPara(cadastroPalavrasView));
		menuSuperior.acaoMenuAdministracao(event -> redirecionarPara(administracaoView));
		menuSuperior.acaoMenuCadastroUsuario(event -> redirecionarPara(cadastroUsuarioView));
	}

	private void configurarMenuSuperior() {
		menuSuperior.configurarPesquisarRapida(event -> {
			String pesquisa = menuSuperior.textoBuscaRapida();
			if (pesquisa != null && !pesquisa.isEmpty()) {
				menuLateral.destacarMenuConsulta();
				consultaController.buscaRapida(pesquisa);
				redirecionarPara(consultaExemplarView);
			}
		});

		menuSuperior.configurarMenuSair(event -> {
			redirecionarPara(movimentacaoView);
			scene.setRoot(loginView.getRoot());
		});
	}

	private void realizarBackup(LocalDateTime data) {
		String nomeArquivo = "biblioteca_" + new Formatador().dataHora(data);
		new Backup().generateOnBackground(nomeArquivo, ".backup");
	}

	private void redirecionarPara(GenericView target) {
		telaPrincipal.setCenter(target.getRoot());
	}

	public static Usuario getUsuario() {
		return usuarioLogado;
	}

	@Override
	public void start(Stage primaryStage) {
		Locale.setDefault(new Locale("pt", "BR"));
		realizarBackup(LocalDateTime.now());

		initViews();
		initControllers();

		configurarMargensViews();
		configurarRedirecionamento();
		configurarMenuSuperior();

		telaPrincipal = new BorderPane();
		telaPrincipal.setTop(menuSuperior.getRoot());
		telaPrincipal.setLeft(menuLateral.getRoot());
		telaPrincipal.setRight(muralAvisos.getRoot());
		telaPrincipal.setCenter(movimentacaoView.getRoot());

		scene = new Scene(loginView.getRoot(), 1600, 900);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		configurarLogin();

		primaryStage.setScene(scene);
		primaryStage.setTitle("Sistema Biblioteca Batuíra");
		primaryStage.setMaximized(true);
		primaryStage.getIcons().add(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
		primaryStage.setOnCloseRequest(event -> {
			if (new Alerta().confirmacao("Deseja realmente sair?")) {
				Platform.exit();
			} else {
				event.consume();
			}
		});
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
