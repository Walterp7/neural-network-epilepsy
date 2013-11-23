package simulationPackage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import networkGUI.ConfigurationUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ConsoleSimRunner {
	public static void main(String[] args) throws IOException, InterruptedException, ParseException {
		// options: seed, simConfig, numOflayers, colconfig
		long seed = 1234565l; // = Long.parseLong(args[0]);
		String simConfigFileName = null; // = args[1];
		int numOfLayers = 4; // = Integer.parseInt(args[2]);
		// String colConfigFileName = null; // = args[3];
		Map<Integer, String> colConfigs = new HashMap<Integer, String>();

		String synapseConfigFileName = null; // = args[4];
		String simUserName = null; // = args[5];

		Options options = new Options();
		options.addOption("s", true, "Seed");
		options.addOption("sim", true, "Simulation config file");
		options.addOption("syn", true, "Synapse config file");
		options.addOption("dcol", true, "Default column config file");
		options.addOption("1col", true, "First column config file");
		options.addOption("2col", true, "Second column config file");
		options.addOption("3col", true, "Third column config file");
		options.addOption("4col", true, "Fourth column config file");
		options.addOption("5col", true, "Fifth column config file");
		options.addOption("name", true, "Simulation name");
		options.addOption("t", true, "Time");
		options.addOption("l", true, "Number of layers");
		options.addOption("c", true, "Number of columns");

		GnuParser parser = new GnuParser();
		CommandLine cmd = parser.parse(options, args);

		ConfigurationUnit confUnit = new ConfigurationUnit();
		if (cmd.hasOption("t")) {
			confUnit.setTotalTime(Integer.parseInt(cmd.getOptionValue("t")));

		}
		else {
			System.out.println("No time of simulation provided");
			return;
		}
		if (cmd.hasOption("s")) {
			seed = Long.parseLong(cmd.getOptionValue("s"));

		}
		else {
			System.out.println("No seed provided - default assumed");

		}
		if (cmd.hasOption("dcol")) {
			colConfigs.put(0, cmd.getOptionValue("dcol"));

		}
		if (cmd.hasOption("1col")) {
			colConfigs.put(1, cmd.getOptionValue("1col"));

		}
		if (cmd.hasOption("2col")) {
			colConfigs.put(2, cmd.getOptionValue("2col"));

		}
		if (cmd.hasOption("3col")) {
			colConfigs.put(3, cmd.getOptionValue("3col"));

		}
		if (cmd.hasOption("4col")) {
			colConfigs.put(4, cmd.getOptionValue("4col"));

		}
		if (cmd.hasOption("5col")) {
			colConfigs.put(5, cmd.getOptionValue("5col"));

		}
		if (colConfigs.isEmpty()) {
			System.out.println("No configuration file for columns given.");
			return;
		}
		if (cmd.hasOption("sim")) {
			simConfigFileName = cmd.getOptionValue("sim");
		}
		else {
			System.out.println("No configuration file for simulation given.");
			return;
		}
		if (cmd.hasOption("syn")) {
			synapseConfigFileName = cmd.getOptionValue("syn");
		}
		else {
			System.out.println("No configuration file for synapse connection given.");
			return;
		}
		if (cmd.hasOption("name")) {
			simUserName = cmd.getOptionValue("name");
		}
		else {
			System.out.println("No name of simulation provided");
			simUserName = "sim";
		}
		int colNum = 5;
		if (cmd.hasOption("c")) {
			colNum = Integer.parseInt(cmd.getOptionValue("c"));
			confUnit.setNumColumns(colNum);
		}
		else {
			System.out.println("Number of columns not specified. Default value (" + colNum + ") assumed.");
			confUnit.setNumColumns(colNum);
		}

		if (cmd.hasOption("l")) {
			numOfLayers = Integer.parseInt(cmd.getOptionValue("l"));

		}
		else {
			System.out.println("Number of columns not specified. Default value (" + numOfLayers + ") assumed.");

		}
		String simName = simUserName + "_" + simConfigFileName.substring(0, simConfigFileName.length() - 4);

		confUnit.setNumSim(1);

		confUnit.setTimeStep(0.1);

		confUnit.addSim2List(simConfigFileName, synapseConfigFileName, simName);
		for (int i = 1; i <= colNum; i++) {
			if (colConfigs.containsKey(i)) {
				confUnit.addCol2List(colConfigs.get(i), numOfLayers);
			} else {
				if (colConfigs.containsKey(0)) {
					confUnit.addCol2List(colConfigs.get(0), numOfLayers);
				} else {
					System.out.println("No config for columns " + i);
					return;
				}
			}
		}

		String fullSimName = simName + "s" + seed;

		ConsoleSimulator sim = new ConsoleSimulator(confUnit);
		String pathName = fullSimName;
		// String pathName = simName;
		File file = new File(pathName);
		if (!file.exists()) {
			new File(file.getAbsolutePath()).mkdir();
		}
		// sim.runSimulation(seed, simName + "s" + seed);
		// sim.runSimulation(seed, simName);
		sim.runSimulation(seed, fullSimName);
	}
}
