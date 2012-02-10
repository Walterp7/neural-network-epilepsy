package networkPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neuronPackage.GaussianInputer;
import neuronPackage.Inputer;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.PickInputer;
import neuronPackage.Status;
import neuronPackage.Synapse;

public class Network {
	private final List<NeuronColumn> allColumns = new ArrayList<NeuronColumn>();
	private final List<NetworkNode> allNodes = new ArrayList<NetworkNode>();
	private final List<Neuron> allNeurons = new ArrayList<Neuron>();
	private final List<Inputer> allInputs = new ArrayList<Inputer>();

	public List<Status> nextStep(double timStep, double timeofSimulation) {
		List<Status> stats = new ArrayList<Status>();

		for (NetworkNode nod : allNodes) {
			Status newStat = null;
			newStat = nod.advance(timStep, timeofSimulation);
			if (newStat != null) {
				stats.add(newStat);
			}
		}
		for (Neuron nod : allNeurons) {

			nod.setCurrentInput();

		}
		return stats;
	}

	void addNeuron(Neuron newNode) {
		allNodes.add(newNode);
	}

	public void addConnection(Synapse newSynapse) {

		allNodes.add(newSynapse);

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
						allNeurons.add(neuron);
						neuron.setId(neurNum++);
					}
				}
			}
		}
		System.out.println("total number of neurons " + neurNum);
		// for (NetworkNode inp : allInputs) {
		// allNodes.add(inp);
		//
		// }
	}

	public void setInputs() {
		for (NetworkNode inp : allInputs) {
			allNodes.add(inp);

		}
	}

	public void initialize(double timeStep, int initTime) {

		GaussianInputer randomInputer = new GaussianInputer();
		PickInputer pickInputer = new PickInputer(0, 5, 10, null);
		for (Neuron nod : allNeurons) {

			randomInputer.addConnection(nod, this);
			pickInputer.addConnection(nod, this);

		}
		allNodes.add(randomInputer);
		allNodes.add(pickInputer);

		for (double t = timeStep; t <= initTime; t += timeStep) {
			nextStep(timeStep, t);
		}
		allNodes.remove(randomInputer);
		allNodes.remove(pickInputer);
		this.setInputs();

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

	public int getNumberOfColumns() {

		return allColumns.size();
	}

	public ArrayList<Integer> getNumberOfNeuronsInColumn() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (NeuronColumn con : allColumns) {
			list.add(con.getNumberOfNeurons());
		}
		return list;
	}

	public void saveToFile(String filename) throws IOException {
		FileWriter outNeurons = new FileWriter(filename);
		for (Neuron neuron : allNeurons) {
			outNeurons.write(neuron.getId() + " " + neuron.getType() + " " + neuron.getLayer());
			for (int i = 0; i < 3; i++) {
				outNeurons.write(" " + neuron.getCoordinates()[i]);
			}

			for (Synapse syn : neuron.getNeuronConnections()) {
				outNeurons.write(" " + syn.getPostSynapticNeuron().getId());
			}
			outNeurons.write("\r\n");
		}
		outNeurons.close();
	}
}
