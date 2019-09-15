package org.casadeguara.dialogos;

import java.util.List;
import org.casadeguara.componentes.CustomComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * Caixa de diálogo que retorna uma lista com o nome dos itens adicionados.
 * @author Gustavo
 * @since 4.0
 */
public class DialogoListaItens extends Dialog<List<String>>{
    
    private CustomComboBox<String> pesquisar;
    private ObservableList<String> listaOriginal;
    private ObservableList<String> listaAtualizada;

    public DialogoListaItens(ObservableList<String> items) {
        listaOriginal = FXCollections.observableArrayList(items);
        listaAtualizada = FXCollections.observableArrayList(items);
        
        getDialogPane().setContent(createContent());
        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        setResultConverter(button -> {
            return (button.equals(ButtonType.FINISH)) ? listaAtualizada : listaOriginal;
        });
    }
    
    private AnchorPane createContent() {
        Button btnAdicionar = new Button("+");
        Button btnRemover = new Button("Remover");
        ListView<String> listView = new ListView<>(listaAtualizada);
        listView.setPrefSize(350.0, 350.0);
        
        pesquisar = new CustomComboBox<>();
        pesquisar.setPrefWidth(535);
        
        AnchorPane painel = new AnchorPane();
        AnchorPane.setTopAnchor(pesquisar, 2.0);
        AnchorPane.setTopAnchor(btnAdicionar, 2.0);
        AnchorPane.setTopAnchor(listView, 35.0);
        AnchorPane.setLeftAnchor(pesquisar, 2.0);
        AnchorPane.setLeftAnchor(listView, 2.0);
        AnchorPane.setRightAnchor(pesquisar, 32.0);
        AnchorPane.setRightAnchor(btnAdicionar, 2.0);
        AnchorPane.setRightAnchor(btnRemover, 2.0);
        AnchorPane.setRightAnchor(listView, 2.0);
        AnchorPane.setBottomAnchor(listView, 35.0);
        AnchorPane.setBottomAnchor(btnRemover, 2.0);
        
        btnAdicionar.setOnAction(event -> adicionar(getItemPesquisado()));
        btnRemover.setOnAction(event -> remover(listView.getSelectionModel().getSelectedItem()));
        
        painel.getChildren().addAll(pesquisar, btnAdicionar, btnRemover,
                listView);
        return painel;
    }
    
    private String getItemPesquisado() {
        return pesquisar.getSelectionModel().getSelectedItem().toUpperCase();
    }
    
    private void adicionar(String item) {
        if(!listaAtualizada.contains(item)) {
            listaAtualizada.add(item);
        }
    }
    
    private void remover(String item) {
        listaAtualizada.remove(item);
    }
    
    public void setListaSugestoes(ObservableList<String> lista) {
        pesquisar.setItems(lista);
    }
}
