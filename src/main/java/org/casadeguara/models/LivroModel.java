package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Autor;
import org.casadeguara.entidades.Editora;
import org.casadeguara.entidades.Exemplar;
import org.casadeguara.entidades.Livro;
import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.entidades.Serie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Objeto de acesso aos dados relacionados à classe Livro.
 * 
 * @author Gustavo
 */
public class LivroModel implements GenericModel<Livro> {

	public int atualizar(List<Exemplar> exemplares) {
		List<Exemplar> listaFiltrada = exemplares.stream()
				.filter(exemplar -> !exemplar.getStatus().equals("Emprestado") && exemplar.getId() > 0)
				.collect(Collectors.toList());

		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement("update exemplar set status = ? where idexemplar = ?")) {

			for (Exemplar e : listaFiltrada) {
				ps.setString(1, e.getStatus());
				ps.setInt(2, e.getId());
				ps.addBatch();
			}
			ps.executeBatch();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar o status dos exemplares");
		}
		return 1;
	}

	public int atualizar(Livro livro) {
		String titulo = livro.getTitulo();

		StringBuilder query = new StringBuilder();
		if (livro.getSerie() != null && livro.getOrdemColecao() != null) {
			query.append("update livro set titulo = ?, tombo = ?, editora = ?, serie = ?, ordem = ? where idlivro = ?");
		} else {
			query.append("update livro set titulo = ?, tombo = ?, editora = ? where idlivro = ?");
		}

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setString(1, titulo);
			ps.setString(2, livro.getTombo());
			ps.setInt(3, livro.getEditora().getId());
			if (livro.getSerie() == null || livro.getOrdemColecao() == null) {
				ps.setInt(4, livro.getId());
			} else {
				ps.setInt(4, livro.getSerie().getId());
				ps.setInt(5, livro.getOrdemColecao());
				ps.setInt(6, livro.getId());
			}
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar o livro");
		}
		return 1;
	}

	public int cadastrar(Livro livro) {
		String titulo = livro.getTitulo();
		String query = "";
		if (livro.getSerie() != null && livro.getOrdemColecao() != null) {
			query = "insert into livro (titulo, tombo, editora, serie, ordem, data_cadastro) values (?,?,?,?,?,current_date)";
		} else {
			query = "insert into livro (titulo, tombo, editora, data_cadastro) values (?,?,?,current_date)";
		}

		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, titulo);
			ps.setString(2, livro.getTombo());
			ps.setInt(3, livro.getEditora().getId());
			if (livro.getSerie() != null || livro.getOrdemColecao() != null) {
				ps.setInt(4, livro.getSerie().getId());
				ps.setInt(5, livro.getOrdemColecao());
			}
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível cadastrar o livro");
		}
		return 0;
	}

	public ObservableList<Livro> consultar(String titulo, int resultados) {
		StringBuilder query = new StringBuilder();
		query.append("select idlivro, tombo, titulo, editora.ideditora, editora.nome, serie.idserie, serie.nome, ordem from livro ");
		query.append("left join editora on (editora = ideditora) ");
		query.append("left join serie on (serie = idserie) ");
		query.append("where tombo || ' - ' || unaccent(titulo) like unaccent(?) limit ?");
		ObservableList<Livro> livros = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, "%" + titulo + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Editora editora = new Editora(rs.getInt(4), rs.getString(5));
					Serie serie = new Serie(rs.getInt(6), rs.getString(7));

					Livro livro = new Livro(rs.getInt(1), rs.getString(2), rs.getString(3));
					livro.setEditora(editora);
					livro.setSerie(serie);
					livro.setOrdemColecao(rs.getInt(8));
					livros.add(livro);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar o livro");
		}
		return livros;
	}

	public String consultarUltimoTombo() {
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement("select max(cast(tombo as bigint)) from livro");
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar o último tombo");
		}
		return "";
	}

	public ObservableList<Autor> consultarAutores(int idlivro) {
		StringBuilder consultaAutores = new StringBuilder();
		consultaAutores.append("select autor, autor.nome from autor_has_livro ");
		consultaAutores.append("inner join autor on (autor = idautor) ");
		consultaAutores.append("where livro = ?");

		ObservableList<Autor> listaAutores = FXCollections.observableArrayList();

		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(consultaAutores.toString())) {

			ps.setInt(1, idlivro);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					listaAutores.add(new Autor(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar os autores");
		}
		return listaAutores;
	}

	public ObservableList<Exemplar> consultarExemplares(int idlivro) {
		StringBuilder consultaExemplares = new StringBuilder();
		consultaExemplares.append("select idexemplar, numero, status from exemplar ");
		consultaExemplares.append("where livro = ? ");
		consultaExemplares.append("order by numero");

		ObservableList<Exemplar> listaExemplares = FXCollections.observableArrayList();
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(consultaExemplares.toString())) {

			ps.setInt(1, idlivro);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Exemplar e = new Exemplar(rs.getInt(1), rs.getInt(2), rs.getString(3));

					listaExemplares.add(e);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar os exemplares");
		}
		return listaExemplares;
	}

	public ObservableList<PalavraChave> consultarPalavrasChave(int idlivro) {
		StringBuilder query = new StringBuilder();
		query.append("select keyword, chave from livro_has_keyword ");
		query.append("inner join keyword on (keyword = idkeyword) ");
		query.append("where livro = ?");

		ObservableList<PalavraChave> listaPalavras = FXCollections.observableArrayList();
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, idlivro);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					listaPalavras.add(new PalavraChave(rs.getInt(1), rs.getString(2)));
				}
			}

		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar as palavras-chave.");
		}
		return listaPalavras;
	}

	public int atualizarListaAutores(int idlivro, ObservableList<Autor> listaAutores) {
		Object[] ids = listaAutores.stream().map(Autor::getId).toArray();

		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_autores_livro(?,?)}")) {

			cs.setInt(1, idlivro);
			cs.setArray(2, con.createArrayOf("integer", ids));
			cs.execute();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar a lista de autores");
		}
		return 1;
	}

	public int atualizarListaExemplares(int idlivro, ObservableList<Exemplar> listaExemplares) {
		Object[] numerosExemplares = listaExemplares.stream().map(Exemplar::getNumero).toArray();

		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_exemplares_livro(?,?)}")) {

			cs.setInt(1, idlivro);
			cs.setArray(2, con.createArrayOf("integer", numerosExemplares));
			cs.execute();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar a lista de exemplares");
		}
		return 1;

	}

	public int atualizarListaPalavrasChave(int idlivro, ObservableList<PalavraChave> listaPalavrasChave) {
		Object[] ids = listaPalavrasChave.stream().map(PalavraChave::getId).toArray();

		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_keywords_livro(?,?)}")) {

			cs.setInt(1, idlivro);
			cs.setArray(2, con.createArrayOf("integer", ids));
			cs.execute();

			return 0;
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar a lista de palavras-chave");
		}
		return 1;
	}

	public boolean verificaTombo(String tombo) {
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement("select 1 from livro where tombo = ?")) {

			ps.setString(1, tombo);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível atualizar a lista de palavras-chave");
		}
		return false;
	}
}
