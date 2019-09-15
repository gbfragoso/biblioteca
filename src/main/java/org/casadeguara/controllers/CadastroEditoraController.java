package org.casadeguara.controllers;

import org.casadeguara.entidades.Editora;
import org.casadeguara.models.EditoraModel;
import org.casadeguara.views.CadastroEditoraView;
import javafx.concurrent.Task;

public class CadastroEditoraController implements GenericController {
    
    private EditoraModel model;
    private CadastroEditoraView view;
    private int currentID = 0;
    
    public CadastroEditoraController(CadastroEditoraView view, EditoraModel model) {
        this.view = view;
        this.model = model;
        
        configureView();
    }
    
    @Override
    public void configureView() {
        if(view != null) {
            view.acaoBotaoAlterar(event -> atualizarEditora());
            view.acaoBotaoCadastrar(event -> cadastrarEditora());
            view.acaoBotaoLimpar(event -> limparCampos());
            view.acaoPesquisarEditora((observable, oldValue, newValue) -> pesquisarEditora(newValue));
            
            view.setListaSugestoes(model.getListaEditoras());
        }
    }
    
    public int atualizarEditora() {
        String novoNome = view.getNomeEditora();

        if (getCurrentID() > 0 && !novoNome.isEmpty()) {
            Editora novaEditora = new Editora(getCurrentID(), novoNome);
            
            Task<Void> atualizarEditora = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Atualizando a editora " + novoNome);
                    if(model.atualizar(novaEditora) == 0) {
                        updateMessage("Atualizando lista de editoras.");
                        model.atualizarListaEditoras();
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
            view.mensagemInformativa("Você esqueceu de selecionar uma editora ou não preencheu\nos campos necessários.");
        }
        return 1;
    }
    
    public int cadastrarEditora() {
        String nome = view.getNomeEditora();

        if (!nome.isEmpty()) {
            Task<Void> cadastrarEditora = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Cadastrando a editora " + nome);
                    if(model.cadastrar(new Editora(0, nome)) == 0) {
                        updateMessage("Atualizando a lista de editoras.");
                        model.atualizarListaEditoras();
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
            view.mensagemInformativa("Campos obrigatórios sem preenchimento.");
        }
        return 1;
    }
    
    public void limparCampos() {
        setCurrentID(0);
        view.limparCampos();
    }
    
    public int pesquisarEditora(String nomeEditora) {
        if(nomeEditora != null && !nomeEditora.isEmpty()) {
            setCurrentID(model.consultarEditora(nomeEditora));
            
            if(getCurrentID() > 0) {
                view.estaCadastrando(false);
                view.setNomeEditora(nomeEditora);
                return 0;
            } else {
                view.mensagemInformativa("Editora não encontrada");
            }
        }
        return 1;
    }
    
    public int getCurrentID() {
        return currentID;
    }
    
    public void setCurrentID(int id) {
        this.currentID = id;
    }
}
