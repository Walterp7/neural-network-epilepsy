package networkPackage;

import java.util.ArrayList;
import java.util.List;

import neuronPackage.Inputer;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.Status;
import neuronPackage.Synapse;

public class Network {
	private final List<NeuronColumn> allColumns = new ArrayList<NeuronColumn>();
	private final List<NetworkNode> allNodes = new ArrayList<NetworkNode>();
	private final List<Inputer> allInputs = new ArrayList<Inputer>();

	public List<Status> nextStep(int time, int timeofSimulation) {
		List<Status> stats = new ArrayList<Status>();

		for (NetworkNode nod : allNodes) {
			Status newStat = null;
			newStat = nod.advance(time, timeofSimulation);
			if (newStat != null) {
				stats.add(newStat);
			}
		}
		for (NetworkNode nod : allNodes) {
			if (nod instanceof Neuron) {
				nod.setCurrentInput();
			}
		}
		return stats;
	}

	void addNeuron(Neuron newNode) {
		allNodes.add(newNode);
	}

	void addConnection(Neuron pre, Neuron post, double w) {
		Synapse newSynapse = new Synapse(w, pre, post);
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
		if ((index < 0) || (index >= allColumns.size())) {
			return null;
		}
		return allColumns.get(index);
	}

	public void setAllNodes() {
		int neurNum = 0;
		for (NeuronColumn col : allColumns) {
			for (NeuronPool pool : col.getPools()) {
				for (NeuronTypePool typePool : pool.getTypePools()) {
					for (Neuron neuron : typePool.getNeurons()) {

						allNodes.add(neuron);
						neuron.setId(neurNum++);
					}
				}
			}
		}
		System.out.println("total number of neurons " + neurNum);
		for (NetworkNode inp : allInputs) {
			allNodes.add(inp);
		}
	}

	public int numberOfColumns() {

		return allColumns.size();
	}

	public int numberOfPools() {

		return allColumns.get(0).numberOfPools();
	}

	public void addColumn(NeuronColumn col) {
		allColumns.add(col);
	}

	public void addInput(Inputer i) {
		allInputs.add(i);

	}

	public void printAllNeurons() {
		for (NetworkNode n : allNodes) {
			if (n instanceof Neuron) {
				System.out.print(((Neuron) n).getId() + " "
						+ ((Neuron) n).getType().ordinal() + ": ");
				for (Synapse s : ((Neuron) n).getNeuronConnections()) {
					System.out.print(s.getPostSynapticNeuron().getId() + "("
							+ s.getPostSynapticNeuron().getType().ordinal()
							+ "), ");
				}
				System.out.println();
			}

		}
		System.out.println("INPUTS");
		int i = 0;
		for (Inputer in : allInputs) {
			System.out.println(i++);
			for (Neuron n : in.getAllInputConnections()) {
				System.out
						.print(n.getId() + " " + n.getType().ordinal() + ", ");

			}
		}
	}
}
