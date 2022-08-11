package org.casadeguara.controllers;

import org.casadeguara.entidades.Usuario;
import org.casadeguara.models.UsuarioModel;
import org.casadeguara.views.CadastroUsuarioView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CadastroUsuarioController implements GenericController {

	private UsuarioModel model;
	private CadastroUsuarioView view;

	public CadastroUsuarioController(CadastroUsuarioView view) {
		this.view = view;
		this.model = new UsuarioModel();

		configureView();
	}

	@Override
	public void configureView() {
		view.acaoBotaoAlterar(event -> alterarUsuario());
		view.acaoBotaoCadastrar(event -> cadastrarUsuario());
		view.acaoBotaoLimpar(event -> limparCampos());
		view.acaoBtnResetar(event -> resetarSenha());
		view.acaoPesquisarUsuario().addListener((observable, oldValue, newValue) -> pesquisarUsuario(newValue));

		view.setAutoComplete(model);
	}

	public void alterarUsuario() {
		Usuario usuarioAtual = view.getUsuarioSelecionado();
		
		if (usuarioAtual != null) {
			usuarioAtual.setNome(view.getNomeUsuario());
			usuarioAtual.setTipo(view.getTipoUsuario());
			usuarioAtual.setListaAcessos(view.getListaAcessos());
			usuarioAtual.setStatus(view.usuarioAtivo());

			Task<Void> atualizarUsuario = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Iniciando a atualização do usuário " + usuarioAtual.getNome());
					if (model.atualizar(usuarioAtual) == 0) {
						updateMessage("Usuário atualizado com sucesso.");
					} else {
						updateMessage("Não foi possível atualizar o usuário");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(atualizarUsuario);
			new Thread(atualizarUsuario).start();
		} else {
			view.mensagemInformativa("Selecione um usuário para alteração.");
		}
	}

	public void cadastrarUsuario() {
		String nome = view.getNomeUsuario();
		String login = view.getLogin();
		String tipo = view.getTipoUsuario();

		if (!(nome.isEmpty() || login.isEmpty())) {
			ObservableList<Integer> listaAcessos = view.getListaAcessos();

			Usuario novoUsuario = new Usuario(0, nome, login, "123", tipo, listaAcessos, true);

			Task<Void> cadastrarUsuario = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateMessage("Iniciando o cadastro do usuário " + nome);
					if (model.cadastrar(novoUsuario) == 0) {
						updateMessage("Usuário cadastrado com sucesso.");
					} else {
						updateMessage("Não foi possível cadastrar o usuário");
						cancel();
					}
					return null;
				}
			};

			view.mensagemProgresso(cadastrarUsuario);
			new Thread(cadastrarUsuario).start();
		} else {
			view.mensagemInformativa("Preencha todos os campos.");
		}
	}

	public void resetarSenha() {
		Usuario usuario = view.getUsuarioSelecionado();
		if (model.trocarSenha("123", usuario.getId()) == 0) {
			view.mensagemSucesso("Senha resetada para 123");
		}
	}

	public void limparCampos() {
		view.limparCampos();
	}

	public int pesquisarUsuario(Usuario usuario) {
		if (usuario != null) {
			view.estaCadastrando(false);
			view.setUsuarioSelecionado(usuario);
			view.setListaAcessos(model.consultarAcessosUsuario(usuario.getId()));
			return 0;
		}
		return 1;
	}
}