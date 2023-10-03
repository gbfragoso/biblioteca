package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.consultas.ConsultaEmprestimo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe oferece consultas prontas para o acervo da biblioteca, separadas
 * por categoria.
 * 
 * @author Gustavo
 */
public class ConsultaEmprestimoModel {

	public ObservableList<ConsultaEmprestimo> pesquisar(String pesquisa) {

		StringBuilder query = new StringBuilder();
		query.append(
				"select a.idemp, b.nome, d.titulo, c.numero, a.data_emprestimo, a.data_devolucao from emprestimo a ");
		query.append("inner join leitor b on (a.leitor = b.idleitor) ");
		query.append("inner join exemplar c on (a.exemplar = c.idexemplar) ");
		query.append("inner join livro d on (c.livro = d.idlivro) ");
		query.append(
				"where unaccent(b.nome) like unaccent(?) or unaccent(d.titulo) like unaccent(?) or d.tombo like (?) ");
		query.append("order by a.idemp");

		ObservableList<ConsultaEmprestimo> resultados = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, pesquisa.toUpperCase());
			ps.setString(2, pesquisa.toUpperCase());
			ps.setString(3, pesquisa.toUpperCase());

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					ConsultaEmprestimo r = new ConsultaEmprestimo(rs.getInt(1), rs.getString(2), rs.getString(3),
							rs.getInt(4), rs.getDate(5), rs.getDate(6));
					resultados.add(r);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível concluir a consulta");
		}

		return resultados;
	}
}