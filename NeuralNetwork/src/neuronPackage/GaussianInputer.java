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
		// TODO Auto-generated method stub

	}

	@Override
	public Status advance(double timeStep, int timeofSimulation) {
		double val = mu + sigma * generator.nextGaussian();
		for (Neuron n : inputConnections) {
			n.addInput(val);
		}
		return null;
	}

	@Override
	public void setCurrentInput() {
		// TODO Auto-generated method stub

	}

}
