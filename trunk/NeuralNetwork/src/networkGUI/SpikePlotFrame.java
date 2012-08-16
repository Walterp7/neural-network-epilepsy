package networkGUI;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
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

public class SpikePlotFrame extends PlotFrame { // plots scatter plot and the

	private final JPanel contentPane;
	List<JFreeChart> charts = new ArrayList<JFreeChart>();
	String frameTitle;
	private static ChartTheme currentTheme = new SimChartTheme("JFree");

	public SpikePlotFrame() {
		setTitle("Network Activity");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 806, 670);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final JFileChooser fc = new JFileChooser("F:/workspaces/neuronWorkspace/NeuralNetwork/data");
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showOpenDialog(null);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						// This is where a real application would open the file.
						if (!file.exists()) {
							new File(file.getAbsolutePath()).mkdir();
						}

						for (JFreeChart chart : charts) {
							ChartUtilities.saveChartAsPNG(
									new java.io.File(file.getAbsolutePath() + "/"
											+ "spikes_col"
											+ charts.indexOf(chart) + ".png"),
									chart, 2000, 300);
						}

					}

				} catch (java.io.IOException exc) {
					System.err.println("Error writing image to file");
				}
			}
		});
		contentPane.add(btnSave);

	}

	@Override
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
		this.setName(title);
		frameTitle = title;

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
		final ChartPanel panel1 = new ChartPanel(chart1, true);
		panel1.setPreferredSize(new java.awt.Dimension(600, 800));
		panel1.setMinimumDrawHeight(10);
		panel1.setMaximumDrawHeight(2000);
		panel1.setMinimumDrawWidth(20);
		panel1.setMaximumDrawWidth(2000);
		contentPane.add(panel1);

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
			final ChartPanel panel = new ChartPanel(chart, true);
			panel.setPreferredSize(new java.awt.Dimension(600, 800));
			panel.setMinimumDrawHeight(10);
			panel.setMaximumDrawHeight(2000);
			panel.setMinimumDrawWidth(20);
			panel.setMaximumDrawWidth(2000);
			contentPane.add(panel);
		}

		this.pack();
		this.setVisible(true);
	}
}
