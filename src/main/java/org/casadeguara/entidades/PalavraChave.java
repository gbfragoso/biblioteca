package org.casadeguara.entidades;

/**
 * Esta classe representa a tabela Keyword no banco de dados.
 * 
 * @author Gustavo
 *
 */
public class PalavraChave {

	private int idpalavra;
	private String assunto;

	public PalavraChave(int idpalavra, String assunto) {
		this.idpalavra = idpalavra;
		this.assunto = assunto;
	}

	public int getId() {
		return idpalavra;
	}

	public void setId(int idpalavra) {
		this.idpalavra = idpalavra;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	@Override
	public String toString() {
		return assunto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assunto == null) ? 0 : assunto.hashCode());
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
		PalavraChave other = (PalavraChave) obj;
		if (assunto == null) {
			if (other.assunto != null) {
				return false;
			}
		} else if (!assunto.equals(other.assunto)) {
			return false;
		}
		return true;
	}

}
