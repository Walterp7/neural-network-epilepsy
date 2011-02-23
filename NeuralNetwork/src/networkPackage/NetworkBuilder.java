package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import neuronPackage.FrequencyInputer;
import neuronPackage.GaussianInputer;
import neuronPackage.Inputer;
import neuronPackage.Neuron;
import neuronPackage.Type;

public class NetworkBuilder {

	private final List<ConnectionDescriptor> allProbabilities = new ArrayList<ConnectionDescriptor>();
	HashMap<Type, double[]> neuronParameters = new HashMap<Type, double[]>();
	private int totalColumnNumber = 0;
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

		totalColumnNumber = Integer.parseInt(parsedLine[0]);
		poolNumber = Integer.parseInt(parsedLine[1]);
		weightMultiplier = Integer.parseInt(parsedLine[2]);

		proportions = new double[poolNumber][4];

		for (int i = 0; i < poolNumber; i++) {
			newLine = in.readLine();
			parsedLine = newLine.trim().split("\\s+");
			totalNueronsInPool[i] = Integer.parseInt(parsedLine[0]);
			newLine = in.readLine();
			parsedLine = newLine.trim().split("\\s+");
			for (int j = 0; j < 4; j++) {
				proportions[i][j] = Double.parseDouble(parsedLine[j]);
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
				double newWeight = Double.parseDouble(parsedLine[6])
						* weightMultiplier;

				descr.setDescription(colnum, layerNum, targetLayer, type,
						targetType, newWeight, prob);

				allProbabilities.add(descr);
			}
		}
		in.close();

	}

	public Network setUpNetwork(String configFile, String inputFile)
			throws IOException {
		loadConfig(configFile);
		Network net = new Network();
		pushNeurons(net);
		setUpConnections(net);
		setInputs(inputFile, net);
		net.setAllNodes();
		return net;
	}

	private void setInputs(String pathname, Network net) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(pathname));
		String newLine;
		String[] parsedLine;

		while ((newLine = in.readLine()) != null) {
			parsedLine = newLine.trim().split("\\s+");
			Inputer newInput = null;
			int wordIndex = 0;
			if (parsedLine[wordIndex].equals("Gaussian")) {
				wordIndex++;
				int mean = Integer.parseInt(parsedLine[wordIndex++]);
				int deviation = Integer.parseInt(parsedLine[wordIndex++]);
				newInput = new GaussianInputer(mean, deviation);
			} else {

				if (parsedLine[wordIndex].equals("Step")) {
					wordIndex++;
					int interTime = Integer.parseInt(parsedLine[wordIndex++]);
					int signalTime = Integer.parseInt(parsedLine[wordIndex++]);
					double value = Double.parseDouble(parsedLine[wordIndex++]);
					newInput = new FrequencyInputer(interTime, signalTime,
							value);
				} else {
					throw new IOException();
				}
			}

			List<Type> typesToConnect = new ArrayList<Type>();

			if (!(parsedLine[wordIndex++].equals("*"))) {
				typesToConnect.add(stringToType(parsedLine[3]));
			} else {
				for (Type t : Type.values()) {
					typesToConnect.add(t);
				}
			}

			int colNum = Integer.parseInt(parsedLine[wordIndex++]);
			int poolNum = Integer.parseInt(parsedLine[wordIndex++]);
			List<Integer> columns = new ArrayList<Integer>();
			List<Integer> pools = new ArrayList<Integer>();
			if (colNum >= 0) {
				columns.add(colNum);
			} else {
				for (int i = 0; i < net.numberOfColumns(); i++) {
					columns.add(i);
				}
			}

			if (poolNum >= 0) {
				pools.add(poolNum);
			} else {
				int size = net.numberOfPools();
				for (int i = 0; i < size; i++) {
					pools.add(i);
				}
			}

			for (Type type : typesToConnect) {
				for (Integer col : columns) {
					for (Integer pool : pools) {
						if (net.getColumn(col).getPool(pool).getTypePool(type) != null) {
							ArrayList<Neuron> listNeuron = net.getColumn(col)
									.getPool(pool).getTypePool(type)
									.getNeurons();

							for (Neuron neur : listNeuron) {
								newInput.addConnection(neur);
							}
						}
					}
				}

			}
			net.addInput(newInput);
		}
		in.close();
	}

	void setUpConnections(Network net) {
		Random generator = new Random(19580427);

		for (ConnectionDescriptor conDesc : allProbabilities) {

			for (int colnumber = 0; colnumber < totalColumnNumber; colnumber++) {
				double prob = conDesc.getProbability();
				double weight = conDesc.getWeight();

				NeuronTypePool outPool = net.getColumn(colnumber)
						.getPool(conDesc.getPoolNumber())
						.getTypePool(conDesc.getType());

				ArrayList<NeuronTypePool> inPools = new ArrayList<NeuronTypePool>();

				if (conDesc.getTargetCol() == 0) {
					inPools.add(net.getColumn(colnumber)
							.getPool(conDesc.getTargetPoolNumber())
							.getTypePool(conDesc.getTargetType()));
				} else {
					if (net.getColumn(colnumber + conDesc.getTargetCol()) != null) {
						inPools.add(net
								.getColumn(colnumber + conDesc.getTargetCol())
								.getPool(conDesc.getTargetPoolNumber())
								.getTypePool(conDesc.getTargetType()));
					}
					if (net.getColumn(colnumber - conDesc.getTargetCol()) != null) {
						inPools.add(net
								.getColumn(colnumber - conDesc.getTargetCol())
								.getPool(conDesc.getTargetPoolNumber())
								.getTypePool(conDesc.getTargetType()));
					}
				}
				for (Neuron outNeuron : outPool.getNeurons()) {
					for (NeuronTypePool inP : inPools) {
						if (inP != null) {
							for (Neuron inNeuron : inP.getNeurons()) {
								double r = generator.nextDouble();
								if (r < prob) {
									net.addConnection(outNeuron, inNeuron,
											weight);
								}

							}
						}
					}
				}

			}
		}
	}

	void pushNeurons(Network net) {
		Random generator = new Random(19580427);
		for (int colnum = 0; colnum < totalColumnNumber; colnum++) {
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
					double typeRandom = generator.nextDouble();

					Neuron newNeuron;
					if (r <= proportions[layer][0]) {

						double[] tempParam = neuronParameters.get(Type.RS)
								.clone();
						tempParam[2] += typeRandom * 5;
						tempParam[3] -= typeRandom * 3;
						newNeuron = new Neuron(tempParam, Type.RS);
						rsPool.addNeuron(newNeuron);

					}
					double p = proportions[layer][0];

					if ((r > p) && (r <= p + proportions[layer][1])) {

						double[] tempParam = neuronParameters.get(Type.IB)
								.clone();
						tempParam[2] -= typeRandom * 5;
						tempParam[3] += typeRandom * 2;
						newNeuron = new Neuron(tempParam, Type.IB);
						ibPool.addNeuron(newNeuron);
					}
					p = p + proportions[layer][1];
					if ((r > p) && (r <= p + proportions[layer][2])) {
						double[] tempParam = neuronParameters.get(Type.FS)
								.clone();
						tempParam[0] -= typeRandom * 0.019;
						tempParam[1] -= typeRandom * 0.025;
						newNeuron = new Neuron(tempParam, Type.FS);
						fsPool.addNeuron(newNeuron);
					}
					p = p + proportions[layer][2];
					if (r > p) {
						double[] tempParam = neuronParameters.get(Type.LTS)
								.clone();
						tempParam[0] -= typeRandom * 0.019;
						tempParam[1] -= typeRandom * 0.025;
						newNeuron = new Neuron(tempParam, Type.LTS);
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
					pool.addTypePool(Type.LTS, ltsPool);
				}
				if (!ibPool.isEmpty()) {
					pool.addTypePool(Type.IB, ibPool);
				}

			}
		}
	}

	Type stringToType(String s) throws IOException {

		if (s.equals("RS")) {
			return Type.RS;
		}
		if (s.equals("IB")) {
			return Type.IB;
		}
		if (s.equals("FS")) {
			return Type.FS;
		}
		if (s.equals("LTS")) {
			return Type.LTS;
		} else {
			throw new IOException();
		}
	}

	public NetworkBuilder() {
		double[] rsPar = { 0.02, 0.2, -65, 8 };
		double[] ibPar = { 0.02, 0.2, -55, 4 };
		double[] ltsPar = { 0.02, 0.25, -65, 2 };
		double[] fsPar = { 0.1, 0.2, -65, 2 };
		neuronParameters.put(Type.FS, fsPar);
		neuronParameters.put(Type.IB, ibPar);
		neuronParameters.put(Type.RS, rsPar);
		neuronParameters.put(Type.LTS, ltsPar);
	}

}
