package neuronPackage;

import java.util.LinkedList;
import java.util.List;

public class Synapse implements NetworkNode { // connects node with neuron
	double synapseWeight;
	int timeDelay;
	Neuron postSynapticNeuron;
	Neuron preSynapticNeuron;
	double t1, trec, tfac, U;
	List<Double> inputs = new LinkedList<Double>();
	int isDepressing;
	double x, y, z, u;

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic,
			double[] params) {
		synapseWeight = weight;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
		timeDelay = 1;
		trec = params[0];
		t1 = params[1];
		tfac = params[2];
		U = params[3];
		isDepressing = (int) params[4];
		x = 0;
		z = 0;
		y = 0.5;
		u = U;
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
		double delta = inputs.remove(0); // 1 if post spiked, else 0
		double A = synapseWeight * delta;

		u = u + timeStep * (1 - isDepressing)
				* (u / tfac + delta * U * (1 - u));
		y = y + timeStep * (-y / t1 + delta * u * x);
		x = x + timeStep * (z / trec - delta * u * x);
		z = z + timeStep * (y / t1 - z / trec);
		postSynapticNeuron.addInput(y * A);

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
