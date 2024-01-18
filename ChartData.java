import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChartData extends JFrame {
    // Dataset para armazenar os dados do gráfico e MemoryUnit para obter contagens
    // de mensagens por utilizador.
    private DefaultCategoryDataset dataset;
    private MemoryUnit memoryUnit;

    // Construtor da classe que cria a interface gráfica com o gráfico de barras.
    public ChartData(String title, MemoryUnit memoryUnit) {
        super(title);

        this.memoryUnit = memoryUnit;

        dataset = createDataset();

        // Cria o gráfico de barras.
        JFreeChart chart = ChartFactory.createBarChart(
                "Número de Mensagens por Utilizador",
                "Utilizador",
                "Número de Mensagens",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Método privado para criar o dataset com base nos dados da MemoryUnit.
    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> userMessageCounts = memoryUnit.getUserMessageCounts();

        for (Map.Entry<String, Integer> entry : userMessageCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Série 1", entry.getKey());
        }

        return dataset;
    }

}
