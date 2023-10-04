package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.consultas.ConsultaSerie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe oferece consultas prontas para o acervo da biblioteca, separadas
 * por categoria.
 * 
 * @author Gustavo
 */
public class ConsultaSerieModel {

	public ObservableList<ConsultaSerie> pesquisar(String pesquisa) {

		StringBuilder query = new StringBuilder();
		query.append("select c.nome, a.tombo, a.titulo, a.ordem  from livro a ");
		query.append("inner join serie c on (a.serie = c.idserie) ");
		query.append("where unaccent(c.nome) like unaccent(?) ");
		query.append(
				"or c.idserie = (select serie from livro d where (d.tombo like (?) or unaccent(d.titulo) like unaccent(?)) ");
		query.append("and serie is not null limit 1) order by a.ordem");

		ObservableList<ConsultaSerie> resultados = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, pesquisa.toUpperCase());
			ps.setString(2, pesquisa.toUpperCase());
			ps.setString(3, pesquisa.toUpperCase());

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					ConsultaSerie r = new ConsultaSerie(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getInt(4));
					resultados.add(r);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível concluir a consulta");
		}

		return resultados;
	}
}