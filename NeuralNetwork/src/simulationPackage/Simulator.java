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
import networkGUI.LinePlot;
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
						net = mag.createNetwork(simDir[0], configFromGUI, timeStep, totalTime, inDescriptor);

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
						XYSeries seriesLFP = new XYSeries("Local Field Potential - column 2");
						XYSeries seriesLFP2 = new XYSeries("Local Field Potential - column 3");
						XYSeries seriesEEG_LPF = new XYSeries("LFP for all columns");

						int neuronNumber = 380;
						XYSeries seriesNeuronTest = new XYSeries("Neuron number " + neuronNumber);

						for (double timeOfSimulation = 0; timeOfSimulation <= totalTime; timeOfSimulation += timeStep) {
							List<Status> stats = net.nextStep(timeStep, timeOfSimulation);

							double psp = 0;
							double voltage = 0; // for local field potential
							double voltage2 = 0; // for local field potential in
													// adjacent column
							double voltage_all = 0;
							double[] pspPerColumn = new double[numOfCols];
							// int counter = 0;

							for (Status s : stats) {
								int columnCounter = 0;

								int neuronId = s.getNumber();

								while (neuronId >= 0) {
									neuronId = neuronId - numOfNeuronsInColumn.get(columnCounter);
									columnCounter++;

								}

								int neuronColNum = columnCounter - 1;
								if (s.getNumber() == neuronNumber) {
									if (s.getTime() < 2 * timeStep) {
										System.out.println(s.getType() + " layer " + s.getLayer());
									}
									seriesNeuronTest.add(s.getTime(), s.getVoltage());
								}
								if (s.fired()) {

									if (s.getType() == Type.RS) {
										dataSeries.get(neuronColNum)[0].add(s.getTime(),
												764 - (s.getNumber() - neuronColNum
												* 764)); // II-III layer on top

									} else {
										if (s.getType() == Type.FS) {
											dataSeries.get(neuronColNum)[1].add(s.getTime(),
													764 - (s.getNumber() - neuronColNum
													* 764));

										} else {
											if (s.getType() == Type.LTS) {
												dataSeries.get(neuronColNum)[2].add(s.getTime(),
														764 - (s.getNumber() - neuronColNum
														* 764));

											} else {
												dataSeries.get(neuronColNum)[3].add(s.getTime(),
														764 - (s.getNumber() - neuronColNum
														* 764));

											}
										}
									}

								}
								if (s.getType() == Type.RS || s.getType() == Type.IB) {
									if ((s.getLayer() == 0) || (s.getLayer() == 2)) {
										if ((s.getColumn() == 1)) {
											voltage += s.getVoltage() / 273;
											// testVoltageCounter++;
											// counter++;
										}
										if (s.getColumn() == 0) {
											voltage2 += s.getVoltage() / 273;

										}
										voltage_all = s.getVoltage();

									}

									psp += s.getPSP();
									pspPerColumn[neuronColNum] += s.getPSP();
								}

							}

							seriesPSP.add(timeOfSimulation, psp / 3000);
							seriesLFP.add(timeOfSimulation, voltage);
							seriesLFP2.add(timeOfSimulation, voltage2);
							seriesEEG_LPF.add(timeOfSimulation, voltage_all / 2000);

							// System.out.println(counter);

							for (int i = 0; i < numOfCols; i++) {
								eegSeries[i].add(timeOfSimulation, pspPerColumn[i] / 3000);
							}

							if ((int) (timeOfSimulation / timeStep) % tenPercent == 0) {

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

						XYSeries seriesReferenceLine = new XYSeries("reference line -75mV");
						seriesReferenceLine.add(0, -75);
						seriesReferenceLine.add(totalTime, -75);

						final XYSeriesCollection datasetEEG_LFP = new XYSeriesCollection();
						datasetEEG_LFP.addSeries(seriesReferenceLine);
						datasetEEG_LFP.addSeries(seriesEEG_LPF);

						final XYSeriesCollection datasetEEG = new XYSeriesCollection();
						datasetEEG.addSeries(seriesPSP);

						final XYSeriesCollection datasetLFP = new XYSeriesCollection();
						datasetLFP.addSeries(seriesReferenceLine);
						datasetLFP.addSeries(seriesLFP);

						double maxLFPplot = Math.max(seriesLFP2.getMaxY(), seriesLFP.getMaxY());
						double minLFPplot = Math.min(seriesLFP2.getMinY(), seriesLFP.getMinY());

						final XYSeriesCollection datasetLFP2 = new XYSeriesCollection();
						datasetLFP2.addSeries(seriesReferenceLine);
						datasetLFP2.addSeries(seriesLFP2);

						XYSeriesCollection[] datasetEEGperColumn = new XYSeriesCollection[numOfCols];
						for (int i = 0; i < numOfCols; i++) {
							datasetEEGperColumn[i] = new XYSeriesCollection();
							datasetEEGperColumn[i].addSeries(eegSeries[i]);
						}

						final XYSeriesCollection datasetNeuronTest = new XYSeriesCollection();
						datasetNeuronTest.addSeries(seriesNeuronTest);

						SpikePlotFrame plotFrame = new SpikePlotFrame();
						plotFrame.plotNetwork(numOfCols, allDatasetSpikes, simName, totalTime);

						EegColumnPlotFrame eegFrame = new EegColumnPlotFrame();
						eegFrame.plotNetwork(numOfCols, datasetEEGperColumn, "EEG per column");

						LinePlot eegPlot = new LinePlot("Simulated EEG " + simName, "simulatedEEG");
						eegPlot.draw(datasetEEG, "(E) Simulated EEG ", false, 0, 0, false);

						LinePlot lfpPlot = new LinePlot("Local Field Potential - col 2 " + simName, "lfp1");
						lfpPlot.draw(datasetLFP, " (C) Local Field Potential (Column 2)", true, minLFPplot,
								maxLFPplot, true);
						// lfpPlot.draw(datasetLFP,
						// " (C) Local Field Potential (Stimulated Column)",
						// true, minLFPplot,
						// maxLFPplot);

						LinePlot lfp2Plot = new LinePlot("Local Field Potential - col 3 " + simName, "lfp2");
						lfp2Plot.draw(datasetLFP2, "(D) Local Field Potential (Column 3)", true, minLFPplot,
								maxLFPplot, true);
						// lfp2Plot.draw(datasetLFP2,
						// "(D) Local Field Potential (Adjacent Column)", true,
						// minLFPplot,
						// maxLFPplot);

						LinePlot eeg_lfpPlot = new LinePlot("Local Field Potential - for all columns " + simName,
								"lfpAllCols");
						eeg_lfpPlot.draw(datasetEEG_LFP, "Local Field Potential - for all columns " + simName, false,
								0, 0, true);

						LinePlot neuronTestPlot = new LinePlot("test: neuron" + neuronNumber, "test" + neuronNumber);
						neuronTestPlot.draw(datasetNeuronTest, "test: neuron" + neuronNumber, false, 0, 0, false);

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
