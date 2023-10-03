package org.casadeguara.controllers;

import java.util.Optional;

import org.casadeguara.dialogos.AutoCompleteDialog;
import org.casadeguara.dialogos.DialogoAlterarChave;
import org.casadeguara.dialogos.DialogoAlterarEmprestimo;
import org.casadeguara.dialogos.DialogoEnviarEmail;
import org.casadeguara.dialogos.DialogoMudancaRegras;
import org.casadeguara.dialogos.DialogoRecuperarEmprestimo;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.etiquetas.GeradorEtiqueta;
import org.casadeguara.impressora.Impressora;
import org.casadeguara.models.AcervoModel;
import org.casadeguara.models.AdministracaoModel;
import org.casadeguara.models.LeitorModel;
import org.casadeguara.models.RegraModel;
import org.casadeguara.negocio.Cobranca;
import org.casadeguara.views.AdministracaoView;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TextInputDialog;

public class AdministracaoController implements GenericController {

	private AdministracaoView view;
	private AdministracaoModel model;
	private Impressora imprimir;
	private RegraModel regraModel;

	public AdministracaoController(AdministracaoView view) {
		this.view = view;
		this.model = new AdministracaoModel();
		
		imprimir = new Impressora();
		regraModel = new RegraModel();

		configureView();
	}

	@Override
	public void configureView() {
		if (view != null) {
			view.acaoBotaoAlterarChaveMestra(event -> alterarChaveMestra());
			view.acaoBotaoAlterarEmprestimo(event -> alterarExemplarEmprestimo());
			view.acaoBotaoConfiguracao(event -> alterarRegrasNegocio());
			view.acaoBotaoCobrancas(event -> realizarCobrancas());
			view.acaoBotaoEtiqueta(event -> geradorEtiquetas());
			view.acaoBotaoRecuperarEmprestimo(event -> recuperarEmprestimoDevolvido());
			view.acaoBtnEmprestimo(event -> emprestimos());
			view.acaoBtnEmprestimoPorData(event -> emprestimosPorData());
			view.acaoBtnEmprestimoPorLivro(event -> emprestimosPorLivro());
			view.acaoBtnEmprestimosAtrasados(event -> emprestimosEmAtraso());
			view.acaoBtnHistorico(event -> historico());
			view.acaoBtnInventario(event -> inventarioGeral());
			view.acaoBtnLeitor(event -> relacaoLeitores());
		}
	}

	public int alterarChaveMestra() {
		DialogoAlterarChave dialogoAlterarChave = new DialogoAlterarChave();
		dialogoAlterarChave.showAndWait().ifPresent(novaChave -> {
			if (novaChave != null && !novaChave.isEmpty()) {
				model.alterarChaveMestra(novaChave);
			}
		});
		return 0;
	}

	public int alterarExemplarEmprestimo() {
		DialogoAlterarEmprestimo dialogoAlterarEmprestimo = new DialogoAlterarEmprestimo(new AcervoModel());
		dialogoAlterarEmprestimo.showAndWait().ifPresent(dadosEmprestimo -> {
			if (!dadosEmprestimo.isEmpty()) {
				Task<Void> trocarExemplar = new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						updateMessage("Trocando o exemplar.");
						model.trocarExemplar(dadosEmprestimo.get(0), dadosEmprestimo.get(1));
						return null;
					}
				};

				view.mensagemProgresso(trocarExemplar);
				new Thread(trocarExemplar).start();
			}
		});
		return 0;
	}

	public int alterarRegrasNegocio() {
		DialogoMudancaRegras dialogoMudancaRegras = new DialogoMudancaRegras(regraModel.consultarRegrasNegocio());
		dialogoMudancaRegras.showAndWait().ifPresent(listaRegras -> {
			if (!listaRegras.isEmpty()) {
				regraModel.atualizarRegrasNegocio(listaRegras);
			}
		});
		return 0;
	}

	public int geradorEtiquetas() {
		GeradorEtiqueta gerador = new GeradorEtiqueta();
		gerador.showAndWait().ifPresent(listaEtiquetas -> {
			if (!listaEtiquetas.isEmpty()) {
				new Impressora().etiquetas(listaEtiquetas);
			}
		});
		return 0;
	}

	public int recuperarEmprestimoDevolvido() {
		DialogoRecuperarEmprestimo dialogoRecuperarEmprestimo = new DialogoRecuperarEmprestimo();
		dialogoRecuperarEmprestimo.showAndWait().ifPresent(idmovimentacao -> {
			if (idmovimentacao > 0) {
				Task<Void> atualizarEmprestimos = new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						int duracaoEmprestimo = regraModel.consultarRegrasNegocio().getDuracaoEmprestimo();
						updateMessage("Recuperando empréstimo.");
						model.recuperarEmprestimo(idmovimentacao, duracaoEmprestimo);
						return null;
					}
				};

				view.mensagemProgresso(atualizarEmprestimos);
				new Thread(atualizarEmprestimos).start();
			}
		});
		return 0;
	}

	public int realizarCobrancas() {
		ObservableList<Cobranca> listaEmprestimosAtrasados = model.getListaEmprestimosAtrasados();
		DialogoEnviarEmail selecionar = new DialogoEnviarEmail(model, listaEmprestimosAtrasados);
		selecionar.showAndWait();

		return 0;
	}
	
	public void emprestimos() {
		imprimir.relacaoEmprestimos("true");
	}

	public void emprestimosEmAtraso() {
		imprimir.relacaoEmprestimos("data_devolucao < current_date");
	}

	public void emprestimosPorLivro() {
		TextInputDialog dialogo = new TextInputDialog();
		dialogo.setHeaderText("Digite o nome do livro");

		Optional<String> resultado = dialogo.showAndWait();
		if (resultado.isPresent() && !resultado.get().isEmpty()) {
			String condicao = "li.titulo = '" + resultado.get().toUpperCase() + "'";
			imprimir.relacaoEmprestimos(condicao);
		}
	}

	public void emprestimosPorData() {
		TextInputDialog dialogo = new TextInputDialog();
		dialogo.setHeaderText("Digite o mês e o ano, ex: 03/2018");

		Optional<String> resultado = dialogo.showAndWait();
		if (resultado.isPresent() && resultado.get().matches("\\d{2}/\\d{4}")) {
			String condicao = "to_char(data_devolucao, 'MM/YYYY') = '" + resultado.get() + "'";
			imprimir.relacaoEmprestimos(condicao);
		}
	}

	public void historico() {
		AutoCompleteDialog<Leitor> dialogo = new AutoCompleteDialog<>("Selecione um leitor",
				"Selecione um leitor para gerar o histórico\nde empréstimos.", new LeitorModel());

		dialogo.showAndWait().ifPresent(leitor -> {
			if (leitor != null) {
				imprimir.historicoLeitor(leitor.getNome());
			}
		});

	}

	public void inventarioGeral() {
		imprimir.relacaoExemplares();
	}

	public void relacaoLeitores() {
		imprimir.relacaoLeitores();
	}
}
