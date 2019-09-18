package org.casadeguara.controllers;

import org.casadeguara.consultas.Resultado;
import org.casadeguara.models.ConsultaModel;
import org.casadeguara.views.ConsultaView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConsultaController implements GenericController{
    
    private ConsultaView view;
    private ConsultaModel model;
    
    public ConsultaController(ConsultaView view) {
        this.view = view;
        this.model = new ConsultaModel();
        
        configureView();
    }
    
    @Override
    public void configureView() {
        view.acaoPesquisar(event -> buscaPersonalizada());
    }
    
    public void buscaPersonalizada() {
        String tipoPesquisa = view.getTipoPesquisa();
        String termoPesquisado = view.getTermoPesquisado();
        verificarResultados(pesquisar(tipoPesquisa, termoPesquisado));
    }
    
    public void buscaRapida(String termoPesquisado) {
        view.setTextoPesquisado(termoPesquisado);
        verificarResultados(pesquisar("Título", termoPesquisado));
    }

    private void verificarResultados(ObservableList<Resultado> resultados) {
        if (resultados.isEmpty()) {
            view.mensagemInformativa("Nenhum livro encontrado para esta pesquisa.");
        } else {
            view.setResultadosConsulta(resultados);
        }
    }
    
    private ObservableList<Resultado> pesquisar(String tipoPesquisa, String termoPesquisado) {
        String pesquisaGenerica = "%" + termoPesquisado + "%";
        switch (tipoPesquisa) {
            case "Autor": return model.porAutor(pesquisaGenerica);
            case "Editora": return model.porEditora(pesquisaGenerica);
            case "Palavra-chave": return model.porPalavraChave(pesquisaGenerica);
            case "Título": return model.porTitulo(pesquisaGenerica);
            case "Tombo": return model.porTombo(termoPesquisado);
            default: return FXCollections.emptyObservableList();
        }
    }
}
