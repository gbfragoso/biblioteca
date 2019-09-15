package org.casadeguara.controllers;

import org.casadeguara.entidades.Autor;
import org.casadeguara.models.AutorModel;
import org.casadeguara.views.CadastroAutorView;
import javafx.concurrent.Task;

public class CadastroAutorController implements GenericController{
    
    private AutorModel model;
    private CadastroAutorView view;
    private int currentID = 0;
    
    public CadastroAutorController(CadastroAutorView view, AutorModel model) {
        this.view = view;
        this.model = model;
        
        configureView();
    }
    
    @Override
    public void configureView() {
        if(view != null) {
            view.acaoBotaoAlterar(event -> atualizarAutor());
            view.acaoBotaoCadastrar(event -> cadastrarAutor());
            view.acaoBotaoLimpar(event -> limparCampos());
            view.acaoPesquisarAutor((observable, oldValue, newValue) -> pesquisarAutor(newValue));
            
            view.setListaSugestoes(model.getListaAutores());
        }
    }
    
    public int atualizarAutor() {
        String novoNome = view.getNomeAutor();

        if (getCurrentID() > 0 && !novoNome.isEmpty() ) {
            Autor novoAutor = new Autor(getCurrentID(), novoNome);
            
            Task<Void> atualizarAutor = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Atualizando o autor " + novoNome);
                    if(model.atualizar(novoAutor) == 0) {
                        updateMessage("Atualizando lista de autores.");
                        model.atualizarListaAutores();
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
            view.mensagemInformativa("Você esqueceu de selecionar um autor ou\nnão preencheu os campos necessários.");
        }
        return 1;
    }
    
    public int cadastrarAutor() {
        String nome = view.getNomeAutor();

        if (!nome.isEmpty()) {
            Task<Void> cadastrarAutor = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Cadastrando o autor " + nome);
                    if(model.cadastrar(new Autor(0, nome)) == 0) {
                        updateMessage("Atualizando lista de autores.");
                        model.atualizarListaAutores();
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
        setCurrentID(0);
        view.limparCampos();
    }
    
    public int pesquisarAutor(String nomeAutor) {
        if(nomeAutor != null && !nomeAutor.isEmpty()) {
            setCurrentID(model.consultarAutor(nomeAutor));

            if(getCurrentID() > 0) {
                view.estaCadastrando(false);
                view.setNomeAutor(nomeAutor);
                return 0;
            } else {
                view.mensagemInformativa("Autor não encontrado.");
            }
        }
        return 1;
    }

    public int getCurrentID() {
        return currentID;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }
}
