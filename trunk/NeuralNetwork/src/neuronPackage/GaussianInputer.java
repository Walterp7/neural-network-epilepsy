package neuronPackage;

import java.util.Random;

public class GaussianInputer extends Inputer {
	private final double mu, sigma;
	boolean toSpike;

	Random generator = new Random(12364579);

	public GaussianInputer() {
		mu = 0;
		sigma = 1;
		toSpike = true;
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
