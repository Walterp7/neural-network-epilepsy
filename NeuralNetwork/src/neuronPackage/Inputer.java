package neuronPackage;

import networkPackage.Network;

public abstract class Inputer implements NetworkNode {

	public void addConnection(Neuron n, Network net) {
		// inputConnections.add(n);

	}

	@Override
	public void setCurrentInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		// TODO Auto-generated method stub
		return null;
	}

}
