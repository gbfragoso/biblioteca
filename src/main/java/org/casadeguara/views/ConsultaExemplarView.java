package org.casadeguara.views;

import org.casadeguara.consultas.ConsultaExemplar;
import org.controlsfx.control.textfield.CustomTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
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
public class ConsultaExemplarView implements GenericView {

	private final AnchorPane painelConsulta;

	private ComboBox<String> cbbTipo;
	private CustomTextField pesquisar;
	private ObservableList<ConsultaExemplar> resultadoConsulta;
	private TableView<ConsultaExemplar> tabelaConsulta;

	public ConsultaExemplarView() {
		resultadoConsulta = FXCollections.observableArrayList();

		painelConsulta = new AnchorPane();

		Label titulo = new Label("Consultas");
		aplicarEstilo(titulo, "titulo");

		Label lblSelect = new Label("Pesquisar por: ");
		pesquisar = new CustomTextField();
		pesquisar.setPrefHeight(33);
		pesquisar.setPromptText("O que você procura?");
		pesquisar.setRight(new ImageView("/images/search.png"));

		cbbTipo = new ComboBox<>();
		cbbTipo.setPrefSize(150, 33);
		cbbTipo.getItems().addAll("Autor", "Editora", "Palavra-chave", "Título", "Tombo");
		cbbTipo.getSelectionModel().select("Título");

		tabelaConsulta = new TableView<>();
		tabelaConsulta.setItems(resultadoConsulta);
		tabelaConsulta.setPlaceholder(new Label("Nenhum resultado para esta consulta"));

		TableColumn<ConsultaExemplar, String> tabelaConsultaTombo = new TableColumn<>("Tombo");
		TableColumn<ConsultaExemplar, Integer> tabelaConsultaExemplar = new TableColumn<>("Ex.");
		TableColumn<ConsultaExemplar, String> tabelaConsultaTitulo = new TableColumn<>("Título");
		TableColumn<ConsultaExemplar, String> tabelaConsultaStatus = new TableColumn<>("Status");

		configurarTamanhoFixo(tabelaConsultaTombo, tabelaConsulta, 0.15);
		configurarTamanhoFixo(tabelaConsultaExemplar, tabelaConsulta, 0.15);
		configurarTamanhoFixo(tabelaConsultaTitulo, tabelaConsulta, 0.50);
		configurarTamanhoFixo(tabelaConsultaStatus, tabelaConsulta, 0.20);

		vincularColunaAtributo(tabelaConsultaTombo, "tombo");
		vincularColunaAtributo(tabelaConsultaTitulo, "titulo");
		vincularColunaAtributo(tabelaConsultaExemplar, "numero");
		vincularColunaAtributo(tabelaConsultaStatus, "status");

		tabelaConsulta.getColumns().addAll(tabelaConsultaTombo, tabelaConsultaTitulo, tabelaConsultaExemplar,
				tabelaConsultaStatus);

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setTopAnchor(lblSelect, 42.0);
		AnchorPane.setTopAnchor(pesquisar, 38.0);
		AnchorPane.setTopAnchor(cbbTipo, 38.0);
		AnchorPane.setTopAnchor(tabelaConsulta, 77.0);
		AnchorPane.setLeftAnchor(lblSelect, 0.0);
		AnchorPane.setLeftAnchor(pesquisar, 260.0);
		AnchorPane.setLeftAnchor(cbbTipo, 105.0);
		AnchorPane.setLeftAnchor(tabelaConsulta, 0.0);
		AnchorPane.setRightAnchor(pesquisar, 0.0);
		AnchorPane.setRightAnchor(tabelaConsulta, 0.0);
		AnchorPane.setBottomAnchor(tabelaConsulta, 0.0);

		painelConsulta.getChildren().addAll(titulo, lblSelect, pesquisar, cbbTipo, tabelaConsulta);
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

	public String getTipoPesquisa() {
		return cbbTipo.getSelectionModel().getSelectedItem();
	}

	public void setResultadosConsulta(ObservableList<ConsultaExemplar> resultados) {
		resultadoConsulta.setAll(resultados);
	}

	@Override
	public AnchorPane getRoot() {
		return painelConsulta;
	}

}
