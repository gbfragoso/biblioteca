package org.casadeguara.etiquetas;

/**
 * Esta classe contém as informações da etiqueta classificadora dos livros.
 * 
 * @author Gustavo
 * @since 2.0
 */
public class Etiqueta {

	private String tombo;
	private int numero;

	public Etiqueta(String tombo, int numero) {
		this.tombo = tombo;
		this.numero = numero;
	}

	public String getTombo() {
		return tombo;
	}

	public void setTombo(String tombo) {
		this.tombo = tombo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
