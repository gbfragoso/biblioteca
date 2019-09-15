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
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Usuario;
import org.casadeguara.listas.DataSourceProvider;
import org.casadeguara.utilitarios.Criptografia;
import javafx.collections.ObservableList;

public class UsuarioModel{
    
    private static final Logger logger = LogManager.getLogger(UsuarioModel.class);
    private DataSourceProvider dataSource;
    
    public UsuarioModel(DataSourceProvider dataSource) {
        this.dataSource = dataSource;
    }
    
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

    public Usuario consultar(String nome) {
        logger.trace("Iniciando a consulta do usuário " + nome);
        
        StringBuilder query = new StringBuilder();
        query.append("select idusuario, nome, tipo, status from usuario ");
        query.append("where nome like ?");

        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     Usuario usuario = new Usuario(rs.getInt(1), rs.getString(2));
                     usuario.setTipo(rs.getString(3));
                     usuario.setStatus(rs.getBoolean(4));
                     return usuario;
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Não foi possível consultar as informações do usuário", ex);
        }
        return null;
    }
    
    public List<Integer> consultarAcessosUsuario(int idusuario) {
        String query = "select acesso from usuario_has_acesso where usuario = ?";
        List<Integer> acessos = new ArrayList<>();

        logger.trace("Iniciando a consulta de acessos do usuário com id: " + idusuario);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
            
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
    
    public ObservableList<String> getListaUsuarios() {
        return dataSource.getListaUsuarios();
    }
    
    public void atualizarListaUsuarios() {
        dataSource.atualizarListaUsuarios();
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
