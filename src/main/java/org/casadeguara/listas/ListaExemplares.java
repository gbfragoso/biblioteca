package org.casadeguara.listas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListaExemplares {
    
    private static final Logger logger = LogManager.getLogger(ListaExemplares.class);
    private ObservableList<Item> lista;
    
    public ListaExemplares() {
        lista = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    }
    
    public ObservableList<Item> get() {
        if(lista.isEmpty()) {
            StringBuilder query = new StringBuilder();
            query.append("select idexemplar, numero, l.tombo::integer, l.titulo, e.status from exemplar as e ");
            query.append("inner join livro as l on (idlivro = livro) ");
            query.append("where e.status in ('Arquivado', 'Disponível') ");
            query.append("order by titulo, numero");
            
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement(query.toString());
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    int id = rs.getInt(1);
                    int numero = rs.getInt(2);
                    int tombo = rs.getInt(3);
                    String titulo = rs.getString(4);
                    String status = rs.getString(5);
                    
                    Item exemplar = new Item(id, numero, tombo, titulo);
                    exemplar.setStatus(status);
                    
                    lista.add(exemplar);
                }
            } catch (SQLException ex) {
                logger.fatal("Não foi possível consultar a lista de exemplares");
            }
        }
        return lista;
    }
    
    public void remover(ObservableList<Item> exemplaresParaEmprestimo) {
        lista.removeAll(exemplaresParaEmprestimo);
    }
    
    public void update() {
        lista.clear();
        get();
    }
}
