package org.casadeguara.models;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.application.Main;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.negocio.Cobranca;
import org.casadeguara.utilitarios.Criptografia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Responsável por transações relacionadas à parte administrativa.
 * @author Gustavo
 */
public class AdministracaoModel {
    
    private static final Logger logger = LogManager.getLogger(AdministracaoModel.class);
    
    private boolean usuarioPossuiPermissao() {
        return !Main.getUsuario().getTipo().equals("Comum");
    }
    
    public int recuperarEmprestimo(int idemp, int duracaoEmprestimo) {
        logger.trace("Iniciando recuperação do empréstimo com id: " + idemp);
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 CallableStatement call = con.prepareCall("{call recuperar_emprestimo(?, ?)}")) {

                call.setInt(1, idemp);
                call.setInt(2, duracaoEmprestimo);
                call.executeQuery();
                return 0;
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar recuperar um empréstimo da base de dados.", ex);
            }
        }
        return 1;
    }
     
    public int alterarChaveMestra(String novaChave) {
        logger.trace("Iniciando a alteração da chave mestra");
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement("update configuracao set chave = ? where idconf = 1")) {
                
                ps.setString(1, new Criptografia().aplicar("SHA-512", novaChave));
                ps.executeUpdate();
                return 0;
            } catch (SQLException | NoSuchAlgorithmException ex) {
                logger.fatal("Erro ao tentar alterar a chave mestra.", ex);
            }
        }
        return 1;
    }
    
    public int trocarExemplar(int idemp, int expnovo) {
        logger.trace("Iniciando a troca do exemplar do empréstimo com id: " + idemp);
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 CallableStatement call = con.prepareCall("{call trocar_exemplar(?,?)}")) {

                call.setInt(1, idemp);
                call.setInt(2, expnovo);
                call.executeQuery();
                return 0;
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar trocar o exemplar de um empréstimo", ex);
            }
        }
        return 1;
    }

	public String getTextoCobranca() {
		logger.trace("Iniciando consulta o texto de cobrança");
        
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement("select cobranca from configuracao where idconf = 1")) {
                
            	try (ResultSet rs = ps.executeQuery()) {;
	                if(rs.next()) {
	                	return rs.getString(1);
	                }
            	}
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar consultar o texto de cobrança.", ex);
            }
        }
        return "";
	}
	
	public int setTextoCobranca(String texto) {
		logger.trace("Iniciando a mudança do texto de cobrança");
		
        if (usuarioPossuiPermissao()) {
            try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement("update configuracao set cobranca = ? where idconf = 1")) {
                
            	ps.setString(1, texto);
            	ps.executeUpdate();
            	return 0;
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar mudar o texto de cobrança.", ex);
            }
        }
        return 1;
	}
	
	public ObservableList<Cobranca> getListaEmprestimosAtrasados() {
		logger.trace("Iniciando a consulta dos empréstimos em atraso para efetuar cobrança");
		
		StringBuilder query = new StringBuilder();
		query.append("select idemp, leitor, email, tombo, titulo, numero, data_emprestimo, data_devolucao, cobranca ");
		query.append("from vw_cobrancas");
		
		ObservableList<Cobranca> listaPossiveisCobrancas = FXCollections.observableArrayList();
        if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement(query.toString());
				 ResultSet rs = ps.executeQuery()) {
                
            	while(rs.next()) {
            		Cobranca cobranca = new Cobranca();
            		cobranca.setIdemprestimo(rs.getInt(1));
            		cobranca.setLeitor(rs.getString(2));
            		cobranca.setEmail(rs.getString(3));
            		cobranca.setTombo(rs.getString(4));
            		cobranca.setTitulo(rs.getString(5));
            		cobranca.setNumero(rs.getInt(6));
            		cobranca.setDataEmprestimo(rs.getDate(7));
            		cobranca.setDataDevolucao(rs.getDate(8));
            		cobranca.setCobranca(rs.getTimestamp(9));
            		
            		listaPossiveisCobrancas.add(cobranca);
            	}
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar mudar o texto de cobrança.", ex);
            }
        }
        return listaPossiveisCobrancas;
	}

	public void atualizarDataCobranca(Object[] ids) {
		logger.trace("Atualizando os empréstimos que foram cobrados");
		
		StringBuilder query = new StringBuilder();
		query.append("update emprestimo set cobranca = localtimestamp where idemp = ANY (?) ");
		
        if (usuarioPossuiPermissao()) {
			try (Connection con = Conexao.abrir();
                 PreparedStatement ps = con.prepareStatement(query.toString())) {
                
				ps.setArray(1, con.createArrayOf("integer", ids));
				ps.executeUpdate();
				
            } catch (SQLException ex) {
                logger.fatal("Erro ao tentar mudar o texto de cobrança.", ex);
            }
        }
	}
}
