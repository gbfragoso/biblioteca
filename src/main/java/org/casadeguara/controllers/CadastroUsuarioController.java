package org.casadeguara.controllers;

import org.casadeguara.entidades.Usuario;
import org.casadeguara.models.UsuarioModel;
import org.casadeguara.views.CadastroUsuarioView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CadastroUsuarioController implements GenericController{
    
    private CadastroUsuarioView view;
    private UsuarioModel model;
    private Usuario usuarioAtual;
    
    public CadastroUsuarioController(CadastroUsuarioView view, UsuarioModel model) {
        this.view = view;
        this.model = model;
        
        configureView();
    }
    
    @Override
    public void configureView() {
        view.acaoBotaoAlterar(event -> alterarUsuario());
        view.acaoBotaoCadastrar(event -> cadastrarUsuario());
        view.acaoBotaoLimpar(event -> limparCampos());
        view.acaoBtnResetar(event -> resetarSenha());
        view.acaoPesquisarUsuario((observable, oldValue, newValue) -> pesquisarUsuario(newValue));
        
        view.setListaSugestoes(model.getListaUsuarios());
    }

    public void alterarUsuario() {
        if(getUsuarioAtual() != null) {
            String nomeUsuario = view.getNomeUsuario();
            usuarioAtual.setNome(nomeUsuario);
            usuarioAtual.setTipo(view.getTipoUsuario());
            usuarioAtual.setListaAcessos(view.getListaAcessos());
            usuarioAtual.setStatus(view.usuarioAtivo());
            
            Task<Void> atualizarUsuario = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Iniciando a atualização do usuário " + nomeUsuario);
                    if(model.atualizar(usuarioAtual) == 0) {
                        updateMessage("Atualizando lista de usuários.");
                        model.atualizarListaUsuarios();
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
                    if(model.cadastrar(novoUsuario) == 0) {
                        updateMessage("Atualizando a lista de usuários");
                        model.atualizarListaUsuarios();
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
        if(model.trocarSenha("123", getUsuarioAtual().getId()) == 0) {
            view.mensagemSucesso("Senha resetada para 123");
        }
    }
    
    public void limparCampos() {
        setUsuarioAtual(null);
        view.limparCampos();
    }
    
    public int pesquisarUsuario(String usuario) {
        if(usuario != null && !usuario.isEmpty()) {
            setUsuarioAtual(model.consultar(usuario));

            if(getUsuarioAtual() != null) {
                usuarioAtual.setListaAcessos(model.consultarAcessosUsuario(usuarioAtual.getId()));
                int[] acessos = usuarioAtual.getListaAcessos().stream().mapToInt(i -> i).toArray();
                
                view.estaCadastrando(false);
                view.setNomeUsuario(usuarioAtual.getNome());
                view.setListaAcessos(acessos);
                
                return 0;
            }
        }
        return 1;
    }

    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public void setUsuarioAtual(Usuario usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }
}
