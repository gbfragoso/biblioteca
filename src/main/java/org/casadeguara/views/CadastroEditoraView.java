package org.casadeguara.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.entidades.Editora;
import org.casadeguara.models.GenericModel;

/**
 * Controi a tela de cadastro de editora.
 * @author Gustavo
 */
public class CadastroEditoraView implements GenericView{

    private AnchorPane painelEditora;
    private Button btnAlterar;
    private Button btnCadastrar;
    private Button btnLimpar;
    private AutoCompleteTextField<Editora> pesquisarEditoras;
    private TextField txtNome;

    public CadastroEditoraView() {
        painelEditora = new AnchorPane();
        
        Label titulo = new Label("Cadastro de Editoras");
        aplicarEstilo(titulo, "titulo");
        
        Label lblCadastrar = new Label("Dados da editora:");
        Label pesquise = new Label("Pesquise uma editora:");
        Label opcoes = new Label("Opções:");

        btnAlterar = new Button("Alterar");
        btnCadastrar = new Button("Cadastrar");
        btnLimpar = new Button("Limpar");
        
        configurarTamanhoBotao(btnCadastrar);
        configurarTamanhoBotao(btnAlterar);
        configurarTamanhoBotao(btnLimpar);
        
        txtNome = new TextField();
        txtNome.setPromptText("Nome da editora");
        txtNome.setPrefWidth(535);
        btnAlterar.setDisable(true);

        pesquisarEditoras = new AutoCompleteTextField<>();
        pesquisarEditoras.setPrefWidth(535);
        pesquisarEditoras.setPromptText("Digite o nome de uma editora");
        
        AnchorPane.setTopAnchor(titulo, 0.0);
        AnchorPane.setTopAnchor(pesquise, 40.0);
        AnchorPane.setTopAnchor(pesquisarEditoras, 70.0);
        AnchorPane.setTopAnchor(lblCadastrar, 130.0);
        AnchorPane.setTopAnchor(txtNome, 160.0);
        AnchorPane.setTopAnchor(opcoes, 220.0);
        AnchorPane.setTopAnchor(btnCadastrar, 250.0);
        AnchorPane.setTopAnchor(btnAlterar, 250.0);
        AnchorPane.setTopAnchor(btnLimpar, 250.0);
        
        AnchorPane.setLeftAnchor(titulo, 0.0);
        AnchorPane.setLeftAnchor(pesquise, 0.0);
        AnchorPane.setLeftAnchor(pesquisarEditoras, 0.0);
        AnchorPane.setLeftAnchor(lblCadastrar, 0.0);
        AnchorPane.setLeftAnchor(txtNome, 0.0);
        AnchorPane.setLeftAnchor(opcoes, 0.0);
        AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
        AnchorPane.setLeftAnchor(btnAlterar, 135.0);
        AnchorPane.setLeftAnchor(btnLimpar, 270.0);

        painelEditora.getChildren().addAll(titulo, pesquise, pesquisarEditoras, lblCadastrar, txtNome,
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
    
    public void acaoPesquisarEditora(EventHandler<ActionEvent> event) {
        pesquisarEditoras.setOnAction(event);
    }
    
    public void estaCadastrando(boolean resposta) {
        btnCadastrar.setDisable(!resposta);
        btnAlterar.setDisable(resposta);
        pesquisarEditoras.setDisable(!resposta);
    }
    
    public void limparCampos() {
        estaCadastrando(true);
        
        txtNome.clear();
        pesquisarEditoras.clear();
    }
    
    public String getNomeEditora() {
        return txtNome.getText().toUpperCase();
    }
    
    public void setNomeEditora(String nomeEditora) {
        txtNome.setText(nomeEditora);
    }
    
    public void setAutoComplete(GenericModel<Editora> model) {
    	pesquisarEditoras.setModel(model);
    }
    
    public Editora getTermoPesquisado() {
    	return pesquisarEditoras.getResult();
    }
    
    @Override
    public AnchorPane getRoot() {
        return painelEditora;
    }
}
