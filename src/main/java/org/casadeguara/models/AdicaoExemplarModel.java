package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.movimentacao.Acervo;

/**
 * Adiciona um exemplar de acordo com as regras de negócio vigentes.
 * 
 * @author Gustavo
 */
public class AdicaoExemplarModel {

	private int intervaloEntreEmprestimos;

	public AdicaoExemplarModel(int intervaloEntreEmprestimos) {
		this.intervaloEntreEmprestimos = intervaloEntreEmprestimos;
	}

	public boolean validar(Acervo exemplar, Leitor leitor) {
		return obedeceIntervaloEmprestimos(exemplar, leitor);
	}

	private boolean obedeceIntervaloEmprestimos(Acervo exemplar, Leitor leitor) {
		StringBuilder query = new StringBuilder();
		query.append("select DATE_PART('day', current_timestamp - data_devolvido) from emprestimo ");
		query.append("where leitor = ? and exemplar = ? ");
		query.append("order by idmov desc");

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, leitor.getId());
			ps.setInt(2, exemplar.getId());

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int intervalo = intervaloEntreEmprestimos - rs.getInt(1);
					if (intervalo > 0) {
						new Alerta().informacao(
								"Esse leitor só poderá pegar este item novamente daqui à " + intervalo + " dia(s).");
						return false;
					}
				}
			}
		} catch (SQLException e) {
			new Alerta().erro("Não foi possível verificar se o empréstimo obdece a restrição");
		}
		return true;
	}
}