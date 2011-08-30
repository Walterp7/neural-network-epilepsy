package networkGUI;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import networkPackage.InputDescriptor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class InputPlotFrame extends JFrame {

	private final JPanel contentPane;

	List<JFreeChart> charts = new ArrayList<JFreeChart>();

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
											+ ".png"), chart, 800, 300);
						}

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

	public void plotInputs(InputDescriptor inDesc) {

		for (XYSeries series : inDesc.getAllSeries()) {

			XYSeriesCollection data = new XYSeriesCollection();
			data.addSeries(series);
			JFreeChart chart = ChartFactory.createXYLineChart(
					series.getDescription(), // chart title
					"[mV]", // x axis label
					"time [ms]", // y axis label
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
