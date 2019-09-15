package org.casadeguara.models;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.utilitarios.Criptografia;

/**
 * Esta classe é responsável pelas operações de autenticação de usuário e controle de acesso.
 * @author Gustavo
 */
public class LoginModel {
    
    private static final Logger logger = LogManager.getLogger();

    public Usuario autenticar(String login, String senha) {
        String query = "select idusuario, nome, tipo from usuario where login = ? and senha = ?";

        logger.trace("Iniciando a autenticação do usuário com login: " + login);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, login);
            ps.setString(2, new Criptografia().aplicar("SHA-512", senha));
            
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    Usuario operador = new Usuario(rs.getInt(1), rs.getString(2));
                    operador.setTipo(rs.getString(3));
                    return operador;
                }
            }
            
            logger.trace("Autenticação do usuário com login: " + login + " bem sucedida");
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.fatal("Ocorreu um erro durante o login.", e);
        }
        return null;
    }

    public boolean[] controleAcesso(int userCod) {
        logger.trace("Consultando o controle de acesso do usuário com id: " + userCod);
        boolean[] acesso = {false, false, false, false, false, false, false, false, false, false};
        String query = "select acesso from usuario_has_acesso where usuario = ?";
        
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, userCod);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    acesso[rs.getInt(1) - 1] = true;
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Ocorreu um erro durante o controle do acesso.", ex);
        }
        return acesso;
    }
}
