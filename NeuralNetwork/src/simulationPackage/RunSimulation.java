package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import networkPackage.Network;
import networkPackage.NetworkBuilder;
import neuronPackage.Status;
import neuronPackage.Type;

public class RunSimulation {
	public static void main(String[] args) throws IOException, InterruptedException {
		final int time = 500;// 1000;
		final double timeStep = 0.1;

		// String[] dirs = { "settings/synch6", "settings/synch20",
		// "settings/noise", "settings/blockade50" };
		// String[] dirs = { "settings/blockade50", "settings/focal",
		// "settings/synch6" };
		// String[] dirs = { "settings/blockade50", "settings/control",
		// "settings/synch6" };
		String[] dirs = { "settings/blockade50", "settings/control",
				"settings/strTest" };
		// String[] dirs = { "settings/synch6", "settings/synch20" };
		ExecutorService exec = Executors.newFixedThreadPool(dirs.length);
		for (final String dir : dirs) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(dir + " starting");
						System.out.println();
						NetworkBuilder mag = new NetworkBuilder();
						Network net = mag.setUpNetwork(dir + "/config_all.txt",
								dir + "/inputs.txt", timeStep);
						FileWriter outSpikes = new FileWriter(dir + "/outputSpikes.txt");
						FileWriter outAll = new FileWriter(dir + "/outputAllNeurons.txt");
						System.out.println(dir + " Simulation start");
						System.out.println();
						for (double timeOfSimulation = 0; timeOfSimulation <= time; timeOfSimulation += timeStep) {
							List<Status> stats = net.nextStep(timeStep, timeOfSimulation);
							double voltage = 0;
							double psp = 0;
							for (Status s : stats) {
								if (s.fired()) {
									outSpikes.write(s.toString() + "\r\n ");
								}
								if (s.getType() == Type.RS) {
									voltage += s.getVoltage();
									psp += s.getPSP();
								}

							}
							outAll.write(timeOfSimulation + " " + voltage + " "
									+ psp + "\r\n ");
							if ((int) (timeOfSimulation / timeStep) % 1000 == 0) {
								System.out.println(dir + ' ' + timeOfSimulation);
							}
						}

						// net.printAllNeurons();
						outSpikes.close();
						outAll.close();
						// AnalyseNetwork analyser = new AnalyseNetwork();
						// analyser.getDegrees(net);
						System.out.println(dir + " done");
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
}
