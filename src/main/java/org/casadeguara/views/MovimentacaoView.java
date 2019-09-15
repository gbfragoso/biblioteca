package org.casadeguara.views;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.componentes.CustomComboBox;
import org.casadeguara.movimentacao.Emprestimo;
import org.casadeguara.movimentacao.Item;
import org.casadeguara.utilitarios.Formatador;

/**
 * Constroi a tela de movimentação.
 * @author Gustavo
 */
@SuppressWarnings("unchecked")
public class MovimentacaoView implements GenericView{

    private final AnchorPane painelMovimentacao;

    private ObservableList<Item> exemplaresParaEmprestimo;
    private ObservableList<Emprestimo> emprestimosAtuais;

    private Button btnAdicionar;
    private Button btnDevolver;
    private Button btnEmprestar;
    private Button btnRecibo;
    private Button btnRemover;
    private Button btnRenovar;
    private CustomComboBox<String> pesquisarLeitores;
    private CustomComboBox<Item> pesquisarExemplares;
    private TableView<Item> tabelaItemsParaEmprestimo;
    private TableView<Emprestimo> tabelaEmprestimosAtuais;
    
    private Formatador formatar;

    public MovimentacaoView() {
        formatar = new Formatador();
        this.exemplaresParaEmprestimo = FXCollections.observableArrayList();
        this.emprestimosAtuais = FXCollections.observableArrayList();
        
        painelMovimentacao = new AnchorPane();
        
        Label titulo = new Label("Movimentação");
        aplicarEstilo(titulo, "titulo");
        
        Label lblLeitor = new Label("Selecione um leitor: ");
        Label lblItems = new Label("Adicione itens para empréstimo");
        Label lblEmprestimos = new Label("Empréstimos ativos do leitor selecionado");
        
        btnAdicionar = new Button("Adicionar");
        btnDevolver = new Button("Devolver item");
        btnEmprestar = new Button("Emprestar Itens");
        btnRecibo = new Button("Gerar recibo");
        btnRemover = new Button("Remover item");
        btnRenovar = new Button("Renovar item");
        
        aplicarEstilo(btnDevolver, "button-danger");
        aplicarEstilo(btnRemover, "button-danger");
        
        configurarTamanhoBotao(btnAdicionar);
        configurarTamanhoBotao(btnDevolver);
        configurarTamanhoBotao(btnEmprestar);
        configurarTamanhoBotao(btnRecibo);
        configurarTamanhoBotao(btnRemover);
        configurarTamanhoBotao(btnRenovar);

        pesquisarExemplares = new CustomComboBox<>();
        pesquisarLeitores = new CustomComboBox<>();
        
        tabelaItemsParaEmprestimo = new TableView<>();
        tabelaEmprestimosAtuais = new TableView<>();

        configurarTabela(tabelaItemsParaEmprestimo, "Nenhum livro adicionado");
        configurarTabela(tabelaEmprestimosAtuais, "Este leitor não possui empréstimos ativos");
        configurarColunasTabelaEmprestimosAtuais();
        configurarColunasTabelaItensParaEmprestimo();

        AnchorPane.setTopAnchor(titulo, 0.0);
        AnchorPane.setTopAnchor(lblLeitor, 43.0);
        AnchorPane.setTopAnchor(lblItems, 95.0);
        AnchorPane.setTopAnchor(btnRecibo, 37.0);
        AnchorPane.setTopAnchor(btnAdicionar, 127.0);
        AnchorPane.setTopAnchor(pesquisarLeitores, 37.0);
        AnchorPane.setTopAnchor(pesquisarExemplares, 127.0);
        AnchorPane.setTopAnchor(tabelaItemsParaEmprestimo, 170.0);
        AnchorPane.setTopAnchor(btnRemover, 325.0);
        AnchorPane.setTopAnchor(btnEmprestar, 325.0);
        AnchorPane.setTopAnchor(lblEmprestimos, 390.0);
        AnchorPane.setTopAnchor(tabelaEmprestimosAtuais, 425.0);
        AnchorPane.setTopAnchor(btnRenovar, 580.0);
        AnchorPane.setTopAnchor(btnDevolver, 580.0);
        
        AnchorPane.setRightAnchor(btnRecibo, 0.0);
        AnchorPane.setRightAnchor(btnAdicionar, 0.0);
        AnchorPane.setRightAnchor(btnEmprestar, 0.0);
        AnchorPane.setRightAnchor(btnDevolver, 0.0);
        AnchorPane.setRightAnchor(pesquisarLeitores, 140.0);
        AnchorPane.setRightAnchor(pesquisarExemplares, 140.0);
        AnchorPane.setRightAnchor(tabelaItemsParaEmprestimo, 0.0);
        AnchorPane.setRightAnchor(tabelaEmprestimosAtuais, 0.0);
        
        AnchorPane.setLeftAnchor(lblLeitor, 0.0);
        AnchorPane.setLeftAnchor(lblItems, 0.0);
        AnchorPane.setLeftAnchor(lblEmprestimos, 0.0);
        AnchorPane.setLeftAnchor(pesquisarLeitores, 165.0);
        AnchorPane.setLeftAnchor(pesquisarExemplares, 0.0);
        AnchorPane.setLeftAnchor(btnRemover, 0.0);
        AnchorPane.setLeftAnchor(btnRenovar, 0.0);
        AnchorPane.setLeftAnchor(tabelaItemsParaEmprestimo, 0.0);
        AnchorPane.setLeftAnchor(tabelaEmprestimosAtuais, 0.0);

        painelMovimentacao.getChildren().addAll(titulo, lblLeitor, lblItems, lblEmprestimos, pesquisarLeitores,
                pesquisarExemplares, btnAdicionar, btnDevolver, btnEmprestar, btnRecibo,
                btnRemover, btnRenovar, tabelaItemsParaEmprestimo, tabelaEmprestimosAtuais);
    }
    
    private void configurarTabela(TableView<?> tabela, String placeholder) {
        tabela.setPrefHeight(150);
        tabela.setPlaceholder(new Label(placeholder));
        tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void configurarColunasTabelaEmprestimosAtuais() {
        TableColumn<Emprestimo, Integer> idemprestimo = new TableColumn<>("Id");
        TableColumn<Emprestimo, String> titulo = new TableColumn<>("Título");
        TableColumn<Emprestimo, Integer> numeroExemplar = new TableColumn<>("Exemplar");
        TableColumn<Emprestimo, LocalDate> dataDevolucao = new TableColumn<>("Devolução");
        
        vincularColunaAtributo(idemprestimo, "idEmprestimo");
        vincularColunaAtributo(titulo, "itemAcervo");
        vincularColunaAtributo(numeroExemplar, "numeroItem");
        vincularColunaAtributo(dataDevolucao, "dataDevolucao");
        
        configurarTamanhoFixo(idemprestimo, tabelaEmprestimosAtuais, 0.10);
        configurarTamanhoFixo(titulo, tabelaEmprestimosAtuais, 0.70);
        configurarTamanhoFixo(numeroExemplar, tabelaEmprestimosAtuais, 0.10);
        configurarTamanhoFixo(dataDevolucao, tabelaEmprestimosAtuais, 0.10);

        dataDevolucao.setCellFactory(coluna -> {
            return new TableCell<Emprestimo, LocalDate>(){
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
        
        tabelaEmprestimosAtuais.setItems(emprestimosAtuais);
        tabelaEmprestimosAtuais.getColumns().addAll(idemprestimo, titulo, numeroExemplar, dataDevolucao);
    }
 
    private void configurarColunasTabelaItensParaEmprestimo() {
        TableColumn<Item, String> tabelaItemsTitulo = new TableColumn<>("Título");
        TableColumn<Item, Integer> tabelaItemsExemplar = new TableColumn<>("Exemplar");
        TableColumn<Item, String> tabelaItemsStatus = new TableColumn<>("Status");
        TableColumn<Item, LocalDate> tabelaItemsDevolucao = new TableColumn<>("Devolução");

        vincularColunaAtributo(tabelaItemsTitulo, "titulo");
        vincularColunaAtributo(tabelaItemsExemplar, "numero");
        vincularColunaAtributo(tabelaItemsStatus, "status");
        vincularColunaAtributo(tabelaItemsDevolucao, "dataDevolucao");
        
        configurarTamanhoFixo(tabelaItemsTitulo, tabelaItemsParaEmprestimo, 0.70);
        configurarTamanhoFixo(tabelaItemsExemplar, tabelaItemsParaEmprestimo, 0.10);
        configurarTamanhoFixo(tabelaItemsStatus, tabelaItemsParaEmprestimo, 0.10);
        configurarTamanhoFixo(tabelaItemsDevolucao, tabelaItemsParaEmprestimo, 0.10);

        tabelaItemsDevolucao.setCellFactory(column -> {
            return new TableCell<Item, LocalDate>(){
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
        
        tabelaItemsStatus.setCellFactory(column -> {
            return new TableCell<Item, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty) {
                        if(item.equals("Arquivado")) {
                            setBackground(corFundo(Color.SANDYBROWN));
                        } else {
                            setBackground(corFundo(Color.LIGHTSEAGREEN));
                        }
                        setText(item);
                    } else {
                        setBackground(null);
                        setText("");
                    }
                }
            };
        });
        
        tabelaItemsParaEmprestimo.setItems(exemplaresParaEmprestimo);
        tabelaItemsParaEmprestimo.getColumns().addAll(tabelaItemsTitulo, tabelaItemsExemplar, 
                tabelaItemsStatus, tabelaItemsDevolucao);
    }
    
    @Override
    public void configurarTamanhoBotao(Button button) {
        button.setPrefSize(135, 32);
    }
    
    private void configurarTamanhoFixo(TableColumn<?,?> coluna, TableView<?> tabela, double percent) {
        coluna.prefWidthProperty().bind(tabela.widthProperty().multiply(percent));
        coluna.setResizable(false);
    }

    private void vincularColunaAtributo(TableColumn<?, ?> coluna, String atributo) {
        coluna.setCellValueFactory(new PropertyValueFactory<>(atributo));
    }
    
    private Background corFundo(Color color) {
        return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    }
    
    public void acaoBtnAdicionar(EventHandler<ActionEvent> event) {
        btnAdicionar.setOnAction(event);
    }

    public void acaoBtnDevolver(EventHandler<ActionEvent> event) {
        btnDevolver.setOnAction(event);
    }

    public void acaoBtnEmprestar(EventHandler<ActionEvent> event) {
        btnEmprestar.setOnAction(event);
    }

    public void acaoBtnRecibo(EventHandler<ActionEvent> event) {
        btnRecibo.setOnAction(event);
    }
    
    public void acaoBtnRemover(EventHandler<ActionEvent> event) {
        btnRemover.setOnAction(event);
    }

    public void acaoBtnRenovar(EventHandler<ActionEvent> event) {
        btnRenovar.setOnAction(event);
    }
    
    public void acaoSelecionarLeitor(ChangeListener<String> listener) {
        pesquisarLeitores.getSelectionModel().selectedItemProperty().addListener(listener);
    }
    
    public Item getExemplarSelecionado() {
        return pesquisarExemplares.getSelectionModel().getSelectedItem();
    }

    public String getLeitorSelecionado() {
        return pesquisarLeitores.getSelectionModel().getSelectedItem();
    }

    public int getQuantidadeItens() {
        return exemplaresParaEmprestimo.size() + emprestimosAtuais.size();
    }
    
    public ObservableList<Item> getExemplaresParaEmprestimo() {
        return exemplaresParaEmprestimo;
    }
    
    public ObservableList<Emprestimo> getEmprestimosSelecionados() {
        return tabelaEmprestimosAtuais.getSelectionModel().getSelectedItems();
    }
    
    public ObservableList<Item> getExemplaresSelecionados() {
        return tabelaItemsParaEmprestimo.getSelectionModel().getSelectedItems();
    }

    public int adicionarExemplar(Item exemplar) {
        if(!exemplaresParaEmprestimo.contains(exemplar)) {
            exemplaresParaEmprestimo.add(exemplar);
            pesquisarExemplares.getSelectionModel().clearSelection();
            return 0;
        }
        return 1;
    }
    
    public void removerEmprestimosSelecionados() {
        emprestimosAtuais.removeAll(getEmprestimosSelecionados());
        tabelaEmprestimosAtuais.getSelectionModel().clearSelection();
    }
    
    public void removerExemplaresSelecionados() {
        exemplaresParaEmprestimo.removeAll(getExemplaresSelecionados());
        tabelaItemsParaEmprestimo.getSelectionModel().clearSelection();
    }
    
    public boolean mensagemAutorizacao(String mensagem) {
        return new Alerta().autorizacao(mensagem);
    }
    
    public void setListaExemplares(ObservableList<Item> listaExemplares) {
        pesquisarExemplares.setItems(null);
        pesquisarExemplares.setItems(listaExemplares);
    }
    
    public void setListaLeitores(ObservableList<String> listaLeitores) {
        pesquisarLeitores.setItems(null);
        pesquisarLeitores.setItems(listaLeitores);
    }
    
    public void setEmprestimosAtuais(ObservableList<Emprestimo> emprestimos) {
        emprestimosAtuais.setAll(emprestimos);
    }
    
    public void limparExemplaresParaEmprestimo() {
        exemplaresParaEmprestimo.clear();
    }
    
    @Override
    public AnchorPane getRoot() {
        return painelMovimentacao;
    }
}
