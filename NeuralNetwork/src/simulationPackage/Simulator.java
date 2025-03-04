package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import networkGUI.ConfigurationUnit;
import networkGUI.EegColumnPlotFrame;
import networkGUI.InputPlotFrame;
import networkGUI.LinePlotFrame;
import networkGUI.PlotFrame;
import networkGUI.SimulationEndDialog;
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
	XYSeries[] seriesLFP;

	XYSeries seriesEEG_LPF = new XYSeries("LFP for all columns");
	FileWriter outFileEEG;
	FileWriter outFileLFP;
	FileWriter outFileIPSP;
	FileWriter outFileActivity;

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
			int numOfColsS;
			try {
				synchronized (Simulator.class) {
					outFileEEG = new FileWriter("eeg" + simName + ".txt");
					outFileLFP = new FileWriter("data_" + simName + ".csv");
					outFileIPSP = new FileWriter("ipsp_" + simName + ".csv");

					outFileActivity = new FileWriter("activity_" + simName + "11-28-12.txt");

					net = mag.createNetwork(simDir[0], 391781649, configFromGUI, timeStep, totalTime, inDescriptor);

					net.saveToFile("neurons" + simName + "11-09-12.txt");
					// net.initialize(timeStep, 300);
					net.setInputs();
					mag.modifyWeights(net);

					allSynapses = net.getAllSynapses();
					allNeurons = net.getAllNeurons();

					// AnalyseNetwork analyser = new AnalyseNetwork();
					// analyser.exportConnections(net);
					ArrayList<Integer> numOfNeuronsInColumn = net.getNumberOfNeuronsInColumn();

					numOfColsS = numOfNeuronsInColumn.size();
					eegSeries = new XYSeries[numOfColsS];
					seriesLFP = new XYSeries[numOfColsS];
					System.out.println(simName + " Simulation start");
					System.out.println();

					for (int i = 0; i < numOfColsS; i++) {
						XYSeries[] newDataSeries = new XYSeries[4];
						newDataSeries[0] = new XYSeries("RS neurons");
						newDataSeries[1] = new XYSeries("FS neurons");
						newDataSeries[2] = new XYSeries("LTS neurons");
						newDataSeries[3] = new XYSeries("IB neurons");
						seriesLFP[i] = new XYSeries("Local Field Potential (col " + (i + 1) + ")");
						dataSeries.add(newDataSeries);
						eegSeries[i] = new XYSeries("Simulated EEG");
					}
				}
				final int numOfCols = numOfColsS;

				int numThreads = 4;

				timeBarrier = new CyclicBarrier(numThreads + 1);
				statsCreationBarrier = new CyclicBarrier(numThreads,
						new Runnable() {
							@Override
							public void run() {

								for (Neuron nod : allNeurons) {
									Status newStat = null;
									nod.setCurrentInput();
									newStat = nod.advance(timeStep, timeOfSimulation);
									if (newStat != null) {
										stats.add(newStat);
									}

								}

								double psp = 0;
								double[] voltage = new double[numOfCols]; // for
																			// local
																			// field
								// potential
								double[] ipsps = new double[5];
								// 345 - col 1, layer V, RS
								// 1075 - col 2, layer V, RS
								// 1825 - col 3, layer V, RS
								// 2645 - col 4, layer V, RS
								int[] neurIndx = { 345, 1075, 1825, 2645, 3400 };
								int numOfCols = eegSeries.length;
								double[] pspPerColumn = new double[numOfCols];
								// int counter = 0;
								for (Status s : stats) {

									int neuronColNum = s.getColumn();

									int totalNeuronsPerColumn = 764;
									// int totalNeuronsPerColumn = 568;
									if (s.fired()) {
										try {
											outFileActivity.write(MessageFormat.format("{0,number,#.#}",
													timeOfSimulation)
													+ " " + s.getNumber() + "\r\n");
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (s.getType() == Type.RS) {
											dataSeries.get(neuronColNum)[0].add(s.getTime(),
													(s.getNumber() - neuronColNum
															* totalNeuronsPerColumn));

										} else {
											if (s.getType() == Type.FS) {
												dataSeries.get(neuronColNum)[1].add(s.getTime(),
														(s.getNumber() - neuronColNum
																* totalNeuronsPerColumn));

											} else {
												if (s.getType() == Type.LTS) {
													dataSeries.get(neuronColNum)[2].add(s.getTime(),
															(s.getNumber() - neuronColNum
																	* totalNeuronsPerColumn));

												} else {
													dataSeries.get(neuronColNum)[3].add(s.getTime(),
															(s.getNumber() - neuronColNum
																	* totalNeuronsPerColumn));

												}
											}
										}

									}
									if (s.getType() == Type.RS || s.getType() == Type.IB) {
										if ((s.getLayer() == Layer.III) || (s.getLayer() == Layer.V)) {

											voltage[s.getColumn()] += s.getVoltage() / 1000; // just
																								// to
																								// keep
																								// it
																								// normalized

											boolean assigned = false;
											int i = 0;
											while ((!assigned) && (i < neurIndx.length)) {
												if (s.getNumber() == neurIndx[i]) {
													ipsps[i] = s.getIPSP();
													assigned = true;

												}
												i++;
											}

										}

										psp = psp + (s.getIPSP() + s.getEPSP()) / 1000; // keep
																						// normalized
										pspPerColumn[neuronColNum] = pspPerColumn[neuronColNum] + (s.getIPSP()
												+ s.getEPSP()) / 1000;
									}

								}

								stats.clear();
								seriesPSP.add(timeOfSimulation, psp); // for
																		// eeg

								if ((int) timeOfSimulation % 100 == 0) {
									listener.reportProgress(timeOfSimulation / totalTime);
								}
								try {
									outFileEEG.write(MessageFormat.format("{0,number,#.#}", timeOfSimulation) + ", "
											+ psp + "\r\n");

									outFileIPSP.write(MessageFormat.format("{0,number,#.#}", timeOfSimulation));
									for (int i = 0; i < neurIndx.length; i++) {
										outFileIPSP.write("," + MessageFormat.format("{0,number,#.#####}", ipsps[i]));
									}
									outFileIPSP.write("\r\n");
									String lineToWriteLfp = ""
											+ MessageFormat.format("{0,number,#.#}", timeOfSimulation);

									for (int i = 0; i < numOfCols; i++) {
										eegSeries[i].add(timeOfSimulation, pspPerColumn[i]);
										seriesLFP[i].add(timeOfSimulation, voltage[i]);

										lineToWriteLfp = lineToWriteLfp + ","
												+ MessageFormat.format("{0,number,#.#####}", (voltage[i]));
									}

									outFileLFP.write(lineToWriteLfp + "\r\n");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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

				List<PlotFrame> plots = new ArrayList<PlotFrame>();

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

				final XYSeriesCollection datasetEEG = new XYSeriesCollection();
				datasetEEG.addSeries(seriesPSP);

				final XYSeriesCollection[] datasetLFP = new XYSeriesCollection[numOfCols];

				XYSeriesCollection[] datasetEEGperColumn = new XYSeriesCollection[numOfCols];
				double maxLFPplot = -1000000;
				double minLFPplot = 30;
				for (int i = 0; i < numOfCols; i++) {
					datasetEEGperColumn[i] = new XYSeriesCollection();
					datasetEEGperColumn[i].addSeries(eegSeries[i]);
					datasetLFP[i] = new XYSeriesCollection();
					datasetLFP[i].addSeries(seriesReferenceLine);
					datasetLFP[i].addSeries(seriesLFP[i]);
					if (seriesLFP[i].getMaxY() > maxLFPplot) {
						maxLFPplot = seriesLFP[i].getMaxY();
					}
					if (minLFPplot > seriesLFP[i].getMinY()) {
						minLFPplot = seriesLFP[i].getMinY();
					}
				}

				SpikePlotFrame plotFrame = new SpikePlotFrame();
				plotFrame.plotNetwork(numOfCols, allDatasetSpikes, simName, totalTime);
				plots.add(plotFrame);

				EegColumnPlotFrame eegFrame = new EegColumnPlotFrame();
				eegFrame.plotNetwork(numOfCols, datasetEEGperColumn, "EEG per column");
				plots.add(eegFrame);

				LinePlotFrame eegPlot = new LinePlotFrame("Simulated EEG " + simName, "simulatedEEG");
				eegPlot.draw(datasetEEG, " Simulated EEG ", false, 0, 0, false, false);
				plots.add(eegPlot);

				for (int i = 0; i < numOfCols; i++) {
					boolean isStimulated = false;
					if (i == 1) {
						isStimulated = true;
					}
					LinePlotFrame lfpPlot = new LinePlotFrame("Local Field Potential" + " col" + (i + 1) + " "
							+ simName,
							"lfp" + i);
					lfpPlot.draw(datasetLFP[i],
							" Local Field Potential (col " + (i + 1) + ")", true,
							minLFPplot,
							maxLFPplot, true, isStimulated);
					plots.add(lfpPlot);
				}

				InputPlotFrame inputFrame = new InputPlotFrame();
				if (!inDescriptor.isEmpty()) {
					inputFrame.plotInputs(inDescriptor);
				}
				plots.add(inputFrame);

				SimulationEndDialog endDialog = new SimulationEndDialog(plots, "data_" + simName + ".csv", "ipsp_"
						+ simName + ".csv");
				listener.reportProgress(10);
				System.out.println(simName + " done");
				outFileEEG.close();
				outFileLFP.close();
				outFileIPSP.close();
				outFileActivity.close();

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
