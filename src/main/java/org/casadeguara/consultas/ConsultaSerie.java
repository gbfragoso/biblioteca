package org.casadeguara.consultas;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Objeto que contém as informações do resultado de uma pesquisa.
 * 
 * @author Gustavo
 * @since 3.5
 */
public class ConsultaSerie {

	private final SimpleStringProperty serie;
	private final SimpleStringProperty tombo;
	private final SimpleStringProperty titulo;
	private final SimpleIntegerProperty ordem;

	public ConsultaSerie(String serie, String tombo, String titulo, int ordem) {
		this.serie = new SimpleStringProperty(this, "serie", serie);
		this.tombo = new SimpleStringProperty(this, "tombo", tombo);
		this.titulo = new SimpleStringProperty(this, "titulo", titulo);
		this.ordem = new SimpleIntegerProperty(this, "ordem", ordem);
	}

	public String getSerie() {
		return serie.get();
	}

	public String getTombo() {
		return tombo.get();
	}

	public String getTitulo() {
		return titulo.get();
	}

	public int getOrdem() {
		return ordem.get();
	}
}
