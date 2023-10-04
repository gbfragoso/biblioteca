package org.casadeguara.controllers;

import org.casadeguara.entidades.Serie;
import org.casadeguara.models.SerieModel;
import org.casadeguara.views.CadastroSerieView;
import javafx.concurrent.Task;

public class CadastroSerieController implements GenericController {

	private SerieModel model;
	private CadastroSerieView view;

	public CadastroSerieController(CadastroSerieView view) {
		this.view = view;
		this.model = new SerieModel();

		configureView();
	}

	@Override
	public void configureView() {
		if (view != null) {
			view.acaoBotaoAlterar(event -> atualizarSerie());
			view.acaoBotaoCadastrar(event -> cadastrarSerie());
			view.acaoBotaoLimpar(event -> limparCampos());
			view.acaoPesquisarSerie().addListener((observable, oldValue, newValue) -> pesquisarSerie(newValue));

			view.setAutoComplete(model);
		}
	}

	public int atualizarSerie() {
		Serie serie = view.getSerieSelecionada();
		String novoNome = view.getNomeSerie();

		if (serie != null && novoNome != null && !novoNome.trim().isEmpty()) {
			serie.setNome(novoNome);

			Task<Void> atualizarSerie = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Atualizando a serie " + novoNome);
					if (model.atualizar(serie) == 0) {
						updateMessage("Série atualizada com sucesso.");
					} else {
						updateMessage("Não foi possível atualizar a série.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(atualizarSerie);
			new Thread(atualizarSerie).start();

			return 0;
		} else {
			view.mensagemInformativa("Campos obrigatórios sem preenchimento.");
		}
		return 1;
	}

	public int cadastrarSerie() {
		String nome = view.getNomeSerie();

		if (nome != null && !nome.trim().isEmpty()) {
			Task<Void> cadastrarSerie = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Cadastrando a serie " + nome);
					if (model.cadastrar(new Serie(0, nome)) == 0) {
						updateMessage("Série cadastrada com sucesso.");
					} else {
						updateMessage("Série não cadastrada. Verifique se ela já existe.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(cadastrarSerie);
			new Thread(cadastrarSerie).start();
			return 0;
		} else {
			view.mensagemInformativa("Você esqueceu de colocar o nome da série de livros");
		}
		return 1;
	}

	public void limparCampos() {
		view.limparCampos();
	}

	public int pesquisarSerie(Serie serie) {
		if (serie != null) {
			view.estaCadastrando(false);
			view.setSerieSelecionada(serie);

			return 0;
		}
		return 1;
	}
}