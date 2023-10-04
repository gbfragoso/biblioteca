package org.casadeguara.models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.application.App;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.movimentacao.Acervo;
import org.casadeguara.movimentacao.Emprestimo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta classe é responsável por emprestar exemplares de acordo com as regras de
 * negócio.
 * 
 * @author Gustavo
 */
public class EmprestimoModel {

	private int duracaoEmprestimo;
	private int limiteEmprestimosPorPessoa;

	public EmprestimoModel(int duracaoEmprestimo, int limiteEmprestimosPorPessoa) {
		this.duracaoEmprestimo = duracaoEmprestimo;
		this.limiteEmprestimosPorPessoa = limiteEmprestimosPorPessoa;
	}

	public ObservableList<Emprestimo> consultar(int idleitor) {
		StringBuilder query = new StringBuilder();
		query.append("select idemp, exemplar, titulo, numero, renovacoes, data_devolucao from emprestimo ");
		query.append("inner join exemplar on (idexemplar = exemplar) ");
		query.append("inner join livro on (exemplar.livro = idlivro) ");
		query.append("where leitor = ? and data_devolvido is null");

		ObservableList<Emprestimo> listaEmprestimos = FXCollections.observableArrayList();
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, idleitor);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Emprestimo e = new Emprestimo(rs.getInt(1), rs.getInt(2), idleitor, rs.getString(3), rs.getInt(4),
							rs.getInt(5), rs.getDate(6).toLocalDate());
					listaEmprestimos.add(e);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível consultar os empréstimos");
		}
		return listaEmprestimos;
	}

	public int emprestar(int idleitor, String nomeLeitor, List<Acervo> exemplares, int quantidadeItens) {
		if (validarEmprestimo(quantidadeItens)) {
			try (Connection con = Conexao.abrir();
					CallableStatement cs = con.prepareCall("{call emprestar(?,?,?,?)}")) {

				for (Acervo e : exemplares) {
					cs.setInt(1, idleitor);
					cs.setInt(2, e.getId());
					cs.setInt(3, App.getUsuario().getId());
					cs.setInt(4, duracaoEmprestimo);
					cs.addBatch();
				}
				cs.executeBatch();
				return 0;
			} catch (SQLException ex) {
				new Alerta().erro("Não foi possível emprestar os exemplares ao leitor");
			}
		}
		return 1;
	}

	public ObservableList<Acervo> gerarRecibo(int idleitor) {
		StringBuilder query = new StringBuilder();
		query.append("select exemplar, a.numero, b.tombo, b.titulo, data_devolucao from emprestimo ");
		query.append("inner join exemplar a on (idexemplar = exemplar) ");
		query.append("inner join livro b on (a.livro = idlivro) ");
		query.append("where leitor = ?");

		ObservableList<Acervo> exp = FXCollections.observableArrayList();
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, idleitor);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Acervo e = new Acervo(rs.getInt(1), Integer.parseInt(rs.getString(2)), rs.getInt(3),
							rs.getString(4), rs.getDate(5).toLocalDate());

					exp.add(e);
				}
			}
		} catch (SQLException ex) {
			new Alerta().erro("Não foi possível gerar o recibo de empréstimo");
		}

		return exp;
	}

	private boolean validarEmprestimo(int quantidadeItens) {
		return obedeceLimiteEmprestimos(quantidadeItens) || permissaoParaProsseguir();
	}

	private boolean obedeceLimiteEmprestimos(int quantidadeItens) {
		return quantidadeItens <= limiteEmprestimosPorPessoa;
	}

	private boolean permissaoParaProsseguir() {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("O limite de empréstimos é de ");
		mensagem.append(limiteEmprestimosPorPessoa);
		mensagem.append(" exemplar(es) por vez.");

		if (App.getUsuario().getTipo().equals("Comum")) {
			mensagem.append("\nPara prosseguir com esta operação digite a chave-mestra:");
			return new Alerta().autorizacao(mensagem.toString());
		} else {
			return true;
		}
	}

}
