package neuronPackage;

import java.util.LinkedList;
import java.util.List;

public class Synapse implements NetworkNode { // connects node with neuron
	double synapseWeight;
	double currentValue;
	double nextValue;
	int timeDelay;
	Neuron postSynapticNeuron;
	Neuron preSynapticNeuron;

	List<Double> inputs = new LinkedList<Double>();

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic) {
		synapseWeight = weight;
		// currentValue = 0;
		// nextValue = 0;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
		timeDelay = 1;
	}

	@Override
	public void addInput(double val) {
		// nextValue = synapseWeight * val;

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
		postSynapticNeuron.addInput(synapseWeight * inputs.remove(0));

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
