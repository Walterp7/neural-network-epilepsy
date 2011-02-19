package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import neuronPackage.Neuron;
import neuronPackage.Type;

public class NetworkBuilder {

	private final HashMap<Type, double[]> neuronParameters = new HashMap<Type, double[]>();
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

		// while ((newLine = in.readLine()) != null)

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

	public void buildNetwork(Network n) {
		double[] par = { 1, 1, 1, 1 };
		Neuron n1 = new Neuron(par, Type.RS);
		Neuron n2 = new Neuron(par, Type.RS);
		Neuron n3 = new Neuron(par, Type.RS);
		n.addNeuron(n1);
		n.addNeuron(n2);
		n.addNeuron(n3);
		n.addConnection(n1, n2, 10);
		n.addConnection(n2, n3, 10);
		n.addConnection(n3, n1, 10);

	}

}
