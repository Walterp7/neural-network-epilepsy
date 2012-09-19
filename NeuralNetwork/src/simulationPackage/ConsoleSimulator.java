package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import networkGUI.ConfigurationUnit;
import networkGUI.LinePlotChart;
import networkGUI.SpikeChartCollection;
import networkPackage.InputDescriptor;
import networkPackage.Network;
import networkPackage.NetworkBuilder;
import networkPackage.NeuronColumn;
import neuronPackage.Layer;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.Status;
import neuronPackage.Type;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ConsoleSimulator {

	// FileWriter outFileEEG;
	FileWriter outFileLFP;
	FileWriter outFileIPSP;
	FileWriter outFileEPSP;
	XYSeries[] seriesLFP;

	double timeOfSimulation = 0;

	private List<NetworkNode> allSynapses = new ArrayList<NetworkNode>(); // plus
	// inputs
	private List<Neuron> allNeurons = new ArrayList<Neuron>();

	private Neuron[] sampleNeurons = null;

	ConfigurationUnit configFromFiles;

	List<Status> stats = new ArrayList<Status>();

	private CyclicBarrier statsCreationBarrier = null;

	private CyclicBarrier timeBarrier = null;

	List<XYSeries[]> dataSeries = new ArrayList<XYSeries[]>();

	ExecutorService executor = Executors.newSingleThreadExecutor();

	public ConsoleSimulator(ConfigurationUnit conf) {
		configFromFiles = conf;
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

	public void runSimulation(long seed, String pathName) throws IOException, InterruptedException {
		final int totalTime = configFromFiles.getTotalTime();// 1000;
		final double timeStep = configFromFiles.getTimeStep();

		final List<String[]> simulations = configFromFiles.getAllSimulations();

		// final int tenPercent = (int) (totalTime / (10 * timeStep));

		for (final String[] simDir : simulations) {

			String simName = simDir[1];

			System.out.println(simName + " starting");
			InputDescriptor inDescriptor = new InputDescriptor();
			NetworkBuilder mag = new NetworkBuilder();

			Network net;
			int numOfColsS = 5;
			try {
				synchronized (ConsoleSimulator.class) {
					// outFileEEG = new FileWriter(pathName + "/eeg" + simName +
					// ".txt");
					outFileLFP = new FileWriter(pathName + "/lfp_" + simName + ".csv");
					outFileIPSP = new FileWriter(pathName + "/ipsp_" + simName + ".csv");
					outFileEPSP = new FileWriter(pathName + "/epsp_" + simName + ".csv");

					net = mag.createNetwork(simDir[0], seed, configFromFiles, timeStep, totalTime, inDescriptor);

					seriesLFP = new XYSeries[numOfColsS];

					for (int i = 0; i < numOfColsS; i++) {
						XYSeries[] newDataSeries = new XYSeries[4];
						newDataSeries[0] = new XYSeries("RS neurons");
						newDataSeries[1] = new XYSeries("FS neurons");
						newDataSeries[2] = new XYSeries("LTS neurons");
						newDataSeries[3] = new XYSeries("IB neurons");

						dataSeries.add(newDataSeries);
						seriesLFP[i] = new XYSeries("Local Field Potential (col " + (i + 1) + ")");
					}

					// System.out.println("initializing");
					net.initialize(timeStep, 300);
					mag.modifyWeights(net);

					allSynapses = net.getAllSynapses();
					allNeurons = net.getAllNeurons();

					ArrayList<Integer> numOfNeuronsInColumn = net.getNumberOfNeuronsInColumn();

					numOfColsS = numOfNeuronsInColumn.size();

				}
				final int numOfCols = numOfColsS;

				initializeSampleNeurons(2, numOfCols, net);

				int numThreads = 4;

				timeBarrier = new CyclicBarrier(numThreads + 1);
				statsCreationBarrier = new CyclicBarrier(numThreads,
						new Runnable() {

							@Override
							public void run() {
								Future future = executor.submit(new Runnable() {

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
										double[] voltage = new double[numOfCols]; // for
																					// local
																					// field
										// potential
										double[] ipsps = new double[sampleNeurons.length];
										double[] epsps = new double[sampleNeurons.length];

										double[] pspPerColumn = new double[numOfCols];
										// int counter = 0;

										for (Status s : stats) {

											int neuronColNum = s.getColumn();

											int totalNeuronsPerColumn = 764;

											if (s.fired()) {

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

													voltage[s.getColumn()] += s.getVoltage() / 1000;

													boolean assigned = false;
													int i = 0;
													while ((!assigned) && (i < sampleNeurons.length)) {
														if (s.getNumber() == sampleNeurons[i].getId()) {
															ipsps[i] = s.getIPSP();
															epsps[i] = s.getEPSP();
															assigned = true;

														}
														i++;

													}

												}

												psp = psp + s.getIPSP() + s.getEPSP();
												pspPerColumn[neuronColNum] = pspPerColumn[neuronColNum] + s.getIPSP()
														+ s.getEPSP();
											}

										}
										stats.clear();

										try {

											outFileIPSP.write(MessageFormat.format("{0,number,#.#}", timeOfSimulation));
											outFileEPSP.write(MessageFormat.format("{0,number,#.#}", timeOfSimulation));
											for (int i = 0; i < sampleNeurons.length; i++) {
												outFileIPSP.write(","
														+ MessageFormat.format("{0,number,#.######}", ipsps[i]));
												outFileEPSP.write(","
														+ MessageFormat.format("{0,number,#.######}", epsps[i]));
											}
											outFileIPSP.write("\n");
											outFileEPSP.write("\n");

											outFileLFP.write(MessageFormat.format("{0,number,#.#}", timeOfSimulation));
											for (int i = 0; i < 5; i++) {
												seriesLFP[i].add(timeOfSimulation, voltage[i]);
												outFileLFP.write(","
														+ MessageFormat.format("{0,number,#.#####}", voltage[i]));
											}
											outFileLFP.write("\n");

										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

								});
								try {
									future.get();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});

				// here create threads and run

				// long t1 = System.currentTimeMillis();
				int totalLength = allSynapses.size();
				int size = totalLength / numThreads + 1;

				for (int i = 0; i < numThreads; i++) {
					new Thread(new Worker(allSynapses.subList(i * size, Math.min((i + 1) * size, totalLength)),
							timeStep, totalTime)).start();

				}
				timeBarrier.await();

				// one more barier
				timeBarrier.await();
				// long t2 = System.currentTimeMillis();
				// System.out.println(t2 - t1);
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
				SpikeChartCollection spikeCharts = new SpikeChartCollection();
				spikeCharts.plotNetwork(numOfCols, allDatasetSpikes, simName, totalTime);
				spikeCharts.save(pathName);

				// timeBarrier.await();
				XYSeries seriesReferenceLine = new XYSeries("reference line -75mV");
				seriesReferenceLine.add(0, -75);
				seriesReferenceLine.add(totalTime, -75);

				final XYSeriesCollection[] datasetLFP = new XYSeriesCollection[numOfCols];
				double maxLFPplot = -1000000;
				double minLFPplot = 30;
				for (int i = 0; i < numOfCols; i++) {

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

				for (int i = 0; i < numOfCols; i++) {
					boolean isStimulated = false;
					if (i == 1) {
						isStimulated = true;
					}
					LinePlotChart lfpPlot = new LinePlotChart("Local Field Potential" + " col" + (i + 1) + " "
							+ simName,
							"lfp" + i);
					lfpPlot.draw(datasetLFP[i],
							" Local Field Potential (col " + (i + 1) + ")", true,
							minLFPplot,
							maxLFPplot, true, isStimulated);
					lfpPlot.save(pathName);
				}

				// outFileEEG.close();
				outFileLFP.close();
				outFileIPSP.close();
				outFileEPSP.close();

			} catch (Throwable e) {

				e.printStackTrace();
			}

		}
		System.out.println(pathName + " done");

		System.exit(0);

	}

	void initializeSampleNeurons(int multiplier, int numberofcolumns, Network net) {
		sampleNeurons = new Neuron[numberofcolumns * multiplier];
		Random gen = new Random(122834762);
		for (int i = 0; i < numberofcolumns; i++) {
			NeuronColumn col = net.getAllColumns().get(i);
			List<Neuron> list = col.getPool(Layer.V).getTypePool(Type.RS).getNeurons();
			sampleNeurons[i] = list.get(gen.nextInt(list.size()));
			sampleNeurons[i + numberofcolumns] = list.get(gen.nextInt(list.size()));

		}

	}
}
