package org.casadeguara.controllers;

import java.time.LocalDate;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.impressora.Impressora;
import org.casadeguara.models.AcervoModel;
import org.casadeguara.models.LeitorModel;
import org.casadeguara.models.MovimentacaoModel;
import org.casadeguara.models.RegraModel;
import org.casadeguara.movimentacao.Emprestimo;
import org.casadeguara.movimentacao.Acervo;
import org.casadeguara.negocio.Regra;
import org.casadeguara.views.MovimentacaoView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class MovimentacaoController implements GenericController{
    
    private MovimentacaoView view;
    private MovimentacaoModel model;
    private Impressora impressora;
    
    private LocalDate dataDevolucao;
    private Leitor leitorSelecionado;

    public MovimentacaoController(MovimentacaoView view) {
        this.view = view;
        this.model = new MovimentacaoModel();
        
        Regra regrasNegocio = new RegraModel().consultarRegrasNegocio();
        dataDevolucao = LocalDate.now().plusDays(regrasNegocio.getDuracaoEmprestimo());
        
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
        view.acaoSelecionarLeitor(event -> pesquisarLeitor());
        
        view.setAutoCompleteLeitor(new LeitorModel());
        view.setAutoCompleteAcervo(new AcervoModel());
    }
    
    public int adicionarExemplarParaEmprestimo() {
        Acervo exemplar = view.getExemplarSelecionado();

        if (exemplar != null && leitorSelecionado != null) {
            if (model.validarAdicaoExemplar(exemplar, leitorSelecionado)) {
                return adicionarExemplar(exemplar);
            }
        } else {
            view.mensagemInformativa("Selecione um leitor ou exemplar");
        }
        return 1;
    }
    
    private int adicionarExemplar(Acervo exemplar) {
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
        ObservableList<Acervo> exemplaresParaEmprestimo = view.getExemplaresParaEmprestimo();
        int quantidadeItens = view.getQuantidadeItens();
        
        if (!exemplaresParaEmprestimo.isEmpty() && leitorSelecionado != null) {
        	int id = leitorSelecionado.getId();
            String nome = leitorSelecionado.getNome();
			
            if(model.emprestar(id, nome, exemplaresParaEmprestimo, quantidadeItens) == 0) {
                impressora.reciboEmprestimo(nome, exemplaresParaEmprestimo);
                
                view.setEmprestimosAtuais(model.consultarEmprestimos(id));
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
        
        if (!emprestimos.isEmpty() && leitorSelecionado != null) {
            model.renovar(emprestimos);
            view.setEmprestimosAtuais(model.consultarEmprestimos(leitorSelecionado.getId()));
            return 0;
        } else {
            view.mensagemInformativa("Você esqueceu de selecionar um item para renovação.");
        }
        return 1;
    }
    
    public int removerExemplarParaEmprestimo() {
        ObservableList<Acervo> exemplaresParaRemover = view.getExemplaresSelecionados();

        if (!exemplaresParaRemover.isEmpty()) {
            view.removerExemplaresSelecionados();
            return 0;
        } else {
            view.mensagemInformativa("Nenhum item foi selecionado para remoção.");
        }
        return 1;
    }
    
    
    public int gerarReciboLeitor() {
    	
        ObservableList<Acervo> emprestimos = model.gerarReciboEmprestimo(leitorSelecionado.getId());
        if(!emprestimos.isEmpty()) {
            impressora.reciboEmprestimo(leitorSelecionado.getNome(), emprestimos);
        } else {
            view.mensagemInformativa("Este leitor não possui empréstimos.");
        }

        return 0;
    }
    
    public int pesquisarLeitor() {
    	Leitor leitor = view.getLeitorSelecionado();
    	
        if(leitor != null) {
            this.leitorSelecionado = leitor;
            view.setEmprestimosAtuais(model.consultarEmprestimos(leitor.getId()));
            return 0;
        }
        return 1;
    }
}
