package neuronPackage;

import java.util.ArrayList;
import java.util.Random;

import networkPackage.Network;

public class GaussianInputer extends Inputer {
	private final double mu, sigma;

	// Random generator = new Random(12364579);
	// Random generator = new Random(123645711);
	// Random generator = new Random(978645711);
	Random generator = new Random(978649719);

	protected final ArrayList<Neuron> inputConnections = new ArrayList<Neuron>();

	@Override
	public void addConnection(Neuron n, Network net, double strength) {
		// inputConnections.add(n);

		inputConnections.add(n);

	}

	@Override
	public void connect(int colNum, Network network) {

		for (Neuron neur : network.getAllNeurons()) {

			addConnection(neur, network, 1);

		}

	}

	public GaussianInputer() {
		mu = 0;
		sigma = 1;

	}

	public GaussianInputer(double m, double s) {
		mu = m;
		sigma = s;

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {

		for (Neuron n : inputConnections) {

			n.addInput(mu + sigma * generator.nextGaussian(), timeStep, timeofSimulation);

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
