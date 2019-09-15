package org.casadeguara.views;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * Constroi a tela de gráficos.
 * 
 * @author Gustavo
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class GraficoView implements GenericView {

    private final GridPane painelEstatistica;

    private ComboBox<Integer> cbbAno;
    private ComboBox<String> cbbMes;
    private LineChart<Number, Number> movimentacaoPorAno;
    private LineChart<String, Number> movimentacaoGeral;
    private PieChart movimentacaoNoMes;
    
    public GraficoView() {
        painelEstatistica = new GridPane();
        
        ColumnConstraints colunas = new ColumnConstraints();
        colunas.setPercentWidth(50);

        painelEstatistica.setHgap(5);
        painelEstatistica.setVgap(5);
        painelEstatistica.setAlignment(Pos.TOP_CENTER);
        painelEstatistica.getColumnConstraints().addAll(colunas, colunas);

        NumberAxis meses = new NumberAxis(1, 12, 1);
        NumberAxis quantidade = new NumberAxis();
        NumberAxis quantidade2 = new NumberAxis();
        CategoryAxis anos = new CategoryAxis();

        meses.setLabel("Meses (número)");

        cbbAno = new ComboBox<>();
        cbbAno.setPromptText("Selecione o ano");
        cbbAno.setPrefWidth(300);

        cbbMes = new ComboBox<>();
        cbbMes.setPromptText("Selecione o mês");
        cbbMes.setPrefWidth(300);
        cbbMes.getItems().addAll("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
                "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");

        movimentacaoNoMes = new PieChart();
        movimentacaoNoMes.setLegendVisible(false);
        movimentacaoPorAno = new LineChart<>(meses, quantidade);
        movimentacaoPorAno.setTitle("Quantidade de empréstimos por público/ano");
        movimentacaoPorAno.setLegendSide(Side.RIGHT);
        movimentacaoGeral = new LineChart<>(anos, quantidade2);
        movimentacaoGeral.setTitle("Quantidade de empréstimos por público");
        movimentacaoGeral.setLegendSide(Side.RIGHT);

        cbbAno.setMaxWidth(Double.MAX_VALUE);
        cbbMes.setMaxWidth(Double.MAX_VALUE);

        painelEstatistica.add(movimentacaoGeral, 0, 0, 2, 1);
        painelEstatistica.add(cbbMes, 0, 2);
        painelEstatistica.add(movimentacaoNoMes, 0, 3);
        painelEstatistica.add(cbbAno, 1, 2);
        painelEstatistica.add(movimentacaoPorAno, 1, 3);
    }
    
    public void atualizarGraficoGeral(List<XYChart.Series<String, Number>> dados) {
        movimentacaoGeral.getData().setAll(dados.get(0), dados.get(1));
    }
    
    public void atualizarGraficoAnual(List<XYChart.Series<Number, Number>> dados) {
        movimentacaoPorAno.getData().setAll(dados.get(0), dados.get(1));
    }
    
    public void atualizarGraficoMensal(ObservableList<PieChart.Data> dados) {
        movimentacaoNoMes.getData().setAll(dados);
    }
    
    public void listenerGraficoAnual(ChangeListener<Integer> listener) {
        cbbAno.valueProperty().addListener(listener);
    }
        
    public void listenerGraficoMensal(ChangeListener<String> listener) {
        cbbMes.valueProperty().addListener(listener);
    }
    
    public void setAnosDisponiveis(ObservableList<Integer> anos) {
        cbbAno.getItems().setAll(anos);
    }
    
    @Override
    public GridPane getRoot() {
        return painelEstatistica;
    }

}
