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
	private ToggleButton consultaEmprestimos;
	private ToggleButton consultaExemplares;
	private ToggleButton consultaSeries;
	private ToggleButton cadastroAutor;
	private ToggleButton cadastroEditora;
	private ToggleButton cadastroLeitor;
	private ToggleButton cadastroLivro;
	private ToggleButton cadastroPalavra;
	private ToggleButton cadastroSerie;
	private ToggleGroup group;

	public MenuLateral() {
		menuLateral = new VBox(10);
		menuLateral.setPrefWidth(250);
		menuLateral.setId("menuLateral");
		Label acervo = new Label("Acervo");
		Label consultas = new Label("Consultas");
		Label cadastro = new Label("Cadastro");

		movimentacao = new ToggleButton("Movimentação");
		consultaEmprestimos = new ToggleButton("Empréstimos");
		consultaExemplares = new ToggleButton("Exemplares");
		consultaSeries = new ToggleButton("Séries");
		cadastroAutor = new ToggleButton("Autores");
		cadastroEditora = new ToggleButton("Editoras");
		cadastroLeitor = new ToggleButton("Leitores");
		cadastroLivro = new ToggleButton("Livros");
		cadastroPalavra = new ToggleButton("Palavras-chave");
		cadastroSerie = new ToggleButton("Séries");

		movimentacao.setPrefSize(250, 32);
		consultaEmprestimos.setPrefSize(250, 32);
		consultaExemplares.setPrefSize(250, 32);
		consultaSeries.setPrefSize(250, 32);
		cadastroAutor.setPrefSize(250, 32);
		cadastroEditora.setPrefSize(250, 32);
		cadastroLeitor.setPrefSize(250, 32);
		cadastroLivro.setPrefSize(250, 32);
		cadastroPalavra.setPrefSize(250, 32);
		cadastroSerie.setPrefSize(250, 32);

		movimentacao.setAlignment(Pos.BASELINE_LEFT);
		consultaEmprestimos.setAlignment(Pos.BASELINE_LEFT);
		consultaExemplares.setAlignment(Pos.BASELINE_LEFT);
		consultaSeries.setAlignment(Pos.BASELINE_LEFT);
		cadastroAutor.setAlignment(Pos.BASELINE_LEFT);
		cadastroEditora.setAlignment(Pos.BASELINE_LEFT);
		cadastroLeitor.setAlignment(Pos.BASELINE_LEFT);
		cadastroLivro.setAlignment(Pos.BASELINE_LEFT);
		cadastroPalavra.setAlignment(Pos.BASELINE_LEFT);
		cadastroSerie.setAlignment(Pos.BASELINE_LEFT);

		group = new ToggleGroup();
		group.getToggles().addAll(movimentacao, consultaEmprestimos, consultaExemplares, consultaSeries, cadastroAutor,
				cadastroEditora, cadastroLeitor, cadastroLivro, cadastroPalavra, cadastroSerie);
		group.selectToggle(movimentacao);

		menuLateral.getChildren().addAll(acervo, movimentacao, consultas, consultaEmprestimos, consultaExemplares,
				consultaSeries, cadastro, cadastroAutor, cadastroEditora, cadastroLeitor, cadastroLivro,
				cadastroPalavra, cadastroSerie);
	}

	public void destacarMenuConsulta() {
		group.selectToggle(consultaExemplares);
	}

	public void configurarAcesso(boolean[] acesso) {
		movimentacao.setDisable(!acesso[0]);
		consultaExemplares.setDisable(!acesso[1]);
		cadastroAutor.setDisable(!acesso[3]);
		cadastroEditora.setDisable(!acesso[4]);
		cadastroLeitor.setDisable(!acesso[5]);
		cadastroLivro.setDisable(!acesso[6]);
		cadastroPalavra.setDisable(!acesso[7]);
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

	public void acaoBotaoCadastroSerie(EventHandler<ActionEvent> event) {
		acaoBotao(cadastroSerie, event);
	}

	public void acaoBotaoConsultaEmprestimos(EventHandler<ActionEvent> event) {
		acaoBotao(consultaEmprestimos, event);
	}

	public void acaoBotaoConsultaExemplares(EventHandler<ActionEvent> event) {
		acaoBotao(consultaExemplares, event);
	}

	public void acaoBotaoConsultaSeries(EventHandler<ActionEvent> event) {
		acaoBotao(consultaSeries, event);
	}

	public void acaoBotaoMovimentacao(EventHandler<ActionEvent> event) {
		acaoBotao(movimentacao, event);
	}

	public VBox getRoot() {
		return menuLateral;
	}
}