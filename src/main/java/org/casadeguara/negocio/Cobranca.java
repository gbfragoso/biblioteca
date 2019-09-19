package org.casadeguara.negocio;

import java.sql.Date;
import java.sql.Timestamp;

public class Cobranca {
	
	private int idemprestimo;
	private String leitor;
	private String email;
	private String tombo;
	private String titulo;
	private int numero;
	private Date dataEmprestimo;
	private Date dataDevolucao;
	private Timestamp cobranca;
	
	public int getIdemprestimo() {
		return idemprestimo;
	}
	public void setIdemprestimo(int idemprestimo) {
		this.idemprestimo = idemprestimo;
	}
	public String getLeitor() {
		return leitor;
	}
	public void setLeitor(String leitor) {
		this.leitor = leitor;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTombo() {
		return tombo;
	}
	public void setTombo(String tombo) {
		this.tombo = tombo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}
	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	public Date getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	public Timestamp getCobranca() {
		return cobranca;
	}
	public void setCobranca(Timestamp cobranca) {
		this.cobranca = cobranca;
	}
}
