package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.alertas.Alerta;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Emprestimo;

/**
 * Esta classe é responsável por renovar um empréstimo de acordo com as regras
 * de negócio.
 * 
 * @author Gustavo
 */
public class RenovacaoModel {

	private static final Logger logger = LogManager.getLogger(RenovacaoModel.class);
	private int duracaoEmprestimo;
	private int duracaoRenovacao;
	private int limiteRenovacoes;

	public RenovacaoModel(int duracaoEmprestimo, int duracaoRenovacao, int limiteRenovacoes) {
		this.duracaoEmprestimo = duracaoEmprestimo;
		this.duracaoRenovacao = duracaoRenovacao;
		this.limiteRenovacoes = limiteRenovacoes;
	}

	public int renovar(List<Emprestimo> emprestimos) {
		logger.trace("Iniciando a renovação dos empréstimos: " + emprestimos);

		Map<Integer, Integer> renovacoes = consultarRenovacoes(emprestimos);
		try (Connection con = Conexao.abrir(); CallableStatement cs = con.prepareCall("{call renovar(?,?,?)}")) {

			for (Emprestimo e : emprestimos) {
				int idemprestimo = e.getIdEmprestimo();
				int quantidadeRenovacoes = renovacoes.get(idemprestimo);

				if (validarRenovacao(e.getItemAcervo(), quantidadeRenovacoes)) {
					cs.setInt(1, idemprestimo);
					cs.setInt(2, calculaTempoRenovacao(quantidadeRenovacoes));
					cs.setInt(3, quantidadeRenovacoes);
					cs.addBatch();
				}
			}

			cs.executeBatch();
			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível renovar os empréstimos");
		}
		return 1;
	}

	private int calculaTempoRenovacao(int quantidadeRenovacoes) {
		return duracaoEmprestimo + ((quantidadeRenovacoes + 1) * duracaoRenovacao);
	}

	private Map<Integer, Integer> consultarRenovacoes(List<Emprestimo> emprestimos) {
		logger.trace("Iniciando consulta de renovações");

		List<Integer> idemprestimos = emprestimos.stream().map(Emprestimo::getIdEmprestimo)
				.collect(Collectors.toList());

		Map<Integer, Integer> renovacoes = new HashMap<>();

		StringBuilder query = new StringBuilder();
		query.append("select idemp, coalesce(quantidade, 0) from emprestimo ");
		query.append("left join renovacao on (emprestimo = idemp) ");
		query.append("where idemp = ANY(?)");

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setArray(1, con.createArrayOf("integer", idemprestimos.toArray()));

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					renovacoes.put(rs.getInt(1), rs.getInt(2));
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar as renovações");
		}

		return renovacoes;
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

		String tipoUsuario = Main.getUsuario().getTipo();
		if (tipoUsuario.equals("Comum")) {
			mensagem.append("\nPara prosseguir com esta operação digite a chave-mestra:");
			return new Alerta().autorizacao(mensagem.toString());
		} else {
			mensagem.append("\nDeseja prosseguir com a renovação?");
			return new Alerta().confirmacao(mensagem.toString());
		}
	}
}
