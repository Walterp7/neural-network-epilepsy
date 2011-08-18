package simulationPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import networkGUI.ConfigurationUnit;
import networkGUI.PlotFrame;
import networkPackage.Network;
import networkPackage.NetworkBuilder;
import neuronPackage.Status;
import neuronPackage.Type;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Simulator {

	private ProgressListener listener;

	ConfigurationUnit configFromGUI;

	public Simulator(ConfigurationUnit conf) {
		configFromGUI = conf;
	}

	public void runSimulation() throws IOException, InterruptedException {
		final int totalTime = configFromGUI.getTotalTime();// 1000;
		final double timeStep = configFromGUI.getTimeStep();

		final List<String[]> simulations = configFromGUI.getAllSimulations();

		final int tenPercent = (int) (totalTime / (10 * timeStep));
		ExecutorService exec = Executors.newFixedThreadPool(simulations.size());
		for (final String[] simDir : simulations) {
			exec.execute(new Runnable() {

				@Override
				public void run() {

					String simName = simDir[1];

					System.out.println(simName + " starting");

					NetworkBuilder mag = new NetworkBuilder();
					Network net;

					try {
						net = mag.setUpNetwork(simDir[0], configFromGUI, timeStep);
						ArrayList<Integer> numOfNeuronsInColumn = net.getNumberOfNeuronsInColumn();

						int numOfCols = numOfNeuronsInColumn.size();

						System.out.println(simName + " Simulation start");
						System.out.println();

						List<XYSeries[]> dataSeries = new ArrayList<XYSeries[]>();

						for (int i = 0; i < numOfCols; i++) {
							XYSeries[] newDataSeries = new XYSeries[4];
							newDataSeries[0] = new XYSeries("RS neurons");
							newDataSeries[1] = new XYSeries("FS neurons");
							newDataSeries[2] = new XYSeries("LTS neurons");
							newDataSeries[3] = new XYSeries("IB neurons");
							dataSeries.add(newDataSeries);
						}

						// XYSeries seriesRS = new XYSeries("RS neurons");
						// XYSeries seriesFS = new XYSeries("FS neurons");
						// XYSeries seriesLTS = new XYSeries("LTS neurons");
						// XYSeries seriesIB = new XYSeries("IB neurons");

						XYSeries seriesPSP = new XYSeries("Simulated EEG");

						for (double timeOfSimulation = 0; timeOfSimulation <= totalTime; timeOfSimulation += timeStep) {
							List<Status> stats = net.nextStep(timeStep, timeOfSimulation);
							// double voltage = 0;
							double psp = 0;
							for (Status s : stats) {
								if (s.fired()) {
									int counter = 0;

									int neuronId = s.getNumber();

									while (neuronId >= 0) {
										neuronId = neuronId - numOfNeuronsInColumn.get(counter);
										counter++;

									}

									int colNum = counter - 1;

									if (s.getType() == Type.RS) {
										dataSeries.get(colNum)[0].add(s.getTime(), s.getNumber());

										// seriesRS.add(s.getTime(),
										// s.getNumber());
									} else {
										if (s.getType() == Type.FS) {
											dataSeries.get(colNum)[1].add(s.getTime(), s.getNumber());
											// seriesFS.add(s.getTime(),
											// s.getNumber());
										} else {
											if (s.getType() == Type.LTS) {
												dataSeries.get(colNum)[2].add(s.getTime(), s.getNumber());
												// seriesLTS.add(s.getTime(),
												// s.getNumber());
											} else {
												dataSeries.get(colNum)[3].add(s.getTime(), s.getNumber());
												// seriesIB.add(s.getTime(),
												// s.getNumber());
											}
										}
									}

								}
								if (s.getType() == Type.RS || s.getType() == Type.IB) {
									// voltage += s.getVoltage();
									psp += s.getPSP();
								}

							}

							seriesPSP.add(timeOfSimulation, psp / 3000);

							if ((int) (timeOfSimulation / timeStep) % tenPercent == 0) {
								// System.out.println(simName + ' ' +
								// timeOfSimulation);

								listener.reportProgress(timeOfSimulation / totalTime);
							}
						}

						XYSeriesCollection[] allDatasetSpikes = new XYSeriesCollection[numOfCols];

						int i = 0;
						for (XYSeries[] data : dataSeries) {
							final XYSeriesCollection datasetSpikes = new XYSeriesCollection();
							datasetSpikes.addSeries(data[3]); // lts
							datasetSpikes.addSeries(data[0]); // fs
							datasetSpikes.addSeries(data[1]); // rs
							datasetSpikes.addSeries(data[2]); // ib
							allDatasetSpikes[i] = datasetSpikes;
							i++;
						}

						final XYSeriesCollection datasetEEG = new XYSeriesCollection();
						datasetEEG.addSeries(seriesPSP);
						// AnalyseNetwork analyser = new AnalyseNetwork();
						// analyser.getDegrees(net);
						// DrawNetwork drawer = new DrawNetwork();

						// drawer.drawAll(datasetSpikes, datasetEEG, simName);
						PlotFrame plotFrame = new PlotFrame();
						plotFrame.plotNetwork(numOfNeuronsInColumn, allDatasetSpikes, simName);

						LinePlot eegPlot = new LinePlot("Spike Pattern: " + simName);
						eegPlot.drawLinePlot(datasetEEG, "Simulated EEG (" + simName + ")");

						listener.reportProgress(10);
						System.out.println(simName + " done");
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
			});
		}
		exec.shutdown();
		exec.awaitTermination(3600, TimeUnit.SECONDS);
		System.out.println("done");
	}

	public void setListener(ProgressListener progressListener) {
		this.listener = progressListener;
	}
}
