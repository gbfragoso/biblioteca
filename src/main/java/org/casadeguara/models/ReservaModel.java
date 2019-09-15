package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.listas.DataSourceProvider;
import org.casadeguara.movimentacao.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe é responsável pelo relacionamento com a tabela Reserva.
 * @author Gustavo
 */
public class ReservaModel {
    
    private static final Logger logger = LogManager.getLogger(ReservaModel.class);
    private IdentificadorModel consultar;
    private final DataSourceProvider dataSource;
    
    public ReservaModel(DataSourceProvider dataSource) {
        this.dataSource = dataSource;
        consultar = new IdentificadorModel();
    }
    
    public ObservableList<String> getListaLeitores() {
        return dataSource.getListaLeitores();
    }
    
    public ObservableList<String> getListaLivros() {
        return dataSource.getListaLivros();
    }
    
    public int consultarLeitor(String nome) {
        return consultar.idLeitor(nome);
    }
    
    public int consultarLivro(String titulo) {
        return consultar.idLivro(titulo.substring(10));
    }
    
    public ObservableList<Reserva> consultarReservas(String titulo) {
        ObservableList<Reserva> reservas = FXCollections.observableArrayList();
        
        StringBuilder query = new StringBuilder();
        query.append("select leitor, livro, tombo, titulo, nome, data_expira from reserva ");
        query.append("inner join livro on(livro = idlivro) ");
        query.append("inner join leitor l on (leitor = idleitor) ");
        query.append("where unaccent(titulo) like unaccent(?)");

        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            ps.setString(1, titulo);

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(new Reserva(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getDate(6).toLocalDate()));
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Ocorreu um erro durante a consulta da reserva deste leitor.", ex);
        }
        return reservas;
    }
    
    public int reservar(int idleitor, int idlivro) {
        StringBuilder query = new StringBuilder();
        query.append("insert into reserva (leitor, livro, data_expira) ");
        query.append("values (?, ?, current_date + interval '7 days')");
        
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setInt(1, idleitor);
            ps.setInt(2, idlivro);
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Ocorreu um erro ao tentar reservar o livro", ex);
        }
        return 1;
    }
    
    public int cancelarReserva(int idleitor, int idlivro) {
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement("delete from reserva where leitor = ? and livro = ?")) {
            
            ps.setInt(1, idleitor);
            ps.setInt(2, idlivro);
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Ocorreu um erro ao tentar cancelar a reserva", ex);
        }
        return 1;
    }
    
    public boolean possuiReserva(int idleitor, int idlivro) {
        boolean possuiReserva = false;

        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement("select leitor from reserva where leitor = ? and livro = ?")) {

            ps.setInt(1, idleitor);
            ps.setInt(2, idlivro);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    possuiReserva = true;
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Ocorreu um erro durante a consulta de reservas.", ex);
        }
        return possuiReserva;
    }

    public int expirarReservas() {
        String query = "delete from reserva where data_expira::timestamp::date = current_date";
        
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Ocorreu um expirar as reservas.", ex);
        }
        return 1;
    }
}
