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
	// int isDepressing;
	double x, y, u;
	double lastSpike;
	double xls, yls, uls;

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic,
			StpParameters params) {
		synapseWeight = weight;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
		timeDelay = 1;
		stpParam = params;
		// isDepressing = (int) params[4];
		lastSpike = 0;
		x = 1;
		y = 0;
		u = 0;
		xls = 1;
		yls = 0;
		uls = 0;
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
		// currentValue = nextValue; // in the future PSP calculated
		// nextValue = 0;
		boolean isSTP = true;
		if (isSTP) {
			double delta = inputs.remove(0); // 1 if post spiked, else 0
			double A = synapseWeight * delta;
			if (delta > 0) {
				double dt = time - lastSpike;
				double tfac = stpParam.getTfac();
				double ti = stpParam.getTi();
				double trec = stpParam.getTrec();
				double U = stpParam.getU();
				u = uls * Math.exp(-dt / tfac);
				x = xls * Math.exp(-dt / trec) + yls * ti * (Math.exp(-dt / trec) - Math.exp(-dt / ti)) / (ti - trec)
						+ 1 - Math.exp(-dt / trec);
				y = yls * Math.exp(-dt / ti);

				u = u + U * (1 - u);
				x = x - x * (u + U * (1 - u));
				y = y + x * (u + U * (1 - u));

				postSynapticNeuron.addInput(y * A);
			} else {
				postSynapticNeuron.addInput(A);
			}
			//
			// u = u + timeStep * (1 - isDepressing)
			// * (u / tfac + delta * U * (1 - u));
			// y = y + timeStep * (-y / t1 + delta * u * x);
			// x = x + timeStep * (z / trec - delta * u * x);
			// z = z + timeStep * (y / t1 - z / trec);

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
}
