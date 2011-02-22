package simulationPackage;

import java.io.IOException;

import networkPackage.Network;
import networkPackage.NetworkBuilder;

public class RunSimulation {
	public static void main(String[] args) throws IOException {
		int time = 3;
		NetworkBuilder mag = new NetworkBuilder();
		Network net = mag.setUpNetwork();
		for (int i = 0; i <= time; i++) {
			System.out.println("Iteration " + i);
			net.nextStep(10);
		}
	}
}
