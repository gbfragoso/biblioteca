package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;

/**
 * Esta classe contém metodos que retornam o ID com base em uma coluna,
 * normalmente o nome.
 * 
 * @author Gustavo
 */
public class IdentificadorModel {

	private static final Logger logger = LogManager.getLogger(IdentificadorModel.class);

	private int consultar(String query, String parametro) {
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, parametro);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar o id", ex);
		}
		return 0;
	}

	public int idAutor(String nome) {
		logger.trace("Iniciando a consulta do id do autor: " + nome);
		return consultar("select idautor from autor where nome like ?", nome);
	}

	public int idEditora(String nome) {
		logger.trace("Iniciando consulta do id da editora:" + nome);
		return consultar("select ideditora from editora where nome like ?", nome);
	}

	public int idLeitor(String nome) {
		logger.trace("Iniciando consulta do id do leitor:" + nome);
		return consultar("select idleitor from leitor where nome like ?", nome);
	}

	public int idLivro(String titulo) {
		logger.trace("Iniciando consulta do id do livro:" + titulo);
		return consultar("select idlivro from livro where titulo like ?", titulo);
	}

	public int idPalavraChave(String nome) {
		logger.trace("Iniciando a consulta da palavra-chave:", nome);
		return consultar("select idkeyword from keyword where chave like ?", nome);
	}
}
