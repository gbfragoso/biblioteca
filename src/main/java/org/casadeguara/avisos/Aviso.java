package org.casadeguara.avisos;

import java.time.LocalDate;

/**
 * Entidade que representa um aviso no mural de avisos.
 * 
 * @author Gustavo
 * @since 4.0
 */
public class Aviso {

	private final int id;
	private LocalDate data;
	private String texto;

	public Aviso(int id, LocalDate data, String texto) {
		this.id = id;
		this.data = data;
		this.texto = texto;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getId() {
		return id;
	}
}
