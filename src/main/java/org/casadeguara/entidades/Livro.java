package org.casadeguara.entidades;

/**
 * Materializa a tabela Livro presente no banco de dados
 * 
 * @author Gustavo
 * @since 1.0
 */
public class Livro{

    private int idlivro;
    private String tombo;
    private String titulo;
    private Editora editora;

    public Livro(int idlivro, String tombo, String titulo) {
        this.idlivro = idlivro;
        this.tombo = tombo;
        this.titulo = titulo;
    }

    public Editora getEditora() {
        return editora;
    }

    public int getId() {
        return idlivro;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public String getTombo() {
        return tombo;
    }
    
    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public void setId(int idlivro) {
        this.idlivro = idlivro;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public void setTombo(String tombo) {
        this.tombo = tombo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tombo == null) ? 0 : tombo.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Livro other = (Livro) obj;
        if (tombo == null) {
            if (other.tombo != null) {
                return false;
            }
        } else if (!tombo.equals(other.tombo)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("%-7s : %s", tombo, titulo);
    }
}
