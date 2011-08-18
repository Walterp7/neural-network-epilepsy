package networkGUI;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotFrame extends JFrame {

	private final JPanel contentPane;
	List<JFreeChart> charts = new ArrayList<JFreeChart>();
	String frameTitle;

	public PlotFrame() {
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
					for (JFreeChart chart : charts) {
						ChartUtilities.saveChartAsPNG(
								new java.io.File(frameTitle + "col" + charts.indexOf(chart) + ".png"), chart, 600, 800);
					}

					SimulationEndDialog newDialog = new SimulationEndDialog();
					newDialog.setVisible(true);
				} catch (java.io.IOException exc) {
					System.err.println("Error writing image to file");
				}
			}
		});
		contentPane.add(btnSave);

	}

	public void plotNetwork(List<Integer> neuronsInColum, XYSeriesCollection[] data,
			String title) {
		this.setName(title);
		frameTitle = title;
		int numberOfColumns = neuronsInColum.size();

		// for (int i = 0; i < numberOfColumns; i++) {
		// PlotPanel newPlotPan = new PlotPanel(i + 1);
		// newPlotPan.plot(dataset1);
		// contentPane.add(newPlotPan);
		// }
		// first plot with title

		JFreeChart chart1 = ChartFactory.createScatterPlot(
				title, // chart title
				"", // x axis label
				"Column 1", // y axis label
				data[0], // data
				PlotOrientation.VERTICAL,
				false, // include legend
				true, // tooltips
				false // urls
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

		for (int i = 2; i < numberOfColumns; i++) {
			JFreeChart chart = ChartFactory.createScatterPlot(
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

		// the last one with legend
		JFreeChart chartLast = ChartFactory.createScatterPlot(
				"", // chart title
				"time [ms]", // x axis label
				"Column " + numberOfColumns, // y axis label
				data[numberOfColumns - 1], // data
				PlotOrientation.VERTICAL,
				false, // include legend
				true, // tooltips
				false // urls
				);

		// force aliasing of the rendered content..
		chartLast.getRenderingHints().put
				(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		charts.add(chartLast);
		final ChartPanel panel = new ChartPanel(chartLast, true);
		panel.setPreferredSize(new java.awt.Dimension(600, 800));
		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);
		contentPane.add(panel);

		this.pack();
		this.setVisible(true);
	}
}
