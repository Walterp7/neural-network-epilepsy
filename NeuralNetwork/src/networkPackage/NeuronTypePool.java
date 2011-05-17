package networkPackage;

import java.util.ArrayList;

import neuronPackage.Neuron;
import neuronPackage.Type;

public class NeuronTypePool {
	ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	Type type;
	private final int colNum;
	private final int layNum;

	public NeuronTypePool(Type t, int cNum, int lNum) {
		type = t;
		colNum = cNum;
		layNum = lNum;
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

}
