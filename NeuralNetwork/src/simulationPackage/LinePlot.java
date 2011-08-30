package simulationPackage;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import networkGUI.SimulationEndDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class LinePlot extends JFrame {

	private static final long serialVersionUID = 1L; // /???
	JFreeChart chart;
	private final JPanel contentPane;

	public LinePlot(String title) {
		super(title);
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
								new java.io.File(file.getAbsolutePath() + "/" + "eeg" + ".png"), chart, 800, 300);

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

	void drawLinePlot(XYSeriesCollection dataset, String title) {
		chart = ChartFactory.createXYLineChart(
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
		contentPane.add(panel);

		this.pack();
		this.setVisible(true);
	}
}
