package org.casadeguara.controllers;

import org.casadeguara.entidades.Editora;
import org.casadeguara.models.EditoraModel;
import org.casadeguara.views.CadastroEditoraView;
import javafx.concurrent.Task;

public class CadastroEditoraController implements GenericController {
    
	private Editora editoraAtual;
    private EditoraModel model;
    private CadastroEditoraView view;
    
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
            view.acaoPesquisarEditora(event -> pesquisarEditora());
            
            view.setAutoComplete(model);
        }
    }
    
    public int atualizarEditora() {
        String novoNome = view.getNomeEditora();

        if (getEditoraAtual() != null && !novoNome.isEmpty()) {
            Editora novaEditora = getEditoraAtual();
            novaEditora.setNome(novoNome);
            
            Task<Void> atualizarEditora = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Atualizando a editora " + novoNome);
                    if(model.atualizar(novaEditora) == 0) {
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
        setEditoraAtual(null);
        view.limparCampos();
    }
    
    public int pesquisarEditora() {
    	Editora editora = view.getTermoPesquisado();
        if(editora != null) {
            setEditoraAtual(editora);
            view.estaCadastrando(false);
            view.setNomeEditora(editora.getNome());
            
            return 0;
        } else {
            view.mensagemInformativa("Editora não encontrada");
        }
        return 1;
    }
    
    public Editora getEditoraAtual() {
        return editoraAtual;
    }
    
    public void setEditoraAtual(Editora editora) {
        this.editoraAtual = editora;
    }
}
