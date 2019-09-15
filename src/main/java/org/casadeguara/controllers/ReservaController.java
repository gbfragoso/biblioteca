package org.casadeguara.controllers;

import org.casadeguara.entidades.Leitor;
import org.casadeguara.entidades.Livro;
import org.casadeguara.models.LeitorModel;
import org.casadeguara.models.LivroModel;
import org.casadeguara.models.ReservaModel;
import org.casadeguara.movimentacao.Reserva;
import org.casadeguara.views.ReservaView;
import javafx.collections.ObservableList;

public class ReservaController implements GenericController {
    
    private ReservaView view;
    private ReservaModel model;
    
    public ReservaController(ReservaView view, ReservaModel model) {
        this.view = view;
        this.model = model;
        
        configureView();
    }

    @Override
    public void configureView() {
        view.acaoBotaoCancelar(event -> cancelarReserva());
        view.acaoBotaoLimpar(event -> limparCampos());
        view.acaoBotaoReservar(event -> adicionarReserva());
        view.acaoPesquisarReserva(event -> pesquisarReservas());
        
        view.setAutoCompleteLeitor(new LeitorModel());
        view.setAutoCompleteLivro(new LivroModel());
        
        model.expirarReservas();
    }
    
    public int adicionarReserva() {
        Leitor leitor = view.getLeitor();
        Livro livro = view.getLivro();
        
        if(leitor == null) {
        	view.mensagemInformativa("Leitor não encontrado");
        	return 1;
        }
        
        if(livro == null) {
        	view.mensagemInformativa("Livro não encontrado");
        	return 1;
        }
        
        int idleitor = leitor.getId();
        int idlivro = livro.getId();

        if (!model.possuiReserva(idleitor, idlivro)) {
            model.reservar(idleitor, idlivro);
            view.limparCampos();
            view.mensagemSucesso("Livro reservado com sucesso");
            
            return 0;
        } else {
            view.mensagemInformativa("Esse leitor já efetuou esta reserva.");
        }
        
        return 1;
    }
    
    public void cancelarReserva() {
        Reserva reservaAtual = view.getReservaSelecionada();

        if (reservaAtual != null) {
            int idleitor = reservaAtual.getIdleitor();
            int idlivro = reservaAtual.getIdlivro();

            if (view.mensagemConfirmacao("Deseja realmente cancelar esta reserva?")) {
                model.cancelarReserva(idleitor, idlivro);
                view.removerReserva(reservaAtual);
            }
        } else {
            view.mensagemInformativa("Selecione um item na tabela.");
        }
    }
    
    public void limparCampos() {
        view.limparCampos();
    }
    
    public void pesquisarReservas() {
        ObservableList<Reserva> reservas = model.consultarReservas("%" + view.getReservaPesquisada() + "%");
        view.setListaReservas(reservas);
    }
}
