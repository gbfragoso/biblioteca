package org.casadeguara.controllers;

import java.time.LocalDate;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.impressora.Impressora;
import org.casadeguara.models.IdentificadorModel;
import org.casadeguara.models.MovimentacaoModel;
import org.casadeguara.models.RegraModel;
import org.casadeguara.movimentacao.Emprestimo;
import org.casadeguara.movimentacao.Item;
import org.casadeguara.negocio.Regra;
import org.casadeguara.views.MovimentacaoView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class MovimentacaoController implements GenericController{
    
    private IdentificadorModel consultar;
    private MovimentacaoView view;
    private MovimentacaoModel model;
    private Impressora impressora;
    
    private LocalDate dataDevolucao;
    private int idleitor;
    private String nomeLeitor;

    public MovimentacaoController(MovimentacaoView view, MovimentacaoModel model) {
        this.view = view;
        this.model = model;
        
        Regra regrasNegocio = new RegraModel().consultarRegrasNegocio();
        dataDevolucao = LocalDate.now().plusDays(regrasNegocio.getDuracaoEmprestimo());
        
        consultar = new IdentificadorModel();
        impressora = new Impressora();
        
        configureView();
    }

    @Override
    public void configureView() {
        view.acaoBtnAdicionar(event -> adicionarExemplarParaEmprestimo());
        view.acaoBtnDevolver(event -> devolverExemplares());
        view.acaoBtnEmprestar(event -> emprestarExemplares());
        view.acaoBtnRecibo(event -> gerarReciboLeitor());
        view.acaoBtnRemover(event -> removerExemplarParaEmprestimo());
        view.acaoBtnRenovar(event -> renovarEmprestimo());
        view.acaoSelecionarLeitor((listener, oldValue, newValue) -> pesquisarLeitor(newValue));
        
        view.setListaLeitores(model.getListaLeitores());
        view.setListaExemplares(model.getListaExemplares());
    }
    
    public int adicionarExemplarParaEmprestimo() {
        Item exemplar = view.getExemplarSelecionado();
        String leitor = view.getLeitorSelecionado();

        if (exemplar != null && leitor != null) {
            if (model.validarAdicaoExemplar(exemplar, leitor)) {
                return adicionarExemplar(exemplar);
            }
        } else {
            view.mensagemInformativa("Selecione um leitor ou exemplar");
        }
        return 1;
    }
    
    private int adicionarExemplar(Item exemplar) {
        exemplar.setDataDevolucao(dataDevolucao);

        if (view.adicionarExemplar(exemplar) == 1) {
            view.mensagemInformativa("Você já adicionou este exemplar.");
            return 1;
        }
        return 0;
    }
    
    public int devolverExemplares() {
        ObservableList<Emprestimo> emprestimos = view.getEmprestimosSelecionados();

        if (!emprestimos.isEmpty()) {
            if (view.mensagemConfirmacao("Deseja realmente devolver o item selecionado?")) {
                Task<Void> devolverExemplar = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updateMessage("Devolvendo os empréstimos.");
                        if (model.devolver(emprestimos) == 0) {
                            updateMessage("Atualizando lista de exemplares disponíveis.");
                            model.atualizarListaExemplares();
                            updateMessage("Devolução efetuada com sucesso.");
                        } else {
                            updateMessage("Não foi possível concluir a devolução.");
                            cancel();
                        }
                        return null;
                    }
                };
                
                new Alerta().progresso(devolverExemplar);
                new Thread(devolverExemplar).start();
                
                devolverExemplar.setOnSucceeded(event -> view.removerEmprestimosSelecionados());
                return 0;
            }
        } else {
            view.mensagemInformativa("Você esqueceu de selecionar um item para devolução.");
        }
        return 1;
    }
    
    public int emprestarExemplares() {
        ObservableList<Item> exemplaresParaEmprestimo = view.getExemplaresParaEmprestimo();
        int quantidadeItens = view.getQuantidadeItens();
        
        if (!exemplaresParaEmprestimo.isEmpty()) {
            if(model.emprestar(idleitor, nomeLeitor, exemplaresParaEmprestimo, quantidadeItens) == 0) {
                model.removerExemplares(exemplaresParaEmprestimo);
                impressora.reciboEmprestimo(nomeLeitor, exemplaresParaEmprestimo);
                
                view.setEmprestimosAtuais(model.consultarEmprestimos(idleitor));
                view.limparExemplaresParaEmprestimo();
            } else {
                view.mensagemInformativa("Empréstimo não pôde ser concluído");
            }
            return 0;
        } else {
            view.mensagemInformativa("Não há itens para emprestar.");
        }
        return 1;
    }
    
    public int renovarEmprestimo() {
        ObservableList<Emprestimo> emprestimos = view.getEmprestimosSelecionados();
        
        if (!emprestimos.isEmpty()) {
            model.renovar(emprestimos);
            view.setEmprestimosAtuais(model.consultarEmprestimos(idleitor));
            return 0;
        } else {
            view.mensagemInformativa("Você esqueceu de selecionar um item para renovação.");
        }
        return 1;
    }
    
    public int removerExemplarParaEmprestimo() {
        ObservableList<Item> exemplaresParaRemover = view.getExemplaresSelecionados();

        if (!exemplaresParaRemover.isEmpty()) {
            view.removerExemplaresSelecionados();
            return 0;
        } else {
            view.mensagemInformativa("Nenhum item foi selecionado para remoção.");
        }
        return 1;
    }
    
    
    public int gerarReciboLeitor() {
        ObservableList<Item> emprestimos = model.gerarReciboEmprestimo(idleitor);
        if(!emprestimos.isEmpty()) {
            impressora.reciboEmprestimo(view.getLeitorSelecionado(), emprestimos);
        } else {
            view.mensagemInformativa("Este leitor não possui empréstimos.");
        }

        return 0;
    }
    
    public int pesquisarLeitor(String nomeLeitor) {
        if(nomeLeitor != null && !nomeLeitor.isEmpty()) {
            this.nomeLeitor = nomeLeitor;
            idleitor = consultar.idLeitor(nomeLeitor);

            if(idleitor > 0) {
                view.setEmprestimosAtuais(model.consultarEmprestimos(idleitor));
                return 0;
            }
        }
        return 1;
    }
}
