package org.casadeguara.controllers;

import org.casadeguara.dialogos.DialogoAlterarChave;
import org.casadeguara.dialogos.DialogoAlterarEmprestimo;
import org.casadeguara.dialogos.DialogoEnviarEmail;
import org.casadeguara.dialogos.DialogoMudancaRegras;
import org.casadeguara.dialogos.DialogoRecuperarEmprestimo;
import org.casadeguara.dialogos.TableViewDialog;
import org.casadeguara.etiquetas.GeradorEtiqueta;
import org.casadeguara.impressora.Impressora;
import org.casadeguara.models.AcervoModel;
import org.casadeguara.models.AdministracaoModel;
import org.casadeguara.models.RegraModel;
import org.casadeguara.negocio.Cobranca;
import org.casadeguara.views.AdministracaoView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AdministracaoController implements GenericController{
    
    private AdministracaoView view;
    private AdministracaoModel model;
    private RegraModel regraModel;

    public AdministracaoController(AdministracaoView view) {
        this.view = view;
        this.model = new AdministracaoModel();
        
        regraModel = new RegraModel();
        
        configureView();
    }
    
    @Override
    public void configureView() {
        if(view != null) {
            view.acaoBotaoAlterarChaveMestra(event -> alterarChaveMestra());
            view.acaoBotaoAlterarEmprestimo(event -> alterarExemplarEmprestimo());
            view.acaoBotaoConfiguracao(event -> alterarRegrasNegocio());
            view.acaoBotaoCobrancas(event -> realizarCobrancas());
            view.acaoBotaoEtiqueta(event -> geradorEtiquetas());
            view.acaoBotaoRecuperarEmprestimo(event -> recuperarEmprestimoDevolvido());
        }
    }
    
    public int alterarChaveMestra() {
        DialogoAlterarChave dialogoAlterarChave = new DialogoAlterarChave();
        dialogoAlterarChave.showAndWait().ifPresent(novaChave -> {
            if (novaChave != null && !novaChave.isEmpty()) {
                model.alterarChaveMestra(novaChave);
            }
        });
        return 0;
    }
    
    public int alterarExemplarEmprestimo() {
        DialogoAlterarEmprestimo dialogoAlterarEmprestimo = new DialogoAlterarEmprestimo(new AcervoModel());
        dialogoAlterarEmprestimo.showAndWait().ifPresent(dadosEmprestimo -> {
            if(!dadosEmprestimo.isEmpty()) {
                Task<Void> trocarExemplar = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        updateMessage("Trocando o exemplar.");
                        model.trocarExemplar(dadosEmprestimo.get(0), dadosEmprestimo.get(1));
                        return null;
                    }
                };
                
                view.mensagemProgresso(trocarExemplar);
                new Thread(trocarExemplar).start();
            }
        });
        return 0;
    }
    
    public int alterarRegrasNegocio() {
        DialogoMudancaRegras dialogoMudancaRegras = new DialogoMudancaRegras(regraModel.consultarRegrasNegocio());
        dialogoMudancaRegras.showAndWait().ifPresent(listaRegras -> {
            if(!listaRegras.isEmpty()) {
                regraModel.atualizarRegrasNegocio(listaRegras);
            }
        });
        return 0;
    }
    
    public int geradorEtiquetas() {
        GeradorEtiqueta gerador = new GeradorEtiqueta();
        gerador.showAndWait().ifPresent(listaEtiquetas -> {
            if(!listaEtiquetas.isEmpty()) {
                new Impressora().etiquetas(listaEtiquetas);
            }
        });
        return 0;
    }
    
    public int recuperarEmprestimoDevolvido() {
        DialogoRecuperarEmprestimo dialogoRecuperarEmprestimo = new DialogoRecuperarEmprestimo();
        dialogoRecuperarEmprestimo.showAndWait().ifPresent(idmovimentacao -> {
            if (idmovimentacao > 0) {
                Task<Void> atualizarEmprestimos = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        int duracaoEmprestimo = regraModel.consultarRegrasNegocio().getDuracaoEmprestimo();
                        updateMessage("Recuperando empréstimo.");
                        model.recuperarEmprestimo(idmovimentacao, duracaoEmprestimo);
                        return null;
                    }
                };
                
                view.mensagemProgresso(atualizarEmprestimos);
                new Thread(atualizarEmprestimos).start();
            }
        });
        return 0;
    }
    
    public int realizarCobrancas() {
    	ObservableList<Cobranca> listaEmprestimosAtrasados = model.getListaEmprestimosAtrasados();
    	TableViewDialog selecionar = new TableViewDialog(listaEmprestimosAtrasados);
    	selecionar.showAndWait().ifPresent(e -> {
    		DialogoEnviarEmail dialogoEnviarEmail = new DialogoEnviarEmail(model, FXCollections.observableArrayList(e));
    		dialogoEnviarEmail.show();
    	});  	
		
    	return 0;
    }
}
