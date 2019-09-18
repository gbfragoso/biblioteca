package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Emprestimo;
import org.casadeguara.movimentacao.Acervo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe é responsável por emprestar exemplares de acordo com as
 * regras de negócio.
 * @author Gustavo
 */
public class EmprestimoModel {
    
    private static final Logger logger = LogManager.getLogger(EmprestimoModel.class);
    private int duracaoEmprestimo;
    private int limiteEmprestimosPorPessoa;

    public EmprestimoModel(int duracaoEmprestimo, int limiteEmprestimosPorPessoa) {
        this.duracaoEmprestimo = duracaoEmprestimo;
        this.limiteEmprestimosPorPessoa = limiteEmprestimosPorPessoa;
    }
    
    public ObservableList<Emprestimo> consultar(int idleitor) {
        StringBuilder query = new StringBuilder();
        query.append("select idemp, exemplar, titulo, numero, data_devolucao from emprestimo ");
        query.append("inner join exemplar on (idexemplar = exemplar) ");
        query.append("inner join livro on (exemplar.livro = idlivro) ");
        query.append("where leitor = ?");
        
        logger.trace("Iniciando consulta de empréstimos do leitor com id: " + idleitor);
        ObservableList<Emprestimo> listaEmprestimos = FXCollections.observableArrayList();
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())){
            
            ps.setInt(1, idleitor);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Emprestimo e = new Emprestimo(
                            rs.getInt(1), 
                            rs.getInt(2),
                            idleitor, 
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getDate(5).toLocalDate());
                    listaEmprestimos.add(e);
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Não foi possível consultar os empréstimos", ex);
        } 
        return listaEmprestimos;
    }
    
    public int emprestar(int idleitor, String nomeLeitor, List<Acervo> exemplares, int quantidadeItens) {
        logger.trace("Iniciando o empréstimo dos exemplares: " + exemplares + "\n ao leitor:" + nomeLeitor);
        
        if(validarEmprestimo(quantidadeItens)) {
            try (Connection con = Conexao.abrir(); 
                 CallableStatement cs = con.prepareCall("{call emprestar(?,?,?,?,?,?,?,?)}")) {

                for (Acervo e : exemplares) {
                    cs.setInt(1, idleitor);
                    cs.setString(2, nomeLeitor);
                    cs.setInt(3, e.getId());
                    cs.setString(4, Integer.toString(e.getTombo()));
                    cs.setString(5, e.getTitulo());
                    cs.setInt(6, e.getNumero());
                    cs.setInt(7, Main.getUsuario().getId());
                    cs.setInt(8, duracaoEmprestimo);
                    cs.addBatch();
                }
                cs.executeBatch();
                return 0;
            } catch (SQLException ex) {
                logger.fatal("Não foi possível emprestar os exemplares ao leitor: " + nomeLeitor, ex);
            }
        }
        return 1;
    }
    
    public ObservableList<Acervo> gerarRecibo(int idleitor) {
        StringBuilder query = new StringBuilder();
        query.append("select exemplar, a.numero, b.tombo, b.titulo, data_devolucao from emprestimo ");
        query.append("inner join exemplar a on (idexemplar = exemplar) ");
        query.append("inner join livro b on (a.livro = idlivro) ");
        query.append("where leitor = ?");
        
        logger.trace("Iniciando a geração do recibo de empréstimo");
        ObservableList<Acervo> exp = FXCollections.observableArrayList();
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())){

            ps.setInt(1, idleitor);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Acervo e = new Acervo(
                            rs.getInt(1),
                            Integer.parseInt(rs.getString(2)),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getDate(5).toLocalDate());

                    exp.add(e);
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Não foi possível gerar o recibo de empréstimo", ex);
        }

        return exp;
    }
    
    private boolean validarEmprestimo(int quantidadeItens) {
        return obedeceLimiteEmprestimos(quantidadeItens) ||
               permissaoParaProsseguir();
    }
    
    private boolean obedeceLimiteEmprestimos(int quantidadeItens) {
        return quantidadeItens <= limiteEmprestimosPorPessoa;
    }
    
    private boolean permissaoParaProsseguir() {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("O limite de empréstimos é de ");
        mensagem.append(limiteEmprestimosPorPessoa);
        mensagem.append(" exemplar(es) por vez.");

        if(Main.getUsuario().getTipo().equals("Comum")) {
            mensagem.append("\nPara prosseguir com esta operação digite a chave-mestra:");
            return new Alerta().autorizacao(mensagem.toString());
        } else {
            return true;
        }
    }
    
}
