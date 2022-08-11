package org.casadeguara.controllers;

import org.casadeguara.entidades.Autor;
import org.casadeguara.models.AutorModel;
import org.casadeguara.views.CadastroAutorView;
import javafx.concurrent.Task;

public class CadastroAutorController implements GenericController {

	private AutorModel model;
	private CadastroAutorView view;

	public CadastroAutorController(CadastroAutorView view) {
		this.view = view;
		this.model = new AutorModel();

		configureView();
	}

	@Override
	public void configureView() {
		if (view != null) {
			view.acaoBotaoAlterar(event -> atualizarAutor());
			view.acaoBotaoCadastrar(event -> cadastrarAutor());
			view.acaoBotaoLimpar(event -> limparCampos());
			view.acaoPesquisarAutor().addListener((observable, oldValue, newValue) -> pesquisarAutor(newValue));

			view.setAutoComplete(model);
		}
	}

	public int atualizarAutor() {
		Autor autor = view.getAutorSelecionado();
		String novoNome = view.getNomeAutor();

		if (autor != null && novoNome != null && !novoNome.trim().isEmpty()) {
			autor.setNome(novoNome);

			Task<Void> atualizarAutor = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Atualizando o autor " + novoNome);
					if (model.atualizar(autor) == 0) {
						updateMessage("Autor atualizado com sucesso.");
					} else {
						updateMessage("Não foi possível atualizar o autor.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(atualizarAutor);
			new Thread(atualizarAutor).start();

			return 0;
		} else {
			view.mensagemInformativa("Campos obrigatórios sem preenchimento.");
		}
		return 1;
	}

	public int cadastrarAutor() {
		String nome = view.getNomeAutor();

		if (nome != null && !nome.trim().isEmpty()) {
			Task<Void> cadastrarAutor = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Cadastrando o autor " + nome);
					if (model.cadastrar(new Autor(0, nome)) == 0) {
						updateMessage("Autor cadastrado com sucesso.");
					} else {
						updateMessage("Autor não cadastrado. Verifique se ele já existe.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(cadastrarAutor);
			new Thread(cadastrarAutor).start();

			return 0;
		} else {
			view.mensagemInformativa("Você esqueceu de colocar o nome do autor.");
		}
		return 1;
	}

	public void limparCampos() {
		view.limparCampos();
	}

	public int pesquisarAutor(Autor autor) {
		if (autor != null) {
			view.estaCadastrando(false);
			view.setAutorSelecionado(autor);
			return 0;
		}
		return 1;
	}
}