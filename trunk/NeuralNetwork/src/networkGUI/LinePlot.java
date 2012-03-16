package networkGUI;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
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

public class LinePlot extends JFrame {

	private static final long serialVersionUID = 1L; // /???
	JFreeChart chart;
	private final JPanel contentPane;
	private static ChartTheme currentTheme = new SimChartTheme("line");
	final String fileName;

	public LinePlot(final String frameTitle, final String fileName) {
		super(frameTitle);

		this.fileName = fileName;
		contentPane = new JPanel();
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

						ChartUtilities.saveChartAsPNG(
								new java.io.File(file.getAbsolutePath() + "/" + fileName + ".png"),
								chart, 2000, 250);

						SimulationEndDialog newDialog = new SimulationEndDialog();
						newDialog.setVisible(true);

					}

				} catch (java.io.IOException exc) {
					System.err.println("Error writing image to file");
				}
			}
		});
		contentPane.add(btnSave);
	}

	public double getMax() {
		return 0;
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
		// renderer.setSeriesPaint(0, Color.red);
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

		final ChartPanel panel = new ChartPanel(chart, true);
		panel.setPreferredSize(new java.awt.Dimension(1000, 300));
		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);
		contentPane.add(panel);

		this.pack();
		this.setVisible(true);
	}
}
