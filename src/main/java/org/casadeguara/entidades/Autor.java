package org.casadeguara.entidades;

/**
 * A classe autor materializa a tabela Autor do banco de dados
 * 
 * @author Gustavo
 * @since 1.0
 */
public class Autor {

	private int idautor;
	private String nome;

	public Autor(int idautor, String nome) {
		this.idautor = idautor;
		this.nome = nome;
	}

	public int getId() {
		return idautor;
	}

	public void setId(int idautor) {
		this.idautor = idautor;
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
		Autor other = (Autor) obj;
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
