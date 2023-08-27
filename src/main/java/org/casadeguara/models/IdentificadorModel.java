package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;

/**
 * Esta classe contém metodos que retornam o ID com base em uma coluna,
 * normalmente o nome.
 * 
 * @author Gustavo
 */
public class IdentificadorModel {

	private int consultar(String query, String parametro) {
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, parametro);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar o id");
		}
		return 0;
	}

	public int idAutor(String nome) {
		return consultar("select idautor from autor where nome like ?", nome);
	}

	public int idEditora(String nome) {
		return consultar("select ideditora from editora where nome like ?", nome);
	}

	public int idLeitor(String nome) {
		return consultar("select idleitor from leitor where nome like ?", nome);
	}

	public int idLivro(String titulo) {
		return consultar("select idlivro from livro where titulo like ?", titulo);
	}

	public int idPalavraChave(String nome) {
		return consultar("select idkeyword from keyword where chave like ?", nome);
	}
}
