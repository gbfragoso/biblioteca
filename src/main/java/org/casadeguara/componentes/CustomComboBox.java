package org.casadeguara.componentes;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * Esta classe extende a funcionalidade comum do combobox oferecendo uma busca de itens conforme o
 * usuário digita. Esses itens aparecem em uma pequena caixa de contexto abaixo do componente que
 * seleciona o item correspondente ao ser clicada.
 * 
 * @author Gustavo
 * @param <T> Tipo do objeto contido no combobox
 */
public class CustomComboBox<T> extends ComboBox<T> {

    private int maxSize = 5;
    private TextField editor;
    private ContextMenu suggestions;
    
    public CustomComboBox() {
        this(null);
    }

    public CustomComboBox(ObservableList<T> items) {
        setItems(items);
        setEditable(true);

        this.editor = getEditor();

        suggestions = new ContextMenu();
        setContextMenu(suggestions);

        editor.textProperty().addListener((value, oldValue, newValue) -> {
            if (newValue.length() > 2) {
                if (!suggestions.isShowing()) {
                    suggestions.show(CustomComboBox.this, Side.BOTTOM, 0, 0);
                }

                populateContextMenu(newValue);
            }
        });

        setConverter(new StringConverter<T>() {

            @Override
            public T fromString(String arg0) {
                return getSelectionModel().getSelectedItem();
            }

            @Override
            public String toString(T item) {
                return (item != null) ? item.toString() : "";
            }
        });

        showingProperty().addListener((value, oldValue, newValue) -> suggestions.hide());
    }

    /**
     * Adiciona os resultados da busca ao menu de contexto.
     * 
     * @param text Texto para a busca
     */
    private void populateContextMenu(String text) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        ObservableList<T> searchResults = searchTerm(text);

        for (T t : searchResults) {
            CustomMenuItem menuItem = new CustomMenuItem(new Label(t.toString()), true);

            menuItem.setOnAction(event -> {
                super.setValue(t);
                editor.positionCaret(t.toString().length());
                suggestions.hide();
            });

            menuItems.add(menuItem);
        }
        suggestions.getItems().setAll(menuItems);
    }

    private ObservableList<T> searchTerm(String term) {
        String searchTerm = removerAcentuacao(term.toLowerCase());
        return getItems().parallelStream()
                .filter(p -> removerAcentuacao(p.toString().toLowerCase()).contains(searchTerm))
                .limit(maxSize)
                .collect(Collectors.toCollection(FXCollections::<T>observableArrayList));
    }

    private static String removerAcentuacao(String text) {
        return (text == null) ? 
                null : Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
