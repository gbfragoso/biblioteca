package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Emprestimo;

/**
 * Esta classe é responsável por devolver um exemplar
 * @author Gustavo
 */
public class DevolucaoModel {
    
    private static final Logger logger = LogManager.getLogger(DevolucaoModel.class);
    
    public int devolver(List<Emprestimo> emprestimos) {
        logger.trace("Iniciando devolucao dos emprestimos:" + emprestimos);
        
        try (Connection con = Conexao.abrir();
             CallableStatement cs = con.prepareCall("{call devolver(?,?,?,?)}")) {
            
            int idoperador = Main.getUsuario().getId();
            for (Emprestimo e : emprestimos) {
                cs.setInt(1, e.getIdEmprestimo());
                cs.setInt(2, e.getIdExemplar());
                cs.setBoolean(3, e.getDataDevolucao().isBefore(LocalDate.now()));
                cs.setInt(4, idoperador);
                cs.addBatch();
            }
            
            cs.executeBatch();
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível consultar a lista de exemplares");
        }
        return 1;
    }
}
