package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.movimentacao.Acervo;

/**
 * Adiciona um exemplar de acordo com as regras de negócio vigentes.
 * @author Gustavo
 */
public class AdicaoExemplarModel {
    
    private static final Logger logger = LogManager.getLogger(AdicaoExemplarModel.class);
    private int intervaloEntreEmprestimos;
    
    public AdicaoExemplarModel(int intervaloEntreEmprestimos) {
        this.intervaloEntreEmprestimos = intervaloEntreEmprestimos;
    }
    
    public boolean validar(Acervo exemplar, Leitor leitor) {
        return obedeceIntervaloEmprestimos(exemplar, leitor) &&
               obedeceFilaEspera(exemplar, leitor);
    }
    
    private boolean obedeceIntervaloEmprestimos(Acervo exemplar, Leitor leitor) {
        StringBuilder query = new StringBuilder();
        query.append("select DATE_PART('day', current_timestamp - data_devolucao) from movimentacao ");
        query.append("where leitor like ? and exemplar like ? ");
        query.append("order by idmov desc");
        
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            ps.setString(1, leitor.getNome());
            ps.setString(2, exemplar.getTitulo());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int intervalo = intervaloEntreEmprestimos - rs.getInt(1);
                    if(intervalo > 0) {
                        new Alerta().informacao("Esse leitor só poderá pegar este item novamente daqui à "+ intervalo + " dia(s).");
                        return false;
                    } 
                }
            }
        } catch (SQLException e) {
            logger.fatal("Não foi possível verificar se o empréstimo obdece a restrição", e);
        }
        return true;
    }
    
    private boolean obedeceFilaEspera(Acervo exemplar, Leitor leitor) {
        try (Connection con = Conexao.abrir();
             CallableStatement cs = con.prepareCall("{call fila_espera(?)}")) {
            
            cs.setString(1, exemplar.getTitulo());

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString(1);
                    if(nome != null && !nome.isEmpty() && !nome.equals(leitor.getNome())) {
                        new Alerta().informacao("Este livro está reservado para o leitor: " + nome);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            logger.fatal("Não foi possível verificar se o empréstimo tem fila de espera", e);
        }
        return true;
    }
}
