package org.casadeguara.listas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import javafx.collections.ObservableList;

public class ListaLivros extends DataSource {
    
    private static final Logger logger = LogManager.getLogger(ListaLivros.class);
    
    public ListaLivros(String query) {
        super(query);
    }
    
    @Override
    public ObservableList<String> get() {
        logger.trace("Iniciando a consulta da lista de livros");
        
        if(super.isEmpty()) {
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement("select tombo, titulo from livro order by titulo");
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    String titulo = String.format("%-7s | %s", rs.getString(1), rs.getString(2));
                    super.add(titulo);
                }
            } catch (SQLException ex) {
                logger.fatal("Não foi possível consultar a lista de livros", ex);
            }
        }
        return super.get();
    }
    
    @Override
    public void update() {
        super.clear();
        get();
    }

}
