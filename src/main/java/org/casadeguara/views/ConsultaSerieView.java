package org.casadeguara.views;

import org.casadeguara.consultas.ConsultaSerie;
import org.controlsfx.control.textfield.CustomTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Controi a tela de consulta.
 * 
 * @author Gustavo
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class ConsultaSerieView implements GenericView {

	private final AnchorPane painelConsulta;

	private CustomTextField pesquisar;
	private ObservableList<ConsultaSerie> resultadoConsulta;
	private TableView<ConsultaSerie> tabelaConsulta;

	public ConsultaSerieView() {
		resultadoConsulta = FXCollections.observableArrayList();

		painelConsulta = new AnchorPane();

		Label titulo = new Label("Consultas");
		aplicarEstilo(titulo, "titulo");

		pesquisar = new CustomTextField();
		pesquisar.setPrefHeight(33);
		pesquisar.setPromptText("Digite o nome da série de livros");
		pesquisar.setRight(new ImageView("/images/search.png"));

		tabelaConsulta = new TableView<>();
		tabelaConsulta.setItems(resultadoConsulta);
		tabelaConsulta.setPlaceholder(new Label("Nenhum resultado para esta consulta"));

		TableColumn<ConsultaSerie, String> tabelaConsultaSerie = new TableColumn<>("Série");
		TableColumn<ConsultaSerie, String> tabelaConsultaTombo = new TableColumn<>("Tombo");
		TableColumn<ConsultaSerie, String> tabelaConsultaTitulo = new TableColumn<>("Título");
		TableColumn<ConsultaSerie, Integer> tabelaConsultaOrdem = new TableColumn<>("Ordem");

		configurarTamanhoFixo(tabelaConsultaTombo, tabelaConsulta, 0.1);
		configurarTamanhoFixo(tabelaConsultaTitulo, tabelaConsulta, 0.4);
		configurarTamanhoFixo(tabelaConsultaSerie, tabelaConsulta, 0.4);
		configurarTamanhoFixo(tabelaConsultaOrdem, tabelaConsulta, 0.1);

		vincularColunaAtributo(tabelaConsultaTombo, "tombo");
		vincularColunaAtributo(tabelaConsultaTitulo, "titulo");
		vincularColunaAtributo(tabelaConsultaSerie, "serie");
		vincularColunaAtributo(tabelaConsultaOrdem, "ordem");

		tabelaConsulta.getColumns().addAll(tabelaConsultaTombo, tabelaConsultaTitulo, tabelaConsultaSerie,
				tabelaConsultaOrdem);

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setTopAnchor(pesquisar, 38.0);
		AnchorPane.setTopAnchor(tabelaConsulta, 77.0);
		AnchorPane.setLeftAnchor(pesquisar, 0.0);
		AnchorPane.setLeftAnchor(tabelaConsulta, 0.0);
		AnchorPane.setRightAnchor(pesquisar, 0.0);
		AnchorPane.setRightAnchor(tabelaConsulta, 0.0);
		AnchorPane.setBottomAnchor(tabelaConsulta, 0.0);

		painelConsulta.getChildren().addAll(titulo, pesquisar, tabelaConsulta);
	}

	private void configurarTamanhoFixo(TableColumn<?, ?> coluna, TableView<?> tabela, double percent) {
		coluna.prefWidthProperty().bind(tabela.widthProperty().multiply(percent));
		coluna.setResizable(false);
	}

	private void vincularColunaAtributo(TableColumn<?, ?> coluna, String atributo) {
		coluna.setCellValueFactory(new PropertyValueFactory<>(atributo));
	}

	public void acaoPesquisar(EventHandler<ActionEvent> event) {
		pesquisar.setOnAction(event);
	}

	public String getTermoPesquisado() {
		return pesquisar.getText();
	}

	public void setTextoPesquisado(String termoPesquisado) {
		pesquisar.setText(termoPesquisado);
	}

	public void setResultadosConsulta(ObservableList<ConsultaSerie> resultados) {
		resultadoConsulta.setAll(resultados);
	}

	@Override
	public AnchorPane getRoot() {
		return painelConsulta;
	}
}
