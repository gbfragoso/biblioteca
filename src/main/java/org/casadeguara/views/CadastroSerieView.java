package org.casadeguara.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.entidades.Serie;
import org.casadeguara.models.GenericModel;

/**
 * Controi a tela de cadastro de editora.
 * 
 * @author Gustavo
 */
public class CadastroSerieView implements GenericView {

	private AnchorPane painelSerie;
	private Button btnAlterar;
	private Button btnCadastrar;
	private Button btnLimpar;
	private AutoCompleteTextField<Serie> pesquisarSeries;
	private TextField txtNome;

	public CadastroSerieView() {
		painelSerie = new AnchorPane();

		Label titulo = new Label("Cadastro de séries de livros");
		aplicarEstilo(titulo, "titulo");

		Label lblCadastrar = new Label("Dados da série:");
		Label pesquise = new Label("Pesquise uma série:");
		Label opcoes = new Label("Opções:");

		btnAlterar = new Button("Alterar");
		btnCadastrar = new Button("Cadastrar");
		btnLimpar = new Button("Limpar");

		configurarTamanhoBotao(btnCadastrar);
		configurarTamanhoBotao(btnAlterar);
		configurarTamanhoBotao(btnLimpar);

		txtNome = new TextField();
		txtNome.setPromptText("Nome da série");
		txtNome.setPrefWidth(535);
		btnAlterar.setDisable(true);

		pesquisarSeries = new AutoCompleteTextField<>();
		pesquisarSeries.setPrefWidth(535);
		pesquisarSeries.setPromptText("Digite o nome de uma série de livros");

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setTopAnchor(pesquise, 40.0);
		AnchorPane.setTopAnchor(pesquisarSeries, 70.0);
		AnchorPane.setTopAnchor(lblCadastrar, 130.0);
		AnchorPane.setTopAnchor(txtNome, 160.0);
		AnchorPane.setTopAnchor(opcoes, 220.0);
		AnchorPane.setTopAnchor(btnCadastrar, 250.0);
		AnchorPane.setTopAnchor(btnAlterar, 250.0);
		AnchorPane.setTopAnchor(btnLimpar, 250.0);

		AnchorPane.setLeftAnchor(titulo, 0.0);
		AnchorPane.setLeftAnchor(pesquise, 0.0);
		AnchorPane.setLeftAnchor(pesquisarSeries, 0.0);
		AnchorPane.setLeftAnchor(lblCadastrar, 0.0);
		AnchorPane.setLeftAnchor(txtNome, 0.0);
		AnchorPane.setLeftAnchor(opcoes, 0.0);
		AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
		AnchorPane.setLeftAnchor(btnAlterar, 135.0);
		AnchorPane.setLeftAnchor(btnLimpar, 270.0);

		painelSerie.getChildren().addAll(titulo, pesquise, pesquisarSeries, lblCadastrar, txtNome, opcoes, btnCadastrar,
				btnLimpar, btnAlterar);
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

	public SimpleObjectProperty<Serie> acaoPesquisarSerie() {
		return pesquisarSeries.selectedValueProperty();
	}

	public void estaCadastrando(boolean resposta) {
		btnCadastrar.setDisable(!resposta);
		btnAlterar.setDisable(resposta);
		pesquisarSeries.setDisable(!resposta);
	}

	public void limparCampos() {
		estaCadastrando(true);

		txtNome.clear();
		pesquisarSeries.clear();
	}

	public String getNomeSerie() {
		return txtNome.getText().toUpperCase();
	}

	public void setNomeSerie(String nomeSerie) {
		txtNome.setText(nomeSerie);
	}

	public void setAutoComplete(GenericModel<Serie> model) {
		pesquisarSeries.setModel(model);
	}

	public void setSerieSelecionada(Serie serie) {
		if (serie != null) {
			setNomeSerie(serie.getNome());
		}
	}

	public Serie getSerieSelecionada() {
		return pesquisarSeries.getResult();
	}

	@Override
	public AnchorPane getRoot() {
		return painelSerie;
	}
}