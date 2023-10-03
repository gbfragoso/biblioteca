package org.casadeguara.views;

import java.time.LocalDate;

import org.casadeguara.consultas.ConsultaEmprestimo;
import org.casadeguara.movimentacao.Emprestimo;
import org.casadeguara.utilitarios.Formatador;
import org.controlsfx.control.textfield.CustomTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
public class ConsultaEmprestimoView implements GenericView {

	private final AnchorPane painelConsulta;

	private CustomTextField pesquisar;
	private ObservableList<ConsultaEmprestimo> resultadoConsulta;
	private TableView<ConsultaEmprestimo> tabelaConsulta;
	private Formatador formatar;

	public ConsultaEmprestimoView() {
		formatar = new Formatador();
		resultadoConsulta = FXCollections.observableArrayList();

		painelConsulta = new AnchorPane();

		Label titulo = new Label("Consultas");
		aplicarEstilo(titulo, "titulo");

		pesquisar = new CustomTextField();
		pesquisar.setPrefHeight(33);
		pesquisar.setPromptText("Digite leitor, tombo ou título do livro");
		pesquisar.setRight(new ImageView("/images/search.png"));

		tabelaConsulta = new TableView<>();
		tabelaConsulta.setItems(resultadoConsulta);
		tabelaConsulta.setPlaceholder(new Label("Nenhum resultado para esta consulta"));

		TableColumn<ConsultaEmprestimo, Integer> tabelaConsultaId = new TableColumn<>("ID");
		TableColumn<ConsultaEmprestimo, String> tabelaConsultaLeitor = new TableColumn<>("Leitor");
		TableColumn<ConsultaEmprestimo, String> tabelaConsultaTitulo = new TableColumn<>("Título");
		TableColumn<ConsultaEmprestimo, Integer> tabelaConsultaExemplar = new TableColumn<>("Ex.");
		TableColumn<ConsultaEmprestimo, LocalDate> tabelaConsultaEmprestimo = new TableColumn<>("Empréstimo");
		TableColumn<ConsultaEmprestimo, LocalDate> tabelaConsultaDevolucao = new TableColumn<>("Devolução");

		tabelaConsultaEmprestimo.setCellFactory(coluna -> {
			return new TableCell<ConsultaEmprestimo, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(formatar.data(item));
					} else {
						setText("");
					}
				}
			};
		});

		tabelaConsultaDevolucao.setCellFactory(coluna -> {
			return new TableCell<ConsultaEmprestimo, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setText(formatar.data(item));
					} else {
						setText("");
					}
				}
			};
		});

		configurarTamanhoFixo(tabelaConsultaId, tabelaConsulta, 0.08);
		configurarTamanhoFixo(tabelaConsultaLeitor, tabelaConsulta, 0.30);
		configurarTamanhoFixo(tabelaConsultaTitulo, tabelaConsulta, 0.30);
		configurarTamanhoFixo(tabelaConsultaExemplar, tabelaConsulta, 0.08);
		configurarTamanhoFixo(tabelaConsultaEmprestimo, tabelaConsulta, 0.10);
		configurarTamanhoFixo(tabelaConsultaDevolucao, tabelaConsulta, 0.10);

		vincularColunaAtributo(tabelaConsultaId, "id");
		vincularColunaAtributo(tabelaConsultaLeitor, "leitor");
		vincularColunaAtributo(tabelaConsultaTitulo, "titulo");
		vincularColunaAtributo(tabelaConsultaExemplar, "numero");
		vincularColunaAtributo(tabelaConsultaEmprestimo, "dataEmprestimo");
		vincularColunaAtributo(tabelaConsultaDevolucao, "dataDevolucao");

		tabelaConsulta.getColumns().addAll(tabelaConsultaId, tabelaConsultaLeitor, tabelaConsultaTitulo,
				tabelaConsultaExemplar, tabelaConsultaEmprestimo, tabelaConsultaDevolucao);

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

	public void setResultadosConsulta(ObservableList<ConsultaEmprestimo> resultados) {
		resultadoConsulta.setAll(resultados);
	}

	@Override
	public AnchorPane getRoot() {
		return painelConsulta;
	}
}
