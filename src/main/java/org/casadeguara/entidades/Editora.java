package org.casadeguara.entidades;

/**
 * Esta classe materializa a tabela Editora do banco de dados.
 * 
 * @author Gustavo
 * @since 1.0
 */
public class Editora {
	
	private int ideditora;
    private String nome;
    
    public Editora(int ideditora, String nome) {
        this.ideditora = ideditora;
        this.nome = nome;
    }

    public int getId() {
        return ideditora;
    }

    public void setId(int codigo) {
        this.ideditora = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
    	return nome;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
        Editora other = (Editora) obj;
        if (nome == null) {
            if (other.nome != null) {
                return false;
            }
        } else if (!nome.equals(other.nome)) {
            return false;
        }
        return true;
    }
}
