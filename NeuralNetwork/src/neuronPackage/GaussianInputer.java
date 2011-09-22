package neuronPackage;

import java.util.ArrayList;
import java.util.Random;

import networkPackage.Network;

public class GaussianInputer extends Inputer {
	private final double mu, sigma;

	Random generator = new Random(12364579);

	protected final ArrayList<Neuron> inputConnections = new ArrayList<Neuron>();

	@Override
	public void addConnection(Neuron n, Network net) {
		// inputConnections.add(n);

		inputConnections.add(n);

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
