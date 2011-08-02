package simulationPackage;

import java.awt.RenderingHints;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class LinePlot extends JFrame {

	private static final long serialVersionUID = 1L; // /???

	public LinePlot(String title) {
		super(title);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	void drawLinePlot(XYSeriesCollection dataset, String title) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				title, // chart title
				"time [ms]", // x axis label
				"[mV]", // y axis label
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
		panel.setPreferredSize(new java.awt.Dimension(1000, 500));
		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);

		setContentPane(panel);
		this.pack();
		this.setVisible(true);
	}
}
