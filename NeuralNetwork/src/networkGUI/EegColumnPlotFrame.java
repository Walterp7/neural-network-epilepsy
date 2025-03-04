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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class EegColumnPlotFrame extends PlotFrame {

	private final JPanel contentPane;
	List<JFreeChart> charts = new ArrayList<JFreeChart>();
	String frameTitle;

	public EegColumnPlotFrame() {
		setTitle("EEG - Network Activity");
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
									new java.io.File(file.getAbsolutePath() + "/" + "eeg_col"
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
					new java.io.File(pathName + "/" + "eeg_col"
							+ charts.indexOf(chart) + ".png"),
					chart, 2000, 300);
		}
	}

	public void plotNetwork(int numberOfColumns, XYSeriesCollection[] data, String title) {
		this.setName(title);
		frameTitle = title;

		for (int i = 1; i < numberOfColumns + 1; i++) {
			JFreeChart chart = ChartFactory.createXYLineChart(
					"", // chart title
					"", // x axis label
					"Column " + i, // y axis label
					data[i - 1], // data
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
