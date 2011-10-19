package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import networkGUI.ColDescr;
import networkGUI.ConfigurationUnit;
import neuronPackage.StpParameters;

public class NetworkBuilder {

	private int totalColumnNumber = 0;
	List<Integer> layersInColumn = new ArrayList<Integer>();
	private double weightMultiplier = 1;
	private double fsInhMultiplier;
	private double ltsInhMultiplier;
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
		parsedLine = newLine.trim().split("\\s+");
		weightMultiplier = Double.parseDouble(parsedLine[0]);
		fsInhMultiplier = Double.parseDouble(parsedLine[1]);
		ltsInhMultiplier = Double.parseDouble(parsedLine[2]);

		// lines with description of inputs
		while ((!(newLine = inSimConf.readLine()).equals("STP configuration"))) {
			if (!(newLine.charAt(0) == '%')) {
				inputs.add(newLine);
			}
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
				// System.out.println(parsedLine[0] + " " + ti + " " + trec +
				// " " + tfac + " " + u + " " + maxy);
				stpParams.put(parsedLine[0], new StpParameters(ti, trec, tfac, u, maxy));
			}
		}
		inSimConf.close();
	}

	public Network createNetwork(String simConfigFile, ConfigurationUnit config, double timestep, double totalTime,
			InputDescriptor inDescriptor) throws IOException { // simConfg
		// general info,colConfList - connections
		loadSimulationConfig(simConfigFile, config);
		Network net = new Network();
		totalColumnNumber = config.getColListSize();
		List<ColumnBuilder> builderList = new ArrayList<ColumnBuilder>();
		// for each column
		for (int colNumber = 0; colNumber < totalColumnNumber; colNumber++) {
			ColumnBuilder newColBuilder = new ColumnBuilder();
			newColBuilder.initialize(config.getColConf(colNumber), layersInColumn.get(colNumber), colNumber,
					weightMultiplier, fsInhMultiplier, ltsInhMultiplier);

			newColBuilder.pushNeurons(net);
			builderList.add(newColBuilder);
		}
		// now neurons are added, we need to connect them
		for (ColumnBuilder colBuilder : builderList) {
			colBuilder.connectNetwork(net, stpParams, timestep);
		}
		InputBuilder inBuild = new InputBuilder();
		inBuild.setInputs(inputs, stpParams, net, inDescriptor, totalTime);
		net.setAllNodes();
		return net;
	}

}
