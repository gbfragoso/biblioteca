package org.casadeguara.consultas;

import java.sql.Date;
import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Objeto que contém as informações do resultado de uma pesquisa.
 * 
 * @author Gustavo
 * @since 3.5
 */
public class ConsultaEmprestimo {

	private final SimpleIntegerProperty id;
	private final SimpleStringProperty leitor;
	private final SimpleStringProperty titulo;
	private final SimpleIntegerProperty numero;
	private final LocalDate dataEmprestimo;
	private final LocalDate dataDevolucao;

	public ConsultaEmprestimo(int id, String leitor, String titulo, int numero, LocalDate dataEmprestimo,
			LocalDate dataDevolucao) {
		this.id = new SimpleIntegerProperty(this, "id", id);
		this.leitor = new SimpleStringProperty(this, "leitor", leitor);
		this.titulo = new SimpleStringProperty(this, "titulo", titulo);
		this.numero = new SimpleIntegerProperty(this, "numero", numero);
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
	}

	public ConsultaEmprestimo(int id, String leitor, String titulo, int numero, Date dataEmprestimo,
			Date dataDevolucao) {
		this.id = new SimpleIntegerProperty(this, "id", id);
		this.leitor = new SimpleStringProperty(this, "leitor", leitor);
		this.titulo = new SimpleStringProperty(this, "titulo", titulo);
		this.numero = new SimpleIntegerProperty(this, "numero", numero);
		this.dataEmprestimo = (dataEmprestimo != null) ? dataEmprestimo.toLocalDate() : null;
		this.dataDevolucao = (dataDevolucao != null) ? dataDevolucao.toLocalDate() : null;
	}

	public int getId() {
		return id.get();
	}

	public int getNumero() {
		return numero.get();
	}

	public String getTitulo() {
		return titulo.get();
	}

	public String getLeitor() {
		return leitor.get();
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}
}
