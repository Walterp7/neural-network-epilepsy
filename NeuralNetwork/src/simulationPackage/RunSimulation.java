package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import networkPackage.Network;
import networkPackage.NetworkBuilder;
import neuronPackage.Status;
import neuronPackage.Type;

public class RunSimulation {
	public static void main(String[] args) throws IOException {
		int time = 1000;
		int timeStep = 1;

		NetworkBuilder mag = new NetworkBuilder();
		Network net = mag.setUpNetwork("config_all.txt", "inputs.txt");
		FileWriter outSpikes = new FileWriter("outputSpikes.txt");
		FileWriter outAll = new FileWriter("outputAllNeurons.txt");

		for (int timeOfSimulation = 0; timeOfSimulation <= time; timeOfSimulation += timeStep) {
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
			outAll.write(timeOfSimulation + " " + voltage + " " + psp + "\r\n ");
		}

		// net.printAllNeurons();
		outSpikes.close();
		outAll.close();
		// AnalyseNetwork analyser = new AnalyseNetwork();
		// analyser.getDegrees(net);
		System.out.println("done");
	}
}
