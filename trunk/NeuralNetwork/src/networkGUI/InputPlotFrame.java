package networkGUI;

import java.awt.Color;
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

import networkPackage.InputDescriptor;

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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class InputPlotFrame extends PlotFrame {

	private final JPanel contentPane;

	List<JFreeChart> charts = new ArrayList<JFreeChart>();
	private static ChartTheme currentTheme = new SimChartTheme("stim");

	public InputPlotFrame() {
		setTitle("Inputs");
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
									new java.io.File(file.getAbsolutePath() + "/" + "inputsCol" + charts.indexOf(chart)
											+ ".png"), chart, 2000, 200);
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
					new java.io.File(pathName + "/" + "inputsCol" + charts.indexOf(chart)
							+ ".png"), chart, 2000, 200);
		}
	}

	protected static JFreeChart createXYLineChart(String title,
			String xAxisLabel,
			String yAxisLabel,
			XYDataset dataset,
			PlotOrientation orientation,
			boolean legend,
			boolean tooltips,
			boolean urls) {

		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		NumberAxis xAxis = new NumberAxis(xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(orientation);
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

	public void plotInputs(InputDescriptor inDesc) {

		for (XYSeries series : inDesc.getAllSeries()) {

			XYSeriesCollection data = new XYSeriesCollection();
			data.addSeries(series);
			JFreeChart chart = createXYLineChart(
					"(A) Input",// series.getDescription(), // chart title
					"time [ms]", // x axis label
					"[mV]", // y axis label
					data, // data
					PlotOrientation.VERTICAL,
					false, // include legend
					true, // tooltips
					false // urls
			);

			// force aliasing of the rendered content..
			chart.getRenderingHints().put
					(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			charts.add(chart);
			final ChartPanel panel = new ChartPanel(chart, true);
			panel.setPreferredSize(new java.awt.Dimension(600, 300));
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
