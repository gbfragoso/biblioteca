package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger(EditoraModel.class);

	@Override
	public int atualizar(Editora editora) {
		String nome = editora.getNome();
		String query = "update editora set nome = ? where ideditora = ?";

		logger.trace("Iniciando a atualização da editora:" + nome);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, nome);
			ps.setInt(2, editora.getId());
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar a editora", ex);
		}
		return 1;
	}

	@Override
	public int cadastrar(Editora editora) {
		String nome = editora.getNome();
		String query = "insert into editora (nome, data_cadastro) values (?, current_date)";

		logger.trace("Inicializando o cadastro da editora: " + nome);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, nome);
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível cadastrar a editora", ex);
		}
		return 1;
	}

	@Override
	public ObservableList<Editora> consultar(String nome, int resultados) {
		String query = "select ideditora, nome from editora where unaccent(nome) like unaccent(?) limit ?";
		ObservableList<Editora> autores = FXCollections.observableArrayList();

		logger.trace("Iniciando a consulta da editora: " + nome);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, "%" + nome + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					autores.add(new Editora(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar a editora", ex);
		}
		return autores;
	}
}
