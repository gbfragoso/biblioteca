package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Aviso;

/**
 * Objeto que contém as consultas relacionadas aos avisos.
 * 
 * @author Gustavo
 */
public class AvisoModel {

	public void atualizar(int id, String novoTexto) {
		StringBuilder query = new StringBuilder();
		query.append("update aviso set texto = ? where idaviso = ?");

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, novoTexto);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException ex) {
			new Alerta().erro("Ocorreu ao alterar o aviso.");
		}
	}

	public void cadastrar(String texto) {
		StringBuilder query = new StringBuilder();
		query.append("insert into aviso(texto) values (?);");

		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, texto);
			ps.executeUpdate();
		} catch (SQLException ex) {
			new Alerta().erro("Ocorreu ao adicionar o aviso.");
		}
	}

	public List<Aviso> consultar() {
		List<Aviso> avisos = new ArrayList<>();

		StringBuilder query = new StringBuilder();
		query.append("select idaviso, data_cadastro, texto from aviso order by idaviso desc limit 3;");

		try (Connection con = Conexao.abrir();
				PreparedStatement ps = con.prepareStatement(query.toString());
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				avisos.add(new Aviso(rs.getInt(1), rs.getDate(2).toLocalDate(), rs.getString(3)));
			}
		} catch (SQLException ex) {
			new Alerta().erro("Ocorreu ao consultar os avisos.");
		}
		return avisos;
	}
}
