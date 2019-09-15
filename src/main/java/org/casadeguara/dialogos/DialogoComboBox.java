package org.casadeguara.dialogos;

import org.casadeguara.componentes.CustomComboBox;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

public class DialogoComboBox extends Dialog<String>{
    
    private CustomComboBox<String> combobox;

    public DialogoComboBox(String prompt, String header, ObservableList<String> lista) {
        setHeaderText(header);

        getDialogPane().setContent(createContent(prompt, lista));
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        
        setResultConverter(button -> {
            if(button.equals(ButtonType.OK)) {
                return combobox.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }
    
    private GridPane createContent(String prompt, ObservableList<String> lista) {
        combobox = new CustomComboBox<>(lista);
        combobox.setPromptText(prompt);
        
        GridPane grid = new GridPane();
        grid.add(combobox, 0, 0);
        return grid;
    }
}
