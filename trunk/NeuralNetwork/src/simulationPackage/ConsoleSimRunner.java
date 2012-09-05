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

		String simName = colConfigFileName.substring(0, colConfigFileName.length() - 4)
				+ "_" + simConfigFileName.substring(0, simConfigFileName.length() - 4);

		ConfigurationUnit confUnit = new ConfigurationUnit();

		confUnit.setNumSim(1);
		confUnit.setNumColumns(5);
		confUnit.setTimeStep(0.1);
		confUnit.setTotalTime(1000);

		confUnit.addSim2List(simConfigFileName, simName);
		for (int i = 0; i < 5; i++) {
			confUnit.addCol2List(colConfigFileName, numOfLayers);
		}

		String fullSimName = simName + "s" + ((int) (seed / 100000000)) + "x" + (seed % 10);

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
