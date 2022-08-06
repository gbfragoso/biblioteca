package org.casadeguara.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.entidades.Autor;
import org.casadeguara.models.GenericModel;

/**
 * Controi a tela de cadastro de autores.
 * 
 * @author Gustavo
 */
public class CadastroAutorView implements GenericView {

	private AnchorPane painelAutor;
	private Button btnAlterar;
	private Button btnCadastrar;
	private Button btnLimpar;
	private AutoCompleteTextField<Autor> pesquisarAutores;
	private TextField txtNome;

	public CadastroAutorView() {
		painelAutor = new AnchorPane();

		Label titulo = new Label("Cadastro de Autores");
		aplicarEstilo(titulo, "titulo");

		Label lblCadastrar = new Label("Dados do autor:");
		Label pesquise = new Label("Pesquise um autor:");
		Label opcoes = new Label("Opções:");

		btnCadastrar = new Button("Cadastrar");
		btnAlterar = new Button("Alterar");
		btnLimpar = new Button("Limpar");

		configurarTamanhoBotao(btnCadastrar);
		configurarTamanhoBotao(btnAlterar);
		configurarTamanhoBotao(btnLimpar);

		txtNome = new TextField();
		txtNome.setPromptText("Nome do autor");
		txtNome.setPrefWidth(535);
		btnAlterar.setDisable(true);

		pesquisarAutores = new AutoCompleteTextField<>();
		pesquisarAutores.setPrefWidth(535);
		pesquisarAutores.setPromptText("Digite o nome do autor");

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setTopAnchor(pesquise, 40.0);
		AnchorPane.setTopAnchor(pesquisarAutores, 70.0);
		AnchorPane.setTopAnchor(lblCadastrar, 130.0);
		AnchorPane.setTopAnchor(txtNome, 160.0);
		AnchorPane.setTopAnchor(opcoes, 220.0);
		AnchorPane.setTopAnchor(btnCadastrar, 250.0);
		AnchorPane.setTopAnchor(btnAlterar, 250.0);
		AnchorPane.setTopAnchor(btnLimpar, 250.0);

		AnchorPane.setLeftAnchor(titulo, 0.0);
		AnchorPane.setLeftAnchor(pesquise, 0.0);
		AnchorPane.setLeftAnchor(pesquisarAutores, 0.0);
		AnchorPane.setLeftAnchor(lblCadastrar, 0.0);
		AnchorPane.setLeftAnchor(txtNome, 0.0);
		AnchorPane.setLeftAnchor(opcoes, 0.0);
		AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
		AnchorPane.setLeftAnchor(btnAlterar, 135.0);
		AnchorPane.setLeftAnchor(btnLimpar, 270.0);

		painelAutor.getChildren().addAll(titulo, pesquise, pesquisarAutores, lblCadastrar, txtNome, opcoes,
				btnCadastrar, btnLimpar, btnAlterar);
	}

	public void acaoBotaoAlterar(EventHandler<ActionEvent> event) {
		btnAlterar.setOnAction(event);
	}

	public void acaoBotaoCadastrar(EventHandler<ActionEvent> event) {
		btnCadastrar.setOnAction(event);
	}

	public void acaoBotaoLimpar(EventHandler<ActionEvent> event) {
		btnLimpar.setOnAction(event);
	}

	public SimpleObjectProperty<Autor> acaoPesquisarAutor() {
		return pesquisarAutores.selectedValueProperty();
	}

	public void estaCadastrando(boolean resposta) {
		btnCadastrar.setDisable(!resposta);
		btnAlterar.setDisable(resposta);
		pesquisarAutores.setDisable(!resposta);
	}

	public void limparCampos() {
		estaCadastrando(true);

		txtNome.clear();
		pesquisarAutores.clear();
	}

	public String getNomeAutor() {
		return txtNome.getText().toUpperCase();
	}

	public void setNomeAutor(String nomeAutor) {
		txtNome.setText(nomeAutor);
	}

	public Autor getTermoPesquisado() {
		return pesquisarAutores.getResult();
	}

	public void setAutoComplete(GenericModel<Autor> model) {
		pesquisarAutores.setModel(model);
	}

	@Override
	public AnchorPane getRoot() {
		return painelAutor;
	}
}
