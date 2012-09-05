package networkGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
import org.jfree.data.xy.XYSeriesCollection;

public class SpikePlotFrame extends PlotFrame { // plots scatter plot and the

	private final JPanel contentPane;
	// List<JFreeChart> charts = new ArrayList<JFreeChart>();
	SpikeChartCollection charts = new SpikeChartCollection();
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

						for (JFreeChart chart : charts.getCharts()) {
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

		charts.save(pathName);
	}

	public void plotNetwork(int numberOfColumns, XYSeriesCollection[] data,
			String title, int totalTime) {
		this.setName(title);
		frameTitle = title;

		charts.plotNetwork(numberOfColumns, data, title, totalTime);

		for (JFreeChart chart : charts.getCharts()) {

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
