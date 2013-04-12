package networkPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
	private final List<NetworkNode> allSynapses = new ArrayList<NetworkNode>();
	private final List<Neuron> allNeurons = new ArrayList<Neuron>();
	private final List<Inputer> allInputs = new ArrayList<Inputer>();

	private final CyclicBarrier barrier = null;

	List<Status> stats = new ArrayList<Status>();

	class Worker implements Runnable {
		List<NetworkNode> workNodes;
		double timeStep, timeofSimulation;

		Worker(List<NetworkNode> nodes, double timeStep, double timeofSimulation) {
			workNodes = nodes;
			this.timeStep = timeStep;
			this.timeofSimulation = timeofSimulation;
		}

		@Override
		public void run() {

			for (NetworkNode nod : workNodes) {
				Status newStat = null;
				newStat = nod.advance(timeStep, timeofSimulation);
				if (newStat != null) {
					stats.add(newStat);
				}
			}
			try {
				barrier.await();
			} catch (InterruptedException ex) {
				return;
			} catch (BrokenBarrierException ex) {
				return;
			}

		}
	}

	/*
	 * public List<Status> nextStep(double timStep, double timeofSimulation)
	 * throws InterruptedException, BrokenBarrierException { stats.clear(); int
	 * numThreads = 4;
	 * 
	 * barrier = new CyclicBarrier(numThreads, new Runnable() {
	 * 
	 * @Override public void run() { for (Neuron nod : allNeurons) {
	 * 
	 * nod.setCurrentInput();
	 * 
	 * } } });
	 * 
	 * int totalLength = allNodes.size(); int size = totalLength / numThreads +
	 * 1; for (int i = 0; i < numThreads; i++) { new Thread(new
	 * Worker(allNodes.subList(i * size, Math.min((i + 1) * size, totalLength -
	 * 1)), timStep, timeofSimulation)).start(); } // waitUntilDone();
	 * barrier.await(); return stats; }
	 */
	void addNeuron(Neuron newNode) {
		allNodes.add(newNode);
	}

	public Neuron getNeuron(int number) {
		for (Neuron n : allNeurons) {
			if (n.getId() == number) {
				return allNeurons.get(number);
			}
		}
		return null;
	}

	public void addConnection(NetworkNode newSynapse) {

		// allNodes.add(newSynapse);
		allSynapses.add(newSynapse);

	}

	public List<NeuronColumn> getAllColumns() {
		return allColumns;
	}

	public List<Neuron> getAllNeurons() {
		return allNeurons;
	}

	public List<NetworkNode> getAllNodes() {
		return allNodes;
	}

	public List<NetworkNode> getAllSynapses() {
		return allSynapses;
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
		for (NetworkNode syn : allSynapses) {
			allNodes.add(syn);
		}
		// System.out.println("total number of neurons " + neurNum);
		// for (NetworkNode inp : allInputs) {
		// allNodes.add(inp);
		//
		// }
	}

	public void setInputs() {
		for (NetworkNode inp : allInputs) {
			allNodes.add(inp);
			allSynapses.add(inp);

		}
	}

	public void initialize(double timeStep, int initTime) {

		GaussianInputer randomInputer = new GaussianInputer();
		PickInputer pickInputer = new PickInputer(0, 5, 3.25, null, null, null);
		for (Neuron nod : allNeurons) {

			randomInputer.addConnection(nod, this, 1);
			pickInputer.addConnection(nod, this, 1);

		}
		allNodes.add(randomInputer);
		allNodes.add(pickInputer);

		for (double t = timeStep; t <= initTime; t += timeStep) {
			// nextStep(timeStep, t);

			for (NetworkNode nod : allNodes) {

				nod.advance(timeStep, t);

			}
			for (Neuron nod : allNeurons) {

				nod.setCurrentInput();

			}
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

	public void addInput(Inputer inputer) {
		allInputs.add(inputer);

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
				outNeurons.write(" " + syn.getPostSynapticNeuron().getId() + " " + syn.getWeight());
			}
			outNeurons.write("\r\n");
		}
		outNeurons.close();
	}

	public void removeNeuron(Neuron neur) {
		allNeurons.remove(neur);
		allNodes.remove(neur);
		for (Synapse syn : neur.getNeuronConnections()) {
			allNodes.remove(syn);
		}
	}

	public void removeSynapse(Synapse syn) {
		allNodes.remove(syn);
		allSynapses.remove(syn);
	}
}
