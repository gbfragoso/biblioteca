package org.casadeguara.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.consultas.Resultado;

/**
 * Esta classe oferece consultas prontas para o acervo da biblioteca, separadas por categoria.
 * @author Gustavo
 */
public class ConsultaModel {
    
    private static final Logger logger = LogManager.getLogger(ConsultaModel.class);
    
    public ObservableList<Resultado> porAutor(String autor) {

        StringBuilder query = new StringBuilder();
        query.append("select b.tombo, b.titulo, a.numero, a.status, f.nome, e.data_devolucao from exemplar a ");
        query.append("inner join livro b on (a.livro = b.idlivro) ");
        query.append("inner join autor_has_livro c on (a.livro = c.livro) ");
        query.append("inner join autor d on (c.autor = d.idautor) ");
        query.append("left outer join emprestimo e on (a.idexemplar = e.exemplar) ");
        query.append("left join leitor f on (e.leitor = f.idleitor) ");
        query.append("where unaccent(d.nome) like unaccent(?) and a.status != 'Deletado'");

        return buscar(query.toString(), autor.toUpperCase());
    }

    public ObservableList<Resultado> porEditora(String editora) {

        StringBuilder query = new StringBuilder();
        query.append("select b.tombo, b.titulo, a.numero, a.status, e.nome, d.data_devolucao from exemplar a ");
        query.append("inner join livro b on (a.livro = b.idlivro) ");
        query.append("inner join editora c on (b.editora = ideditora) ");
        query.append("left outer join emprestimo d on (a.idexemplar = d.exemplar) ");
        query.append("left join leitor e on (d.leitor = e.idleitor) ");
        query.append("where unaccent(c.nome) like unaccent(?) and a.status != 'Deletado'");

        return buscar(query.toString(), editora.toUpperCase());
    }

    public ObservableList<Resultado> porPalavraChave(String palavra) {

        StringBuilder query = new StringBuilder();
        query.append("select b.tombo, b.titulo, a.numero, a.status, f.nome, e.data_devolucao from exemplar a ");
        query.append("inner join livro b on (a.livro = b.idlivro) ");
        query.append("inner join livro_has_keyword c on (a.livro = c.livro) ");
        query.append("inner join keyword d on (c.keyword = d.idkeyword) ");
        query.append("left outer join emprestimo e on (a.idexemplar = e.exemplar) ");
        query.append("left join leitor f on (e.leitor = f.idleitor) ");
        query.append("where unaccent(d.chave) like unaccent(?) and a.status != 'Deletado'");

        return buscar(query.toString(), palavra.toUpperCase());
    }

    public ObservableList<Resultado> porTitulo(String titulo) {

        StringBuilder query = new StringBuilder();
        query.append("select c.tombo, c.titulo, a.numero, a.status, d.nome, b.data_devolucao from exemplar a ");
        query.append("inner join livro c on (a.livro = c.idlivro) ");
        query.append("left outer join emprestimo b on (a.idexemplar = b.exemplar) ");
        query.append("left join leitor d on (b.leitor = d.idleitor) ");
        query.append("where unaccent(titulo) like unaccent(?) and a.status != 'Deletado'");
        return buscar(query.toString(), titulo.toUpperCase());
    }

    public ObservableList<Resultado> porTombo(String tombo) {
        StringBuilder query = new StringBuilder();
        query.append("select c.tombo, c.titulo, a.numero, a.status, d.nome, b.data_devolucao from exemplar a ");
        query.append("inner join livro c on (a.livro = c.idlivro) ");
        query.append("left outer join emprestimo b on (a.idexemplar = b.exemplar) ");
        query.append("left join leitor d on (b.leitor = d.idleitor) ");
        query.append("where c.tombo = ? and a.status != 'Deletado'");

        return buscar(query.toString(), tombo);
    }

    private ObservableList<Resultado> buscar(String query, String param) {
        ObservableList<Resultado> resultados = FXCollections.observableArrayList();
        
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(query)) {
             ps.setString(1, param);

            try(ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Resultado r = new Resultado(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5),
                            dataPadrao(rs.getDate(6)));
                    
                    resultados.add(r);
                }
            }
        } catch (SQLException ex) {
            logger.fatal("Não foi possível concluir a consulta", ex);
        }
        return resultados;
    }

    private LocalDate dataPadrao(Date date) {
        return (date != null) ? date.toLocalDate() : LocalDate.of(2100, 01, 01);
    }
}
