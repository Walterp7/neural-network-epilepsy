package neuronPackage;

import java.util.ArrayList;

import networkPackage.Network;

public class ConstantInputer extends Inputer {
	private final double value;

	protected final ArrayList<Neuron> inputConnections = new ArrayList<Neuron>();
	Type type;

	@Override
	public void addConnection(Neuron n, Network net, double strength) {
		// inputConnections.add(n);

		inputConnections.add(n);

	}

	public ConstantInputer(double v, Type type) {
		value = v;
		this.type = type;
	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {

		for (Neuron n : inputConnections) {
			n.addInput(value, timeStep, timeofSimulation);

		}

		return null;
	}

	@Override
	public void connect(int colNum, Network network) {

		for (Neuron neur : network.getAllNeurons()) {
			if (neur.getType() == type) {
				if (type == Type.RS) {
					// if (neur.getLayer() == Layer.IV) {
					addConnection(neur, network, 1);
					// }
				} else {
					addConnection(neur, network, 1);
				}
			}

		}

	}

	@Override
	public void setCurrentInput() {

	}

	@Override
	public void addInput(double val, double time, double timeStep) {
		// TODO Auto-generated method stub

	}

}
