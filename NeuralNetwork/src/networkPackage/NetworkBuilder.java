package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import neuronPackage.Neuron;
import neuronPackage.Type;

public class NetworkBuilder {

	private final List<ConnectionDescriptor> allProbabilities = new ArrayList<ConnectionDescriptor>();
	HashMap<Type, double[]> neuronParameters = new HashMap<Type, double[]>();
	private int columnNumber = 0;
	private int poolNumber = 0;
	private double weightMultiplier = 1;

	double[][] proportions;
	int[] totalNueronsInPool = new int[4];

	void loadConfig(String pathname) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(pathname));

		String newLine;
		String[] parsedLine;

		newLine = in.readLine();
		parsedLine = newLine.trim().split("\\s+");

		columnNumber = Integer.parseInt(parsedLine[0]);
		poolNumber = Integer.parseInt(parsedLine[1]);
		weightMultiplier = Integer.parseInt(parsedLine[3]);

		proportions = new double[poolNumber][4];

		for (int i = 0; i < poolNumber; i++) {
			newLine = in.readLine();
			parsedLine = newLine.trim().split("\\s+");
			totalNueronsInPool[i] = Integer.parseInt(parsedLine[0]);
			newLine = in.readLine();
			parsedLine = newLine.trim().split("\\s+");
			for (int j = 0; j < 4; j++) {
				proportions[i][j] = Integer.parseInt(parsedLine[j]);
			}

		}

		while ((newLine = in.readLine()) != null) {
			if (!newLine.trim().equals("")) {
				parsedLine = newLine.trim().split("\\s+");

				ConnectionDescriptor descr = new ConnectionDescriptor();

				int layerNum = Integer.parseInt(parsedLine[0]);
				Type type = stringToType(parsedLine[1]);
				int colnum = Integer.parseInt(parsedLine[2]);
				int targetLayer = Integer.parseInt(parsedLine[3]);
				Type targetType = stringToType(parsedLine[4]);
				double prob = Double.parseDouble(parsedLine[5]);
				double w = Double.parseDouble(parsedLine[6]) * weightMultiplier;

				descr.setDescription(colnum, layerNum, targetLayer, type,
						targetType, w, prob);

				allProbabilities.add(descr);
			}
		}

	}

	Network setUpNetwork() {

		Network net = new Network();
		pushNeurons(net);
		// set connections
		// for (ConnectionDescriptor conDesc : allProbabilities) {
		// for(NeuronColumn currentColumn: net.getAllColumns()){
		// NeuronTypePool outPool = currentColumn.get
		// }
		//
		// }

		return net;
	}

	void pushNeurons(Network net) {
		Random generator = new Random(19580427);
		for (int colnum = 0; colnum < columnNumber; colnum++) {
			NeuronColumn col = new NeuronColumn();
			net.addColumn(col);
			for (int layer = 0; layer < poolNumber; layer++) {
				NeuronPool pool = new NeuronPool();
				col.addPool(pool);
				NeuronTypePool rsPool = new NeuronTypePool(Type.RS);
				NeuronTypePool ibPool = new NeuronTypePool(Type.IB);
				NeuronTypePool fsPool = new NeuronTypePool(Type.FS);
				NeuronTypePool ltsPool = new NeuronTypePool(Type.LTS);

				for (int i = 0; i < totalNueronsInPool[layer]; i++) {
					double r = 100 * generator.nextDouble();
					Neuron newNeuron;
					if (r <= proportions[layer][0]) {
						newNeuron = new Neuron(neuronParameters.get(Type.RS),
								Type.RS);
						rsPool.addNeuron(newNeuron);
					}
					double p = proportions[layer][0];

					if ((r > p) && (r <= p + proportions[layer][1])) {
						newNeuron = new Neuron(neuronParameters.get(Type.IB),
								Type.IB);
						ibPool.addNeuron(newNeuron);
					}
					p = p + proportions[layer][1];
					if ((r > p) && (r <= p + proportions[layer][2])) {
						newNeuron = new Neuron(neuronParameters.get(Type.FS),
								Type.FS);
						fsPool.addNeuron(newNeuron);
					}
					p = p + proportions[layer][2];
					if (r > p) {
						newNeuron = new Neuron(neuronParameters.get(Type.LTS),
								Type.LTS);
						ltsPool.addNeuron(newNeuron);
					}
				}
				if (!rsPool.isEmpty()) {
					pool.addTypePool(Type.RS, rsPool);
				}
				if (!fsPool.isEmpty()) {
					pool.addTypePool(Type.FS, fsPool);
				}
				if (!ltsPool.isEmpty()) {
					pool.addTypePool(Type.LTS, rsPool);
				}
				if (!ibPool.isEmpty()) {
					pool.addTypePool(Type.IB, rsPool);
				}

			}
		}
	}

	Type stringToType(String s) {

		if (s.equals("RS")) {
			return Type.RS;
		}
		if (s.equals("IB")) {
			return Type.IB;
		}
		if (s.equals("FS")) {
			return Type.FS;
		} else {
			return Type.LTS;
		}
	}

	public NetworkBuilder() {
		double[] rsPar = { 1, 1, 1, 1 };
		double[] ibPar = { 1, 1, 1, 1 };
		double[] ltsPar = { 1, 1, 1, 1 };
		double[] fsPar = { 1, 1, 1, 1 };
		neuronParameters.put(Type.FS, fsPar);
		neuronParameters.put(Type.IB, ibPar);
		neuronParameters.put(Type.RS, rsPar);
		neuronParameters.put(Type.LTS, ltsPar);
	}
	//
	// public void buildNetwork(Network n) {
	// double[] par = { 1, 1, 1, 1 };
	// Neuron n1 = new Neuron(par, Type.RS);
	// Neuron n2 = new Neuron(par, Type.RS);
	// Neuron n3 = new Neuron(par, Type.RS);
	// n.addNeuron(n1);
	// n.addNeuron(n2);
	// n.addNeuron(n3);
	// n.addConnection(n1, n2, 10);
	// n.addConnection(n2, n3, 10);
	// n.addConnection(n3, n1, 10);
	//
	// }

}
