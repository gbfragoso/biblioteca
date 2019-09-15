package org.casadeguara.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.models.GenericModel;
import org.controlsfx.control.CheckListView;

/**
 * Controi a tela de cadastro de usuários
 * @author Gustavo
 */
public class CadastroUsuarioView implements GenericView {
	
	private AnchorPane painelUsuario;
	private Button btnAlterar;
	private Button btnCadastrar;
	private Button btnLimpar;
	private Button btnResetar;
	private CheckBox chbAdmin;
	private CheckBox chbAtivo;
	private CheckListView<String> clvAcessos;
	private AutoCompleteTextField<Usuario> pesquisarUsuarios;
	private TextField txtLogin;
	private TextField txtNome;
    
	public CadastroUsuarioView() {
		painelUsuario = new AnchorPane();
		
		Label titulo = new Label("Tela de Cadastro de Usuário");
        aplicarEstilo(titulo, "titulo");
        
        Label pesquise = new Label("Pesquise um usuário:");
        Label dados = new Label("Dados do usuário:");
        Label opcoes = new Label("Opções:");
        Label lblNome = new Label("Nome: ");
        Label lblLogin = new Label("Login: ");
        Label lblAcessos = new Label("Marque somente o que pode ser acessado:");
        
        txtNome = new TextField();
        txtLogin = new TextField();
        chbAdmin = new CheckBox("Este usuário é um administrador");
        chbAtivo = new CheckBox("Inativo");
        btnAlterar = new Button("Alterar");
        btnCadastrar = new Button("Cadastrar");
        btnLimpar = new Button("Limpar");
        btnResetar = new Button("Resetar senha");
        
        txtNome.setPrefWidth(480);
        txtLogin.setPrefWidth(480);
        
        pesquisarUsuarios = new AutoCompleteTextField<>();
        pesquisarUsuarios.setPrefWidth(535);
        pesquisarUsuarios.setPromptText("Digite o nome do usuário");

        configurarTamanhoBotao(btnAlterar);
        configurarTamanhoBotao(btnCadastrar);
        configurarTamanhoBotao(btnLimpar);
        configurarTamanhoBotao(btnResetar);

        ObservableList<String> acessos = FXCollections.observableArrayList();
        acessos.addAll("Movimentação", 
                "Consulta",
                "Reserva",
                "Cadastro de autores", 
                "Cadastro de editoras",
                "Cadastro de leitores", 
                "Cadastro de livros",  
                "Cadastro de palavras-chave",
                "Gráficos", 
                "Impressos");

        clvAcessos= new CheckListView<>(acessos);
        clvAcessos.setPrefSize(535, 150);

        AnchorPane.setTopAnchor(titulo, 0.0);
        AnchorPane.setTopAnchor(pesquise, 40.0);
        AnchorPane.setTopAnchor(pesquisarUsuarios, 70.0);
        AnchorPane.setTopAnchor(dados, 130.0);
        AnchorPane.setTopAnchor(lblNome, 163.0);
        AnchorPane.setTopAnchor(lblLogin, 198.0);
        AnchorPane.setTopAnchor(txtNome, 160.0);
        AnchorPane.setTopAnchor(txtLogin, 195.0);
        AnchorPane.setTopAnchor(chbAdmin, 245.0);
        AnchorPane.setTopAnchor(chbAtivo, 245.0);
        AnchorPane.setTopAnchor(lblAcessos, 290.0);
        AnchorPane.setTopAnchor(clvAcessos, 320.0);
        AnchorPane.setTopAnchor(opcoes, 500.0);
        AnchorPane.setTopAnchor(btnCadastrar, 530.0);
        AnchorPane.setTopAnchor(btnLimpar, 530.0);
        AnchorPane.setTopAnchor(btnAlterar, 530.0);
        AnchorPane.setTopAnchor(btnResetar, 530.0);
        
        AnchorPane.setLeftAnchor(titulo, 0.0);
        AnchorPane.setLeftAnchor(pesquise, 0.0);
        AnchorPane.setLeftAnchor(pesquisarUsuarios, 0.0);
        AnchorPane.setLeftAnchor(dados, 0.0);
        AnchorPane.setLeftAnchor(lblNome, 0.0);
        AnchorPane.setLeftAnchor(lblLogin, 0.0);
        AnchorPane.setLeftAnchor(txtNome, 60.0);
        AnchorPane.setLeftAnchor(txtLogin, 60.0);
        AnchorPane.setLeftAnchor(chbAdmin, 0.0);
        AnchorPane.setLeftAnchor(chbAtivo, 200.0);
        AnchorPane.setLeftAnchor(lblAcessos, 0.0);
        AnchorPane.setLeftAnchor(clvAcessos, 0.0);
        AnchorPane.setLeftAnchor(opcoes, 0.0);
        AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
        AnchorPane.setLeftAnchor(btnLimpar, 135.0);
        AnchorPane.setLeftAnchor(btnAlterar, 270.0);
        AnchorPane.setLeftAnchor(btnResetar, 405.0);
        
        painelUsuario.getChildren().addAll(titulo, pesquise, pesquisarUsuarios, dados, lblNome,
                lblLogin, txtNome, txtLogin, lblAcessos, btnCadastrar, clvAcessos, 
                opcoes, btnLimpar, btnAlterar, btnResetar, chbAdmin, chbAtivo);
	}
	
	public void acaoBotaoAlterar(EventHandler<ActionEvent> event) {
		btnAlterar.setOnAction(event);
	}
	
	public void acaoBotaoCadastrar(EventHandler<ActionEvent> event) {
		btnCadastrar.setOnAction(event);
	}
	
    public void acaoBtnResetar(EventHandler<ActionEvent> event) {
        btnResetar.setOnAction(event);
    }
    
	public void acaoBotaoLimpar(EventHandler<ActionEvent> event) {
		btnLimpar.setOnAction(event);
	}
	
	public void acaoPesquisarUsuario(EventHandler<ActionEvent> event) {
	    pesquisarUsuarios.setOnAction(event);
	}
	
	public void estaCadastrando(boolean resposta) {
	    btnAlterar.setDisable(resposta);
	    btnCadastrar.setDisable(!resposta);
	    btnResetar.setDisable(resposta);
	    txtLogin.setDisable(!resposta);
	    pesquisarUsuarios.setDisable(!resposta);
	}
	
	public void limparCampos() {
	    estaCadastrando(true);
	    
	    pesquisarUsuarios.clear();
        txtNome.clear();
        txtLogin.clear();
        chbAdmin.setSelected(false);
        clvAcessos.getCheckModel().clearChecks();
	}
	
	public String getNomeUsuario() {
	    return txtNome.getText();
	}
	
	public void setNomeUsuario(String nome) {
        txtNome.setText(nome);
    }
	
	public String getLogin() {
	    return txtLogin.getText();
	}
	
	public void setLogin(String login) {
        txtLogin.setText(login);
    }
	
	public String getTipoUsuario() {
	    return (chbAdmin.isSelected()) ? "Admin" : "Comum";
	}
	
	public boolean usuarioAtivo() {
	    return chbAtivo.isSelected();
	}
	
	public ObservableList<Integer> getListaAcessos() {
	    return clvAcessos.getCheckModel().getCheckedIndices();
	}
	
	public void setListaAcessos(int[] acessos) {
	    clvAcessos.getCheckModel().clearChecks();
        clvAcessos.getCheckModel().checkIndices(acessos);
	}
	
	public Usuario getTermoPesquisado() {
		return pesquisarUsuarios.getResult();
	}
	
	public void setAutoComplete(GenericModel<Usuario> model) {
		pesquisarUsuarios.setModel(model);
	}
	
	public void isInativo(boolean value) {
		chbAtivo.setSelected(value);
	}
	
	public void isAdmin(boolean valor) {
		chbAdmin.setSelected(valor);
	}
	
	@Override
	public AnchorPane getRoot() {
		return painelUsuario;
	}
}
