package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.listas.DataSourceProvider;
import javafx.collections.ObservableList;

/**
 * Esta classe realiza as operações de relacionadas a tabela Keyword no banco de dados.
 * 
 * @author Gustavo
 * @since 3.0
 */
public class PalavraChaveModel{
    
    private static final Logger logger = LogManager.getLogger();
    private DataSourceProvider dataSource;
    private IdentificadorModel consultar;
    
    public PalavraChaveModel(DataSourceProvider dataSource) {
        consultar = new IdentificadorModel();
        this.dataSource = dataSource;
    }
    
    public int atualizar(PalavraChave chave) {
        String query = "update keyword set chave = ? where idkeyword = ?";
        String assunto = chave.getAssunto();
        
        logger.trace("Iniciando a atualização da palavra-chave: " + assunto);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, assunto);
            ps.setInt(2, chave.getId());
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível atualizar a palavra-chave");
        }
        return 1;
    }

    public int cadastrar(PalavraChave palavraChave) {
        String query = "insert into keyword (chave) values (?)";
        String assunto = palavraChave.getAssunto();
        
        logger.trace("Iniciando o cadastro da palavra-chave:", assunto);
        try (Connection con = Conexao.abrir();
            PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, assunto);
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível cadastrar a palavra-chave", ex);
        }
        return 1;
    }
    
    public void atualizarListaPalavras() {
        dataSource.atualizarListaPalavras();
    }
    
    public int consultarPalavraChave(String nome) {
        return consultar.idPalavraChave(nome);
    }
    
    public ObservableList<String> getListaPalavras() {
        return dataSource.getListaPalavras();
    }
}
