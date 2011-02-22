package neuronPackage;

import java.util.ArrayList;
import java.util.Random;

public abstract class GaussianInputer implements NetworkNode {
	private final double mu, sigma;
	private final ArrayList<Synapse> inputConnections = new ArrayList<Synapse>();
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
		for (Synapse s : inputConnections) {
			s.addInput(val);
		}

	}

	@Override
	public void setCurrentInput() {
		// TODO Auto-generated method stub

	}

	public void addSynapse(Synapse newSyn) {
		inputConnections.add(newSyn);
	}

}
