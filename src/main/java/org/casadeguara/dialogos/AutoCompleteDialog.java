package org.casadeguara.dialogos;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.models.GenericModel;

/**
 * Janela de diálogo com um textfield autocompletável
 * 
 * @author Gustavo
 */
public class AutoCompleteDialog<T> extends Dialog<T> {

	private AutoCompleteTextField<T> field;

	public AutoCompleteDialog(String prompt, String header, GenericModel<T> model) {
		setHeaderText(header);

		DialogPane dialogPane = getDialogPane();
		dialogPane.setContent(createContent(prompt, model));
		dialogPane.getButtonTypes().add(ButtonType.OK);

		setResultConverter(button -> {
			if (button != null && button.equals(ButtonType.OK)) {
				return field.getResult();
			}
			return null;
		});
	}

	private VBox createContent(String prompt, GenericModel<T> model) {
		field = new AutoCompleteTextField<>(model, 5);
		field.setPromptText(prompt);

		VBox content = new VBox();
		content.getChildren().add(field);
		return content;
	}
}
