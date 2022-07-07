package org.casadeguara.consultas;

import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Objeto que contém as informações do resultado de uma pesquisa.
 * 
 * @author Gustavo
 * @since 3.5
 */
public class Resultado {

	private final SimpleStringProperty tombo;
	private final SimpleStringProperty titulo;
	private final SimpleStringProperty status;
	private final SimpleStringProperty leitor;
	private final SimpleIntegerProperty numero;
	private final LocalDate devolucao;

	public Resultado(String tombo, String titulo, int numero, String status, String leitor, LocalDate devolucao) {
		this.tombo = new SimpleStringProperty(this, "tombo", tombo);
		this.titulo = new SimpleStringProperty(this, "titulo", titulo);
		this.numero = new SimpleIntegerProperty(this, "numero", numero);
		this.status = new SimpleStringProperty(this, "status", status);
		this.leitor = new SimpleStringProperty(this, "leitor", leitor);
		this.devolucao = devolucao;
	}

	public LocalDate getDevolucao() {
		return devolucao;
	}

	public String getLeitor() {
		return leitor.get();
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
