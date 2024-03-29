package org.casadeguara.controllers;

import org.casadeguara.builder.LeitorBuilder;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.impressora.Impressora;
import org.casadeguara.models.LeitorModel;
import org.casadeguara.views.CadastroLeitorView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CadastroLeitorController implements GenericController {

	private LeitorModel model;
	private CadastroLeitorView view;

	public CadastroLeitorController(CadastroLeitorView view) {
		this.view = view;
		this.model = new LeitorModel();

		configureView();
	}

	@Override
	public void configureView() {
		view.acaoBotaoAlterar(event -> atualizarLeitor());
		view.acaoBotaoCadastrar(event -> cadastrarLeitor());
		view.acaoBotaoImprimirFicha(event -> imprimirFichaCadastro());
		view.acaoBotaoLimpar(event -> limparCampos());
		view.acaoPesquisarLeitor().addListener((observable, oldValue, newValue) -> pesquisarLeitor(newValue));

		view.setAutoComplete(model);
	}

	public int atualizarLeitor() {
		Leitor leitor = view.getLeitorSelecionado();
		String nome = view.getNomeLeitor();

		if (leitor != null && nome != null && !nome.trim().isEmpty()) {
			Leitor novoLeitor = construirLeitor(leitor.getId(), nome);

			Task<Void> atualizarLeitor = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Atualizando o leitor " + nome);
					if (model.atualizar(novoLeitor) == 0) {
						updateMessage("Leitor atualizado com sucesso.");
					} else {
						updateMessage("Não foi possível atualizar o leitor.");
						cancel();
					}
					return null;
				}
			};
			view.mensagemProgresso(atualizarLeitor);
			new Thread(atualizarLeitor).start();

			return 0;
		} else {
			view.mensagemInformativa("Campos obrigatórios sem preenchimento.");
		}
		return 1;
	}

	public int cadastrarLeitor() {
		String nome = view.getNomeLeitor();

		if (nome != null && !nome.trim().isEmpty()) {
			Task<Void> cadastrarLeitor = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Cadastrando o leitor " + nome);
					if (model.cadastrar(construirLeitor(0, nome)) == 0) {
						updateMessage("Leitor cadastrado com sucesso.");
					} else {
						updateMessage("Leitor não cadastrado. Verifique se ele já existe.");
						cancel();
					}
					return null;
				}

			};

			view.mensagemProgresso(cadastrarLeitor);
			new Thread(cadastrarLeitor).start();
			return 0;
		} else {
			view.mensagemInformativa("Campos obrigatórios vazios.");
		}
		return 1;
	}

	public void imprimirFichaCadastro() {
		ObservableList<Leitor> cadastroLeitor = FXCollections.observableArrayList();
		cadastroLeitor.add(view.getLeitorSelecionado());
		new Impressora().fichaCadastro(cadastroLeitor);
	}

	public void limparCampos() {
		view.limparCampos();
	}

	public int pesquisarLeitor(Leitor leitor) {
		if (leitor != null) {
			view.estaCadastrando(false);
			view.setLeitorSelecionado(leitor);
			return 0;
		}
		return 1;
	}

	private Leitor construirLeitor(int idleitor, String nome) {
		return new LeitorBuilder(nome).id(idleitor).email(view.getEmailLeitor()).telefone1(view.getTelefone1Leitor())
				.telefone2(view.getTelefone2Leitor()).logradouro(view.getLogradouro()).bairro(view.getBairro())
				.complemento(view.getComplemento()).cep(view.getCep()).cpf(view.getCpfLeitor()).rg(view.getRgLeitor())
				.sexo(view.getSexoLeitor()).isTrabalhador(view.isTrabalhador()).isAtivo(!view.isInativo())
				.isIncompleto(view.isCadastroIncompleto()).build();
	}
}