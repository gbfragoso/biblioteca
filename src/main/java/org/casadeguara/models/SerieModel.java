package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Serie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe gerencia o relacionamento com a tabela Editora no banco de dados.
 * 
 * @author Gustavo Fragoso
 */
public class SerieModel implements GenericModel<Serie> {

	@Override
	public int atualizar(Serie serie) {
		String nome = serie.getNome();
		String query = "update serie set nome = ? where idserie = ?";

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, nome);
			ps.setInt(2, serie.getId());
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar a série");
		}
		return 1;
	}

	@Override
	public int cadastrar(Serie serie) {
		String nome = serie.getNome();
		String query = "insert into serie (nome, data_cadastro) values (?, current_date)";

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, nome);
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível cadastrar a série");
		}
		return 1;
	}

	@Override
	public ObservableList<Serie> consultar(String nome, int resultados) {
		String query = "select idserie, nome from serie where unaccent(nome) like unaccent(?) limit ?";
		ObservableList<Serie> series = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, "%" + nome + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					series.add(new Serie(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar a série");
		}
		return series;
	}
}
