package org.casadeguara.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.componentes.MaskedTextField;
import org.casadeguara.componentes.NotificationButton;
import org.casadeguara.entidades.Editora;
import org.casadeguara.entidades.Livro;
import org.casadeguara.entidades.Serie;
import org.casadeguara.models.GenericModel;

/**
 * Tela de cadastro de livros.
 * 
 * @author Gustavo
 * @since 2.0
 */
public class CadastroLivroView implements GenericView {

	private final AnchorPane painelLivro;

	private Button btnAlterar;
	private Button btnLimpar;
	private Button btnCadastrar;
	private Button btnSugerir;
	private AutoCompleteTextField<Livro> pesquisarLivros;
	private AutoCompleteTextField<Editora> pesquisarEditoras;
	private AutoCompleteTextField<Serie> pesquisarSerie;
	private MaskedTextField txtTombo;
	private NotificationButton btnAdicionarAutor;
	private NotificationButton btnAdicionarExemplar;
	private NotificationButton btnAdicionarPalavra;
	private TextField txtTitulo;
	private MaskedTextField txtOrdem;

	public CadastroLivroView() {
		painelLivro = new AnchorPane();

		Label titulo = new Label("Cadastro de Livros");
		aplicarEstilo(titulo, "titulo");

		Label info = new Label("Dados do livro");
		Label pesquise = new Label("Pesquise um livro:");
		Label lblEditora = new Label("Publicado por:*");
		Label lblTitulo = new Label("Título:*");
		Label lblTombo = new Label("Tombo:*");
		Label lblSerie = new Label("Série ou Coleção:");
		Label lblOrdem = new Label("Ordem na coleção:");

		Label lblIncluir = new Label("Informações Adicionais");
		Label opcoes = new Label("Opções");

		pesquisarLivros = new AutoCompleteTextField<>();
		pesquisarLivros.setPromptText("Digite o tombo ou título de um livro");
		pesquisarLivros.setPrefWidth(535);

		pesquisarEditoras = new AutoCompleteTextField<>();
		pesquisarEditoras.setPrefWidth(535);

		pesquisarSerie = new AutoCompleteTextField<>();
		pesquisarSerie.setPrefWidth(535);

		txtTitulo = new TextField();
		txtTombo = new MaskedTextField("#######", ' ');
		txtOrdem = new MaskedTextField("###", ' ');
		txtTitulo.setPrefWidth(535);
		txtTombo.setPrefWidth(535);
		txtOrdem.setPrefWidth(535);

		btnAdicionarAutor = new NotificationButton("Autores");
		btnAdicionarExemplar = new NotificationButton("Exemplares");
		btnAdicionarPalavra = new NotificationButton("Palavras-chave");
		btnAdicionarAutor.setPrefSize(180.0, 32);
		btnAdicionarExemplar.setPrefSize(180.0, 32);
		btnAdicionarPalavra.setPrefSize(180.0, 32);
		corNotificacao(btnAdicionarAutor);
		corNotificacao(btnAdicionarExemplar);
		corNotificacao(btnAdicionarPalavra);

		btnAlterar = new Button("Alterar");
		btnCadastrar = new Button("Cadastrar");
		btnLimpar = new Button("Limpar");
		btnSugerir = new Button("Último tombo");
		btnAlterar.setPrefSize(130.0, 32);
		btnCadastrar.setPrefSize(130.0, 32);
		btnLimpar.setPrefSize(130.0, 32);
		btnSugerir.setPrefSize(130.0, 32);

		btnAlterar.setDisable(true);

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setTopAnchor(pesquise, 40.0);
		AnchorPane.setTopAnchor(pesquisarLivros, 70.0);
		AnchorPane.setTopAnchor(info, 130.0);
		AnchorPane.setTopAnchor(lblTitulo, 163.0);
		AnchorPane.setTopAnchor(txtTitulo, 160.0);
		AnchorPane.setTopAnchor(lblTombo, 198.0);
		AnchorPane.setTopAnchor(txtTombo, 195.0);
		AnchorPane.setTopAnchor(lblEditora, 233.0);
		AnchorPane.setTopAnchor(pesquisarEditoras, 230.0);
		AnchorPane.setTopAnchor(lblSerie, 268.0);
		AnchorPane.setTopAnchor(pesquisarSerie, 265.0);
		AnchorPane.setTopAnchor(lblOrdem, 303.0);
		AnchorPane.setTopAnchor(txtOrdem, 300.0);
		AnchorPane.setTopAnchor(lblIncluir, 338.0);
		AnchorPane.setTopAnchor(btnAdicionarAutor, 360.0);
		AnchorPane.setTopAnchor(btnAdicionarExemplar, 360.0);
		AnchorPane.setTopAnchor(btnAdicionarPalavra, 360.0);
		AnchorPane.setTopAnchor(opcoes, 450.0);
		AnchorPane.setTopAnchor(btnCadastrar, 480.0);
		AnchorPane.setTopAnchor(btnAlterar, 480.0);
		AnchorPane.setTopAnchor(btnLimpar, 480.0);
		AnchorPane.setTopAnchor(btnSugerir, 480.0);

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setLeftAnchor(pesquise, 0.0);
		AnchorPane.setLeftAnchor(pesquisarLivros, 0.0);
		AnchorPane.setLeftAnchor(info, 0.0);
		AnchorPane.setLeftAnchor(lblTitulo, 0.0);
		AnchorPane.setLeftAnchor(txtTitulo, 130.0);
		AnchorPane.setLeftAnchor(lblTombo, 0.0);
		AnchorPane.setLeftAnchor(txtTombo, 130.0);
		AnchorPane.setLeftAnchor(lblEditora, 0.0);
		AnchorPane.setLeftAnchor(pesquisarEditoras, 130.0);
		AnchorPane.setLeftAnchor(lblSerie, 0.0);
		AnchorPane.setLeftAnchor(pesquisarSerie, 130.0);
		AnchorPane.setLeftAnchor(lblOrdem, 0.0);
		AnchorPane.setLeftAnchor(txtOrdem, 130.0);
		AnchorPane.setLeftAnchor(lblIncluir, 0.0);
		AnchorPane.setLeftAnchor(btnAdicionarAutor, 0.0);
		AnchorPane.setLeftAnchor(btnAdicionarExemplar, 185.0);
		AnchorPane.setLeftAnchor(btnAdicionarPalavra, 370.0);
		AnchorPane.setLeftAnchor(opcoes, .0);
		AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
		AnchorPane.setLeftAnchor(btnAlterar, 135.0);
		AnchorPane.setLeftAnchor(btnLimpar, 270.0);
		AnchorPane.setLeftAnchor(btnSugerir, 405.0);

		painelLivro.getChildren().addAll(titulo, info, pesquise, lblEditora, lblIncluir, lblTitulo, lblTombo, lblSerie,
				lblOrdem, opcoes, txtTitulo, txtTombo, txtOrdem, pesquisarEditoras, pesquisarLivros, pesquisarSerie,
				btnAdicionarAutor, btnAdicionarExemplar, btnAdicionarPalavra, btnCadastrar, btnAlterar, btnSugerir,
				btnLimpar);
	}

	private void corNotificacao(NotificationButton button) {
		button.createNotification(Color.RED);
	}

	public void acaoBtnAlterar(EventHandler<ActionEvent> event) {
		btnAlterar.setOnAction(event);
	}

	public void acaoBtnAdicionarAutor(EventHandler<ActionEvent> event) {
		btnAdicionarAutor.setOnAction(event);
	}

	public void acaoBtnAdicionarExemplar(EventHandler<ActionEvent> event) {
		btnAdicionarExemplar.setOnAction(event);
	}

	public void acaoBtnAdicionarPalavra(EventHandler<ActionEvent> event) {
		btnAdicionarPalavra.setOnAction(event);
	}

	public void acaoBtnCadastrar(EventHandler<ActionEvent> event) {
		btnCadastrar.setOnAction(event);
	}

	public void acaoBtnLimpar(EventHandler<ActionEvent> event) {
		btnLimpar.setOnAction(event);
	}

	public void acaoBtnSugerir(EventHandler<ActionEvent> event) {
		btnSugerir.setOnAction(event);
	}

	public SimpleObjectProperty<Livro> acaoPesquisarLivro() {
		return pesquisarLivros.selectedValueProperty();
	}

	public void estaCadastrando(boolean resposta) {
		btnAlterar.setDisable(resposta);
		btnCadastrar.setDisable(!resposta);
		pesquisarLivros.setDisable(!resposta);
	}

	public void limparCampos() {
		estaCadastrando(true);
		limparNotificacoes();

		txtTitulo.clear();
		txtTombo.clear();
		txtOrdem.clear();
		pesquisarLivros.clear();
		pesquisarEditoras.clear();
		pesquisarSerie.clear();
	}

	public void limparNotificacoes() {
		btnAdicionarAutor.setNotificationText(0);
		btnAdicionarExemplar.setNotificationText(0);
		btnAdicionarPalavra.setNotificationText(0);
	}

	public String getTituloLivro() {
		return txtTitulo.getText().toUpperCase();
	}

	public void setTituloLivro(String titulo) {
		txtTitulo.setText(titulo.toUpperCase());
	}

	public String getTomboLivro() {
		return txtTombo.getPlainText();
	}

	public void setTomboLivro(String tombo) {
		txtTombo.setPlainText(tombo);
	}

	public Editora getEditora() {
		return pesquisarEditoras.getResult();
	}

	public void setEditora(Editora editora) {
		if (editora == null || editora.getId() == 0) {
			return;
		}
		pesquisarEditoras.setResult(editora);
	}

	public Serie getSerie() {
		return pesquisarSerie.getResult();
	}

	public void setSerie(Serie serie) {
		if (serie == null || serie.getId() == 0) {
			return;
		}
		pesquisarSerie.setResult(serie);
	}

	public String getOrdemColecao() {
		return txtOrdem.getText();
	}

	public void setOrdemColecao(String ordem) {
		txtOrdem.setPlainText(ordem);
	}

	public void quantidadeAutores(int valor) {
		btnAdicionarAutor.setNotificationText(valor);
	}

	public void quantidadeExemplares(int valor) {
		btnAdicionarExemplar.setNotificationText(valor);
	}

	public void quantidadePalavrasChave(int valor) {
		btnAdicionarPalavra.setNotificationText(valor);
	}

	public void setAutoCompleteEditora(GenericModel<Editora> editoraModel) {
		pesquisarEditoras.setModel(editoraModel);
	}

	public void setAutoCompleteLivro(GenericModel<Livro> model) {
		pesquisarLivros.setModel(model);
	}

	public void setAutoCompleteSerie(GenericModel<Serie> model) {
		pesquisarSerie.setModel(model);
	}

	public Livro getLivroSelecionado() {
		return pesquisarLivros.getResult();
	}

	@Override
	public AnchorPane getRoot() {
		return painelLivro;
	}
}
