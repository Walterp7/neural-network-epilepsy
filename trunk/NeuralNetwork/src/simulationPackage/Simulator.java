package simulationPackage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import networkGUI.ConfigurationUnit;
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

		final List<String> simulations = configFromGUI.getAllSimulations();

		final int tenPercent = (int) (totalTime / (10 * timeStep));
		ExecutorService exec = Executors.newFixedThreadPool(simulations.size());
		for (final String simDir : simulations) {
			exec.execute(new Runnable() {

				@Override
				public void run() {

					int orderOfSimulation = simulations.indexOf(simDir);

					String simName = configFromGUI.getSimName(orderOfSimulation);

					System.out.println(simName + " starting");

					NetworkBuilder mag = new NetworkBuilder();
					Network net;

					try {
						net = mag.setUpNetwork(simDir, configFromGUI, timeStep);

						System.out.println(simName + " Simulation start");
						System.out.println();

						XYSeries seriesRS = new XYSeries("RS neurons");
						XYSeries seriesFS = new XYSeries("FS neurons");
						XYSeries seriesLTS = new XYSeries("LTS neurons");
						XYSeries seriesIB = new XYSeries("IB neurons");

						XYSeries seriesPSP = new XYSeries("Simulated EEG");

						for (double timeOfSimulation = 0; timeOfSimulation <= totalTime; timeOfSimulation += timeStep) {
							List<Status> stats = net.nextStep(timeStep, timeOfSimulation);
							// double voltage = 0;
							double psp = 0;
							for (Status s : stats) {
								if (s.fired()) {

									if (s.getType() == Type.RS) {
										seriesRS.add(s.getTime(), s.getNumber());
									} else {
										if (s.getType() == Type.FS) {
											seriesFS.add(s.getTime(), s.getNumber());
										} else {
											if (s.getType() == Type.LTS) {
												seriesLTS.add(s.getTime(), s.getNumber());
											} else {
												seriesIB.add(s.getTime(), s.getNumber());
											}
										}
									}

								}
								if (s.getType() == Type.RS) {
									// voltage += s.getVoltage();
									psp += s.getPSP();
								}

							}

							seriesPSP.add(timeOfSimulation, psp / 3000);

							if ((int) (timeOfSimulation / timeStep) % tenPercent == 0) {
								System.out.println(simName + ' ' + timeOfSimulation);

								listener.reportProgress(timeOfSimulation / totalTime);
							}
						}

						final XYSeriesCollection datasetSpikes = new XYSeriesCollection();
						datasetSpikes.addSeries(seriesLTS);
						datasetSpikes.addSeries(seriesFS);
						datasetSpikes.addSeries(seriesRS);
						datasetSpikes.addSeries(seriesIB);

						final XYSeriesCollection datasetEEG = new XYSeriesCollection();
						datasetEEG.addSeries(seriesPSP);
						// AnalyseNetwork analyser = new AnalyseNetwork();
						// analyser.getDegrees(net);
						DrawNetwork drawer = new DrawNetwork();
						drawer.drawAll(datasetSpikes, datasetEEG, simName);
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
