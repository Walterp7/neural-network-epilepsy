package simulationPackage;

import org.jfree.data.xy.XYSeriesCollection;

public class DrawNetwork {
	// for each type of plot another class

	void drawAll(XYSeriesCollection dataset1, XYSeriesCollection dataset2, String title) {
		SpikeScatterPlot spikePlot = new SpikeScatterPlot("Spike Pattern: " + title);
		spikePlot.drawSpikeScatterPlot(dataset1, "Spike Pattern (" + title + ")");

		LinePlot eegPlot = new LinePlot("Spike Pattern: " + title);
		eegPlot.drawLinePlot(dataset2, "Simulated EEG (" + title + ")");
	}
}
