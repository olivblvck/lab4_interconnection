package pl.uni.graphs;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import javax.swing.JFrame;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * CSV export and XChart visualization utility for Epidemic Spreading data.
 * Generates both CSV files and PNG plots for SIR model results.
 */
public class EpidemicDataExporter {

    /**
     * Export epidemic statistics to a CSV file.
     */
    public static void saveToCsv(String filename,
                                 List<Integer> susceptible,
                                 List<Integer> infected,
                                 List<Integer> recovered) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("iteration,susceptible,infected,recovered\n");

            for (int t = 0; t < susceptible.size(); t++) {
                writer.write(t + "," +
                        susceptible.get(t) + "," +
                        infected.get(t) + "," +
                        recovered.get(t) + "\n");
            }

            System.out.println("✓ CSV saved: " + filename);
        } catch (IOException e) {
            System.err.println("ERROR: Cannot save CSV: " + filename);
            throw e;
        }
    }

    /**
     * Create and save epidemic chart as a PNG image.
     * Shows S(t), I(t), R(t) curves over time.
     */
    public static void saveChartAsPNG(String filename,
                                      String title,
                                      List<Integer> susceptible,
                                      List<Integer> infected,
                                      List<Integer> recovered) {
        try {
            XYChart chart = buildChart(title, susceptible, infected, recovered);
            BitmapEncoder.saveBitmap(chart, filename, BitmapEncoder.BitmapFormat.PNG);
            System.out.println("✓ PNG saved: " + filename);
        } catch (IOException e) {
            System.err.println("ERROR: Cannot save PNG: " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Display epidemic chart in an interactive window.
     * Returns the created JFrame (so it can be disposed later if desired).
     */
    public static JFrame showChart(String title,
                                   List<Integer> susceptible,
                                   List<Integer> infected,
                                   List<Integer> recovered) {
        try {
            XYChart chart = buildChart(title, susceptible, infected, recovered);

            JFrame frame = (JFrame) new SwingWrapper(chart).displayChart();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Try to bring the window to front (helps on macOS)
            frame.setAlwaysOnTop(true);
            frame.toFront();
            frame.requestFocus();
            frame.setAlwaysOnTop(false);

            System.out.println("✓ Chart displayed in a window");
            return frame;

        } catch (Exception e) {
            System.err.println("ERROR: Cannot display chart");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Build a chart with English labels (used for both PNG export and GUI).
     */
    private static XYChart buildChart(String title,
                                      List<Integer> susceptible,
                                      List<Integer> infected,
                                      List<Integer> recovered) {
        int n = susceptible.size();

        double[] xData = new double[n];
        double[] sData = new double[n];
        double[] iData = new double[n];
        double[] rData = new double[n];

        for (int k = 0; k < n; k++) {
            xData[k] = k;
            sData[k] = susceptible.get(k);
            iData[k] = infected.get(k);
            rData[k] = recovered.get(k);
        }

        XYChart chart = new XYChartBuilder()
                .width(1200)
                .height(700)
                .title("Epidemic Evolution - " + title)
                .xAxisTitle("Iteration (time steps)")
                .yAxisTitle("Number of people")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setLegendBackgroundColor(new java.awt.Color(255, 255, 255, 200));
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setChartTitleVisible(true);

        chart.addSeries("S(t) - Susceptible", xData, sData).setLineColor(java.awt.Color.BLUE);
        chart.addSeries("I(t) - Infected", xData, iData).setLineColor(java.awt.Color.RED);
        chart.addSeries("R(t) - Recovered", xData, rData).setLineColor(java.awt.Color.GREEN);

        return chart;
    }
}
