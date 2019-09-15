package org.casadeguara.controllers;

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
        
        view.setListaLeitores(model.getListaLeitores());
        view.setListaLivros(model.getListaLivros());
        
        model.expirarReservas();
    }
    
    public void adicionarReserva() {
        String leitor = view.getLeitor();
        String livro = view.getLivro();

        if (leitor != null && livro != null) {
            int idleitor = model.consultarLeitor(leitor);
            int idlivro = model.consultarLivro(livro);

            if (!model.possuiReserva(idleitor, idlivro)) {
                model.reservar(idleitor, idlivro);
                view.limparCampos();
                
                view.mensagemSucesso("Livro reservado com sucesso");
            } else {
                view.mensagemInformativa("Esse leitor já efetuou esta reserva.");
            }
        } else {
            view.mensagemInformativa("Você esqueceu de selecionar um leitor ou item.");
        }
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
