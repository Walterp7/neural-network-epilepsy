package networkPackage;

import java.util.ArrayList;
import java.util.Random;

import neuronPackage.Neuron;
import neuronPackage.Type;

public class NeuronTypePool {
	ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	Type type;

	public NeuronTypePool(Type t) {
		type = t;

	}

	public boolean isEmpty() {

		return neurons.isEmpty();
	}

	void addNeuron(Neuron newNeuron) {
		if (newNeuron.getType() != type) {
			// throw exception
		}
		neurons.add(newNeuron);
	}

	public ArrayList<Neuron> getNeurons() {

		return neurons;
	}

	public void setNeurons(ArrayList<Neuron> neurons) {
		this.neurons = neurons;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Neuron getRandomNeuron(Random gen, double fraction) {
		return neurons.get((gen.nextInt((int) (fraction * neurons.size()))));
	}
}
