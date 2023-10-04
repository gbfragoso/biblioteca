package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.application.App;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Emprestimo;

/**
 * Esta classe é responsável por renovar um empréstimo de acordo com as regras
 * de negócio.
 * 
 * @author Gustavo
 */
public class RenovacaoModel {

	private int duracaoEmprestimo;
	private int duracaoRenovacao;
	private int limiteRenovacoes;

	public RenovacaoModel(int duracaoEmprestimo, int duracaoRenovacao, int limiteRenovacoes) {
		this.duracaoEmprestimo = duracaoEmprestimo;
		this.duracaoRenovacao = duracaoRenovacao;
		this.limiteRenovacoes = limiteRenovacoes;
	}

	public int renovar(List<Emprestimo> emprestimos) {

		StringBuilder query = new StringBuilder();
		query.append("update emprestimo set data_devolucao = data_emprestimo + ?, renovacoes = renovacoes + 1 ");
		query.append("where idemp = ?");

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			for (Emprestimo e : emprestimos) {
				int idemprestimo = e.getIdEmprestimo();
				int quantidadeRenovacoes = e.getQuantidadeRenovacoes();

				if (validarRenovacao(e.getItemAcervo(), quantidadeRenovacoes)) {
					ps.setInt(1, calculaTempoRenovacao(quantidadeRenovacoes));
					ps.setInt(2, idemprestimo);
					ps.addBatch();
				}
			}

			ps.executeBatch();
			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível renovar os empréstimos");
		}
		return 1;
	}

	private int calculaTempoRenovacao(int quantidadeRenovacoes) {
		return duracaoEmprestimo + ((quantidadeRenovacoes + 1) * duracaoRenovacao);
	}

	private boolean validarRenovacao(String titulo, int quantidadeRenovacoes) {
		return obedeceLimiteRenovacoes(quantidadeRenovacoes) || permissaoParaProsseguir(titulo, quantidadeRenovacoes);
	}

	private boolean obedeceLimiteRenovacoes(int quantidadeRenovacoes) {
		return quantidadeRenovacoes + 1 <= limiteRenovacoes;
	}

	private boolean permissaoParaProsseguir(String titulo, int quantidadeRenovacoes) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("O empréstimo do livro ");
		mensagem.append(titulo);
		mensagem.append(" já foi renovado ");
		mensagem.append(quantidadeRenovacoes);
		mensagem.append(" vez(es).");

		String tipoUsuario = App.getUsuario().getTipo();
		if (tipoUsuario.equals("Comum")) {
			mensagem.append("\nPara prosseguir com esta operação digite a chave-mestra:");
			return new Alerta().autorizacao(mensagem.toString());
		} else {
			mensagem.append("\nDeseja prosseguir com a renovação?");
			return new Alerta().confirmacao(mensagem.toString());
		}
	}
}
