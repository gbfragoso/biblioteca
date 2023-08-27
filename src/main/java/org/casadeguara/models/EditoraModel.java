package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Editora;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe gerencia o relacionamento com a tabela Editora no banco de dados.
 * 
 * @author Gustavo Fragoso
 */
public class EditoraModel implements GenericModel<Editora> {

	@Override
	public int atualizar(Editora editora) {
		String nome = editora.getNome();
		String query = "update editora set nome = ? where ideditora = ?";

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, nome);
			ps.setInt(2, editora.getId());
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar a editora");
		}
		return 1;
	}

	@Override
	public int cadastrar(Editora editora) {
		String nome = editora.getNome();
		String query = "insert into editora (nome, data_cadastro) values (?, current_date)";

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, nome);
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível cadastrar a editora");
		}
		return 1;
	}

	@Override
	public ObservableList<Editora> consultar(String nome, int resultados) {
		String query = "select ideditora, nome from editora where unaccent(nome) like unaccent(?) limit ?";
		ObservableList<Editora> autores = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, "%" + nome + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					autores.add(new Editora(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar a editora");
		}
		return autores;
	}
}
