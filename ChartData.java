
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class ChartData extends JFrame {

    public ChartData(String title) {
        super(title);

        // Cria o conjunto de dados
        CategoryDataset dataset = createDataset();

        // Cria o gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Exemplo de Gráfico de Barras",
                "Categorias",
                "Valores",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Adiciona o gráfico de barras a um painel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Adiciona dados ao conjunto de dados
        dataset.addValue(5, "Série 1", "Categoria 1");
        dataset.addValue(8, "Série 1", "Categoria 2");
        dataset.addValue(3, "Série 1", "Categoria 3");
        dataset.addValue(12, "Série 1", "Categoria 4");

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChartData example = new ChartData("Exemplo de Gráfico de Barras");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
