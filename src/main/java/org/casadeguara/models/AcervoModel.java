package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Acervo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AcervoModel implements GenericModel<Acervo> {

	@Override
	public int atualizar(Acervo t) {
		return 0;
	}

	@Override
	public int cadastrar(Acervo t) {
		return 0;
	}

	@Override
	public ObservableList<Acervo> consultar(String column, int resultados) {
		StringBuilder query = new StringBuilder();
		query.append("select idexemplar, numero, tombo, titulo, status from acervo ");
		query.append("where tombo || ' - ' || unaccent(titulo) || ' - EX: ' || numero like unaccent(?) limit ? ");

		ObservableList<Acervo> lista = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setString(1, "%" + column.toUpperCase() + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt(1);
					int numero = rs.getInt(2);
					int tombo = rs.getInt(3);
					String titulo = rs.getString(4);
					String status = rs.getString(5);

					Acervo exemplar = new Acervo(id, numero, tombo, titulo);
					exemplar.setStatus(status);

					lista.add(exemplar);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar a lista de exemplares");
		}
		return lista;
	}

}
