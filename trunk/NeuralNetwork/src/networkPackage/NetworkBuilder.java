package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neuronPackage.Type;

public class NetworkBuilder {

	private final List<ConnectionDescriptor> allProbabilities = new ArrayList<ConnectionDescriptor>();

	private int totalColumnNumber = 0;
	private int poolNumber = 0;
	private double weightMultiplier = 1;
	private double inhMultiplier;
	double[][] proportions;
	int[] totalNueronsInPool = new int[4];

	void loadConfig(String pathname) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(pathname));

		String newLine;
		String[] parsedLine;

		newLine = in.readLine();

		while ((newLine.charAt(0) == '%')) {
			newLine = in.readLine();
		}
		parsedLine = newLine.trim().split("\\s+");
		totalColumnNumber = Integer.parseInt(parsedLine[0]);
		poolNumber = Integer.parseInt(parsedLine[1]);
		weightMultiplier = Integer.parseInt(parsedLine[2]);
		inhMultiplier = Double.parseDouble(parsedLine[3]);

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
			if ((!newLine.trim().equals("")) && (!(newLine.charAt(0) == '%'))) {
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
				if ((type == Type.LTS) || (type == Type.FS)) {
					newWeight = newWeight * inhMultiplier;
				}
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
		InputBuilder inBuild = new InputBuilder();
		ConnectionsBuilder conBuild = new ConnectionsBuilder();
		NeuronStructureBuilder strBuild = new NeuronStructureBuilder();
		strBuild.pushNeurons(net, proportions, totalNueronsInPool,
				totalColumnNumber, poolNumber);
		conBuild.setUpConnections(net, allProbabilities, totalColumnNumber);
		inBuild.setInputs(inputFile, net);
		net.setAllNodes();
		return net;
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

}
