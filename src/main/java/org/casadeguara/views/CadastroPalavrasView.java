package org.casadeguara.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.models.GenericModel;

/**
 * Constroi a tela de cadastro de palavras-chave
 * @author Gustavo
 */
public class CadastroPalavrasView implements GenericView{

    private AnchorPane painelAssunto;
    private Button btnAlterar;
    private Button btnCadastrar;
    private Button btnLimpar;
    private AutoCompleteTextField<PalavraChave> pesquisarPalavras;
    private TextField txtAssunto;

    public CadastroPalavrasView() {
        painelAssunto = new AnchorPane();
        
        Label titulo = new Label("Cadastro de Palavras-chave");
        aplicarEstilo(titulo, "titulo");
        
        Label lblCadastrar = new Label("Dados da palavra-chave:");
        Label pesquise = new Label("Pesquise uma palavra-chave:");
        Label opcoes = new Label("Opções:");

        btnCadastrar = new Button("Cadastrar");
        btnAlterar = new Button("Alterar");
        btnLimpar = new Button("Limpar");
        
        configurarTamanhoBotao(btnCadastrar);
        configurarTamanhoBotao(btnAlterar);
        configurarTamanhoBotao(btnLimpar);
        
        txtAssunto = new TextField();
        txtAssunto.setPrefWidth(535);
        txtAssunto.setPromptText("Digite uma palavra-chave");
        
        btnAlterar.setDisable(true);

        pesquisarPalavras = new AutoCompleteTextField<>();
        pesquisarPalavras.setPrefWidth(535);
        pesquisarPalavras.setPromptText("Digite uma palavra-chave");

        AnchorPane.setTopAnchor(titulo, 0.0);
        AnchorPane.setTopAnchor(pesquise, 40.0);
        AnchorPane.setTopAnchor(pesquisarPalavras, 70.0);
        AnchorPane.setTopAnchor(lblCadastrar, 130.0);
        AnchorPane.setTopAnchor(txtAssunto, 160.0);
        AnchorPane.setTopAnchor(opcoes, 220.0);
        AnchorPane.setTopAnchor(btnCadastrar, 250.0);
        AnchorPane.setTopAnchor(btnAlterar, 250.0);
        AnchorPane.setTopAnchor(btnLimpar, 250.0);
        
        AnchorPane.setLeftAnchor(titulo, 0.0);
        AnchorPane.setLeftAnchor(pesquise, 0.0);
        AnchorPane.setLeftAnchor(pesquisarPalavras, 0.0);
        AnchorPane.setLeftAnchor(lblCadastrar, 0.0);
        AnchorPane.setLeftAnchor(txtAssunto, 0.0);
        AnchorPane.setLeftAnchor(opcoes, 0.0);
        AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
        AnchorPane.setLeftAnchor(btnAlterar, 135.0);
        AnchorPane.setLeftAnchor(btnLimpar, 270.0);
 
        painelAssunto.getChildren().addAll(titulo, pesquise, pesquisarPalavras, lblCadastrar, txtAssunto,
                opcoes, btnCadastrar, btnLimpar, btnAlterar);
    }
    
    public void acaoBotaoAlterar(EventHandler<ActionEvent> event) {
        btnAlterar.setOnAction(event);
    }

    public void acaoBotaoCadastrar(EventHandler<ActionEvent> event) {
        btnCadastrar.setOnAction(event);
    }
    
    public void acaoBotaoLimpar(EventHandler<ActionEvent> event) {
        btnLimpar.setOnAction(event);
    }

    public void acaoPesquisarPalavraChave(EventHandler<ActionEvent> event) {
        pesquisarPalavras.setOnAction(event);
    }
    
    public void estaCadastrando(boolean resposta) {
        btnAlterar.setDisable(resposta);
        btnCadastrar.setDisable(!resposta);
        pesquisarPalavras.setDisable(!resposta);
    }

    public void limparCampos() {
        estaCadastrando(true);
        
        txtAssunto.clear();
        pesquisarPalavras.clear();
    }
    
    public String getPalavraChave() {
        return txtAssunto.getText().toUpperCase();
    }
    
    public void setPalavraChave(String novaPalavra) {
        txtAssunto.setText(novaPalavra);
    }
    
    public void setAutoComplete(GenericModel<PalavraChave> model) {
    	pesquisarPalavras.setModel(model);
    }
    
    public PalavraChave getTermoPesquisado() {
    	return pesquisarPalavras.getResult();
    }
    
    @Override
    public AnchorPane getRoot() {
        return painelAssunto;
    }
}
