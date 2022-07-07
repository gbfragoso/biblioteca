package org.casadeguara.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

/**
 * Constroi a tela de Administração
 * 
 * @author Gustavo Fragoso
 */
public class AdministracaoView implements GenericView {

	private TilePane painelAdmin;
	private Button btnConfiguracao;
	private Button btnCobrancas;
	private Button btnEtiqueta;
	private Button btnRecuperar;
	private Button btnAlterarEmprestimo;
	private Button btnAlterarChaveMestra;

	public AdministracaoView() {
		painelAdmin = new TilePane();
		painelAdmin.setHgap(5);
		painelAdmin.setVgap(5);
		painelAdmin.setAlignment(Pos.TOP_CENTER);

		btnAlterarChaveMestra = new Button("Alterar chave mestra");
		btnConfiguracao = new Button("Configurações");
		btnCobrancas = new Button("Cobranças");
		btnEtiqueta = new Button("Gerador de etiquetas");
		btnAlterarEmprestimo = new Button("Alterar empréstimo");
		btnRecuperar = new Button("Recuperar empréstimo");

		configurarTamanhoBotao(btnAlterarChaveMestra);
		configurarTamanhoBotao(btnConfiguracao);
		configurarTamanhoBotao(btnCobrancas);
		configurarTamanhoBotao(btnEtiqueta);
		configurarTamanhoBotao(btnRecuperar);
		configurarTamanhoBotao(btnAlterarEmprestimo);

		painelAdmin.getChildren().addAll(btnAlterarChaveMestra, btnConfiguracao, btnCobrancas, btnEtiqueta,
				btnAlterarEmprestimo, btnRecuperar);
	}

	@Override
	public void configurarTamanhoBotao(Button button) {
		button.setPrefSize(250, 80);
	}

	public void acaoBotaoAlterarChaveMestra(EventHandler<ActionEvent> event) {
		btnAlterarChaveMestra.setOnAction(event);
	}

	public void acaoBotaoAlterarEmprestimo(EventHandler<ActionEvent> event) {
		btnAlterarEmprestimo.setOnAction(event);
	}

	public void acaoBotaoConfiguracao(EventHandler<ActionEvent> event) {
		btnConfiguracao.setOnAction(event);
	}

	public void acaoBotaoCobrancas(EventHandler<ActionEvent> event) {
		btnCobrancas.setOnAction(event);
	}

	public void acaoBotaoEtiqueta(EventHandler<ActionEvent> event) {
		btnEtiqueta.setOnAction(event);
	}

	public void acaoBotaoRecuperarEmprestimo(EventHandler<ActionEvent> event) {
		btnRecuperar.setOnAction(event);
	}

	@Override
	public TilePane getRoot() {
		return painelAdmin;
	}
}
