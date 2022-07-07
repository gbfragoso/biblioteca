package org.casadeguara.dialogos;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;

public class DialogoAlterarChave extends Dialog<String> {

	private PasswordField txtChaveMestra;

	public DialogoAlterarChave() {
		setTitle("Alteração da chave mestra");
		setHeaderText("Digite a nova chave mestra");

		ButtonType alterar = new ButtonType("Alterar", ButtonData.OK_DONE);
		ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(alterar, cancelar);
		dialogPane.setContent(createContent());

		setResultConverter(button -> {
			if (button == alterar) {
				return txtChaveMestra.getText();
			}
			return null;
		});
	}

	public VBox createContent() {
		Label textoJanela = new Label("Insira a nova chave mestra:");
		txtChaveMestra = new PasswordField();

		VBox content = new VBox(5);
		content.getChildren().addAll(textoJanela, txtChaveMestra);

		return content;
	}

}
