package org.casadeguara.views;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.casadeguara.entidades.Aviso;
import org.casadeguara.models.AvisoModel;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MuralAvisos {

	private AnchorPane muralAvisos;
	private AvisoModel avisoDao;
	private List<Aviso> avisos;
	private Label data1;
	private Label data2;
	private Label data3;
	private TextArea aviso1;
	private TextArea aviso2;
	private TextArea aviso3;

	public MuralAvisos() {
		this.avisoDao = new AvisoModel();

		muralAvisos = new AnchorPane();
		muralAvisos.setPrefWidth(350);
		muralAvisos.setId("mural-avisos");

		Label titulo = new Label("Mural de Avisos");
		titulo.getStyleClass().add("titulo");
		data1 = new Label();
		data2 = new Label();
		data3 = new Label();

		aviso1 = new TextArea();
		aviso1.setId("text-area-highlight");
		aviso1.setEditable(false);
		aviso1.setPrefHeight(120);
		aviso1.setWrapText(true);

		aviso2 = new TextArea();
		aviso2.setEditable(false);
		aviso2.setPrefHeight(120);
		aviso2.setWrapText(true);

		aviso3 = new TextArea();
		aviso3.setEditable(false);
		aviso3.setPrefHeight(120);
		aviso3.setWrapText(true);

		Button novoAviso = new Button("", new ImageView("/images/plus.png"));
		Button editar1 = new Button("", new ImageView("/images/editar.png"));
		Button editar2 = new Button("", new ImageView("/images/editar.png"));
		Button editar3 = new Button("", new ImageView("/images/editar.png"));
		novoAviso.getStyleClass().add("button-transparent");
		editar1.getStyleClass().add("button-transparent");
		editar2.getStyleClass().add("button-transparent");
		editar3.getStyleClass().add("button-transparent");

		novoAviso.setOnAction((action) -> {
			String novoTexto = inserirTexto("");

			if (novoTexto != null) {
				avisoDao.cadastrar(novoTexto);
				atualizarMuralAvisos();
			}
		});

		editar1.setOnAction((action) -> {
			atualizarAviso(avisos.get(0).getId(), aviso1.getText());
		});

		editar2.setOnAction((action) -> {
			atualizarAviso(avisos.get(1).getId(), aviso2.getText());
		});

		editar3.setOnAction((action) -> {
			atualizarAviso(avisos.get(2).getId(), aviso3.getText());
		});

		novoAviso.setPrefWidth(40);
		editar1.setPrefWidth(40);
		editar2.setPrefWidth(40);
		editar3.setPrefWidth(40);

		AnchorPane.setTopAnchor(titulo, 40.0);
		AnchorPane.setTopAnchor(novoAviso, 38.0);
		AnchorPane.setTopAnchor(data1, 100.0);
		AnchorPane.setTopAnchor(editar1, 98.0);
		AnchorPane.setTopAnchor(aviso1, 130.0);
		AnchorPane.setTopAnchor(data2, 280.0);
		AnchorPane.setTopAnchor(editar2, 278.0);
		AnchorPane.setTopAnchor(aviso2, 310.0);
		AnchorPane.setTopAnchor(data3, 460.0);
		AnchorPane.setTopAnchor(editar3, 458.0);
		AnchorPane.setTopAnchor(aviso3, 490.0);

		AnchorPane.setLeftAnchor(titulo, 20.0);
		AnchorPane.setLeftAnchor(data1, 20.0);
		AnchorPane.setLeftAnchor(aviso1, 0.0);
		AnchorPane.setLeftAnchor(data2, 20.0);
		AnchorPane.setLeftAnchor(aviso2, 0.0);
		AnchorPane.setLeftAnchor(data3, 20.0);
		AnchorPane.setLeftAnchor(aviso3, 0.0);

		AnchorPane.setRightAnchor(novoAviso, 20.0);
		AnchorPane.setRightAnchor(editar1, 20.0);
		AnchorPane.setRightAnchor(editar2, 20.0);
		AnchorPane.setRightAnchor(editar3, 20.0);
		AnchorPane.setRightAnchor(aviso1, 0.0);
		AnchorPane.setRightAnchor(aviso2, 0.0);
		AnchorPane.setRightAnchor(aviso3, 0.0);

		muralAvisos.getChildren().addAll(titulo, novoAviso, editar1, editar2, editar3, data1, data2, data3, aviso1,
				aviso2, aviso3);

		atualizarMuralAvisos();
	}

	private void atualizarAviso(int idaviso, String textoAtual) {
		String novoTexto = inserirTexto(textoAtual);
		if (novoTexto != null) {
			avisoDao.atualizar(idaviso, novoTexto);
			atualizarMuralAvisos();
		}
	}

	private String inserirTexto(String texto) {
		TextArea text = new TextArea(texto);
		text.setWrapText(true);
		text.lengthProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() > 300) {
				text.setText(text.getText().substring(0, 300));
			}
		});

		Dialog<?> dialog = new Dialog<>();
		dialog.getDialogPane().setContent(text);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialog.showAndWait();

		return (dialog.getResult() == ButtonType.OK && !text.getText().isEmpty()) ? text.getText() : "";
	}

	public void atualizarMuralAvisos() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		avisos = avisoDao.consultar();
		Aviso a1 = avisos.get(0);
		Aviso a2 = avisos.get(1);
		Aviso a3 = avisos.get(2);

		data1.setText(dtf.format(a1.getData()));
		data2.setText(dtf.format(a2.getData()));
		data3.setText(dtf.format(a3.getData()));

		aviso1.setText(a1.getTexto());
		aviso2.setText(a2.getTexto());
		aviso3.setText(a3.getTexto());
	}

	public AnchorPane getRoot() {
		return muralAvisos;
	}
}
