package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import networkGUI.ConfigurationUnit;
import networkGUI.EegColumnPlotFrame;
import networkGUI.InputPlotFrame;
import networkGUI.LinePlot;
import networkGUI.SpikePlotFrame;
import networkPackage.InputDescriptor;
import networkPackage.Network;
import networkPackage.NetworkBuilder;
import neuronPackage.Layer;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.Status;
import neuronPackage.Type;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Simulator {

	private ProgressListener listener;
	List<XYSeries[]> dataSeries = new ArrayList<XYSeries[]>();
	XYSeries[] eegSeries;
	XYSeries seriesPSP = new XYSeries("Simulated EEG");
	XYSeries seriesLFP = new XYSeries("Local Field Potential - column 2");
	XYSeries seriesLFP2 = new XYSeries("Local Field Potential - column 3");
	XYSeries seriesEEG_LPF = new XYSeries("LFP for all columns");
	FileWriter outFileEEG;

	double timeOfSimulation = 0;

	private List<NetworkNode> allSynapses = new ArrayList<NetworkNode>(); // plus
																			// inputs
	private List<Neuron> allNeurons = new ArrayList<Neuron>();

	ConfigurationUnit configFromGUI;

	List<Status> stats = new ArrayList<Status>();

	private CyclicBarrier statsCreationBarrier = null;

	private CyclicBarrier timeBarrier = null;

	public Simulator(ConfigurationUnit conf) {
		configFromGUI = conf;
	}

	class Worker implements Runnable {
		List<NetworkNode> workNodes;
		double timeStep, totalTime;

		Worker(List<NetworkNode> nodes, double timeStep, double totalTime) {
			workNodes = nodes;
			this.timeStep = timeStep;
			this.totalTime = totalTime;
		}

		@Override
		public void run() {
			try {
				timeBarrier.await();
			} catch (InterruptedException ex) {
				return;
			} catch (BrokenBarrierException ex) {
				return;
			}
			for (double currentTime = 0; currentTime <= totalTime; currentTime += timeStep) {
				timeOfSimulation = currentTime;
				// List<Status> stats = net.nextStep(timeStep,
				// timeOfSimulation);

				// here modify only own list
				for (NetworkNode nod : workNodes) {
					nod.advance(timeStep, currentTime);

				}

				try {
					statsCreationBarrier.await();
				} catch (InterruptedException ex) {
					return;
				} catch (BrokenBarrierException ex) {
					return;
				}

			}
			try {
				timeBarrier.await();
			} catch (InterruptedException ex) {
				return;
			} catch (BrokenBarrierException ex) {
				return;
			}
		}
	}

	public void runSimulation() throws IOException, InterruptedException {
		final int totalTime = configFromGUI.getTotalTime();// 1000;
		final double timeStep = configFromGUI.getTimeStep();

		final List<String[]> simulations = configFromGUI.getAllSimulations();

		// final int tenPercent = (int) (totalTime / (10 * timeStep));

		for (final String[] simDir : simulations) {

			String simName = simDir[1];

			System.out.println(simName + " starting");
			InputDescriptor inDescriptor = new InputDescriptor();
			NetworkBuilder mag = new NetworkBuilder();
			Network net;

			try {
				outFileEEG = new FileWriter("eeg" + simName + ".txt");

				net = mag.createNetwork(simDir[0], configFromGUI, timeStep, totalTime, inDescriptor);

				net.saveToFile("neurons" + simName + ".txt");
				net.initialize(timeStep, 0);
				mag.modifyWeights(net);

				allSynapses = net.getAllSynapses();
				allNeurons = net.getAllNeurons();

				AnalyseNetwork analyser = new AnalyseNetwork();
				analyser.exportConnections(net);
				ArrayList<Integer> numOfNeuronsInColumn = net.getNumberOfNeuronsInColumn();

				int numOfCols = numOfNeuronsInColumn.size();
				eegSeries = new XYSeries[numOfCols];
				System.out.println(simName + " Simulation start");
				System.out.println();

				for (int i = 0; i < numOfCols; i++) {
					XYSeries[] newDataSeries = new XYSeries[4];
					newDataSeries[0] = new XYSeries("RS neurons");
					newDataSeries[1] = new XYSeries("FS neurons");
					newDataSeries[2] = new XYSeries("LTS neurons");
					newDataSeries[3] = new XYSeries("IB neurons");
					dataSeries.add(newDataSeries);
					eegSeries[i] = new XYSeries("Simulated EEG");
				}

				// int neuronNumber = 380;
				// XYSeries seriesNeuronTest = new XYSeries("Neuron number " +
				// neuronNumber);
				int numThreads = 8;

				timeBarrier = new CyclicBarrier(numThreads + 1);
				statsCreationBarrier = new CyclicBarrier(numThreads,
						new Runnable() {
							@Override
							public void run() {

								for (Neuron nod : allNeurons) {
									Status newStat = null;
									newStat = nod.advance(timeStep, timeOfSimulation);
									if (newStat != null) {
										stats.add(newStat);
									}
									nod.setCurrentInput();

								}

								double psp = 0;
								double voltage = 0; // for local field
													// potential
								double voltage2 = 0; // for local field
														// potential in
														// adjacent column
								double voltage_all = 0;
								int numOfCols = eegSeries.length;
								double[] pspPerColumn = new double[numOfCols];
								// int counter = 0;
								for (Status s : stats) {

									int neuronColNum = s.getColumn();

									if (s.fired()) {

										if (s.getType() == Type.RS) {
											dataSeries.get(neuronColNum)[0].add(s.getTime(),
													(s.getNumber() - neuronColNum
													* 764));

										} else {
											if (s.getType() == Type.FS) {
												dataSeries.get(neuronColNum)[1].add(s.getTime(),
														(s.getNumber() - neuronColNum
														* 764));

											} else {
												if (s.getType() == Type.LTS) {
													dataSeries.get(neuronColNum)[2].add(s.getTime(),
															(s.getNumber() - neuronColNum
															* 764));

												} else {
													dataSeries.get(neuronColNum)[3].add(s.getTime(),
															(s.getNumber() - neuronColNum
															* 764));

												}
											}
										}

									}
									if (s.getType() == Type.RS || s.getType() == Type.IB) {
										if ((s.getLayer() == Layer.III) || (s.getLayer() == Layer.V)) {

											if ((s.getColumn() == 1)) {

												voltage += s.getVoltage() / 273;

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
								stats.clear();
								seriesPSP.add(timeOfSimulation, psp / 3000);

								try {
									outFileEEG.write(timeOfSimulation + ", " + psp / 3000 + "\r\n");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								seriesLFP.add(timeOfSimulation, voltage);
								seriesLFP2.add(timeOfSimulation, voltage2);
								seriesEEG_LPF.add(timeOfSimulation, voltage_all / 2000);

								for (int i = 0; i < numOfCols; i++) {
									eegSeries[i].add(timeOfSimulation, pspPerColumn[i] / 3000);
								}
							}
						});

				// here create threads and run

				long t1 = System.currentTimeMillis();
				int totalLength = allSynapses.size();
				int size = totalLength / numThreads + 1;

				for (int i = 0; i < numThreads; i++) {
					new Thread(new Worker(allSynapses.subList(i * size, Math.min((i + 1) * size, totalLength)),
							timeStep, totalTime)).start();

				}
				timeBarrier.await();

				// one more barier
				timeBarrier.await();
				long t2 = System.currentTimeMillis();
				System.out.println(t2 - t1);
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

				// final XYSeriesCollection datasetNeuronTest = new
				// XYSeriesCollection();
				// datasetNeuronTest.addSeries(seriesNeuronTest);

				SpikePlotFrame plotFrame = new SpikePlotFrame();
				plotFrame.plotNetwork(numOfCols, allDatasetSpikes, simName, totalTime);

				EegColumnPlotFrame eegFrame = new EegColumnPlotFrame();
				eegFrame.plotNetwork(numOfCols, datasetEEGperColumn, "EEG per column");

				LinePlot eegPlot = new LinePlot("Simulated EEG " + simName, "simulatedEEG");
				eegPlot.draw(datasetEEG, " Simulated EEG ", false, 0, 0, false);

				LinePlot lfpPlot = new LinePlot("Local Field Potential - col 2 " + simName, "lfp1");
				lfpPlot.draw(datasetLFP,
						" Local Field Potential (stimulated column) ", true,
						minLFPplot,
						maxLFPplot, true);

				LinePlot lfp2Plot = new LinePlot("Local Field Potential - col 3 " + simName, "lfp2");
				lfp2Plot.draw(datasetLFP2,
						"  Local Field Potential (adjacent column)", true,
						minLFPplot,
						maxLFPplot, true);

				LinePlot eeg_lfpPlot = new LinePlot("Local Field Potential - for all columns " + simName,
						"lfpAllCols");
				eeg_lfpPlot.draw(datasetEEG_LFP, "Local Field Potential - for all columns " + simName, false,
						0, 0, true);

				// LinePlot neuronTestPlot = new LinePlot("test: neuron" +
				// neuronNumber, "test" + neuronNumber);
				// neuronTestPlot.draw(datasetNeuronTest, "test: neuron" +
				// neuronNumber, false, 0, 0, false);

				// HistogramPlot hist = new
				// HistogramPlot("Network Activity", "histogram");
				// hist.draw(allDatasetSpikes[1], "Histogram", false, 0,
				// 0, false);

				InputPlotFrame inputFrame = new InputPlotFrame();
				if (!inDescriptor.isEmpty()) {
					inputFrame.plotInputs(inDescriptor);
				}
				listener.reportProgress(10);
				System.out.println(simName + " done");
				outFileEEG.close();

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		System.out.println("done");
	}

	public void setListener(ProgressListener progressListener) {
		this.listener = progressListener;
	}
}
