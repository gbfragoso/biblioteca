package org.casadeguara.controllers;

import java.util.Optional;
import org.casadeguara.dialogos.AutoCompleteDialog;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.impressora.Impressora;
import org.casadeguara.models.LeitorModel;
import org.casadeguara.views.ImpressosView;
import javafx.scene.control.TextInputDialog;

public class ImpressosController implements GenericController {

    private ImpressosView view;
    private Impressora imprimir;
    
    public ImpressosController(ImpressosView view) {
        this.view = view;
        
        imprimir = new Impressora();
        
        configureView();
    }

    @Override
    public void configureView() {
        view.acaoBtnEmprestimo(event -> emprestimos());
        view.acaoBtnEmprestimoPorData(event -> emprestimosPorData());
        view.acaoBtnEmprestimoPorLivro(event -> emprestimosPorLivro());
        view.acaoBtnEmprestimosAtrasados(event -> emprestimosEmAtraso());
        view.acaoBtnHistorico(event -> historico());
        view.acaoBtnInventario(event -> inventarioGeral());
        view.acaoBtnLeitor(event -> relacaoLeitores());
    }

    public void emprestimos() {
        imprimir.relacaoEmprestimos("true");
    }

    public void emprestimosEmAtraso() {
        imprimir.relacaoEmprestimos("data_devolucao < current_date");
    }

    public void emprestimosPorLivro() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setHeaderText("Digite o nome do livro");
        
        Optional<String> resultado = dialogo.showAndWait();
        if(resultado.isPresent() && !resultado.get().isEmpty()) {
            String condicao = "li.titulo = '" + resultado.get().toUpperCase() + "'";
            imprimir.relacaoEmprestimos(condicao);
        }
    }

    public void emprestimosPorData() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setHeaderText("Digite o mês e o ano, ex: 03/2018");
        
        Optional<String> resultado = dialogo.showAndWait();
        if(resultado.isPresent() && resultado.get().matches("\\d{2}/\\d{4}")) {
            String condicao = "to_char(data_devolucao, 'MM/YYYY') = '" + resultado.get() + "'";
            imprimir.relacaoEmprestimos(condicao);
        }
    }

    public void historico() {
        AutoCompleteDialog<Leitor> dialogo = new AutoCompleteDialog<>(
        	"Selecione um leitor", 
            "Selecione um leitor para gerar o histórico\nde empréstimos.",
            new LeitorModel());
        
        dialogo.showAndWait().ifPresent(leitor -> {
            if (leitor != null) {
                imprimir.historicoLeitor(leitor.getNome());
            }
        });
        
    }

    public void inventarioGeral() {
        imprimir.relacaoExemplares();
    }

    public void relacaoLeitores() {
        imprimir.relacaoLeitores();
    }

}
