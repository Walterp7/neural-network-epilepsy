package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import networkGUI.ColDescr;
import networkGUI.ConfigurationUnit;

public class NetworkBuilder {

	private int totalColumnNumber = 0;
	List<Integer> layersInColumn = new ArrayList<Integer>();
	private double weightMultiplier = 1;
	private double fsInhMultiplier;
	private double ltsInhMultiplier;

	void loadBasicConfig(String simConfigPath, ConfigurationUnit config) throws IOException {

		totalColumnNumber = config.getColListSize();
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
		inSimConf.close();

	}

	public Network setUpNetwork(String simConfigFile, ConfigurationUnit config, double timestep, double totalTime,
			InputDescriptor inDescriptor) throws IOException { // simConfg
		// general info,colConfList - connections
		loadBasicConfig(simConfigFile, config);
		Network net = new Network();

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
			colBuilder.connectNetwork(net, timestep);
		}
		InputBuilder inBuild = new InputBuilder();
		inBuild.setInputs(simConfigFile, net, inDescriptor, totalTime);
		net.setAllNodes();
		return net;
	}

}
