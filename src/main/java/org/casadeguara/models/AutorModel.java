package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Autor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe gerencia o relacionamento com a tabela Autor no banco de dados.
 * 
 * @author Gustavo Fragoso
 */
public class AutorModel implements GenericModel<Autor> {

	@Override
	public int atualizar(Autor autor) {
		String nome = autor.getNome();
		String query = "update autor set nome = ? where idautor = ?";

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, nome);
			ps.setInt(2, autor.getId());
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar o autor");
		}
		return 1;
	}

	@Override
	public int cadastrar(Autor novoAutor) {
		String nome = novoAutor.getNome();
		String query = "insert into autor (nome, data_cadastro) values (?, current_date)";

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, nome);
			ps.executeUpdate();
			
			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível cadastrar o autor");
		}
		return 1;
	}

	@Override
	public ObservableList<Autor> consultar(String nome, int resultados) {
		String query = "select idautor, nome from autor where unaccent(nome) like unaccent(?) limit ?";
		ObservableList<Autor> autores = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, "%" + nome + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					autores.add(new Autor(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar o autor");
		}
		return autores;
	}
}