package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.negocio.Regra;

/**
 * Esta classe consulta as regras de negócio.
 * 
 * @author Gustavo
 */
public class RegraModel {

	private static final Logger logger = LogManager.getLogger(RegraModel.class);

	public Regra consultarRegrasNegocio() {
		StringBuilder query = new StringBuilder();
		query.append("select duracao_emprestimo, duracao_renovacao, ");
		query.append("limite_emprestimo, limite_renovacao, intervalo_emprestimo ");
		query.append("from configuracao");

		logger.trace("Iniciando a consulta das regras de negócio da aplicação");
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(query.toString());
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				Regra regra = new Regra();
				regra.setDuracaoEmprestimo(rs.getInt(1));
				regra.setDuracaoRenovacao(rs.getInt(2));
				regra.setLimiteEmprestimosPorPessoa(rs.getInt(3));
				regra.setLimiteRenovacoes(rs.getInt(4));
				regra.setIntervaloEntreEmprestimos(rs.getInt(5));

				return regra;
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível carregar as regras de negócio", ex);
		}

		return null;
	}

	public int atualizarRegrasNegocio(List<Integer> regras) {
		logger.trace("Iniciando atualização das regras de negócio");

		StringBuilder query = new StringBuilder();
		query.append("update configuracao set duracao_emprestimo = ?, ");
		query.append("duracao_renovacao = ?, limite_emprestimo = ?, ");
		query.append("limite_renovacao = ?, intervalo_emprestimo = ? ");
		query.append("where idconf = 1");

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, regras.get(0));
			ps.setInt(2, regras.get(1));
			ps.setInt(3, regras.get(2));
			ps.setInt(4, regras.get(3));
			ps.setInt(5, regras.get(4));

			ps.executeUpdate();
			return 0;
		} catch (SQLException ex) {
			logger.fatal("Erro ao atualizar as regras de negócio.", ex);
		}
		return 1;
	}
}
