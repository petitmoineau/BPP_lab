import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.Map;

public class StatisticsVisualizer extends JFrame {

    public StatisticsVisualizer(Map<String, Integer> eventCounts) {
        super("Event Statistics");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : eventCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Events", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Event Counts",
                "Event Type",
                "Count",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        setContentPane(chartPanel);
    }

    public static void visualizeEventStatistics(Map<String, Integer> eventCounts) {
        SwingUtilities.invokeLater(() -> {
            StatisticsVisualizer visualizer = new StatisticsVisualizer(eventCounts);
            visualizer.setSize(800, 400);
            visualizer.setLocationRelativeTo(null);
            visualizer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            visualizer.setVisible(true);
        });
    }
}
