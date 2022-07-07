package org.casadeguara.models;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.utilitarios.Criptografia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioModel implements GenericModel<Usuario> {

	private static final Logger logger = LogManager.getLogger(UsuarioModel.class);

	@Override
	public int atualizar(Usuario usuario) {
		String nome = usuario.getNome();

		logger.trace("Iniciando a atualização do usuário: " + nome);
		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call atualizar_usuario(?,?,?,?,?)}")) {

			cs.setInt(1, usuario.getId());
			cs.setString(2, nome);
			cs.setString(3, usuario.getTipo());
			cs.setBoolean(4, usuario.getStatus());
			cs.setArray(5, con.createArrayOf("integer", usuario.getListaAcessos().toArray()));
			cs.executeQuery();

			return 0;
		} catch (SQLException ex) {
			logger.fatal("Não foi possível atualizar o usuário", ex);
		}
		return 1;
	}

	@Override
	public int cadastrar(Usuario novoUsuario) {
		String nome = novoUsuario.getNome();

		logger.trace("Iniciando o cadastro do usuário: " + nome);
		try (Connection con = Conexao.abrir();
				CallableStatement cs = con.prepareCall("{call cadastrar_usuario(?,?,?,?,?)}")) {

			cs.setString(1, nome);
			cs.setString(2, novoUsuario.getLogin());
			cs.setString(3, new Criptografia().aplicar("SHA-512", novoUsuario.getSenha()));
			cs.setString(4, novoUsuario.getTipo());
			cs.setArray(5, con.createArrayOf("integer", novoUsuario.getListaAcessos().toArray()));
			cs.executeQuery();

			return 0;
		} catch (SQLException | NoSuchAlgorithmException ex) {
			logger.fatal("Não foi possível cadastrar o usuário", ex);
		}
		return 1;
	}

	@Override
	public ObservableList<Usuario> consultar(String nome, int resultados) {
		logger.trace("Iniciando a consulta do usuário " + nome);

		Usuario usuario = Main.getUsuario();
		String tipo = usuario.getTipo();
		int id = usuario.getId();

		List<String> tiposPermitidos = new ArrayList<>();
		if (tipo.equals("Root")) {
			tiposPermitidos.add("Admin");
		}
		tiposPermitidos.add("Comum");

		StringBuilder query = new StringBuilder();
		query.append("select idusuario, nome, tipo, status from usuario ");
		query.append("where unaccent(upper(nome)) like unaccent(?) ");
		query.append("and idusuario not in (?,?) and tipo = ANY(?)");

		ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setString(1, "%" + nome + "%");
			ps.setInt(2, 1); // Root
			ps.setInt(3, id);
			ps.setArray(4, con.createArrayOf("varchar", tiposPermitidos.toArray(new String[tiposPermitidos.size()])));

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Usuario u = new Usuario(rs.getInt(1), rs.getString(2));
					u.setTipo(rs.getString(3));
					u.setStatus(rs.getBoolean(4));
					usuarios.add(u);
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar as informações do usuário", ex);
		}
		return usuarios;
	}

	public List<Integer> consultarAcessosUsuario(int idusuario) {
		String query = "select acesso from usuario_has_acesso where usuario = ?";
		List<Integer> acessos = new ArrayList<>();

		logger.trace("Iniciando a consulta de acessos do usuário com id: " + idusuario);
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, idusuario);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					acessos.add(rs.getInt(1) - 1);
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Não foi possível consultar os acessos do usuário", ex);
		}
		return acessos;
	}

	public int trocarSenha(String novaSenha, int idusuario) {
		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement("update usuario set senha = ? where idusuario = ?")) {

			ps.setString(1, new Criptografia().aplicar("SHA-512", novaSenha));
			ps.setInt(2, idusuario);
			ps.executeUpdate();

			return 0;
		} catch (SQLException | NoSuchAlgorithmException ex) {
			logger.fatal("Não foi possível trocar a senha do usuário");
		}
		return 1;
	}
}
