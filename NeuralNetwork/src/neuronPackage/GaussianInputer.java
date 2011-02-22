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
	public void advance(double timeStep) {
		double val = mu + sigma * generator.nextGaussian();
		for (Neuron n : inputConnections) {
			n.addInput(val);
		}

	}

	@Override
	public void setCurrentInput() {
		// TODO Auto-generated method stub

	}

}
