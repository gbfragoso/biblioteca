package org.casadeguara.controllers;

import org.casadeguara.entidades.Editora;
import org.casadeguara.models.EditoraModel;
import org.casadeguara.views.CadastroEditoraView;
import javafx.concurrent.Task;

public class CadastroEditoraController implements GenericController {

	private EditoraModel model;
	private CadastroEditoraView view;

	public CadastroEditoraController(CadastroEditoraView view) {
		this.view = view;
		this.model = new EditoraModel();

		configureView();
	}

	@Override
	public void configureView() {
		if (view != null) {
			view.acaoBotaoAlterar(event -> atualizarEditora());
			view.acaoBotaoCadastrar(event -> cadastrarEditora());
			view.acaoBotaoLimpar(event -> limparCampos());
			view.acaoPesquisarEditora().addListener((observable, oldValue, newValue) -> pesquisarEditora(newValue));

			view.setAutoComplete(model);
		}
	}

	public int atualizarEditora() {
		Editora editora = view.getEditoraSelecionada();
		String novoNome = view.getNomeEditora();

		if (editora != null && novoNome != null && !novoNome.trim().isEmpty()) {
			editora.setNome(novoNome);

			Task<Void> atualizarEditora = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Atualizando a editora " + novoNome);
					if (model.atualizar(editora) == 0) {
						updateMessage("Editora atualizada com sucesso.");
					} else {
						updateMessage("Não foi possível atualizar a editora.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(atualizarEditora);
			new Thread(atualizarEditora).start();

			return 0;
		} else {
			view.mensagemInformativa("Campos obrigatórios sem preenchimento.");
		}
		return 1;
	}

	public int cadastrarEditora() {
		String nome = view.getNomeEditora();

		if (nome != null && !nome.trim().isEmpty()) {
			Task<Void> cadastrarEditora = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Cadastrando a editora " + nome);
					if (model.cadastrar(new Editora(0, nome)) == 0) {
						updateMessage("Editora cadastrada com sucesso.");
					} else {
						updateMessage("Editora não cadastrada. Verifique se ela já existe.");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(cadastrarEditora);
			new Thread(cadastrarEditora).start();
			return 0;
		} else {
			view.mensagemInformativa("Você esqueceu de colocar o nome da editora");
		}
		return 1;
	}

	public void limparCampos() {
		view.limparCampos();
	}

	public int pesquisarEditora(Editora editora) {
		if (editora != null) {
			view.estaCadastrando(false);
			view.setEditoraSelecionada(editora);

			return 0;
		}
		return 1;
	}
}