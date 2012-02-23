package neuronPackage;

import java.util.ArrayList;

import networkPackage.Network;

public class ConstantInputer extends Inputer {
	private final double value;

	protected final ArrayList<Neuron> inputConnections = new ArrayList<Neuron>();

	@Override
	public void addConnection(Neuron n, Network net, double strength) {
		// inputConnections.add(n);

		inputConnections.add(n);

	}

	public ConstantInputer(double v) {
		value = v;

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {

		for (Neuron n : inputConnections) {
			n.addInput(value, timeStep, timeofSimulation);

		}

		return null;
	}

	@Override
	public void setCurrentInput() {

	}

	@Override
	public void addInput(double val, double time, double timeStep) {
		// TODO Auto-generated method stub

	}

}
