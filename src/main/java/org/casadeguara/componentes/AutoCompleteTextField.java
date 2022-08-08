package org.casadeguara.componentes;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.casadeguara.models.GenericModel;

public class AutoCompleteTextField<T> extends TextField {

	private ContextMenu suggestions;
	private GenericModel<T> model;
	private final int resultados;
	private SimpleObjectProperty<T> result = new SimpleObjectProperty<>();

	public AutoCompleteTextField() {
		this(null, 5);
	}

	public AutoCompleteTextField(GenericModel<T> model, int resultados) {
		this.resultados = resultados;

		suggestions = new ContextMenu();
		setContextMenu(suggestions);
		setEditable(true);

		if (model != null) {
			setModel(model);
		}
	}

	/**
	 * Adiciona os resultados da busca ao menu de contexto.
	 * 
	 * @param text Texto para a busca
	 */
	private void populateContextMenu(String text) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		ObservableList<T> searchResults = model.consultar(text.toUpperCase(), resultados);

		for (T t : searchResults) {
			String result = t.toString();
			CustomMenuItem menuItem = new CustomMenuItem(new Label(result), true);

			menuItem.setOnAction(event -> {
				setText(result);
				positionCaret(result.length());
				this.result.set(t);
				suggestions.hide();
			});

			menuItems.add(menuItem);
		}
		suggestions.getItems().setAll(menuItems);
	}

	@Override
	public void clear() {
		super.clear();
		result = new SimpleObjectProperty<>();
	}

	public T getResult() {
		return result.get();
	}

	public void setResult(T t) {
		this.result = new SimpleObjectProperty<>(t);
		setText(result.get().toString());
	}

	public SimpleObjectProperty<T> selectedValueProperty() {
		return this.result;
	}

	public void setModel(GenericModel<T> model) {
		this.model = model;

		textProperty().addListener((value, oldValue, newValue) -> {
			if (newValue.length() > 2) {
				if (!suggestions.isShowing()) {
					suggestions.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
				}

				populateContextMenu(newValue);
			}
		});
	}
}
