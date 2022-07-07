package org.casadeguara.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.CustomTextField;
import java.time.LocalDate;
import org.casadeguara.consultas.Resultado;
import org.casadeguara.utilitarios.Formatador;

/**
 * Controi a tela de consulta.
 * 
 * @author Gustavo
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class ConsultaView implements GenericView {

	private final AnchorPane painelConsulta;

	private ComboBox<String> cbbTipo;
	private CustomTextField pesquisar;
	private ObservableList<Resultado> resultadoConsulta;
	private TableView<Resultado> tabelaConsulta;

	private Formatador formatar;

	public ConsultaView() {
		formatar = new Formatador();
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

		TableColumn<Resultado, String> tabelaConsultaTombo = new TableColumn<>("Tombo");
		TableColumn<Resultado, Integer> tabelaConsultaExemplar = new TableColumn<>("Ex.");
		TableColumn<Resultado, String> tabelaConsultaTitulo = new TableColumn<>("Título");
		TableColumn<Resultado, String> tabelaConsultaStatus = new TableColumn<>("Status");
		TableColumn<Resultado, String> tabelaConsultaLeitor = new TableColumn<>("Leitor");
		TableColumn<Resultado, LocalDate> tabelaConsultaDevolucao = new TableColumn<>("Devolução");

		configurarTamanhoFixo(tabelaConsultaTombo, tabelaConsulta, 0.10);
		configurarTamanhoFixo(tabelaConsultaExemplar, tabelaConsulta, 0.05);
		configurarTamanhoFixo(tabelaConsultaTitulo, tabelaConsulta, 0.30);
		configurarTamanhoFixo(tabelaConsultaStatus, tabelaConsulta, 0.10);
		configurarTamanhoFixo(tabelaConsultaDevolucao, tabelaConsulta, 0.10);
		configurarTamanhoFixo(tabelaConsultaLeitor, tabelaConsulta, 0.30);

		vincularColunaAtributo(tabelaConsultaTombo, "tombo");
		vincularColunaAtributo(tabelaConsultaTitulo, "titulo");
		vincularColunaAtributo(tabelaConsultaExemplar, "numero");
		vincularColunaAtributo(tabelaConsultaStatus, "status");
		vincularColunaAtributo(tabelaConsultaDevolucao, "devolucao");
		vincularColunaAtributo(tabelaConsultaLeitor, "leitor");

		tabelaConsultaDevolucao.setCellFactory(coluna -> {
			return new TableCell<Resultado, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty && !item.equals(LocalDate.of(2100, 01, 01))) {
						setText(formatar.data(item));
					} else {
						setText("");
					}
				}
			};
		});

		tabelaConsulta.getColumns().addAll(tabelaConsultaTombo, tabelaConsultaExemplar, tabelaConsultaTitulo,
				tabelaConsultaLeitor, tabelaConsultaStatus, tabelaConsultaDevolucao);

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

	public void setResultadosConsulta(ObservableList<Resultado> resultados) {
		resultadoConsulta.setAll(resultados);
	}

	@Override
	public AnchorPane getRoot() {
		return painelConsulta;
	}

}
