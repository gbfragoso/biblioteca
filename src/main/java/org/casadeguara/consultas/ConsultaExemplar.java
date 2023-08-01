package org.casadeguara.consultas;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Objeto que contém as informações do resultado de uma pesquisa.
 * 
 * @author Gustavo
 * @since 3.5
 */
public class ConsultaExemplar {

	private final SimpleStringProperty tombo;
	private final SimpleStringProperty titulo;
	private final SimpleStringProperty status;
	private final SimpleIntegerProperty numero;

	public ConsultaExemplar(String tombo, String titulo, int numero, String status) {
		this.tombo = new SimpleStringProperty(this, "tombo", tombo);
		this.titulo = new SimpleStringProperty(this, "titulo", titulo);
		this.numero = new SimpleIntegerProperty(this, "numero", numero);
		this.status = new SimpleStringProperty(this, "status", status);
	}

	public int getNumero() {
		return numero.get();
	}

	public String getStatus() {
		return status.get();
	}

	public String getTitulo() {
		return titulo.get();
	}

	public String getTombo() {
		return tombo.get();
	}
}
