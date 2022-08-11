package org.casadeguara.controllers;

import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.models.PalavraChaveModel;
import org.casadeguara.views.CadastroPalavrasView;
import javafx.concurrent.Task;

public class CadastroPalavrasController implements GenericController {

	private PalavraChaveModel model;
	private CadastroPalavrasView view;

	public CadastroPalavrasController(CadastroPalavrasView view) {
		this.view = view;
		this.model = new PalavraChaveModel();

		configureView();
	}

	@Override
	public void configureView() {
		if (view != null) {
			view.acaoBotaoAlterar(event -> atualizarPalavraChave());
			view.acaoBotaoCadastrar(event -> cadastrarPalavraChave());
			view.acaoBotaoLimpar(event -> limparCampos());
			view.acaoPesquisarPalavraChave()
					.addListener((observable, oldValue, newValue) -> pesquisarPalavraChave(newValue));

			view.setAutoComplete(model);
		}
	}

	public int atualizarPalavraChave() {
		PalavraChave palavraChave = view.getPalavraChaveSelecionada();
		String novoAssunto = view.getPalavraChave();

		if (palavraChave != null && novoAssunto != null && !novoAssunto.trim().isEmpty()) {
			palavraChave.setAssunto(novoAssunto);

			Task<Void> atualizarPalavraChave = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Atualizando a palavra-chave " + novoAssunto);
					if (model.atualizar(palavraChave) == 0) {
						updateMessage("Palavra-chave alterada com sucesso.");
					} else {
						updateMessage("Não foi possível atualizar a palavra-chave.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(atualizarPalavraChave);
			new Thread(atualizarPalavraChave).start();
			return 0;
		} else {
			view.mensagemInformativa(
					"Você esqueceu de selecionar um assunto\nou não preencheu os campos obrigatórios.");
		}
		return 1;
	}

	public int cadastrarPalavraChave() {
		String assunto = view.getPalavraChave();

		if (assunto != null && !assunto.trim().isEmpty()) {
			Task<Void> cadastrarPalavraChave = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Cadastrando a palavra-chave " + assunto);
					if (model.cadastrar(new PalavraChave(0, assunto)) > 0) {
						updateMessage("Palavra-chave cadastrada com sucesso.");
					} else {
						updateMessage("Palavra-chave não cadastrada. Verifique se ela já existe.");
						cancel();
					}
					return null;
				}

			};

			view.mensagemProgresso(cadastrarPalavraChave);
			new Thread(cadastrarPalavraChave).start();
			return 0;
		} else {
			view.mensagemInformativa("Você esqueceu de colocar o nome da palavra-chave.");
		}
		return 1;
	}

	public void limparCampos() {
		view.limparCampos();
	}

	public int pesquisarPalavraChave(PalavraChave palavraChave) {
		if (palavraChave != null) {
			view.estaCadastrando(false);
			view.setPalavraChaveSelecionada(palavraChave);

			return 0;
		}
		return 1;
	}
}