package org.casadeguara.controllers;

import java.time.LocalDate;
import org.casadeguara.models.GraficoModel;
import org.casadeguara.views.GraficoView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GraficoController implements GenericController {
    
    private GraficoView view;
    private GraficoModel model;
    
    public GraficoController(GraficoView view) {
        this.view = view;
        this.model = new GraficoModel();
        
        configureView();
    }

    @Override
    public void configureView() {
        view.atualizarGraficoGeral(model.movimentacaoGeral());
        view.listenerGraficoAnual((listener, oldValue, newValue) -> atualizarGraficoAnual(newValue));
        view.listenerGraficoMensal((listener, oldValue, newValue) -> atualizarGraficoMensal(newValue));
        view.setAnosDisponiveis(anosEntre(LocalDate.now().getYear(), 2012));
    }
    
    public void atualizarGraficoAnual(Integer ano) {
        view.atualizarGraficoAnual(model.movimentacaoPorMes(ano));
    }
    
    public void atualizarGraficoMensal(String mes) {
        view.atualizarGraficoMensal(model.movimentacaoEm(mes , 2018));
    }
    
    public ObservableList<Integer> anosEntre(int anoAtual, int anoDestino) {
        ObservableList<Integer> anos = FXCollections.observableArrayList();
        for (int i = anoAtual; i > anoDestino; i--) {
            anos.add(i);
        }
        return anos;
    }

}
