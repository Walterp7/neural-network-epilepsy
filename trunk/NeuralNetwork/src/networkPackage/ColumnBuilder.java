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

public class ColumnBuilder {

	int colID;

	HashMap<Type, double[]> neuronParameters = new HashMap<Type, double[]>();

	double[][] proportions;
	int[] totalNeuronsInPool = new int[4];

	int numberOfLayers;
	Random generator;

	private final List<ConnectionDescriptor> allProbabilities = new ArrayList<ConnectionDescriptor>();

	public ColumnBuilder() {
		double[] rsPar = { 0.02, 0.2, -65, 8 };
		double[] ibPar = { 0.02, 0.2, -55, 4 };
		double[] ltsPar = { 0.02, 0.25, -65, 2 };
		double[] fsPar = { 0.1, 0.2, -65, 2 };
		neuronParameters.put(Type.FS, fsPar);
		neuronParameters.put(Type.IB, ibPar);
		neuronParameters.put(Type.RS, rsPar);
		neuronParameters.put(Type.LTS, ltsPar);
	}

	void initialize(String pathname, int numOfLayers, int colNum, double weightMultiplier, double fsMultiplier,
			double ltsMultiplier)
			throws IOException {

		colID = colNum;
		numberOfLayers = numOfLayers;
		BufferedReader in = new BufferedReader(new FileReader(pathname));

		generator = new Random(19580427 + colNum);

		proportions = new double[numOfLayers][4];

		// System.out.println("layers " + numOfLayers);

		String newLine;
		String[] parsedLine;
		newLine = in.readLine();

		while ((newLine.charAt(0) == '%')) {
			newLine = in.readLine();
		}

		for (int i = 0; i < numOfLayers; i++) {
			newLine = in.readLine();
			parsedLine = newLine.trim().split("\\s+");
			totalNeuronsInPool[i] = Integer.parseInt(parsedLine[0]);
			newLine = in.readLine();
			parsedLine = newLine.trim().split("\\s+");
			for (int j = 0; j < 4; j++) {
				proportions[i][j] = Double.parseDouble(parsedLine[j]);
			}

		}

		while ((newLine = in.readLine()) != null) {
			if ((!newLine.trim().equals("")) && (!(newLine.charAt(0) == '%'))) {
				parsedLine = newLine.trim().split("\\s+");

				ConnectionDescriptor descr = new ConnectionDescriptor();

				int layerNum = Integer.parseInt(parsedLine[0]);
				Type type = stringToType(parsedLine[1]);
				int targetColNum = Integer.parseInt(parsedLine[2]);
				int targetLayer = Integer.parseInt(parsedLine[3]);
				Type targetType = stringToType(parsedLine[4]);
				double prob = Double.parseDouble(parsedLine[5]);
				double newWeight = Double.parseDouble(parsedLine[6]) * weightMultiplier;

				if ((type == Type.LTS)) {
					newWeight = newWeight * ltsMultiplier;
				}

				if ((type == Type.LTS) || (type == Type.FS)) {
					newWeight = newWeight * fsMultiplier;
				}
				descr.setDescription(targetColNum, layerNum, targetLayer, type,
						targetType, newWeight, prob);

				allProbabilities.add(descr);
			}
		}
		in.close();

	}

	void pushNeurons(Network net) {

		NeuronColumn col = new NeuronColumn();
		net.addColumn(col);

		for (int layer = 0; layer < numberOfLayers; layer++) {
			NeuronPool pool = new NeuronPool();
			col.addPool(pool);
			NeuronTypePool rsPool = new NeuronTypePool(Type.RS);
			NeuronTypePool ibPool = new NeuronTypePool(Type.IB);
			NeuronTypePool fsPool = new NeuronTypePool(Type.FS);
			NeuronTypePool ltsPool = new NeuronTypePool(Type.LTS);

			for (int i = 0; i < totalNeuronsInPool[layer]; i++) {
				double r = 100 * generator.nextDouble();
				double typeRandom = generator.nextDouble();

				Neuron newNeuron;
				if (r <= proportions[layer][0]) {

					double[] tempParam = neuronParameters.get(Type.RS)
							.clone();
					tempParam[2] += typeRandom * 5;
					tempParam[3] -= typeRandom * 3;
					newNeuron = new Neuron(tempParam, Type.RS);
					newNeuron.setCoordinates(getCoordinates(colID, layer));
					rsPool.addNeuron(newNeuron);

				}
				double p = proportions[layer][0];

				if ((r > p) && (r <= p + proportions[layer][1])) {

					double[] tempParam = neuronParameters.get(Type.IB)
							.clone();
					tempParam[2] -= typeRandom * 5;
					tempParam[3] += typeRandom * 2;
					newNeuron = new Neuron(tempParam, Type.IB);
					newNeuron.setCoordinates(getCoordinates(colID, layer));
					ibPool.addNeuron(newNeuron);
				}
				p = p + proportions[layer][1];
				if ((r > p) && (r <= p + proportions[layer][2])) {
					double[] tempParam = neuronParameters.get(Type.FS)
							.clone();
					tempParam[0] -= typeRandom * 0.019;
					tempParam[1] -= typeRandom * 0.025;
					newNeuron = new Neuron(tempParam, Type.FS);
					newNeuron.setCoordinates(getCoordinates(colID, layer));
					fsPool.addNeuron(newNeuron);
				}
				p = p + proportions[layer][2];
				if (r > p) {
					double[] tempParam = neuronParameters.get(Type.LTS)
							.clone();
					tempParam[0] -= typeRandom * 0.019;
					tempParam[1] -= typeRandom * 0.025;
					newNeuron = new Neuron(tempParam, Type.LTS);
					newNeuron.setCoordinates(getCoordinates(colID, layer));
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

	void connectNetwork(Network net, double timeStep) {
		ConnectionsBuilder connectionBuilder = new ConnectionsBuilder();
		connectionBuilder.setUpConnections(net, allProbabilities, colID, timeStep);

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

	int[] getCoordinates(int col, int layer) {
		int[] cords = new int[3]; // microns
		cords[0] = generator.nextInt(401) + col * 400;
		cords[1] = generator.nextInt(401);
		if (layer == 0) {
			cords[2] = generator.nextInt(400);
		}
		if (layer == 1) {
			cords[2] = generator.nextInt(200) + 400;
		}
		if (layer == 2) {
			cords[2] = generator.nextInt(700) + 600;
		}
		if (layer == 3) {
			cords[2] = generator.nextInt(700) + 1300;
		}
		return cords;
	}
}
