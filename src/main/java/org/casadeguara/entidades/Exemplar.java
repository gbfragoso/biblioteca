package org.casadeguara.entidades;

/**
 * Esta classe representa parcialmente um exemplar no banco de dados.
 * 
 * @author Gustavo
 * @since 1.0
 */
public class Exemplar {

    private final int id;
    private final int numero;
    private String status;
    
    public Exemplar(int idexemplar, int numero) {
        this(idexemplar, numero, "Disponível");
    }
    
    public Exemplar(int idexemplar, int numero, String status) {
        this.id = idexemplar;
        this.numero = numero;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status; 
    }
}
