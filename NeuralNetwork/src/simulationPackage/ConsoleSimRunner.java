package simulationPackage;

import java.io.File;
import java.io.IOException;

import networkGUI.ConfigurationUnit;

public class ConsoleSimRunner {
	public static void main(String[] args) throws IOException, InterruptedException {
		// options: seed, simConfig, numOflayers, colconfig
		long seed = Long.parseLong(args[0]);
		String simConfigFileName = args[1];
		int numOfLayers = Integer.parseInt(args[2]);
		String colConfigFileName = args[3];
		String synapseConfigFileName = args[4];
		String simUserName = args[5];

		String simName = simUserName + "_" + simConfigFileName.substring(0, simConfigFileName.length() - 4);

		ConfigurationUnit confUnit = new ConfigurationUnit();

		int colNum = 5;
		confUnit.setNumSim(1);
		confUnit.setNumColumns(colNum);
		confUnit.setTimeStep(0.1);
		confUnit.setTotalTime(1000);

		confUnit.addSim2List(simConfigFileName, synapseConfigFileName, simName);
		for (int i = 0; i < colNum; i++) {
			confUnit.addCol2List(colConfigFileName, numOfLayers);
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
