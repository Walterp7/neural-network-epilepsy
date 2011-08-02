package simulationPackage;

import java.awt.RenderingHints;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class SpikeScatterPlot extends JFrame {

	private static final long serialVersionUID = 1L; // /???

	public SpikeScatterPlot(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	void drawSpikeScatterPlot(XYSeriesCollection dataset, String title) {

		// final NumberAxis domainAxis = new NumberAxis("time [ms]");
		// domainAxis.setAutoRangeIncludesZero(true);
		// final NumberAxis rangeAxis = new NumberAxis("neuron number");
		// rangeAxis.setAutoRangeIncludesZero(true);

		// final FastScatterPlot plot = new FastScatterPlot(dataset, domainAxis,
		// rangeAxis);
		// final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);
		JFreeChart chart = ChartFactory.createScatterPlot(
				title, // chart title
				"time [ms]", // x axis label
				"neurons", // y axis label
				dataset, // data ***-----PROBLEM------***
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
				);

		// force aliasing of the rendered content..
		chart.getRenderingHints().put
				(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final ChartPanel panel = new ChartPanel(chart, true);
		panel.setPreferredSize(new java.awt.Dimension(1000, 1000));
		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);

		setContentPane(panel);
		this.pack();
		this.setVisible(true);
	}
}
