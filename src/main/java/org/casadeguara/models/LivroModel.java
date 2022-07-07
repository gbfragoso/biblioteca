package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Autor;
import org.casadeguara.entidades.Editora;
import org.casadeguara.entidades.Exemplar;
import org.casadeguara.entidades.Livro;
import org.casadeguara.entidades.PalavraChave;

/**
 * Objeto de acesso aos dados relacionados à classe Livro.
 * 
 * @author Gustavo
 */
public class LivroModel implements GenericModel<Livro> {

	private static final Logger logger = LogManager.getLogger(LivroModel.class);

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
			logger.fatal("Não foi possível atualizar o status dos exemplares", ex);
		}
		return 1;
	}

	public int atualizar(Livro livro) {
		String titulo = livro.getTitulo();

		StringBuilder query = new StringBuilder();
		query.append("update livro set titulo = ?, tombo = ?, editora = ? where idlivro = ?");

		logger.trace("Iniciando a atualização do livro: " + titulo);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setString(1, titulo);
			ps.setString(2, livro.getTombo());
			ps.setInt(3, livro.getEditora().getId());
			ps.setInt(4, livro.getId());
			ps.executeUpdate();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar o livro " + titulo, ex);
		}
		return 1;
	}

	public int cadastrar(Livro livro) {
		String titulo = livro.getTitulo();
		String query = "insert into livro (titulo, tombo, editora, data_cadastro) values (?,?,?,current_date)";

		logger.trace("Iniciando o cadastro do livro: " + titulo);
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, titulo);
			ps.setString(2, livro.getTombo());
			ps.setInt(3, livro.getEditora().getId());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível cadastrar o livro: " + titulo, ex);
		}
		return 0;
	}

	public ObservableList<Livro> consultar(String titulo, int resultados) {
		StringBuilder query = new StringBuilder();
		query.append("select idlivro, tombo, titulo, editora.ideditora, editora.nome from livro ");
		query.append("left join editora on (editora = ideditora) ");
		query.append("where tombo || ' - ' || unaccent(titulo) like unaccent(?) limit ?");
		ObservableList<Livro> livros = FXCollections.observableArrayList();

		logger.trace("Iniciando a consulta do livro: " + titulo);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setString(1, "%" + titulo + "%");
			ps.setInt(2, resultados);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Editora editora = new Editora(rs.getInt(4), rs.getString(5));

					Livro livro = new Livro(rs.getInt(1), rs.getString(2), rs.getString(3));
					livro.setEditora(editora);
					livros.add(livro);
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar o livro", ex);
		}
		return livros;
	}

	public String consultarUltimoTombo() {
		logger.trace("Consultando último tombo disponível");

		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement("select max(cast(tombo as bigint)) from livro");
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar o último tombo", ex);
		}
		return "";
	}

	public ObservableList<Autor> consultarAutores(int idlivro) {
		StringBuilder consultaAutores = new StringBuilder();
		consultaAutores.append("select autor, autor.nome from autor_has_livro ");
		consultaAutores.append("inner join autor on (autor = idautor) ");
		consultaAutores.append("where livro = ?");

		ObservableList<Autor> listaAutores = FXCollections.observableArrayList();

		logger.trace("Consultando os autores do livro com id: " + idlivro);
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(consultaAutores.toString())) {

			ps.setInt(1, idlivro);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					listaAutores.add(new Autor(rs.getInt(1), rs.getString(2)));
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar os autores", ex);
		}
		return listaAutores;
	}

	public ObservableList<Exemplar> consultarExemplares(int idlivro) {
		StringBuilder consultaExemplares = new StringBuilder();
		consultaExemplares.append("select idexemplar, numero, status from exemplar ");
		consultaExemplares.append("where livro = ? ");
		consultaExemplares.append("order by numero");

		ObservableList<Exemplar> listaExemplares = FXCollections.observableArrayList();
		logger.trace("Iniciando a consulta de exemplares do livro com id: " + idlivro);
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
			logger.fatal("Não foi possível consultar os exemplares", ex);
		}
		return listaExemplares;
	}

	public ObservableList<PalavraChave> consultarPalavrasChave(int idlivro) {
		StringBuilder query = new StringBuilder();
		query.append("select keyword, chave from livro_has_keyword ");
		query.append("inner join keyword on (keyword = idkeyword) ");
		query.append("where livro = ?");

		ObservableList<PalavraChave> listaPalavras = FXCollections.observableArrayList();
		logger.trace("Iniciando a consulta de palavras-chave do livro com id: " + idlivro);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, idlivro);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					listaPalavras.add(new PalavraChave(rs.getInt(1), rs.getString(2)));
				}
			}

		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar as palavras-chave.", ex);
		}
		return listaPalavras;
	}

	public int atualizarListaAutores(int idlivro, ObservableList<Autor> listaAutores) {
		Object[] ids = listaAutores.stream().map(Autor::getId).toArray();

		logger.trace("Iniciando a atualização da lista de autores do livro com id:" + idlivro);
		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_autores_livro(?,?)}")) {

			cs.setInt(1, idlivro);
			cs.setArray(2, con.createArrayOf("integer", ids));
			cs.execute();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar a lista de autores", ex);
		}
		return 1;
	}

	public int atualizarListaExemplares(int idlivro, ObservableList<Exemplar> listaExemplares) {
		Object[] numerosExemplares = listaExemplares.stream().map(Exemplar::getNumero).toArray();

		logger.trace("Iniciando a atualização da lista de exemplares do livro com id:" + idlivro);
		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_exemplares_livro(?,?)}")) {

			cs.setInt(1, idlivro);
			cs.setArray(2, con.createArrayOf("integer", numerosExemplares));
			cs.execute();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar a lista de exemplares", ex);
		}
		return 1;

	}

	public int atualizarListaPalavrasChave(int idlivro, ObservableList<PalavraChave> listaPalavrasChave) {
		Object[] ids = listaPalavrasChave.stream().map(PalavraChave::getId).toArray();

		logger.trace("Iniciando a atualização da lista de palavras-chave do livro com id:" + idlivro);
		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_keywords_livro(?,?)}")) {

			cs.setInt(1, idlivro);
			cs.setArray(2, con.createArrayOf("integer", ids));
			cs.execute();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar a lista de palavras-chave", ex);
		}
		return 1;
	}

	public boolean verificaTombo(String tombo) {
		logger.trace("Verificando a existência do tombo:" + tombo);
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement("select 1 from livro where tombo = ?")) {

			ps.setString(1, tombo);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar a lista de palavras-chave", ex);
		}
		return false;
	}
}
