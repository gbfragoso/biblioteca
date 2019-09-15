package org.casadeguara.models;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.listas.DataSourceProvider;
import org.casadeguara.movimentacao.Item;
import org.casadeguara.utilitarios.Criptografia;
import javafx.collections.ObservableList;

/**
 * Responsável por transações relacionadas à parte administrativa.
 * @author Gustavo
 */
public class AdministracaoModel {
    
    private static final Logger logger = LogManager.getLogger(AdministracaoModel.class);
    private DataSourceProvider dataSource;
    
    public AdministracaoModel(DataSourceProvider dataSource) {
        this.dataSource = dataSource;
    }
    
    private boolean usuarioPossuiPermissao() {
        return !Main.getUsuario().getTipo().equals("Comum");
    }
    
    public void atualizarListaExemplares() {
        dataSource.atualizarListaExemplares();
    }
    
    public ObservableList<Item> getListaExemplares() {
        return dataSource.getListaExemplares();
    }
    
    public int recuperarEmprestimo(int idemp, int duracaoEmprestimo) {
        logger.trace("Iniciando recuperação do empréstimo com id: " + idemp);
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 CallableStatement call = con.prepareCall("{call recuperar_emprestimo(?, ?)}")) {

                call.setInt(1, idemp);
                call.setInt(2, duracaoEmprestimo);
                call.executeQuery();
                return 0;
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar recuperar um empréstimo da base de dados.", ex);
            }
        }
        return 1;
    }
     
    public int alterarChaveMestra(String novaChave) {
        logger.trace("Iniciando a alteração da chave mestra");
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement("update configuracao set chave = ? where idconf = 1")) {
                
                ps.setString(1, new Criptografia().aplicar("SHA-512", novaChave));
                ps.executeUpdate();
                return 0;
            } catch (SQLException | NoSuchAlgorithmException ex) {
                logger.fatal("Erro ao tentar alterar a chave mestra.", ex);
            }
        }
        return 1;
    }
    
    public int trocarExemplar(int idemp, int expnovo) {
        logger.trace("Iniciando a troca do exemplar do empréstimo com id: " + idemp);
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 CallableStatement call = con.prepareCall("{call trocar_exemplar(?,?)}")) {

                call.setInt(1, idemp);
                call.setInt(2, expnovo);
                call.executeQuery();
                return 0;
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar trocar o exemplar de um empréstimo", ex);
            }
        }
        return 1;
    }
}
