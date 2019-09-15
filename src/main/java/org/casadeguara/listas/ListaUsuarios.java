package org.casadeguara.listas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import javafx.collections.ObservableList;

public class ListaUsuarios extends DataSource{
    
    private static final Logger logger = LogManager.getLogger(ListaUsuarios.class);
    
    public ListaUsuarios(String query) {
        super(query);
    }
    
    @Override
    public ObservableList<String> get() {
        logger.trace("Iniciando a consulta da lista de usuários");
        
        if (super.isEmpty()) {
            String query = "select nome from usuario where tipo = ANY(?) and idusuario != ? order by nome";

            String[] root = {"Admin", "Comum"};
            String[] comum = {"Comum"};

            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setArray(1, con.createArrayOf("varchar", (Main.getUsuario().getTipo().equals("Root")) ? root : comum));
                ps.setInt(2, Main.getUsuario().getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        super.add(rs.getString(1));
                    }
                }
            } catch (SQLException ex) {
                logger.error("Não foi possível consultar os usuários");
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
