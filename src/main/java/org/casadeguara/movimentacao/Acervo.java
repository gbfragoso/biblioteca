package org.casadeguara.movimentacao;

import java.time.LocalDate;
import org.casadeguara.negocio.Regra;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Esta representa uma junção de algumas informações da tabela de exemplar e de
 * empréstimo.
 * 
 * @author Gustavo
 * @since 1.0
 */
public class Acervo {

	private final SimpleIntegerProperty id;
	private final SimpleIntegerProperty numero;
	private final SimpleIntegerProperty tombo;
	private final SimpleStringProperty titulo;
	private SimpleStringProperty status;
	private LocalDate dataDevolucao;

	/**
	 * Ao criar um item para empréstimo, a data de devolução padrão utilizada é a da
	 * regra de negócio.
	 * 
	 * @param idexemplar ID do exemplar
	 * @param numero     Número do exemplar
	 * @param tombo      Tombo do livro
	 * @param titulo     Título do livro
	 */
	public Acervo(int idexemplar, int numero, int tombo, String titulo) {
		this(idexemplar, numero, tombo, titulo, LocalDate.now().plusDays(new Regra().getDuracaoEmprestimo()));
	}

	public Acervo(int idexemplar, int numero, int tombo, String titulo, LocalDate dataDevolucao) {
		this.id = new SimpleIntegerProperty(this, "id", idexemplar);
		this.numero = new SimpleIntegerProperty(this, "numero", numero);
		this.tombo = new SimpleIntegerProperty(this, "tombo", tombo);
		this.titulo = new SimpleStringProperty(this, "titulo", titulo);
		this.status = new SimpleStringProperty(this, "status", "");

		if (dataDevolucao != null) {
			this.dataDevolucao = dataDevolucao;
		}
	}

	public int getId() {
		return id.get();
	}

	public int getNumero() {
		return numero.get();
	}

	public int getTombo() {
		return tombo.get();
	}

	public String getTitulo() {
		return titulo.get();
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate novaData) {
		dataDevolucao = novaData;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	@Override
	public String toString() {
		return String.format("%s - %s - Ex: %d", getTombo(), getTitulo(), getNumero());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.getValue().hashCode());
		result = prime * result + ((tombo == null) ? 0 : tombo.getValue().hashCode());
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
		Acervo other = (Acervo) obj;
		if (numero == null) {
			if (other.numero != null) {
				return false;
			}
		} else if (!numero.getValue().equals(other.numero.getValue())) {
			return false;
		}
		if (tombo == null) {
			if (other.tombo != null) {
				return false;
			}
		} else if (!tombo.getValue().equals(other.tombo.getValue())) {
			return false;
		}
		return true;
	}
}
