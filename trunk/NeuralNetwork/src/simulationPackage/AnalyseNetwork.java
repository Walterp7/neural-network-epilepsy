package simulationPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import networkPackage.Network;
import networkPackage.NeuronPool;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.Synapse;
import neuronPackage.Type;

class AnalyseNetwork {

	void exportConnections(Network n) throws IOException {
		FileWriter outFile = new FileWriter("network-nodes.txt");
		for (NeuronPool pool : n.getColumn(0).getPools()) {
			for (Neuron neur : pool.getNeurons()) {

				for (Synapse syn : neur.getNeuronConnections()) {
					Neuron outNeur = syn.getPostSynapticNeuron();
					outFile.write(neur.getId() + neur.typeLayer2String() + "," + outNeur.getId()
							+ outNeur.typeLayer2String() + "," + syn.getWeight() + "," + syn.getTimeDelay() + "\r\n");
				}
			}
		}
		outFile.close();
	}

	void getDegrees(Network n) {
		List<NetworkNode> l = n.getAllNodes();

		HashMap<NetworkNode, Integer> allNodesDeg = new HashMap<NetworkNode, Integer>();

		for (NetworkNode node : l) {
			if (node instanceof Neuron) {
				if (allNodesDeg.containsKey(node)) {
					int deg = allNodesDeg.get(node)
							+ ((Neuron) node).getNeuronConnections().size();
					allNodesDeg.remove(node);
					allNodesDeg.put(node, deg);
				} else {
					int deg = ((Neuron) node).getNeuronConnections().size();
					allNodesDeg.put(node, deg);
				}
			}
			if (node instanceof Synapse) {
				Neuron neur = ((Synapse) node).getPostSynapticNeuron();
				if (allNodesDeg.containsKey(neur)) {
					int deg = allNodesDeg.get(neur) + 1;
					allNodesDeg.remove(neur);
					allNodesDeg.put(neur, deg);
				} else {

					allNodesDeg.put(neur, 1);
				}
			}
		}

		HashMap<Integer, Integer> rsmap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> ltsmap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> fsmap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> ibmap = new HashMap<Integer, Integer>();

		for (Entry<NetworkNode, Integer> entry : allNodesDeg.entrySet()) {
			Neuron neuron = (Neuron) entry.getKey();
			int degree = entry.getValue();
			if (neuron.getType() == Type.RS) {
				if (rsmap.containsKey(degree)) {
					int number = rsmap.get(degree) + 1;
					rsmap.remove(degree);
					rsmap.put(degree, number);
				} else {
					rsmap.put(degree, 1);
				}
			}
			if (neuron.getType() == Type.FS) {
				if (fsmap.containsKey(degree)) {
					int number = fsmap.get(degree) + 1;
					fsmap.remove(degree);
					fsmap.put(degree, number);
				} else {
					fsmap.put(degree, 1);
				}
			}

			if (neuron.getType() == Type.LTS) {
				if (ltsmap.containsKey(degree)) {
					int number = ltsmap.get(degree) + 1;
					ltsmap.remove(degree);
					ltsmap.put(degree, number);
				} else {
					ltsmap.put(degree, 1);
				}
			}

			if (neuron.getType() == Type.IB) {
				if (ibmap.containsKey(degree)) {
					int number = ibmap.get(degree) + 1;
					ibmap.remove(degree);
					ibmap.put(degree, number);
				} else {
					ibmap.put(degree, 1);
				}
			}
		}

		System.out.println("for RS neurons:");
		for (Entry<Integer, Integer> entry : rsmap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		System.out.println("for FS neurons:");
		for (Entry<Integer, Integer> entry : fsmap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		System.out.println("for LTS neurons:");
		for (Entry<Integer, Integer> entry : ltsmap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		System.out.println("for IB neurons:");
		for (Entry<Integer, Integer> entry : ibmap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}
}
