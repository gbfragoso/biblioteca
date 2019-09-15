package org.casadeguara.movimentacao;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;

/**
 * Classe que contém as informações necessárias para uma reserva de livro.
 * 
 * @author Gustavo
 * @since 2.0
 */
public class Reserva {

    private final int idleitor;
    private final int idlivro;
    public final SimpleStringProperty tombo;
    public final SimpleStringProperty titulo;
    public final SimpleStringProperty leitor;
    public final LocalDate data;

    public Reserva(int idleitor, int idlivro, String tombo, String titulo, String leitor, LocalDate dataReserva) {
        this.idleitor = idleitor;
        this.idlivro = idlivro;
        this.tombo = new SimpleStringProperty(this, "tombo", tombo);
        this.titulo = new SimpleStringProperty(this, "titulo", titulo);
        this.leitor = new SimpleStringProperty(this, "leitor", leitor);
        data = dataReserva;
    }

    public int getIdleitor() {
        return idleitor;
    }

    public int getIdlivro() {
        return idlivro;
    }

    public LocalDate getData() {
        return data;
    }

    public String getTombo() {
        return tombo.get();
    }

    public String getTitulo() {
        return titulo.get();
    }

    public String getLeitor() {
        return leitor.get();
    }
}
