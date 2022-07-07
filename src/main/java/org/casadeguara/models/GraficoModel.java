package org.casadeguara.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.casadeguara.conexao.Conexao;

/**
 * Classe responsável pela geração dos gráficos que existem no sistema.
 * 
 * @author Gustavo Fragoso
 */
public class GraficoModel {

	private static final Logger logger = LogManager.getLogger(GraficoModel.class);
	private Map<String, Integer> meses;

	public GraficoModel() {
		meses = new HashMap<>();
		meses.put("Janeiro", 1);
		meses.put("Fevereiro", 2);
		meses.put("Março", 3);
		meses.put("Abril", 4);
		meses.put("Maio", 5);
		meses.put("Junho", 6);
		meses.put("Julho", 7);
		meses.put("Agosto", 8);
		meses.put("Setembro", 9);
		meses.put("Outubro", 10);
		meses.put("Novembro", 11);
		meses.put("Dezembro", 12);
	}

	public ObservableList<PieChart.Data> movimentacaoEm(String mes, int ano) {
		StringBuilder query = new StringBuilder();
		query.append("select EXTRACT(month from data_emprestimo) as mes, ");
		query.append("count(idmov) filter (where trab = true) as trabalhadores, ");
		query.append("count(idmov) filter (where trab = false) as frequentadores ");
		query.append("from movimentacao ");
		query.append("inner join leitor on (leitor = nome) ");
		query.append("where EXTRACT(month from data_emprestimo) = ? and extract (year from data_emprestimo) = ? ");
		query.append("group by mes ");
		query.append("order by mes");

		logger.trace("Iniciando a consulta da movimentação no mês em " + mes + " de " + ano);
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, meses.get(mes));
			ps.setInt(2, ano);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					pieChartData.add(new PieChart.Data("Trabalhadores: " + rs.getInt(2), rs.getInt(2)));
					pieChartData.add(new PieChart.Data("Frequentadores: " + rs.getInt(3), rs.getInt(3)));
				}
			}
		} catch (SQLException ex) {
			logger.fatal("Ocorreu um erro durante a geração do gráfico.", ex);
		}
		return pieChartData;
	}

	public List<XYChart.Series<Number, Number>> movimentacaoPorMes(int ano) {
		StringBuilder query = new StringBuilder();
		query.append("select EXTRACT(month from data_emprestimo) as mes, ");
		query.append("count(idmov) filter (where trab = true) as trabalhadores, ");
		query.append("count(idmov) filter (where trab = false) as frequentadores ");
		query.append("from movimentacao ");
		query.append("inner join leitor on (leitor = nome) ");
		query.append("where EXTRACT(year from data_emprestimo) = ? ");
		query.append("group by mes ");
		query.append("order by mes");

		logger.trace("Iniciando a consulta da movimentação em " + ano);
		List<XYChart.Series<Number, Number>> result = new ArrayList<>();
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			ps.setInt(1, ano);

			XYChart.Series<Number, Number> trabalhadores = new XYChart.Series<>();
			XYChart.Series<Number, Number> frequentadores = new XYChart.Series<>();

			trabalhadores.setName("Trabalhadores");
			frequentadores.setName("Frequentadores");

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int mes = rs.getInt(1);
					trabalhadores.getData().add(new Data<Number, Number>(mes, rs.getInt(2)));
					frequentadores.getData().add(new Data<Number, Number>(mes, rs.getInt(3)));
				}
			}

			result.add(trabalhadores);
			result.add(frequentadores);
		} catch (SQLException ex) {
			logger.fatal("Ocorreu um erro durante a geração do gráfico.", ex);
		}
		return result;
	}

	public List<XYChart.Series<String, Number>> movimentacaoGeral() {
		StringBuilder query = new StringBuilder();
		query.append("select EXTRACT(year from data_emprestimo) as year, ");
		query.append("count(idmov) filter (where trab = true) as trabalhadores, ");
		query.append("count(idmov) filter (where trab = false) as frequentadores ");
		query.append("from movimentacao ");
		query.append("inner join leitor on (leitor = nome) ");
		query.append("group by year ");
		query.append("order by year");

		List<XYChart.Series<String, Number>> result = new ArrayList<>();
		logger.trace("Iniciando a consulta da movimentação geral");
		try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(query.toString())) {

			XYChart.Series<String, Number> trabalhadores = new XYChart.Series<>();
			XYChart.Series<String, Number> frequentadores = new XYChart.Series<>();

			trabalhadores.setName("Trabalhadores");
			frequentadores.setName("Frequentadores");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String ano = rs.getString(1);
					trabalhadores.getData().add(new Data<String, Number>(ano, rs.getInt(2)));
					frequentadores.getData().add(new Data<String, Number>(ano, rs.getInt(3)));
				}
			}

			result.add(trabalhadores);
			result.add(frequentadores);
		} catch (SQLException ex) {
			logger.fatal("Ocorreu um erro durante a geração do gráfico.", ex);
		}
		return result;
	}

}
