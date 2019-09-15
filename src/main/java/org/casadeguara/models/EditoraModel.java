package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Editora;
import org.casadeguara.listas.DataSourceProvider;
import javafx.collections.ObservableList;

/**
 * Esta classe gerencia o relacionamento com a tabela Editora no banco de dados.
 * @author Gustavo Fragoso
 */
public class EditoraModel{
    
    private static final Logger logger = LogManager.getLogger(EditoraModel.class);
    private IdentificadorModel consultar;
    private DataSourceProvider dataSource;
    
    public EditoraModel(DataSourceProvider dataSource) {
        consultar = new IdentificadorModel();
        this.dataSource = dataSource;
    }
    
    public int atualizar(Editora editora) {
        String nome = editora.getNome();
        String query = "update editora set nome = ? where ideditora = ?";

        logger.trace("Iniciando a atualização da editora:" + nome);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, nome);
            ps.setInt(2, editora.getId());
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível atualizar a editora", ex);
        }
        return 1;
    }

    public int cadastrar(Editora editora) {
        String nome = editora.getNome();
        String query = "insert into editora (nome, data_cadastro) values (?, current_date)";
        
        logger.trace("Inicializando o cadastro da editora: " + nome);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, nome);
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível cadastrar a editora", ex);
        }
        return 1;
    }
    
    public void atualizarListaEditoras() {
        dataSource.atualizarListaEditoras();
    }
    
    public ObservableList<String> getListaEditoras() {
        return dataSource.getListaEditoras();
    }
    
    public int consultarEditora(String nome) {
        return consultar.idEditora(nome);
    }
}
