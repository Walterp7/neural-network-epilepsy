package networkPackage;

import java.util.HashMap;
import java.util.Random;

import neuronPackage.Neuron;
import neuronPackage.Type;

public class NeuronStructureBuilder {
	HashMap<Type, double[]> neuronParameters = new HashMap<Type, double[]>();
	Random generator = new Random(19580427);

	public NeuronStructureBuilder() {
		double[] rsPar = { 0.02, 0.2, -65, 8 };
		double[] ibPar = { 0.02, 0.2, -55, 4 };
		double[] ltsPar = { 0.02, 0.25, -65, 2 };
		double[] fsPar = { 0.1, 0.2, -65, 2 };
		neuronParameters.put(Type.FS, fsPar);
		neuronParameters.put(Type.IB, ibPar);
		neuronParameters.put(Type.RS, rsPar);
		neuronParameters.put(Type.LTS, ltsPar);
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

	void pushNeurons(Network net, double[][] proportions,
			int[] totalNueronsInPool, int totalColumnNumber, int poolNumber) {
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
						newNeuron.setCoordinates(getCoordinates(colnum, layer));
						rsPool.addNeuron(newNeuron);

					}
					double p = proportions[layer][0];

					if ((r > p) && (r <= p + proportions[layer][1])) {

						double[] tempParam = neuronParameters.get(Type.IB)
								.clone();
						tempParam[2] -= typeRandom * 5;
						tempParam[3] += typeRandom * 2;
						newNeuron = new Neuron(tempParam, Type.IB);
						newNeuron.setCoordinates(getCoordinates(colnum, layer));
						ibPool.addNeuron(newNeuron);
					}
					p = p + proportions[layer][1];
					if ((r > p) && (r <= p + proportions[layer][2])) {
						double[] tempParam = neuronParameters.get(Type.FS)
								.clone();
						tempParam[0] -= typeRandom * 0.019;
						tempParam[1] -= typeRandom * 0.025;
						newNeuron = new Neuron(tempParam, Type.FS);
						newNeuron.setCoordinates(getCoordinates(colnum, layer));
						fsPool.addNeuron(newNeuron);
					}
					p = p + proportions[layer][2];
					if (r > p) {
						double[] tempParam = neuronParameters.get(Type.LTS)
								.clone();
						tempParam[0] -= typeRandom * 0.019;
						tempParam[1] -= typeRandom * 0.025;
						newNeuron = new Neuron(tempParam, Type.LTS);
						newNeuron.setCoordinates(getCoordinates(colnum, layer));
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
}
