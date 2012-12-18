package neuronPackage;

import java.util.ArrayList;

import networkPackage.Network;

public class ConstantInputer extends Inputer {
	private final double value;

	protected final ArrayList<Neuron> inputConnections = new ArrayList<Neuron>();
	private final Type type;
	private final String layer;

	@Override
	public void addConnection(Neuron n, Network net, double strength) {
		// inputConnections.add(n);

		inputConnections.add(n);

	}

	public ConstantInputer(double v, Type type, String layer) {
		value = v;
		this.type = type;
		this.layer = layer;
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
			if ((colNum == -1) || (neur.getColNum() == colNum)) {
				if (neur.getType() == type) {
					if ((layer.equals("-1")) || (neur.getLayer().toString().equals(layer))) {
						addConnection(neur, network, 1);
					}
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
