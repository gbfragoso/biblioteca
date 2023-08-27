package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.consultas.ConsultaExemplar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe oferece consultas prontas para o acervo da biblioteca, separadas
 * por categoria.
 * 
 * @author Gustavo
 */
public class ConsultaExemplarModel {

	public ObservableList<ConsultaExemplar> porAutor(String autor) {

		StringBuilder query = new StringBuilder();
		query.append("select b.tombo, b.titulo, a.numero, a.status from exemplar a ");
		query.append("inner join livro b on (a.livro = b.idlivro) ");
		query.append("inner join autor_has_livro c on (a.livro = c.livro) ");
		query.append("inner join autor d on (c.autor = d.idautor) ");
		query.append("where unaccent(d.nome) like unaccent(?) and a.status != 'Deletado' ");
		query.append("order by b.tombo::int, a.numero");

		return buscar(query.toString(), autor.toUpperCase());
	}

	public ObservableList<ConsultaExemplar> porEditora(String editora) {

		StringBuilder query = new StringBuilder();
		query.append("select b.tombo, b.titulo, a.numero, a.status from exemplar a ");
		query.append("inner join livro b on (a.livro = b.idlivro) ");
		query.append("inner join editora c on (b.editora = ideditora) ");
		query.append("where unaccent(c.nome) like unaccent(?) and a.status != 'Deletado' ");
		query.append("order by b.tombo::int, a.numero");

		return buscar(query.toString(), editora.toUpperCase());
	}

	public ObservableList<ConsultaExemplar> porPalavraChave(String palavra) {

		StringBuilder query = new StringBuilder();
		query.append("select b.tombo, b.titulo, a.numero, a.status from exemplar a ");
		query.append("inner join livro b on (a.livro = b.idlivro) ");
		query.append("inner join livro_has_keyword c on (a.livro = c.livro) ");
		query.append("inner join keyword d on (c.keyword = d.idkeyword) ");
		query.append("where unaccent(d.chave) like unaccent(?) and a.status != 'Deletado' ");
		query.append("order by b.tombo::int, a.numero");

		return buscar(query.toString(), palavra.toUpperCase());
	}

	public ObservableList<ConsultaExemplar> porTitulo(String titulo) {

		StringBuilder query = new StringBuilder();
		query.append("select c.tombo, c.titulo, a.numero, a.status from exemplar a ");
		query.append("inner join livro c on (a.livro = c.idlivro) ");
		query.append("where unaccent(titulo) like unaccent(?) and a.status != 'Deletado' ");
		query.append("order by c.tombo::int, a.numero");
		
		return buscar(query.toString(), titulo.toUpperCase());
	}

	public ObservableList<ConsultaExemplar> porTombo(String tombo) {
		StringBuilder query = new StringBuilder();
		query.append("select c.tombo, c.titulo, a.numero, a.status from exemplar a ");
		query.append("inner join livro c on (a.livro = c.idlivro) ");
		query.append("where c.tombo = ? and a.status != 'Deletado' ");
		query.append("order by c.tombo::int, a.numero");

		return buscar(query.toString(), tombo);
	}

	private ObservableList<ConsultaExemplar> buscar(String query, String param) {
		ObservableList<ConsultaExemplar> resultados = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, param);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					ConsultaExemplar r = new ConsultaExemplar(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4));
					resultados.add(r);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível concluir a consulta");
		}
		return resultados;
	}
}