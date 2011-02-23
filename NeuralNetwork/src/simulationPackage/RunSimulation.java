package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import networkPackage.Network;
import networkPackage.NetworkBuilder;
import neuronPackage.Status;

public class RunSimulation {
	public static void main(String[] args) throws IOException {
		int time = 1000;
		int timeStep = 1;

		NetworkBuilder mag = new NetworkBuilder();
		Network net = mag.setUpNetwork("config_all.txt", "inputs.txt");
		FileWriter outFile = new FileWriter("output.txt");

		for (int timeOfSimulation = 0; timeOfSimulation <= time; timeOfSimulation += timeStep) {
			List<Status> stats = net.nextStep(timeStep, timeOfSimulation);
			for (Status s : stats) {
				outFile.write(s.toString() + "\r\n \r\n");
			}
		}

		// net.printAllNeurons();
		outFile.close();
	}
}
