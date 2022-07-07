package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.PalavraChave;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe realiza as operações de relacionadas a tabela Keyword no banco de
 * dados.
 * 
 * @author Gustavo
 * @since 3.0
 */
public class PalavraChaveModel implements GenericModel<PalavraChave> {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public int atualizar(PalavraChave chave) {
		String query = "update keyword set chave = ? where idkeyword = ?";
		String assunto = chave.getAssunto();

		logger.trace("Iniciando a atualização da palavra-chave: " + assunto);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, assunto);
			ps.setInt(2, chave.getId());
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar a palavra-chave");
		}
		return 1;
	}

	@Override
	public int cadastrar(PalavraChave palavraChave) {
		String query = "insert into keyword (chave) values (?)";
		String assunto = palavraChave.getAssunto();

		logger.trace("Iniciando o cadastro da palavra-chave:", assunto);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, assunto);
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível cadastrar a palavra-chave", ex);
		}
		return 1;
	}

	@Override
	public ObservableList<PalavraChave> consultar(String chave, int resultados) {
		String query = "select idkeyword, chave from keyword where unaccent(chave) like unaccent(?) limit ?";
		ObservableList<PalavraChave> palavras = FXCollections.observableArrayList();

		logger.trace("Iniciando a consulta da palavra-chave: " + chave);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, "%" + chave + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					palavras.add(new PalavraChave(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar a palavra-chave", ex);
		}
		return palavras;
	}
}
