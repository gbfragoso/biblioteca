package org.casadeguara.dialogos;

import java.util.List;
import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.models.GenericModel;

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
public class ListViewDialog<T> extends Dialog<List<T>>{
    
    private AutoCompleteTextField<T> pesquisar;
    private ObservableList<T> listaOriginal;
    private ObservableList<T> listaAtualizada;

    public ListViewDialog(GenericModel<T> model, ObservableList<T> items) {
        listaOriginal = FXCollections.observableArrayList(items);
        listaAtualizada = FXCollections.observableArrayList(items);
        
        getDialogPane().setContent(createContent(model));
        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        setResultConverter(button -> {
            return (button.equals(ButtonType.FINISH)) ? listaAtualizada : listaOriginal;
        });
    }
    
    private AnchorPane createContent(GenericModel<T> model) {
        Button btnAdicionar = new Button("+");
        Button btnRemover = new Button("Remover");
        ListView<T> listView = new ListView<>(listaAtualizada);
        listView.setPrefSize(350.0, 350.0);
        
        pesquisar = new AutoCompleteTextField<>(model, 5);
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
    
    private T getItemPesquisado() {
        return pesquisar.getResult();
    }
    
    private void adicionar(T item) {
        if(!listaAtualizada.contains(item)) {
            listaAtualizada.add(item);
        }
    }
    
    private void remover(T item) {
        listaAtualizada.remove(item);
    }
}
