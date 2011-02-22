package neuronPackage;

import java.util.ArrayList;

public abstract class Inputer implements NetworkNode {
	protected final ArrayList<Neuron> inputConnections = new ArrayList<Neuron>();

	public void addConnection(Neuron n) {
		inputConnections.add(n);
	}

	@Override
	public void setCurrentInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addInput(double val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void advance(double timeStep) {
		// TODO Auto-generated method stub

	}
}
