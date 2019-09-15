package org.casadeguara.dialogos;

import java.util.List;
import org.casadeguara.componentes.RangeTextField;
import org.casadeguara.entidades.Exemplar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class DialogoAdicionarExemplar extends Dialog<List<Exemplar>>{
    
    private RangeTextField numeroExemplares;
    private Button btnAdicionar;
    private Button btnMudarStatus;
    private TableView<Exemplar> tabela;
    
    private ObservableList<Exemplar> listaOriginal;
    private ObservableList<Exemplar> listaExemplares;

    public DialogoAdicionarExemplar(ObservableList<Exemplar> lista) {
        listaOriginal = FXCollections.observableArrayList(lista);
        listaExemplares = FXCollections.observableArrayList(lista);
        
        getDialogPane().setContent(createContent());
        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        setResultConverter(button -> {
            return (button.equals(ButtonType.FINISH)) ? listaExemplares : listaOriginal;
        });
    }
    
    @SuppressWarnings("unchecked")
    private AnchorPane createContent() {
        numeroExemplares = new RangeTextField();
        btnAdicionar = new Button("+");
        btnMudarStatus = new Button("Mudar Status");
        
        tabela = new TableView<>();
        tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Exemplar, Integer> numero = new TableColumn<>("Número");
        TableColumn<Exemplar, String> status = new TableColumn<>("Status");

        numero.setCellValueFactory(new PropertyValueFactory<Exemplar, Integer>("numero"));
        status.setCellValueFactory(new PropertyValueFactory<Exemplar, String>("status"));
        numero.prefWidthProperty().bind(tabela.widthProperty().multiply(0.45));
        status.prefWidthProperty().bind(tabela.widthProperty().multiply(0.45));

        tabela.setItems(listaExemplares);
        tabela.getColumns().addAll(numero, status);
        tabela.setPrefSize(350.0, 350.0);
        
        AnchorPane painel = new AnchorPane();
        AnchorPane.setTopAnchor(numeroExemplares, 2.0);
        AnchorPane.setTopAnchor(btnAdicionar, 2.0);
        AnchorPane.setTopAnchor(tabela, 35.0);
        AnchorPane.setLeftAnchor(numeroExemplares, 2.0);
        AnchorPane.setLeftAnchor(tabela, 2.0);
        AnchorPane.setRightAnchor(numeroExemplares, 30.0);
        AnchorPane.setRightAnchor(btnMudarStatus, 2.0);
        AnchorPane.setRightAnchor(btnAdicionar, 2.0);
        AnchorPane.setRightAnchor(tabela, 2.0);
        AnchorPane.setBottomAnchor(tabela, 35.0);
        AnchorPane.setBottomAnchor(btnMudarStatus, 2.0);
        
        btnAdicionar.setOnAction(event -> adicionar(gerarExemplares()));
        btnMudarStatus.setOnAction(event -> mudarStatusExemplar());

        painel.getChildren().addAll(numeroExemplares, btnAdicionar, tabela, btnMudarStatus);
        return painel;
    }
    
    private ObservableList<Exemplar> gerarExemplares() {
        List<Integer> numeros = numeroExemplares.getRange();
        ObservableList<Exemplar> lista = FXCollections.observableArrayList();
        numeros.stream().forEach(num -> lista.add(new Exemplar(0, num)));
        return lista;
    }
    
    private void adicionar(List<Exemplar> lista) {
        for(Exemplar e: lista) {
            if(!listaExemplares.contains(e)) {
                listaExemplares.add(e);
            }
        }
    }
    
    private void mudarStatusExemplar() {
        List<Exemplar> exemplares = tabela.getSelectionModel().getSelectedItems();

        ObservableList<String> escolhas = FXCollections.observableArrayList();
        escolhas.addAll("Arquivado", "Disponível", "Evangelização", "Indisponível");

        ChoiceDialog<String> choice = new ChoiceDialog<>("Disponível", escolhas);
        choice.showAndWait();
        String novoStatus = choice.getResult();

        if (novoStatus != null) {
            exemplares.stream().forEach(e -> e.setStatus(novoStatus));
        }

        tabela.setItems(null);
        tabela.layout();
        tabela.setItems(listaExemplares);
    }
}

