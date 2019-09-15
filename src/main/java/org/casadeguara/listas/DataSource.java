package org.casadeguara.listas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataSource {
    
    private static final Logger logger = LogManager.getLogger(DataSource.class);
    private ObservableList<String> lista;
    private final String query;
    
    public DataSource(String query) {
        lista = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        this.query = query;
    }
    
    protected boolean add(String element) {
        return lista.add(element);
    }
    
    protected void clear() {
        lista.clear();
    }
    
    public ObservableList<String> get(){
        if(isEmpty()) {
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
            
                while (rs.next()) {
                    add(rs.getString(1));
                }
            } catch (SQLException ex) {
                logger.fatal(ex);
            }
        }
        return lista;
    }
    
    public boolean isEmpty() {
        return lista.isEmpty();
    }

    public void update() {
        clear();
        get();
    }
}
