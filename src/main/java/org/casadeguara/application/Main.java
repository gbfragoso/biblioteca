package org.casadeguara.application;

import java.time.LocalDateTime;
import java.util.Locale;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.controllers.AdministracaoController;
import org.casadeguara.controllers.CadastroAutorController;
import org.casadeguara.controllers.CadastroEditoraController;
import org.casadeguara.controllers.CadastroLeitorController;
import org.casadeguara.controllers.CadastroLivroController;
import org.casadeguara.controllers.CadastroPalavrasController;
import org.casadeguara.controllers.CadastroUsuarioController;
import org.casadeguara.controllers.ConsultaController;
import org.casadeguara.controllers.GraficoController;
import org.casadeguara.controllers.ImpressosController;
import org.casadeguara.controllers.LoginController;
import org.casadeguara.controllers.MovimentacaoController;
import org.casadeguara.controllers.ReservaController;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.listas.DataSourceProvider;
import org.casadeguara.models.AdministracaoModel;
import org.casadeguara.models.AutorModel;
import org.casadeguara.models.ConsultaModel;
import org.casadeguara.models.EditoraModel;
import org.casadeguara.models.GraficoModel;
import org.casadeguara.models.ImpressosModel;
import org.casadeguara.models.LeitorModel;
import org.casadeguara.models.LivroModel;
import org.casadeguara.models.LoginModel;
import org.casadeguara.models.MovimentacaoModel;
import org.casadeguara.models.PalavraChaveModel;
import org.casadeguara.models.ReservaModel;
import org.casadeguara.models.UsuarioModel;
import org.casadeguara.utilitarios.Backup;
import org.casadeguara.utilitarios.Formatador;
import org.casadeguara.views.AdministracaoView;
import org.casadeguara.views.MenuLateral;
import org.casadeguara.views.MenuSuperior;
import org.casadeguara.views.MuralAvisos;
import org.casadeguara.views.CadastroAutorView;
import org.casadeguara.views.CadastroEditoraView;
import org.casadeguara.views.CadastroLeitorView;
import org.casadeguara.views.CadastroLivroView;
import org.casadeguara.views.CadastroPalavrasView;
import org.casadeguara.views.CadastroUsuarioView;
import org.casadeguara.views.ConsultaView;
import org.casadeguara.views.GenericView;
import org.casadeguara.views.GraficoView;
import org.casadeguara.views.ImpressosView;
import org.casadeguara.views.LoginView;
import org.casadeguara.views.MovimentacaoView;
import org.casadeguara.views.ReservaView;

/**
 * Essa classe é a classe principal da aplicação.
 * @author Gustavo
 * @version 4.0
 */
public class Main extends Application {

    private static Usuario usuarioLogado = new Usuario();
    
    private Scene scene;
    private Conexao conexao;
    
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
    private ConsultaView consultaView;
    private GraficoView graficoView;
    private ImpressosView impressosView;
    private LoginView loginView;
    private MovimentacaoView movimentacaoView;
    private ReservaView reservaView;
    
    private AdministracaoModel administracaoModel;
    private AutorModel autorModel;
    private EditoraModel editoraModel;
    private LeitorModel leitorModel;
    private LivroModel livroModel;
    private PalavraChaveModel palavraChaveModel;
    private ConsultaModel consultaModel;
    private GraficoModel graficoModel;
    private ImpressosModel impressosModel;
    private LoginModel loginModel;
    private MovimentacaoModel movimentacaoModel;
    private ReservaModel reservaModel;
    private UsuarioModel usuarioModel;

    private ConsultaController consultaController;
    private LoginController loginController;

    @Override
    public void init() {
        this.conexao = new Conexao();
    }
    
    private void initModels() {
        DataSourceProvider dataSource = new DataSourceProvider();
        administracaoModel = new AdministracaoModel(dataSource);
        autorModel = new AutorModel(dataSource);
        consultaModel = new ConsultaModel();
        editoraModel = new EditoraModel(dataSource);
        graficoModel = new GraficoModel();
        impressosModel = new ImpressosModel(dataSource);
        leitorModel = new LeitorModel(dataSource);
        livroModel = new LivroModel(dataSource);
        loginModel = new LoginModel();
        movimentacaoModel = new MovimentacaoModel(dataSource);
        palavraChaveModel = new PalavraChaveModel(dataSource);
        reservaModel = new ReservaModel(dataSource);
        usuarioModel = new UsuarioModel(dataSource);
    }
    
    private void initViews() {
        administracaoView = new AdministracaoView();
        cadastroAutorView = new CadastroAutorView();
        cadastroEditoraView = new CadastroEditoraView();
        cadastroLeitorView = new CadastroLeitorView();
        cadastroLivroView = new CadastroLivroView();
        cadastroPalavrasView = new CadastroPalavrasView();
        cadastroUsuarioView = new CadastroUsuarioView();
        consultaView = new ConsultaView();
        graficoView = new GraficoView();
        impressosView = new ImpressosView();
        loginView = new LoginView();
        movimentacaoView = new MovimentacaoView();
        menuLateral = new MenuLateral();
        menuSuperior = new MenuSuperior();
        muralAvisos = new MuralAvisos();
        reservaView = new ReservaView();
    }
    
    private void initControllers() {
        new AdministracaoController(administracaoView, administracaoModel);
        new CadastroAutorController(cadastroAutorView, autorModel);
        new CadastroEditoraController(cadastroEditoraView, editoraModel);
        new CadastroLeitorController(cadastroLeitorView, leitorModel);
        new CadastroLivroController(cadastroLivroView, livroModel);
        new CadastroPalavrasController(cadastroPalavrasView, palavraChaveModel);
        new CadastroUsuarioController(cadastroUsuarioView, usuarioModel);
        consultaController = new ConsultaController(consultaView, consultaModel);
        new GraficoController(graficoView, graficoModel);
        new ImpressosController(impressosView, impressosModel);
        loginController = new LoginController(loginView, loginModel);
        new MovimentacaoController(movimentacaoView, movimentacaoModel);
        new ReservaController(reservaView, reservaModel);
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
        configurarMargem(consultaView);
        configurarMargem(graficoView);
        configurarMargem(movimentacaoView);
        configurarMargem(impressosView);
        configurarMargem(reservaView);
    }
    
    private void configurarRedirecionamento() {
        menuLateral.acaoBotaoMovimentacao(event -> redirecionarPara(movimentacaoView));
        menuLateral.acaoBotaoConsulta(event -> redirecionarPara(consultaView));
        menuLateral.acaoBotaoReserva(event -> redirecionarPara(reservaView));
        menuLateral.acaoBotaoCadastroAutor(event -> redirecionarPara(cadastroAutorView));
        menuLateral.acaoBotaoCadastroEditora(event -> redirecionarPara(cadastroEditoraView));
        menuLateral.acaoBotaoCadastroLeitor(event -> redirecionarPara(cadastroLeitorView));
        menuLateral.acaoBotaoCadastroLivro(event -> redirecionarPara(cadastroLivroView));
        menuLateral.acaoBotaoCadastroPalavras(event -> redirecionarPara(cadastroPalavrasView));
        menuLateral.acaoBotaoGraficos(event -> redirecionarPara(graficoView));
        menuLateral.acaoBotaoImpressos(event -> redirecionarPara(impressosView));
        menuSuperior.acaoMenuAdministracao(event -> redirecionarPara(administracaoView));
        menuSuperior.acaoMenuCadastroUsuario(event -> redirecionarPara(cadastroUsuarioView));
    }
    
    private void configurarMenuSuperior() {
        menuSuperior.configurarPesquisarRapida(event -> {
            String pesquisa = menuSuperior.textoBuscaRapida();
            if(pesquisa != null && !pesquisa.isEmpty()) {
                menuLateral.destacarMenuConsulta();
                consultaController.buscaRapida(pesquisa);
                redirecionarPara(consultaView);
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
        
        primaryStage.setTitle("Sistema Biblioteca Batuíra");
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        primaryStage.setOnCloseRequest(event -> {
            if (new Alerta().confirmacao("Deseja realmente sair?")) {
                conexao.encerrar();
                Platform.exit();
            } else {
                event.consume();
            }
        });
        
        Service<Void> carregandoTelas = new Service<Void>() {
            
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    
                    @Override
                    protected Void call() throws Exception {
                        updateMessage("Carregando modelos");
                        initModels();
                        updateMessage("Carregando telas");
                        initViews();
                        updateMessage("Carregando controladores");
                        initControllers();
                        
                        updateMessage("Configurando interface");
                        configurarMargensViews();
                        configurarRedirecionamento();
                        configurarMenuSuperior();
                        
                        updateMessage("Configurando tela inicial");
                        telaPrincipal = new BorderPane();
                        telaPrincipal.setTop(menuSuperior.getRoot());
                        telaPrincipal.setLeft(menuLateral.getRoot());
                        telaPrincipal.setRight(muralAvisos.getRoot());
                        telaPrincipal.setCenter(movimentacaoView.getRoot());
                        
                        updateMessage("Configurando tela login");
                        scene = new Scene(loginView.getRoot(), 1600, 900);
                        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                        configurarLogin();
                        return null;
                    }
                    
                };
            }
        }; 
        
        new Alerta().progresso(carregandoTelas);
        carregandoTelas.start();
        carregandoTelas.setOnSucceeded(event -> {
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
