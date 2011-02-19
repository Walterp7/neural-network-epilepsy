package networkPackage;

import java.util.ArrayList;
import java.util.List;

import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.Synapse;

public class Network {
	// public ArrayList<NetworkNode> allNodes = new ArrayList<NetworkNode>();

	private final List<NeuronColumn> allColumns = new ArrayList<NeuronColumn>();
	private final List<NetworkNode> allNodes = new ArrayList<NetworkNode>();

	public void nextStep(int time) {
		for (NetworkNode nod : allNodes) {
			if (nod instanceof Neuron) {
				// CURRENT INPUT
			}
			nod.advance(time);
		}
		for (NetworkNode nod : allNodes) {
			if (nod instanceof Neuron) {
				nod.setCurrentInput();
			}
		}
	}

	void addNeuron(Neuron newNode) {
		allNodes.add(newNode);
	}

	void addConnection(Neuron pre, Neuron post, double w) {
		Synapse newSynapse = new Synapse(w, post);
		allNodes.add(newSynapse);
		pre.addSynapse(newSynapse);

	}

	public List<NeuronColumn> getAllColumns() {
		return allColumns;
	}

	public List<NetworkNode> getAllNodes() {
		return allNodes;
	}

	public NeuronColumn getColumn(int index) {
		return allColumns.get(index);
	}

	public void setAllNodes() {
		for (NeuronColumn col : allColumns) {
			for (NeuronPool pool : col.getPools()) {
				for (NeuronTypePool typePool : pool.getTypePool()) {
					for (Neuron neuron : typePool.getNeurons()) {
						allNodes.add(neuron);
						for (Synapse syn : neuron.getNeuronConnections()) {
							allNodes.add(syn);
						}
					}
				}
			}
		}
	}

	public void addColumn(NeuronColumn col) {
		allColumns.add(col);
	}
}
