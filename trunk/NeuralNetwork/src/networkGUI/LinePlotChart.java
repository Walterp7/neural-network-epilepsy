package networkGUI;

import java.awt.Color;
import java.awt.RenderingHints;
import java.io.IOException;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class LinePlotChart {

	JFreeChart chart;

	private static ChartTheme currentTheme = new SimChartTheme("line");
	final String fileName;

	public LinePlotChart(final String frameTitle, final String fileName) {
		this.fileName = fileName;
	}

	public void save(String pathName) throws IOException {
		ChartUtilities.saveChartAsPNG(new java.io.File(pathName + "/" + fileName + ".png"), chart, 2000, 250);
	}

	protected static JFreeChart createXYLineChart(String title,
			String xAxisLabel,
			String yAxisLabel,
			XYDataset dataset,
			PlotOrientation orientation,
			boolean legend,
			boolean tooltips,
			boolean urls, boolean customRange, double lower, double upper, boolean reverse) {

		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		NumberAxis xAxis = new NumberAxis(xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);
		yAxis.setAutoRangeIncludesZero(false);
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);
		// renderer.setSeriesPaint(1, Color.red);
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(orientation);

		yAxis.setInverted(reverse);
		if (customRange) {
			yAxis.setRange(lower, upper);
		}
		if (tooltips) {
			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		}
		if (urls) {
			renderer.setURLGenerator(new StandardXYURLGenerator());
		}

		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, legend);

		currentTheme.apply(chart);

		return chart;

	}

	public void draw(XYSeriesCollection dataset, String title, boolean customRange, double lower, double upper,
			boolean reverseYAxis, boolean isStimulated) {

		if (isStimulated) {
			currentTheme = new SimChartTheme("stim");
		} else {
			currentTheme = new SimChartTheme("line");
		}
		chart = createXYLineChart(
				title, // chart title
				"time [ms]", // x axis label
				"[mV]", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL,
				false, // include legend
				true, // tooltips
				false, // urls
				customRange,
				lower,
				upper,
				reverseYAxis
				);

		// force aliasing of the rendered content..
		chart.getRenderingHints().put
				(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	}

}
