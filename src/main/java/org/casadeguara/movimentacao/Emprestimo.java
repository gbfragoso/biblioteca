package org.casadeguara.movimentacao;

import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Materializa as informações da tabela de empréstimo do banco de dados
 * 
 * @author Gustavo Fragoso
 */
public class Emprestimo {

	private final int idExemplar;
	private final int idLeitor;
	private final int quantidadeRenovacoes;

	private final SimpleIntegerProperty idEmprestimo;
	private SimpleStringProperty itemAcervo;
	private SimpleIntegerProperty numeroItem;
	private LocalDate dataDevolucao;

	public Emprestimo(int idemp, int idexemplar, int idleitor, String nomeItem, int numero, int quantidadeRenovacoes, LocalDate dataDevolucao) {
		this.idExemplar = idexemplar;
		this.idLeitor = idleitor;
		this.quantidadeRenovacoes = quantidadeRenovacoes;

		idEmprestimo = new SimpleIntegerProperty(this, "idEmprestimo", idemp);
		itemAcervo = new SimpleStringProperty(this, "itemAcervo", nomeItem);
		numeroItem = new SimpleIntegerProperty(this, "numeroItem", numero);

		this.dataDevolucao = dataDevolucao;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public int getIdEmprestimo() {
		return idEmprestimo.get();
	}

	public int getIdExemplar() {
		return idExemplar;
	}

	public int getIdLeitor() {
		return idLeitor;
	}

	public String getItemAcervo() {
		return itemAcervo.get();
	}

	public int getNumeroItem() {
		return numeroItem.get();
	}

	public int getQuantidadeRenovacoes() {
		return quantidadeRenovacoes;
	}
}
