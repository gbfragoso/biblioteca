package org.casadeguara.negocio;

/**
 * Abstração das regras de negócio relacionadas à movimentação do acervo.
 * 
 * @author Gustavo
 * @since 3.0
 */
public class Regra {

	private int duracaoEmprestimo;
	private int duracaoRenovacao;
	private int duracaoReserva = 7;
	private int limiteRenovacoes;
	private int limiteEmprestimosPorPessoa;
	private int intervaloEntreEmprestimos;

	public int getDuracaoEmprestimo() {
		return duracaoEmprestimo;
	}

	public int getDuracaoRenovacao() {
		return duracaoRenovacao;
	}

	public int getDuracaoReserva() {
		return duracaoReserva;
	}

	public int getIntervaloEntreEmprestimos() {
		return intervaloEntreEmprestimos;
	}

	public int getLimiteEmprestimosPorPessoa() {
		return limiteEmprestimosPorPessoa;
	}

	public int getLimiteRenovacoes() {
		return limiteRenovacoes;
	}

	public int getLimiteTempo() {
		return duracaoEmprestimo + (limiteRenovacoes * duracaoRenovacao);
	}

	public void setDuracaoEmprestimo(int duracaoEmprestimo) {
		this.duracaoEmprestimo = duracaoEmprestimo;
	}

	public void setDuracaoRenovacao(int duracaoRenovacao) {
		this.duracaoRenovacao = duracaoRenovacao;
	}

	public void setIntervaloEntreEmprestimos(int intervaloEntreEmprestimos) {
		this.intervaloEntreEmprestimos = intervaloEntreEmprestimos;
	}

	public void setLimiteEmprestimosPorPessoa(int limiteEmprestimosPorPessoa) {
		this.limiteEmprestimosPorPessoa = limiteEmprestimosPorPessoa;
	}

	public void setLimiteRenovacoes(int limiteRenovacoes) {
		this.limiteRenovacoes = limiteRenovacoes;
	}
}
