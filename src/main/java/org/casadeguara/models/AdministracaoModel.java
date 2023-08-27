package org.casadeguara.models;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.application.App;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.negocio.Cobranca;
import org.casadeguara.utilitarios.Criptografia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Responsável por transações relacionadas à parte administrativa.
 * 
 * @author Gustavo
 */
public class AdministracaoModel {

	private boolean usuarioPossuiPermissao() {
		return !App.getUsuario().getTipo().equals("Comum");
	}

	public int recuperarEmprestimo(int idemp, int duracaoEmprestimo) {
		if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
					CallableStatement call = con.prepareCall("{call recuperar_emprestimo(?, ?)}")) {

				call.setInt(1, idemp);
				call.setInt(2, duracaoEmprestimo);
				call.executeQuery();
				return 0;
			} catch (SQLException ex) {
			}
		}
		return 1;
	}

	public int alterarChaveMestra(String novaChave) {
		if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
					PreparedStatement ps = con.prepareStatement("update configuracao set chave = ? where idconf = 1")) {

				ps.setString(1, new Criptografia().aplicar("SHA-512", novaChave));
				ps.executeUpdate();
				return 0;
			} catch (SQLException | NoSuchAlgorithmException ex) {
			}
		}
		return 1;
	}

	public int trocarExemplar(int idemp, int expnovo) {
		if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
					CallableStatement call = con.prepareCall("{call trocar_exemplar(?,?)}")) {

				call.setInt(1, idemp);
				call.setInt(2, expnovo);
				call.executeQuery();
				return 0;
			} catch (SQLException ex) {
			}
		}
		return 1;
	}

	public String getTextoCobranca() {
		if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
					PreparedStatement ps = con.prepareStatement("select cobranca from configuracao where idconf = 1")) {

				try (ResultSet rs = ps.executeQuery()) {
					;
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			} catch (SQLException ex) {
			}
		}
		return "";
	}

	public int setTextoCobranca(String texto) {
		if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
					PreparedStatement ps = con
							.prepareStatement("update configuracao set cobranca = ? where idconf = 1")) {

				ps.setString(1, texto);
				ps.executeUpdate();
				return 0;
			} catch (SQLException ex) {
				new Alerta().erro("Erro ao tentar mudar o texto de cobrança.");
			}
		}
		return 1;
	}

	public ObservableList<Cobranca> getListaEmprestimosAtrasados() {
		StringBuilder query = new StringBuilder();
		query.append("select leitor, email, livros from vw_cobrancas");

		ObservableList<Cobranca> listaPossiveisCobrancas = FXCollections.observableArrayList();
		if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
					PreparedStatement ps = con.prepareStatement(query.toString());
					ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					listaPossiveisCobrancas
							.add(new Cobranca(rs.getString(1), rs.getString(2), (String[]) rs.getArray(3).getArray()));
				}
			} catch (SQLException ex) {
				new Alerta().erro("Erro ao tentar mudar o texto de cobrança.");
			}
		}
		return listaPossiveisCobrancas;
	}
}
