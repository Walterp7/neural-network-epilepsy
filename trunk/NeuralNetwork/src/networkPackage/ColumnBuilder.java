package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import neuronPackage.Layer;
import neuronPackage.Neuron;
import neuronPackage.StpParameters;
import neuronPackage.Type;

public class ColumnBuilder {

	int colID;

	HashMap<Type, double[]> neuronParameters = new HashMap<Type, double[]>();

	double[][] proportions;
	Layer[] layerNames;
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

	void initialize(String pathname, int numOfLayers, int colNum)
			throws IOException {

		colID = colNum;
		numberOfLayers = numOfLayers;
		BufferedReader in = new BufferedReader(new FileReader(pathname));

		generator = new Random(19580427 + colNum);
		layerNames = new Layer[numOfLayers];
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
			layerNames[i] = stringToLayer(newLine.trim());
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

				Layer layerName = stringToLayer(parsedLine[0]);
				Type type = stringToType(parsedLine[1]);
				int targetColNum = Integer.parseInt(parsedLine[2]);
				Layer targetLayer = stringToLayer(parsedLine[3]);
				Type targetType = stringToType(parsedLine[4]);
				double prob = Double.parseDouble(parsedLine[5]);
				double newWeight = Double.parseDouble(parsedLine[6]);
				double std = Double.parseDouble(parsedLine[7]);

				// if ((type == Type.LTS)) {
				// newWeight = newWeight * ltsMultiplier;
				// }
				//
				// if ((type == Type.LTS) || (type == Type.FS)) {
				// newWeight = newWeight * fsMultiplier;
				// }
				descr.setDescription(targetColNum, layerName, targetLayer, type,
						targetType, newWeight, std, prob);

				allProbabilities.add(descr);
			}
		}
		in.close();

	}

	void pushNeurons(Network net) {
		Random generator = new Random(391781649);
		NeuronColumn col = new NeuronColumn(colID);
		net.addColumn(col);

		for (int layerNum = 0; layerNum < numberOfLayers; layerNum++) {
			NeuronPool pool = new NeuronPool(layerNames[layerNum]);
			col.addPool(pool);
			NeuronTypePool rsPool = new NeuronTypePool(Type.RS);
			NeuronTypePool ibPool = new NeuronTypePool(Type.IB);
			NeuronTypePool fsPool = new NeuronTypePool(Type.FS);
			NeuronTypePool ltsPool = new NeuronTypePool(Type.LTS);

			for (int i = 0; i < totalNeuronsInPool[layerNum]; i++) {
				double r = 100 * generator.nextDouble();
				double typeRandom = generator.nextDouble();

				Neuron newNeuron;
				if (r <= proportions[layerNum][0]) {

					double[] tempParam = neuronParameters.get(Type.RS)
							.clone();
					tempParam[2] += typeRandom * 5;
					tempParam[3] -= typeRandom * 3;
					newNeuron = new Neuron(tempParam, Type.RS, colID, layerNames[layerNum], generator.nextDouble() * 10);
					newNeuron.setCoordinates(getCoordinates(colID, layerNames[layerNum]));
					rsPool.addNeuron(newNeuron);

				}
				double p = proportions[layerNum][0];

				if ((r > p) && (r <= p + proportions[layerNum][1])) {

					double[] tempParam = neuronParameters.get(Type.IB)
							.clone();
					tempParam[2] -= typeRandom * 5;
					tempParam[3] += typeRandom * 2;
					newNeuron = new Neuron(tempParam, Type.IB, colID, layerNames[layerNum], generator.nextDouble() * 10);
					newNeuron.setCoordinates(getCoordinates(colID, layerNames[layerNum]));
					ibPool.addNeuron(newNeuron);
				}
				p = p + proportions[layerNum][1];
				if ((r > p) && (r <= p + proportions[layerNum][2])) {
					double[] tempParam = neuronParameters.get(Type.FS)
							.clone();
					tempParam[0] -= typeRandom * 0.019;
					tempParam[1] -= typeRandom * 0.025;
					newNeuron = new Neuron(tempParam, Type.FS, colID, layerNames[layerNum], generator.nextDouble() * 10);
					newNeuron.setCoordinates(getCoordinates(colID, layerNames[layerNum]));
					fsPool.addNeuron(newNeuron);
				}
				p = p + proportions[layerNum][2];
				if (r > p) {
					double[] tempParam = neuronParameters.get(Type.LTS)
							.clone();
					tempParam[0] -= typeRandom * 0.019;
					tempParam[1] -= typeRandom * 0.025;
					newNeuron = new Neuron(tempParam, Type.LTS, colID, layerNames[layerNum],
							generator.nextDouble() * 10);
					newNeuron.setCoordinates(getCoordinates(colID, layerNames[layerNum]));
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

	void connectNetwork(Network net, HashMap<String, StpParameters> stpParams, double timeStep) {
		ConnectionsBuilder connectionBuilder = new ConnectionsBuilder();
		connectionBuilder.setUpConnections(net, allProbabilities, stpParams, colID, timeStep);

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

	Layer stringToLayer(String s) throws IOException {

		if (s.equals("III")) {
			return Layer.III;
		}
		if (s.equals("IV")) {
			return Layer.IV;
		}
		if (s.equals("V")) {
			return Layer.V;
		}
		if (s.equals("VI")) {
			return Layer.VI;
		} else {
			System.out.println(s);
			throw new IOException();
		}
	}

	int[] getCoordinates(int col, Layer layer) {
		int[] cords = new int[3]; // microns
		cords[0] = generator.nextInt(401) + col * 400;
		cords[1] = generator.nextInt(401);
		if (layer == Layer.III) {
			cords[2] = generator.nextInt(400);
		}
		if (layer == Layer.IV) {
			cords[2] = generator.nextInt(200) + 400;
		}
		if (layer == Layer.V) {
			cords[2] = generator.nextInt(600) + 600;
		}
		if (layer == Layer.VI) {
			cords[2] = generator.nextInt(600) + 1200;
		}
		return cords;
	}
}
