package org.casadeguara.etiquetas;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.componentes.RangeTextField;

/**
 * Cria uma janela para geração de etiquetas para os livros.
 * 
 * @author Gustavo Fragoso
 * @since 2.0
 */
public class GeradorEtiqueta extends Dialog<List<Etiqueta>> {

	private Button btnAdicionar;
	private Button btnExcluir;
	private Button btnLimpar;
	private TextField txtTombo;
	private RangeTextField txtNumero;
	private ObservableList<Etiqueta> etiquetas = FXCollections.observableArrayList();

	public GeradorEtiqueta() {
		ButtonType imprimir = new ButtonType("Imprimir", ButtonData.FINISH);
		initModality(Modality.WINDOW_MODAL);
		setTitle("Gerador de etiquetas");
		getDialogPane().setContent(createScene());
		getDialogPane().getButtonTypes().addAll(imprimir, ButtonType.CLOSE);

		setResultConverter(button -> {
			return (button.equals(imprimir)) ? etiquetas : FXCollections.emptyObservableList();
		});
	}

	@SuppressWarnings("unchecked")
	private AnchorPane createScene() {
		AnchorPane pane = new AnchorPane();

		Label lb1 = new Label("Tombo:");
		Label lb2 = new Label("Exemplar:");
		txtTombo = new TextField();
		txtTombo.setPrefWidth(150);
		txtNumero = new RangeTextField();
		txtNumero.setPrefWidth(150);

		TableColumn<Etiqueta, String> tombo = new TableColumn<>("Tombo");
		TableColumn<Etiqueta, Integer> numero = new TableColumn<>("Número");
		tombo.setCellValueFactory(new PropertyValueFactory<Etiqueta, String>("tombo"));
		numero.setCellValueFactory(new PropertyValueFactory<Etiqueta, Integer>("numero"));

		TableView<Etiqueta> tabela = new TableView<>(etiquetas);
		tabela.getColumns().addAll(tombo, numero);
		tabela.setPrefWidth(300);
		tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		btnAdicionar = new Button("Adicionar");
		btnExcluir = new Button("Excluir Selecionados");
		btnLimpar = new Button("Limpar lista");
		btnExcluir.setPrefWidth(150);
		btnLimpar.setPrefWidth(150);

		btnAdicionar.setOnAction(event -> adicionarAvulso());
		btnExcluir.setOnAction(event -> etiquetas.removeAll(tabela.getSelectionModel().getSelectedItems()));
		btnLimpar.setOnAction(event -> etiquetas.clear());
		btnAdicionar.setDefaultButton(true);

		AnchorPane.setTopAnchor(lb1, 3.0);
		AnchorPane.setTopAnchor(lb2, 3.0);
		AnchorPane.setTopAnchor(txtTombo, 0.0);
		AnchorPane.setTopAnchor(txtNumero, 0.0);
		AnchorPane.setTopAnchor(btnAdicionar, 0.0);
		AnchorPane.setTopAnchor(tabela, 35.0);
		AnchorPane.setLeftAnchor(lb1, 0.0);
		AnchorPane.setLeftAnchor(lb2, 200.0);
		AnchorPane.setLeftAnchor(txtTombo, 45.0);
		AnchorPane.setLeftAnchor(txtNumero, 255.0);
		AnchorPane.setLeftAnchor(btnAdicionar, 410.0);
		AnchorPane.setLeftAnchor(tabela, 0.0);
		AnchorPane.setRightAnchor(tabela, 0.0);
		AnchorPane.setRightAnchor(btnExcluir, 155.0);
		AnchorPane.setRightAnchor(btnLimpar, 0.0);
		AnchorPane.setBottomAnchor(btnLimpar, 5.0);
		AnchorPane.setBottomAnchor(btnExcluir, 5.0);
		AnchorPane.setBottomAnchor(tabela, 35.0);

		pane.getChildren().addAll(lb1, lb2, txtTombo, txtNumero, tabela, btnAdicionar, btnExcluir, btnLimpar);
		return pane;
	}

	private void adicionarAvulso() {
		String t = txtTombo.getText();

		if (!t.isEmpty() && !txtNumero.getText().isEmpty()) {
			List<Integer> numeros = txtNumero.getRange();

			for (Integer i : numeros) {
				etiquetas.add(new Etiqueta(t, i));
			}

			txtTombo.clear();
			txtNumero.clear();
			txtTombo.requestFocus();
		} else {
			new Alerta().informacao("Preencha os campos.");
		}
	}
}
