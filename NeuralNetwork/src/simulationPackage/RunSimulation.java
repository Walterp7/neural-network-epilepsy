package simulationPackage;

import networkPackage.Network;
import networkPackage.NetworkBuilder;

public class RunSimulation {
	public static void main(String[] args) {
		Network net = new Network();
		NetworkBuilder mag = new NetworkBuilder();
		// mag.buildNetwork(net);
		for (int i = 0; i <= 3; i++) {
			System.out.println("Iteration " + i);
			net.nextStep(10);
		}
	}
}
