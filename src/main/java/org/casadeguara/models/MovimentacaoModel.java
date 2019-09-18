package org.casadeguara.models;

import java.util.List;

import org.casadeguara.entidades.Leitor;
import org.casadeguara.movimentacao.Emprestimo;
import org.casadeguara.movimentacao.Acervo;
import org.casadeguara.negocio.Regra;
import javafx.collections.ObservableList;

/**
 * Classe responsável por coordenar as operações que movimentam o acervo,
 * chamando os métodos corretos nas classes model de cada operação.
 * @author Gustavo
 */
public class MovimentacaoModel {
    
    private AdicaoExemplarModel adicaoExemplar;
    private DevolucaoModel devolucao;
    private EmprestimoModel emprestimo;
    private RenovacaoModel renovacao;

    public MovimentacaoModel() {
        Regra regras = new RegraModel().consultarRegrasNegocio();
        
        int duracaoEmprestimo = regras.getDuracaoEmprestimo();
        int duracaoRenovacao = regras.getDuracaoRenovacao();
        int limiteEmprestimosPorPessoa = regras.getLimiteEmprestimosPorPessoa();
        int limiteRenovacoes = regras.getLimiteRenovacoes();
        int intervaloEntreEmprestimos = regras.getIntervaloEntreEmprestimos();
        
        adicaoExemplar = new AdicaoExemplarModel(intervaloEntreEmprestimos);
        devolucao = new DevolucaoModel();
        emprestimo = new EmprestimoModel(duracaoEmprestimo, limiteEmprestimosPorPessoa);
        renovacao = new RenovacaoModel(duracaoEmprestimo, duracaoRenovacao, limiteRenovacoes);
    }

    public ObservableList<Emprestimo> consultarEmprestimos(int idleitor) {
        return emprestimo.consultar(idleitor);
    }
    
    public ObservableList<Acervo> gerarReciboEmprestimo(int idleitor) {
        return emprestimo.gerarRecibo(idleitor);
    }
    
    public int devolver(List<Emprestimo> emprestimos) {
        return devolucao.devolver(emprestimos);
    }

    public int emprestar(int idleitor, String nomeLeitor, List<Acervo> exemplares, int quantidadeItens) {
        return emprestimo.emprestar(idleitor, nomeLeitor, exemplares, quantidadeItens);
    }

    public int renovar(List<Emprestimo> emprestimos) {
        return renovacao.renovar(emprestimos);
    }
    
    public boolean validarAdicaoExemplar(Acervo exemplar, Leitor leitor) {
        return adicaoExemplar.validar(exemplar, leitor);
    }
}
