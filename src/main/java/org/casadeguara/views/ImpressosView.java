package org.casadeguara.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

/**
 * Constroi a tela de impressos.
 * 
 * @author Gustavo
 */
public class ImpressosView implements GenericView {

	private TilePane painelImpressos;
	private Button btnEmprestimo;
	private Button btnInventario;
	private Button btnHistorico;
	private Button btnLeitor;
	private Button btnEmprestimoData;
	private Button btnEmprestimoLivro;
	private Button btnEmprestimosAtrasados;

	public ImpressosView() {
		painelImpressos = new TilePane();

		painelImpressos.setHgap(5);
		painelImpressos.setVgap(5);
		painelImpressos.setAlignment(Pos.TOP_CENTER);

		btnEmprestimo = new Button("Empréstimos");
		btnEmprestimoData = new Button("Empréstimos por Data");
		btnEmprestimoLivro = new Button("Empréstimos por Livro");
		btnEmprestimosAtrasados = new Button("Empréstimos Atrasados");
		btnHistorico = new Button("Histórico do leitor");
		btnInventario = new Button("Inventário");
		btnLeitor = new Button("Relação de Leitores");

		configurarTamanhoBotao(btnEmprestimo);
		configurarTamanhoBotao(btnEmprestimoData);
		configurarTamanhoBotao(btnEmprestimoLivro);
		configurarTamanhoBotao(btnEmprestimosAtrasados);
		configurarTamanhoBotao(btnHistorico);
		configurarTamanhoBotao(btnInventario);
		configurarTamanhoBotao(btnLeitor);

		painelImpressos.getChildren().addAll(btnEmprestimo, btnEmprestimoData, btnEmprestimoLivro,
				btnEmprestimosAtrasados, btnHistorico, btnInventario, btnLeitor);
	}

	@Override
	public void configurarTamanhoBotao(Button button) {
		button.setPrefSize(250, 80);
	}

	public void acaoBtnEmprestimo(EventHandler<ActionEvent> event) {
		btnEmprestimo.setOnAction(event);
	}

	public void acaoBtnEmprestimoPorData(EventHandler<ActionEvent> event) {
		btnEmprestimoData.setOnAction(event);
	}

	public void acaoBtnEmprestimoPorLivro(EventHandler<ActionEvent> event) {
		btnEmprestimoLivro.setOnAction(event);
	}

	public void acaoBtnEmprestimosAtrasados(EventHandler<ActionEvent> event) {
		btnEmprestimosAtrasados.setOnAction(event);
	}

	public void acaoBtnHistorico(EventHandler<ActionEvent> event) {
		btnHistorico.setOnAction(event);
	}

	public void acaoBtnInventario(EventHandler<ActionEvent> event) {
		btnInventario.setOnAction(event);
	}

	public void acaoBtnLeitor(EventHandler<ActionEvent> event) {
		btnLeitor.setOnAction(event);
	}

	@Override
	public TilePane getRoot() {
		return painelImpressos;
	}
}
