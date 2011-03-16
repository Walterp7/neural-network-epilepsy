package neuronPackage;

public class Synapse implements NetworkNode { // connects node with neuron
	double synapseWeight;
	double currentValue;
	double nextValue;
	Neuron postSynapticNeuron;
	Neuron preSynapticNeuron;

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic) {
		synapseWeight = weight;
		currentValue = 0;
		nextValue = 0;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
	}

	@Override
	public void addInput(double val) {
		nextValue = synapseWeight * val;
	}

	@Override
	public void setCurrentInput() {
		// currentValue = nextValue;
		// nextValue = 0;
	}

	@Override
	public Status advance(double timeStep, int time) {
		currentValue = nextValue; // in the future PSP calculated
		nextValue = 0;
		postSynapticNeuron.addInput(currentValue);
		currentValue = 0;
		return null;
	}

	public Neuron getPostSynapticNeuron() {
		return postSynapticNeuron;
	}
}
