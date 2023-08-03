package org.casadeguara.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class MenuLateral {

	private VBox menuLateral;
	private ToggleButton movimentacao;
	private ToggleButton consulta;
	private ToggleButton cadastroAutor;
	private ToggleButton cadastroEditora;
	private ToggleButton cadastroLeitor;
	private ToggleButton cadastroLivro;
	private ToggleButton cadastroPalavra;
	private ToggleButton relatorios;
	private ToggleGroup group;

	public MenuLateral() {
		menuLateral = new VBox(10);
		menuLateral.setPrefWidth(250);
		menuLateral.setId("menuLateral");
		Label acervo = new Label("Acervo");
		Label cadastro = new Label("Cadastro");
		Label estatisticas = new Label("Estatísticas");

		movimentacao = new ToggleButton("Movimentação");
		consulta = new ToggleButton("Consultas");
		cadastroAutor = new ToggleButton("Autor");
		cadastroEditora = new ToggleButton("Editora");
		cadastroLeitor = new ToggleButton("Leitor");
		cadastroLivro = new ToggleButton("Livro");
		cadastroPalavra = new ToggleButton("Palavra-chave");
		relatorios = new ToggleButton("Relatórios");

		movimentacao.setPrefSize(250, 32);
		consulta.setPrefSize(250, 32);
		cadastroAutor.setPrefSize(250, 32);
		cadastroEditora.setPrefSize(250, 32);
		cadastroLeitor.setPrefSize(250, 32);
		cadastroLivro.setPrefSize(250, 32);
		cadastroPalavra.setPrefSize(250, 32);
		relatorios.setPrefSize(250, 32);

		movimentacao.setAlignment(Pos.BASELINE_LEFT);
		consulta.setAlignment(Pos.BASELINE_LEFT);
		cadastroAutor.setAlignment(Pos.BASELINE_LEFT);
		cadastroEditora.setAlignment(Pos.BASELINE_LEFT);
		cadastroLeitor.setAlignment(Pos.BASELINE_LEFT);
		cadastroLivro.setAlignment(Pos.BASELINE_LEFT);
		cadastroPalavra.setAlignment(Pos.BASELINE_LEFT);
		relatorios.setAlignment(Pos.BASELINE_LEFT);

		group = new ToggleGroup();
		group.getToggles().addAll(movimentacao, consulta, cadastroAutor, cadastroEditora, cadastroLeitor,
				cadastroLivro, cadastroPalavra, relatorios);
		group.selectToggle(movimentacao);

		menuLateral.getChildren().addAll(acervo, movimentacao, consulta, cadastro, cadastroAutor,
				cadastroEditora, cadastroLeitor, cadastroLivro, cadastroPalavra, estatisticas, relatorios);
	}

	public void destacarMenuConsulta() {
		group.selectToggle(consulta);
	}

	public void configurarAcesso(boolean[] acesso) {
		movimentacao.setDisable(!acesso[0]);
		consulta.setDisable(!acesso[1]);
		cadastroAutor.setDisable(!acesso[3]);
		cadastroEditora.setDisable(!acesso[4]);
		cadastroLeitor.setDisable(!acesso[5]);
		cadastroLivro.setDisable(!acesso[6]);
		cadastroPalavra.setDisable(!acesso[7]);
		relatorios.setDisable(!acesso[9]);
	}

	private void acaoBotao(ToggleButton button, EventHandler<ActionEvent> event) {
		button.setOnAction(event);
	}

	public void acaoBotaoCadastroAutor(EventHandler<ActionEvent> event) {
		acaoBotao(cadastroAutor, event);
	}

	public void acaoBotaoCadastroEditora(EventHandler<ActionEvent> event) {
		acaoBotao(cadastroEditora, event);
	}

	public void acaoBotaoCadastroLeitor(EventHandler<ActionEvent> event) {
		acaoBotao(cadastroLeitor, event);
	}

	public void acaoBotaoCadastroLivro(EventHandler<ActionEvent> event) {
		acaoBotao(cadastroLivro, event);
	}

	public void acaoBotaoCadastroPalavras(EventHandler<ActionEvent> event) {
		acaoBotao(cadastroPalavra, event);
	}

	public void acaoBotaoConsulta(EventHandler<ActionEvent> event) {
		acaoBotao(consulta, event);
	}

	public void acaoBotaoImpressos(EventHandler<ActionEvent> event) {
		acaoBotao(relatorios, event);
	}

	public void acaoBotaoMovimentacao(EventHandler<ActionEvent> event) {
		acaoBotao(movimentacao, event);
	}

	public VBox getRoot() {
		return menuLateral;
	}
}