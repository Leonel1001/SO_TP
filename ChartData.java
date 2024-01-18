import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChartData extends JFrame {

    private DefaultCategoryDataset dataset;
    private MemoryUnit memoryUnit;

    public ChartData(String title, MemoryUnit memoryUnit) {
        super(title);

        // Initialize MemoryUnit
        this.memoryUnit = memoryUnit;

        // Create the dataset
        dataset = createDataset();

        // Create the bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Número de Mensagens por Utilizador",
                "Utilizador",
                "Número de Mensagens",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Add the bar chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);

        // Set up the window
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Retrieve user message counts from MemoryUnit
        Map<String, Integer> userMessageCounts = memoryUnit.getUserMessageCounts();

        // Add data to the dataset
        for (Map.Entry<String, Integer> entry : userMessageCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Série 1", entry.getKey());
        }

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryUnit memoryUnit = new MemoryUnit();
            // Assuming users send messages, update the memory unit with user and message
            memoryUnit.saveMessage("User1: Hello!");
            memoryUnit.saveMessage("User2: Hi there!");
            memoryUnit.saveMessage("User1: Another message from User1!");

            // Open the chart page
            new ChartData("Número de Mensagens por Utilizador", memoryUnit);
        });
    }
}
