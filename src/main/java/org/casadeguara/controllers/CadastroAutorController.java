package org.casadeguara.controllers;

import org.casadeguara.entidades.Autor;
import org.casadeguara.models.AutorModel;
import org.casadeguara.views.CadastroAutorView;
import javafx.concurrent.Task;

public class CadastroAutorController implements GenericController{
    
	private Autor autorAtual;
    private AutorModel model;
    private CadastroAutorView view;
    
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
            view.acaoPesquisarAutor(event -> pesquisarAutor());
            
            view.setAutoComplete(model);
        }
    }
    
    public int atualizarAutor() {
        String novoNome = view.getNomeAutor();

        if (getAutorAtual() != null && !novoNome.isEmpty() ) {
            Autor novoAutor = getAutorAtual();
            novoAutor.setNome(novoNome);
            
            Task<Void> atualizarAutor = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Atualizando o autor " + novoNome);
                    if(model.atualizar(novoAutor) == 0) {
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
        setAutorAtual(null);
        view.limparCampos();
    }
    
    public int pesquisarAutor() {
        Autor autor = view.getTermoPesquisado();
        
    	if(autor != null) {
            setAutorAtual(autor);
            view.estaCadastrando(false);
            view.setNomeAutor(autor.getNome());
            return 0;
        } else {
            view.mensagemInformativa("Autor não encontrado.");
        }
        return 1;
    }

    public Autor getAutorAtual() {
        return autorAtual;
    }

    public void setAutorAtual(Autor autorAtual) {
        this.autorAtual = autorAtual;
    }
}
