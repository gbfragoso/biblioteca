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

	private final int limit;
	private ContextMenu suggestions;
	private GenericModel<T> model;
	private SimpleObjectProperty<T> selectedValue = new SimpleObjectProperty<>();

	public AutoCompleteTextField() {
		this(null, 5);
	}

	public AutoCompleteTextField(GenericModel<T> model, int limit) {
		this.limit = limit;
		this.suggestions = new ContextMenu();

		setContextMenu(this.suggestions);
		setEditable(true);
		setModel(model);
	}

	@Override
	public void clear() {
		super.clear();
		this.selectedValue.set(null);
	}

	public T getResult() {
		return selectedValue.get();
	}

	public void setResult(T t) {
		this.selectedValue.set(t);
		setText(selectedValue.get().toString());
	}

	public SimpleObjectProperty<T> selectedValueProperty() {
		return this.selectedValue;
	}

	public void setModel(GenericModel<T> model) {
		this.model = model;
		configureTextProperty();
	}

	private void configureTextProperty() {
		if (this.model != null) {
			textProperty().addListener((value, oldValue, newValue) -> {
				if (newValue.length() > 2) {
					if (!this.suggestions.isShowing()) {
						this.suggestions.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
					}

					populateContextMenu(newValue);
				}
			});
		}
	}

	private void populateContextMenu(String text) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		ObservableList<T> searchResults = this.model.consultar(text.toUpperCase(), limit);

		for (T t : searchResults) {
			String result = t.toString();
			CustomMenuItem menuItem = new CustomMenuItem(new Label(result), true);

			menuItem.setOnAction(event -> {
				setText(result);
				positionCaret(result.length());
				this.selectedValue.set(t);
				this.suggestions.hide();
			});

			menuItems.add(menuItem);
		}
		this.suggestions.getItems().setAll(menuItems);
	}
}
