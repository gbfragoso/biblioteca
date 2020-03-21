package org.casadeguara.negocio;

public class Cobranca {

	private final String leitor;
	private final String email;
	private final String[] livros;

	public Cobranca(String leitor, String email, String[] livros) {
		this.leitor = leitor;
		this.email = email;
		this.livros = livros;
	}

	public String getLeitor() {
		return leitor;
	}

	public String getEmail() {
		return email;
	}

	public String[] getLivros() {
		return livros;
	}
}
