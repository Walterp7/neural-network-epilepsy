package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import networkGUI.ConfigurationUnit;
import networkGUI.LinePlotChart;
import networkGUI.SpikeChartCollection;
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

public class ConsoleSimulator {

	// FileWriter outFileEEG;
	FileWriter outFileLFP;
	FileWriter outFileIPSP;
	FileWriter outFileEPSP;
	XYSeries[] seriesLFP;

	double timeOfSimulation = 0;
	// private final int[] neurIndx = { 345, 1075, 1825, 2645, 3400 };
	private final int[] neurIndx = { 346, 345, 1077, 1078, 1826, 1827, 2644, 2645, 3498, 3498 };
	private List<NetworkNode> allSynapses = new ArrayList<NetworkNode>(); // plus
	// inputs
	private List<Neuron> allNeurons = new ArrayList<Neuron>();

	ConfigurationUnit configFromFiles;

	List<Status> stats = new ArrayList<Status>();

	private CyclicBarrier statsCreationBarrier = null;

	private CyclicBarrier timeBarrier = null;

	List<XYSeries[]> dataSeries = new ArrayList<XYSeries[]>();

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

			boolean[] neuronsCorrect = { false, false, false, false, false, false, false, false, false, false };

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

					// int[] tempIndexes = { 302, 848, 1394, 1940, 2486 };
					int[] tempIndexes = { 300, 846, 1392, 1939, 2484 };
					Random gen = new Random(2394862);

					Neuron tempNeuron;
					boolean areNeuronsCorrect = false;

					while ((!areNeuronsCorrect)) {
						for (int i = 0; i < neurIndx.length; i++) {
							tempNeuron = net.getNeuron(neurIndx[i]);
							if (tempNeuron != null) {
								if ((!neuronsCorrect[i]) && (tempNeuron.getType() == Type.RS)
										&& (tempNeuron.getLayer() == Layer.V)
										&& (net.getNeuron(neurIndx[i]).getColNum() == i / 2)) {
									neuronsCorrect[i] = true;

								} else {
									neurIndx[i] = (i / 2 + 1) * gen.nextInt(700);
									if (neurIndx[i] <= 10) {
										neurIndx[i] = (i / 2 + 1) * 600;
									}
								}
							} else {
								if (!neuronsCorrect[i]) {
									neurIndx[i] = tempIndexes[i / 2] - 3;
								}
							}

						}
						areNeuronsCorrect = true;
						for (boolean isCorrect : neuronsCorrect) {

							if (!isCorrect) {
								areNeuronsCorrect = false;
							}
						}
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

				int numThreads = 4;

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
								double[] voltage = new double[numOfCols]; // for
																			// local
																			// field
								// potential
								double[] ipsps = new double[neurIndx.length];
								double[] epsps = new double[neurIndx.length];
								// 345 - col 1, layer V, RS
								// 1075 - col 2, layer V, RS
								// 1825 - col 3, layer V, RS
								// 2645 - col 4, layer V, RS

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
											while ((!assigned) && (i < neurIndx.length)) {
												if (s.getNumber() == neurIndx[i]) {
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
									for (int i = 0; i < neurIndx.length; i++) {
										outFileIPSP.write("," + MessageFormat.format("{0,number,#.######}", ipsps[i]));
										outFileEPSP.write("," + MessageFormat.format("{0,number,#.######}", epsps[i]));
									}
									outFileIPSP.write("\n");
									outFileEPSP.write("\n");
									outFileIPSP.flush();
									outFileEPSP.flush();
									StringBuffer lineToWriteLfp = new StringBuffer(MessageFormat.format(
											"{0,number,#.#}", timeOfSimulation));
									for (int i = 0; i < numOfCols; i++) {
										seriesLFP[i].add(timeOfSimulation, voltage[i]);
										lineToWriteLfp.append(","
												+ MessageFormat.format("{0,number,#.#####}", voltage[i]));
									}
									outFileLFP.write(lineToWriteLfp + "\n");
									outFileLFP.flush();
								} catch (IOException e) {
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
}
