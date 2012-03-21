package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import networkGUI.ColDescr;
import networkGUI.ConfigurationUnit;
import neuronPackage.Neuron;
import neuronPackage.StpParameters;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class NetworkBuilder {

	private int totalColumnNumber = 0;
	List<Integer> layersInColumn = new ArrayList<Integer>();
	private double weightMultiplier = 1;
	private double fsInhMultiplier = 1;
	private double ltsInhMultiplier = 1;

	private String colNumFS = "*";
	private String colNumLTS = "*";

	List<String> inputs = new ArrayList<String>();
	HashMap<String, StpParameters> stpParams = new HashMap<String, StpParameters>();

	void loadSimulationConfig(String simConfigPath, ConfigurationUnit config) throws IOException {

		for (ColDescr colDescr : config.getAllColConfDescr()) {
			layersInColumn.add(colDescr.getLayersInCol());
		}

		BufferedReader inSimConf = new BufferedReader(new FileReader(simConfigPath));

		String newLine;
		String[] parsedLine;

		newLine = inSimConf.readLine();

		while ((newLine.charAt(0) == '%')) {
			newLine = inSimConf.readLine();
		}

		while ((newLine.charAt(0) != '%')) {

			parsedLine = newLine.trim().split("\\s+");
			if (parsedLine[0].equals("ALL")) {
				weightMultiplier = Double.parseDouble(parsedLine[1]);
			} else {
				if (parsedLine[0].equals("FS")) {
					fsInhMultiplier = Double.parseDouble(parsedLine[2]);
					colNumFS = parsedLine[1];
				} else {
					if (parsedLine[0].equals("LTS")) {
						ltsInhMultiplier = Double.parseDouble(parsedLine[2]);
						colNumLTS = parsedLine[1];
					}
				}
			}
			newLine = inSimConf.readLine();
		}

		while ((newLine.charAt(0) == '%')) {
			newLine = inSimConf.readLine();

		}
		// lines with description of inputs
		while ((!newLine.equals("STP configuration"))) {
			if (!(newLine.charAt(0) == '%')) {
				inputs.add(newLine);
			}

			newLine = inSimConf.readLine();
		}
		// lines with description on STP
		while ((newLine = inSimConf.readLine()) != null) {
			if (!(newLine.charAt(0) == '%')) {

				parsedLine = newLine.trim().split("\\s+");
				double ti = Double.parseDouble(parsedLine[1]);
				double trec = Double.parseDouble(parsedLine[2]);
				double tfac = Double.parseDouble(parsedLine[3]);
				double u = Double.parseDouble(parsedLine[4]);
				double maxy = Double.parseDouble(parsedLine[5]);
				stpParams.put(parsedLine[0], new StpParameters(ti, trec, tfac, u, maxy));
			}
		}
		inSimConf.close();
	}

	public void modifyWeights(Network net) {
		System.out.println("modifying weights");
		if (weightMultiplier * fsInhMultiplier * ltsInhMultiplier != 1) {
			for (Neuron neuron : net.getAllNeurons()) {
				double multiplier = weightMultiplier;
				if (fsInhMultiplier != 1) {
					if (neuron.getType() == Type.FS) {
						if (colNumFS.equals("*")) {
							multiplier = multiplier * fsInhMultiplier;
						} else {
							int colNum = Integer.parseInt(colNumFS);
							if (neuron.getColNum() == colNum) {
								multiplier = multiplier * fsInhMultiplier;
							}
						}
					}
				}
				if (ltsInhMultiplier != 1) {
					if (neuron.getType() == Type.LTS) {
						if (colNumFS.equals("*")) {
							multiplier = multiplier * ltsInhMultiplier;
						} else {
							int colNum = Integer.parseInt(colNumLTS);
							if (neuron.getColNum() == colNum) {
								multiplier = multiplier * ltsInhMultiplier;
							}
						}
					}
				}
				if (multiplier != 1) {
					for (Synapse syn : neuron.getNeuronConnections()) {
						syn.multiplyWeight(multiplier);

					}
				}
			}
		}
	}

	public Network createNetwork(String simConfigFile, long seed, ConfigurationUnit config, double timestep,
			double totalTime,
			InputDescriptor inDescriptor) throws Exception { // simConfg
		// general info,colConfList - connections
		loadSimulationConfig(simConfigFile, config);
		Network net = new Network();
		totalColumnNumber = config.getColListSize();
		List<ColumnBuilder> builderList = new ArrayList<ColumnBuilder>();
		// for each column
		for (int colNumber = 0; colNumber < totalColumnNumber; colNumber++) {
			ColumnBuilder newColBuilder = new ColumnBuilder();
			newColBuilder.initialize(config.getColConf(colNumber), layersInColumn.get(colNumber), colNumber);

			newColBuilder.pushNeurons(net, seed);
			builderList.add(newColBuilder);
		}
		// now neurons are added, we need to connect them
		for (ColumnBuilder colBuilder : builderList) {
			colBuilder.connectNetwork(net, stpParams, timestep);
		}
		InputBuilder inBuild = new InputBuilder();
		net.setAllNodes();
		inBuild.setInputs(inputs, stpParams, net, inDescriptor, totalTime);

		return net;
	}

}
