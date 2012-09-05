package networkGUI;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartCollection {

	List<JFreeChart> charts = new ArrayList<JFreeChart>();

	private static ChartTheme currentTheme = new SimChartTheme("JFree");

	public void save(String pathName) throws IOException {
		for (JFreeChart chart : charts) {
			ChartUtilities.saveChartAsPNG(
					new java.io.File(pathName
							+ "/" + "spikes_col"
							+ charts.indexOf(chart) + ".png"),
					chart, 2000, 300);
		}
	}

	public JFreeChart createSpikePlot(String title, String xAxisLabel,
			String yAxisLabel, XYDataset dataset, PlotOrientation orientation,
			boolean legend, boolean tooltips, boolean urls, int totalTime) {

		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		NumberAxis xAxis = new NumberAxis(xAxisLabel); // zakres dodac!
		xAxis.setAutoRangeIncludesZero(true);
		xAxis.setRange(0, totalTime);

		String[] labels = new String[764];
		for (int i = 0; i < 764; i++) {
			labels[i] = "x";
		}

		// final String[] labs = { "II/III", "IV", "V", "VI" };
		final String[] labs = { "VI", "V", "IV", "II/III" };
		final int[] pos = { 100, 300, 457, 652 };
		// final int[] pos = { 80, 300, 487, 670 };
		LabelAxis yAxis = new LabelAxis(yAxisLabel, labels, labs, pos);
		// NumberAxis yAxis = new NumberAxis();
		// yAxis.setInverted(true);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);

		XYToolTipGenerator toolTipGenerator = null;
		if (tooltips) {
			toolTipGenerator = new StandardXYToolTipGenerator();
		}

		XYURLGenerator urlGenerator = null;
		if (urls) {
			urlGenerator = new StandardXYURLGenerator();
		}
		XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
		renderer.setBaseToolTipGenerator(toolTipGenerator);
		renderer.setURLGenerator(urlGenerator);

		plot.setRenderer(renderer);
		plot.setOrientation(orientation);

		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, legend);
		currentTheme.apply(chart);
		return chart;

	}

	public void plotNetwork(int numberOfColumns, XYSeriesCollection[] data,
			String title, int totalTime) {

		JFreeChart chart1 = createSpikePlot(
				"Spike pattern - " + title, // chart title
				"", // x axis label
				"Column 1", // y axis label
				data[0], // data
				PlotOrientation.VERTICAL,
				false, // include legend
				true, // tooltips
				false, // urls
				totalTime
				);

		// force aliasing of the rendered content..
		chart1.getRenderingHints().put
				(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		charts.add(chart1);

		for (int i = 2; i <= numberOfColumns; i++) {
			JFreeChart chart = createSpikePlot(
					"", // chart title
					"", // x axis label
					"Column " + i, // y axis label
					data[i - 1], // data
					PlotOrientation.VERTICAL,
					false, // include legend
					true, // tooltips
					false, // urls
					totalTime
					);

			// force aliasing of the rendered content..
			chart.getRenderingHints().put
					(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			charts.add(chart);

		}

	}

	public List<JFreeChart> getCharts() {
		return charts;
	}

	public int indexOf(JFreeChart chart) {
		return charts.indexOf(chart);
	}

}
