package simulationPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import networkGUI.ConfigurationUnit;
import networkGUI.EegColumnPlotFrame;
import networkGUI.InputPlotFrame;
import networkGUI.SpikePlotFrame;
import networkPackage.InputDescriptor;
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
					InputDescriptor inDescriptor = new InputDescriptor();
					NetworkBuilder mag = new NetworkBuilder();
					Network net;

					try {
						net = mag.setUpNetwork(simDir[0], configFromGUI, timeStep, totalTime, inDescriptor);

						ArrayList<Integer> numOfNeuronsInColumn = net.getNumberOfNeuronsInColumn();

						int numOfCols = numOfNeuronsInColumn.size();

						System.out.println(simName + " Simulation start");
						System.out.println();

						List<XYSeries[]> dataSeries = new ArrayList<XYSeries[]>();
						XYSeries[] eegSeries = new XYSeries[numOfCols];

						for (int i = 0; i < numOfCols; i++) {
							XYSeries[] newDataSeries = new XYSeries[4];
							newDataSeries[0] = new XYSeries("RS neurons");
							newDataSeries[1] = new XYSeries("FS neurons");
							newDataSeries[2] = new XYSeries("LTS neurons");
							newDataSeries[3] = new XYSeries("IB neurons");
							dataSeries.add(newDataSeries);
							eegSeries[i] = new XYSeries("Simulated EEG");
						}

						XYSeries seriesPSP = new XYSeries("Simulated EEG");

						for (double timeOfSimulation = 0; timeOfSimulation <= totalTime; timeOfSimulation += timeStep) {
							List<Status> stats = net.nextStep(timeStep, timeOfSimulation);
							// double voltage = 0;
							double psp = 0;
							double[] pspPerColumn = new double[numOfCols];
							for (Status s : stats) {
								int counter = 0;

								int neuronId = s.getNumber();

								while (neuronId >= 0) {
									neuronId = neuronId - numOfNeuronsInColumn.get(counter);
									counter++;

								}

								int neuronColNum = counter - 1;
								if (s.fired()) {

									if (s.getType() == Type.RS) {
										dataSeries.get(neuronColNum)[0].add(s.getTime(), s.getNumber());

									} else {
										if (s.getType() == Type.FS) {
											dataSeries.get(neuronColNum)[1].add(s.getTime(), s.getNumber());

										} else {
											if (s.getType() == Type.LTS) {
												dataSeries.get(neuronColNum)[2].add(s.getTime(), s.getNumber());

											} else {
												dataSeries.get(neuronColNum)[3].add(s.getTime(), s.getNumber());

											}
										}
									}

								}
								if (s.getType() == Type.RS || s.getType() == Type.IB) {
									// voltage += s.getVoltage();
									psp += s.getPSP();
									pspPerColumn[neuronColNum] += s.getPSP();
								}

							}

							seriesPSP.add(timeOfSimulation, psp / 3000);
							for (int i = 0; i < numOfCols; i++) {
								eegSeries[i].add(timeOfSimulation, pspPerColumn[i] / 3000);
							}

							if ((int) (timeOfSimulation / timeStep) % tenPercent == 0) {
								// System.out.println(simName + ' ' +
								// timeOfSimulation);

								listener.reportProgress(timeOfSimulation / totalTime);
							}
						}

						XYSeriesCollection[] allDatasetSpikes = new XYSeriesCollection[numOfCols];

						int count = 0;
						for (XYSeries[] data : dataSeries) {
							final XYSeriesCollection datasetSpikes = new XYSeriesCollection();
							datasetSpikes.addSeries(data[3]); // lts
							datasetSpikes.addSeries(data[0]); // fs
							datasetSpikes.addSeries(data[1]); // rs
							datasetSpikes.addSeries(data[2]); // ib
							allDatasetSpikes[count] = datasetSpikes;
							count++;
						}

						final XYSeriesCollection datasetEEG = new XYSeriesCollection();
						datasetEEG.addSeries(seriesPSP);

						XYSeriesCollection[] datasetEEGperColumn = new XYSeriesCollection[numOfCols];
						for (int i = 0; i < numOfCols; i++) {
							datasetEEGperColumn[i] = new XYSeriesCollection();
							datasetEEGperColumn[i].addSeries(eegSeries[i]);
						}

						// AnalyseNetwork analyser = new AnalyseNetwork();
						// analyser.getDegrees(net);

						SpikePlotFrame plotFrame = new SpikePlotFrame();
						plotFrame.plotNetwork(numOfCols, allDatasetSpikes, simName);

						EegColumnPlotFrame eegFrame = new EegColumnPlotFrame();
						eegFrame.plotNetwork(numOfCols, datasetEEGperColumn, "EEG per column");

						LinePlot eegPlot = new LinePlot("Spike Pattern: " + simName);
						eegPlot.drawLinePlot(datasetEEG, "Simulated EEG (" + simName + ")");

						InputPlotFrame inputFrame = new InputPlotFrame();
						if (!inDescriptor.isEmpty()) {
							inputFrame.plotInputs(inDescriptor);
						}
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
