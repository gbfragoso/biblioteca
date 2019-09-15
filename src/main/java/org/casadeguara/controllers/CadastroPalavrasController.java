package org.casadeguara.controllers;

import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.models.PalavraChaveModel;
import org.casadeguara.views.CadastroPalavrasView;
import javafx.concurrent.Task;

public class CadastroPalavrasController implements GenericController{
    
    private CadastroPalavrasView view;
    private PalavraChaveModel model;
    private int currentID = 0;
    
    public CadastroPalavrasController(CadastroPalavrasView view, PalavraChaveModel model) {
        this.view = view;
        this.model = model;
        
        configureView();
    }

    @Override
    public void configureView() {
        if(view != null) {
            view.acaoBotaoAlterar(event -> atualizarPalavraChave());
            view.acaoBotaoCadastrar(event -> cadastrarPalavraChave());
            view.acaoBotaoLimpar(event -> limparCampos());
            view.acaoPesquisarPalavraChave((observable, oldValue, newValue) -> pesquisarPalavraChave(newValue));
            
            view.setListaSugestoes(model.getListaPalavras());
        }
    }
    
    public int atualizarPalavraChave() {
        String novoAssunto = view.getPalavraChave();

        if (getCurrentID() > 0 && !novoAssunto.isEmpty()) {
            PalavraChave palavraChave = new PalavraChave(getCurrentID() , novoAssunto);
            
            Task<Void> atualizarPalavraChave = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Atualizando a palavra-chave " + novoAssunto);
                    if(model.atualizar(palavraChave) == 0) {
                        updateMessage("Atualizando lista de palavras-chave.");
                        model.atualizarListaPalavras();
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
        
        if (!assunto.isEmpty()) {
            Task<Void> cadastrarPalavraChave = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Cadastrando a palavra-chave " + assunto);
                    if(model.cadastrar(new PalavraChave(0, assunto)) > 0) {
                        updateMessage("Atualizando a lista de palavras-chave.");
                        model.atualizarListaPalavras();
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
        setCurrentID(0);
        view.limparCampos();
    }
    
    public int pesquisarPalavraChave(String novaPalavra) {
        if(novaPalavra != null && !novaPalavra.isEmpty()) {
            setAutorAtual(model.consultarPalavraChave(novaPalavra));

            if(getCurrentID() > 0) {
                view.estaCadastrando(false);
                view.setPalavraChave(novaPalavra);

                return 0;
            } else {
                view.mensagemInformativa("Palavra-chave não encontrada");
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
