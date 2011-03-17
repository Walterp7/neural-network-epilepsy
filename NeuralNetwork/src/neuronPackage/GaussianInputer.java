package neuronPackage;

import java.util.Random;

public class GaussianInputer extends Inputer {
	private final double mu, sigma;

	Random generator = new Random(12364579);

	public GaussianInputer() {
		mu = 0;
		sigma = 1;
	}

	public GaussianInputer(double m, double s) {
		mu = m;
		sigma = s;
	}

	@Override
	public void addInput(double val) {

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		for (Neuron n : inputConnections) {
			n.addInput(mu + sigma * generator.nextGaussian());
		}
		return null;
	}

	@Override
	public void setCurrentInput() {

	}

}
