package org.casadeguara.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.CustomTextField;
import java.time.LocalDate;
import org.casadeguara.componentes.CustomComboBox;
import org.casadeguara.movimentacao.Reserva;
import org.casadeguara.utilitarios.Formatador;

/**
 * Controi a tela de reserva.
 * @author Gustavo
 */
public class ReservaView implements GenericView{

    private final AnchorPane painelReserva;

    private Button btnCancelar;
    private Button btnLimpar;
    private Button btnReservar;
    private CustomComboBox<String> pesquisarLeitores;
    private CustomComboBox<String> pesquisarLivros;
    private CustomTextField pesquisarReservas;
    private TableView<Reserva> tabelaReserva;

    private ObservableList<Reserva> listaReservas;
    
    private Formatador formatar;

    @SuppressWarnings("unchecked")
    public ReservaView() {
        listaReservas = FXCollections.observableArrayList();
        formatar = new Formatador();
        
        Label titulo = new Label("Reservas");
        titulo.getStyleClass().add("titulo");
        Label lblReserva = new Label("Reserve um item para um leitor:");
        Label lblConsulta = new Label("Consulte uma reserva:");

        pesquisarLeitores = new CustomComboBox<>();
        pesquisarLeitores.setPromptText("Digite o nome de um leitor");
        pesquisarLeitores.setPrefWidth(535.0);

        pesquisarLivros = new CustomComboBox<>();
        pesquisarLivros.setPromptText("Digite o nome de um livro");
        pesquisarLivros.setPrefWidth(535.0);
        
        btnCancelar = new Button("Cancelar reserva");
        btnLimpar = new Button("Limpar");
        btnReservar = new Button("Reservar");
        
        configurarTamanhoBotao(btnCancelar);
        configurarTamanhoBotao(btnLimpar);
        configurarTamanhoBotao(btnReservar);

        pesquisarReservas = new CustomTextField();
        pesquisarReservas.setPromptText("Pesquise reservas pelo nome do leitor");
        pesquisarReservas.setRight(new ImageView("/images/search.png"));

        tabelaReserva = new TableView<>();
        tabelaReserva.setPlaceholder(new Label("Não há reservas"));

        TableColumn<Reserva, String> tabelaReservaTombo = new TableColumn<>("Tombo");
        TableColumn<Reserva, String> tabelaReservaTitulo = new TableColumn<>("Título");
        TableColumn<Reserva, String> tabelaReservaLeitor = new TableColumn<>("Leitor");
        TableColumn<Reserva, LocalDate> tabelaReservaData = new TableColumn<>("Expira em");

        configurarTamanhoFixo(tabelaReservaTombo, tabelaReserva, 0.10);
        configurarTamanhoFixo(tabelaReservaTitulo, tabelaReserva, 0.35);
        configurarTamanhoFixo(tabelaReservaLeitor, tabelaReserva, 0.35);
        configurarTamanhoFixo(tabelaReservaData, tabelaReserva, 0.15);

        vincularColunaAtributo(tabelaReservaTombo, "tombo");
        vincularColunaAtributo(tabelaReservaTitulo, "titulo");
        vincularColunaAtributo(tabelaReservaLeitor, "leitor");
        vincularColunaAtributo(tabelaReservaData, "data");
        
        tabelaReservaData.setCellFactory(coluna -> {
            return new TableCell<Reserva, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty) {
                        setText(formatar.data(item));
                    } else {
                        setText("");
                    }
                }
            };
        });

        tabelaReserva.setItems(listaReservas);
        tabelaReserva.setPrefHeight(200);
        tabelaReserva.getColumns().addAll(tabelaReservaTombo, tabelaReservaTitulo,
                tabelaReservaLeitor, tabelaReservaData);

        painelReserva = new AnchorPane();
        AnchorPane.setTopAnchor(titulo, 00.0);
        AnchorPane.setTopAnchor(lblReserva, 40.0);
        AnchorPane.setTopAnchor(pesquisarLeitores, 70.0);
        AnchorPane.setTopAnchor(pesquisarLivros, 110.0);
        AnchorPane.setTopAnchor(btnLimpar, 150.0);
        AnchorPane.setTopAnchor(btnReservar, 150.0);
        AnchorPane.setTopAnchor(lblConsulta, 230.0);
        AnchorPane.setTopAnchor(pesquisarReservas, 270.0);
        AnchorPane.setTopAnchor(tabelaReserva, 310.0);
        AnchorPane.setTopAnchor(btnCancelar, 520.0);
        AnchorPane.setLeftAnchor(lblReserva, 0.0);
        AnchorPane.setLeftAnchor(lblConsulta, 0.0);
        AnchorPane.setLeftAnchor(pesquisarLeitores, 0.0);
        AnchorPane.setLeftAnchor(pesquisarLivros, 0.0);
        AnchorPane.setLeftAnchor(pesquisarReservas, 0.0);
        AnchorPane.setLeftAnchor(btnLimpar, 137.0);
        AnchorPane.setLeftAnchor(btnReservar, 0.0);
        AnchorPane.setLeftAnchor(btnCancelar, 0.0);
        AnchorPane.setLeftAnchor(tabelaReserva, 0.0);
        AnchorPane.setRightAnchor(pesquisarReservas, 0.0);
        AnchorPane.setRightAnchor(tabelaReserva, 0.0);

        painelReserva.getChildren().addAll(titulo, lblReserva, pesquisarLeitores, pesquisarLivros,
                btnReservar, lblConsulta, btnCancelar, btnLimpar, pesquisarReservas, tabelaReserva);
    }
    
    private void configurarTamanhoFixo(TableColumn<?,?> coluna, TableView<?> tabela, double percent) {
        coluna.prefWidthProperty().bind(tabela.widthProperty().multiply(percent));
        coluna.setResizable(false);
    }
    
    private void vincularColunaAtributo(TableColumn<?, ?> coluna, String atributo) {
        coluna.setCellValueFactory(new PropertyValueFactory<>(atributo));
    }

    public void acaoBotaoCancelar(EventHandler<ActionEvent> event) {
        btnCancelar.setOnAction(event);
    }

    public void acaoBotaoLimpar(EventHandler<ActionEvent> event) {
        btnLimpar.setOnAction(event);
    }
    
    public void acaoBotaoReservar(EventHandler<ActionEvent> event) {
        btnReservar.setOnAction(event);
    }
    
    public void acaoPesquisarReserva(EventHandler<ActionEvent> event) {
        pesquisarReservas.setOnAction(event);
    }
    
    public String getLeitor() {
        return pesquisarLeitores.getSelectionModel().getSelectedItem();
    }
    
    public String getLivro() {
        return pesquisarLivros.getSelectionModel().getSelectedItem();
    }
    
    public void setListaLeitores(ObservableList<String> lista) {
        pesquisarLeitores.setItems(lista);
    }
    
    public void setListaLivros(ObservableList<String> lista) {
        pesquisarLivros.setItems(lista);
    }
    
    public void setListaReservas(ObservableList<Reserva> lista) {
        listaReservas.setAll(lista);
    }
    
    public String getReservaPesquisada() {
        return pesquisarReservas.getText().toUpperCase();
    }
    
    public Reserva getReservaSelecionada() {
        return tabelaReserva.getSelectionModel().getSelectedItem();
    }
    
    public void limparCampos() {
        pesquisarLeitores.getSelectionModel().clearSelection();
        pesquisarLivros.getSelectionModel().clearSelection();
    }
    
    public void removerReserva(Reserva reservaAtual) {
        listaReservas.remove(reservaAtual);
        tabelaReserva.getSelectionModel().clearSelection();
    }
    
    @Override
    public AnchorPane getRoot() {
        return painelReserva;
    }

}
