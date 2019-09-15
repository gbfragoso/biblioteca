package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.builder.LeitorBuilder;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.listas.DataSourceProvider;
import javafx.collections.ObservableList;

/**
 * Esta classe gerencia o relacionamento com a tabela Leitor no banco de dados.
 * @author Gustavo
 */
public class LeitorModel{
    
    private static final Logger logger = LogManager.getLogger(LeitorModel.class);
    private DataSourceProvider dataSource;
    
    public LeitorModel(DataSourceProvider dataSource) {
        this.dataSource = dataSource;
    }
    
    private void incluirParametros(PreparedStatement ps, Leitor leitor) throws SQLException {
        ps.setString(1, leitor.getNome());
        ps.setString(2, leitor.getEmail());
        ps.setString(3, leitor.getTelefone1());
        ps.setString(4, leitor.getTelefone2());
        ps.setString(5, leitor.getLogradouro());
        ps.setString(6, leitor.getBairro());
        ps.setString(7, leitor.getComplemento());
        ps.setString(8, leitor.getCep());
        ps.setString(9, leitor.getCidade());
        ps.setString(10, leitor.getSexo());
        ps.setString(11, leitor.getRg());
        ps.setString(12, leitor.getCpf());
        ps.setBoolean(13, leitor.isAtivo());
        ps.setBoolean(14, leitor.isTrab());
        ps.setBoolean(15, leitor.isIncompleto());
    }

    public int atualizar(Leitor leitor) {
        String nome = leitor.getNome();
        
        StringBuilder query = new StringBuilder();
        query.append("update leitor set ");
        query.append("nome = ?, email = ?, telefone = ?, ");
        query.append("celular = ?, logradouro = ?, bairro = ?, ");
        query.append("complemento = ?, cep = ?, cidade = ?, ");
        query.append("sexo = ?, rg = ?, cpf = ?, ");
        query.append("status = ?, trab = ?, incompleto = ? ");
        query.append("where idleitor = ?");
        
        logger.trace("Iniciando a atualização do leitor: " + nome);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            incluirParametros(ps, leitor);
            ps.setInt(16, leitor.getId());
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível atualizar o leitor", ex);
        }
        return 1;
    }

    public int cadastrar(Leitor leitor) {
        String nome = leitor.getNome();
        
        StringBuilder query = new StringBuilder();
        query.append("insert into leitor(");
        query.append("nome, email, telefone, celular, logradouro, ");
        query.append("bairro, complemento, cep, cidade, sexo, ");
        query.append("rg, cpf, status, trab, incompleto, data_cadastro) ");
        query.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, current_date)");
        
        logger.trace("Iniciando cadastro do leitor:" + nome);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            incluirParametros(ps, leitor);
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            logger.fatal("Não foi possível cadastrar o leitor", ex);
        }
        return 1;
    }
    
    public Leitor consultar(String nome) {
        StringBuilder query = new StringBuilder();
        query.append("select idleitor, nome, email, telefone, celular, ");
        query.append("logradouro, bairro, complemento, cep, cidade, sexo, ");
        query.append("rg, cpf, status, trab, incompleto ");
        query.append("from leitor ");
        query.append("where nome like ?");

        logger.trace("Iniciando consulta do leitor: " + nome);
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirLeitor(rs);
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Não foi possível consultar o leitor", ex);
        }
        return null;
    }
    
    public void atualizarListaLeitores() {
        dataSource.atualizarListaLeitores();
    }
    
    public ObservableList<String> getListaLeitores() {
        return dataSource.getListaLeitores();
    }

    private Leitor construirLeitor(ResultSet rs) throws SQLException {
        return new LeitorBuilder(rs.getString(2))
                .id(rs.getInt(1))
                .email(rs.getString(3))
                .telefone1(rs.getString(4))
                .telefone2(rs.getString(5))
                .logradouro(rs.getString(6))
                .bairro(rs.getString(7))
                .complemento(rs.getString(8))
                .cep(rs.getString(9))
                .cidade(rs.getString(10))
                .sexo(rs.getString(11))
                .rg(rs.getString(12))
                .cpf(rs.getString(13))
                .isAtivo(rs.getBoolean(14))
                .isTrabalhador(rs.getBoolean(15))
                .isIncompleto(rs.getBoolean(16))
                .build();
    }
}
