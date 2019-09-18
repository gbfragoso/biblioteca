package org.casadeguara.impressora;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.componentes.JasperViewerFX;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.etiquetas.Etiqueta;
import org.casadeguara.movimentacao.Acervo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Classe responsável por tudo que é impresso.
 * 
 * @author Gustavo
 * @since 3.0
 */
public class Impressora {
    
    private static final Logger logger = LogManager.getLogger(Impressora.class);

    /**
     * Gera um PDF com etiquetas, contendo o tombo do livro e número do exemplar.
     * 
     * @param etiquetas Lista de etiquetas.
     */
    public void etiquetas(List<Etiqueta> etiquetas) {
        JRBeanCollectionDataSource array = new JRBeanCollectionDataSource(etiquetas);
        visualizar("Folha de Etiquetas", "/reports/etiquetas63.jasper", null, array);
    }

    /**
     * Gera uma ficha de cadastro com os dados do cadastro de leitor.
     * 
     * @param ficha Um objeto ficha de cadastro.
     */
    public void fichaCadastro(List<Leitor> ficha) {
        JRBeanCollectionDataSource fichas = new JRBeanCollectionDataSource(ficha);
        visualizar("Ficha de Cadastro", "/reports/cadastroleitor.jasper", null, fichas);
    }

    /**
     * Gera um relatório com o histórico de empréstimos do leitor.
     * 
     * @param nome Nome do leitor
     */
    public void historicoLeitor(String nome) {
        Map<String, Object> params = new HashMap<>();
        params.put("leitor", nome);

        visualizar("Histórico do Leitor", "/reports/historico.jasper", params);
    }

    /**
     * Gera um recibo de empréstimo com os exemplares em posse do leitor.
     * 
     * @param leitor Leitor
     * @param exemplares Lista de exemplares
     */
    public void reciboEmprestimo(String leitor, List<Acervo> exemplares) {
        Map<String, Object> params = new HashMap<>();
        params.put("leitor", leitor);

        JRBeanCollectionDataSource array = new JRBeanCollectionDataSource(exemplares);
        visualizar("Recibo de Empréstimo", "/reports/recibo.jasper", params, array);
    }

    /**
     * Gera uma lista com empréstimos de acordo com determinada regra.
     * 
     * @param tiporelacao Tipo de empréstimo a ser retornado
     */
    public void relacaoEmprestimos(String tiporelacao) {
        Map<String, Object> params = new HashMap<>();
        params.put("where", tiporelacao);

        visualizar("Relação de Empréstimos", "/reports/relatorioemprestimo.jasper", params);
    }

    /**
     * Gera uma lista com todos os exemplares cadastrados no sistema.
     */
    public void relacaoExemplares() {
        visualizar("Relação de Exemplares", "/reports/relatorioexemplar.jasper", null);
    }

    /**
     * Gera uma lista com todos os leitores cadastrados no sistema.
     */
    public void relacaoLeitores() {
        visualizar("Relação de Leitores", "/reports/relatorioleitor.jasper");
    }

    /**
     * Métodos usados para abrir uma janela de visualização do PDF antes da impressão.
     * 
     * @param titulo Título da janela
     * @param modelo Modelo do relatório
     */
    private void visualizar(String titulo, String modelo) {
        visualizar(titulo, modelo, null);
    }

    private void visualizar(String titulo, String modelo, Map<String, Object> params) {
        try (Connection con = Conexao.abrir()) {
            JasperReport jreport = (JasperReport) JRLoader.loadObject(getClass().getResource(modelo));
            JasperPrint jprint = JasperFillManager.fillReport(jreport, params, con);
            new JasperViewerFX().viewReport(titulo, jprint);
        } catch (JRException | SQLException ex) {
            logger.fatal("Erro ao visualizar este relatório", ex);
        }
    }

    private void visualizar(String titulo, String modelo, Map<String, Object> params,
            JRBeanCollectionDataSource source) {
        try {
            JasperReport jreport = (JasperReport) JRLoader.loadObject(getClass().getResource(modelo));
            JasperPrint jprint = JasperFillManager.fillReport(jreport, params, source);
            new JasperViewerFX().viewReport(titulo, jprint);
        } catch (JRException ex) {
            logger.fatal("Erro ao visualizar este relatório", ex);
        }
    }
}
