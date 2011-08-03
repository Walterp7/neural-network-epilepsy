package neuronPackage;

import java.util.LinkedList;
import java.util.List;

import networkPackage.StpParameters;

public class Synapse implements NetworkNode { // connects node with neuron
	double synapseWeight;
	int timeDelay;
	Neuron postSynapticNeuron;
	Neuron preSynapticNeuron;
	// double ti, trec, tfac, U;
	StpParameters stpParam;
	List<Double> inputs = new LinkedList<Double>();

	double x, y, u;
	double lastSpike;

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic,
			StpParameters params) {
		synapseWeight = weight;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
		timeDelay = 1;
		stpParam = params;
		lastSpike = 0;
		x = 1;
		y = 0;
		u = 0;

	}

	@Override
	public void addInput(double val) {
		inputs.add(val);
	}

	@Override
	public void setCurrentInput() {
		// currentValue = nextValue;
		// nextValue = 0;
	}

	@Override
	public Status advance(double timeStep, double time) {

		boolean isSTP = true;
		if (isSTP) {
			double delta = inputs.remove(0); // 1 if pre spiked, else 0
			double A = synapseWeight * delta;
			if (delta > 0) {
				double dt = time - lastSpike;
				double tfac = stpParam.getTfac();
				double ti = stpParam.getTi();
				double trec = stpParam.getTrec();
				double U = stpParam.getU();
				double maxY = stpParam.getMaxY();

				u = u * Math.exp(-dt / tfac) * (1 - U) + U;
				double xtmp = (x * Math.exp(-dt / trec) + y * ti * (Math.exp(-dt / trec) - Math.exp(-dt / ti))
						/ (ti - trec)
						+ 1 - Math.exp(-dt / trec));

				x = xtmp * (1 - u);
				y = y + xtmp * u;

				postSynapticNeuron.addInput(y * A / maxY);

			} else {
				postSynapticNeuron.addInput(A);
			}

		} else {
			postSynapticNeuron.addInput(synapseWeight * inputs.remove(0));
		}
		return null;
	}

	public Neuron getPostSynapticNeuron() {
		return postSynapticNeuron;
	}

	public void setTimeDelay(int value) {
		if (value <= 0) {
			value = 1;
		}
		timeDelay = value;
		double zero = 0;
		for (int i = 0; i < value; i++) {
			inputs.add(zero);
		}
	}

	public int getTimeDelay() {
		return timeDelay;
	}

	public double getWeight() {
		return synapseWeight;
	}
}
