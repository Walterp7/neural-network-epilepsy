package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import networkGUI.ColDescr;
import networkGUI.ConfigurationUnit;
import neuronPackage.Layer;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
import neuronPackage.StpParameters;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class NetworkBuilder {

	private int totalColumnNumber = 0;
	List<Integer> layersInColumn = new ArrayList<Integer>();
	private double weightMultiplier = 1;
	private double fsInhMultiplier = 1;
	private double ltsInhMultiplier = 1;
	private Layer layerToMultiplyFS = null;
	private Layer layerToMultiplyLTS = null;
	private int colNumFS = -1;
	private int colNumLTS = -1;
	private double percentRewired;

	// List<ColLayerPair> layersToRemove = new ArrayList<ColLayerPair>();
	HashMap<Integer, List> layersToRemove = new HashMap<Integer, List>();

	List<String> inputs = new ArrayList<String>();
	HashMap<String, StpParameters> stpParams = new HashMap<String, StpParameters>();
	HashMap<String, PSPparameters> pspParams = new HashMap<String, PSPparameters>();
	HashMap<String, PSPparameters> secondaryPspParams = new HashMap<String, PSPparameters>();

	void loadSimulationConfig(String simConfigPath, String synapseConfigPath, ConfigurationUnit config)
			throws IOException {
		/*
		 * TO DO: simCOnfPath and synapseCOnfigPath should be in
		 * ConfigurationUnit
		 */

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
					fsInhMultiplier = Double.parseDouble(parsedLine[3]);
					if (!parsedLine[1].equals("*")) {
						colNumFS = Integer.parseInt(parsedLine[1]);
					}

					if (!parsedLine[2].equals("*")) {
						layerToMultiplyFS = Layer.valueOf(parsedLine[2]);
					}
				} else {
					if (parsedLine[0].equals("LTS")) {
						ltsInhMultiplier = Double.parseDouble(parsedLine[3]);
						if (!parsedLine[1].equals("*")) {
							colNumLTS = Integer.parseInt(parsedLine[1]);
						}
						if (!parsedLine[2].equals("*")) {
							layerToMultiplyLTS = Layer.valueOf(parsedLine[2]);
						}
					}

				}
			}
			newLine = inSimConf.readLine();
		}

		while ((newLine.charAt(0) == '%')) {
			newLine = inSimConf.readLine();

		}
		// lines with description of lessions
		if (!newLine.startsWith("FALSE")) {

			newLine = inSimConf.readLine();
			percentRewired = Double.parseDouble(newLine);
			newLine = inSimConf.readLine();
			while ((newLine.charAt(0) != '%')) {
				parsedLine = newLine.trim().split("\\s+");
				Integer colKey = Integer.parseInt(parsedLine[0]);
				if (layersToRemove.containsKey(colKey)) {
					layersToRemove.get(colKey).add(Layer.valueOf(parsedLine[1]));
				} else {
					layersToRemove.put(colKey, new ArrayList<Layer>());
					layersToRemove.get(colKey).add(Layer.valueOf(parsedLine[1]));
				}

				newLine = inSimConf.readLine();
			}
		}
		newLine = inSimConf.readLine();
		while ((newLine.charAt(0) == '%')) {
			newLine = inSimConf.readLine();

		}
		// lines with description of inputs
		while ((newLine != null)) {
			if (!(newLine.charAt(0) == '%')) {
				inputs.add(newLine);
			}

			newLine = inSimConf.readLine();
		}
		inSimConf.close();

		BufferedReader stpPspConfig = new BufferedReader(new FileReader(synapseConfigPath));
		// lines with description on STP
		while (((newLine = stpPspConfig.readLine()) != null) && (!newLine.equals("PSP configuration"))) {
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
		// lines with description on PSP
		while (((newLine = stpPspConfig.readLine()) != null) && (!newLine.equals("SECONDARY PSP configuration"))) {
			if (!(newLine.charAt(0) == '%')) {

				parsedLine = newLine.trim().split("\\s+");
				double t1 = Double.parseDouble(parsedLine[1]);
				double t2 = Double.parseDouble(parsedLine[2]);
				double norm = Double.parseDouble(parsedLine[3]);
				double range = Double.parseDouble(parsedLine[4]);

				pspParams.put(parsedLine[0], new PSPparameters(t1, t2, norm, range));
			}
		}
		// lines with secondary description on PSP
		while ((newLine = stpPspConfig.readLine()) != null) {
			if (!(newLine.charAt(0) == '%')) {
				parsedLine = newLine.trim().split("\\s+");
				double t1 = Double.parseDouble(parsedLine[1]);
				double t2 = Double.parseDouble(parsedLine[2]);
				double norm = Double.parseDouble(parsedLine[3]);
				double range = Double.parseDouble(parsedLine[4]);

				secondaryPspParams.put(parsedLine[0], new PSPparameters(t1, t2, norm, range));
			}
		}
		stpPspConfig.close();
	}

	public void modifyWeights(Network net) {
		// System.out.println("modifying weights");
		if ((weightMultiplier != 1) || (fsInhMultiplier != 1) || (ltsInhMultiplier != 1)) {
			for (Neuron neuron : net.getAllNeurons()) {
				double multiplier = weightMultiplier;
				if ((fsInhMultiplier != 1) && (neuron.getType() == Type.FS)) {
					int colToMultiply = colNumFS;
					Layer layerToMultiply = layerToMultiplyFS;
					if (colNumFS == -1) {
						colToMultiply = neuron.getColNum();
					}
					if (layerToMultiplyFS == null) {
						layerToMultiply = neuron.getLayer();

					}

					if ((neuron.getColNum() == colToMultiply) && (neuron.getLayer() == layerToMultiply)) {
						multiplier = multiplier * fsInhMultiplier;
					}

				}
				if ((ltsInhMultiplier != 1) && (neuron.getType() == Type.LTS)) {

					int colToMultiply = colNumLTS;
					Layer layerToMultiply = layerToMultiplyLTS;
					if (colNumLTS == -1) {
						colToMultiply = neuron.getColNum();
					}
					if (layerToMultiplyLTS == null) {
						layerToMultiply = neuron.getLayer();

					}

					if ((neuron.getColNum() == colToMultiply) && (neuron.getLayer() == layerToMultiply)) {
						multiplier = multiplier * ltsInhMultiplier;
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

	private void makeLessions(Network net) {
		if (!layersToRemove.isEmpty()) {
			List<Neuron> neuronsToRemove = new LinkedList<Neuron>();
			List<Synapse> synapsesToRemove = new LinkedList<Synapse>();
			System.out.println("makeLessions");
			Random gen = new Random(2984336201l);
			for (NetworkNode syn : net.getAllSynapses()) {
				Neuron postSynNeuron = ((Synapse) syn).getPostSynapticNeuron();
				Neuron preSynNeuron = ((Synapse) syn).getPreSynapticNeuron();
				if (layersToRemove.containsKey(postSynNeuron.getColNum())) {
					int colNum = postSynNeuron.getColNum();
					if (layersToRemove.get(colNum).contains(postSynNeuron.getLayer())) {

						if (!((preSynNeuron.getColNum() == colNum) && (preSynNeuron.getLayer() == postSynNeuron
								.getLayer()))) { // if the presynaptic neuron
													// isn't in the layer to be
													// removed
							// rewire
							if (percentRewired > 0) {
								int randomNumber = gen.nextInt(101);
								if (randomNumber <= percentRewired * 100) {
									// System.out.println("rewire");
									Type type = preSynNeuron.getType();
									int colToConnect = preSynNeuron.getColNum();
									Layer layer = preSynNeuron.getLayer();
									Neuron newPostSynNeuron = net.getColumn(colToConnect).getPool(layer)
											.getTypePool(type)
											.getRandomNeuron(gen);
									((Synapse) syn).setPostSynapticNeuron(newPostSynNeuron);
								} else {
									// System.out.println("not rewire");
									synapsesToRemove.add((Synapse) syn);
								}

							} else {
								// or not
								synapsesToRemove.add((Synapse) syn);
							}

							if (!neuronsToRemove.contains(postSynNeuron)) {
								neuronsToRemove.add(postSynNeuron);
							}
							postSynNeuron = null;
						} else { // the presynaptic neuron is in the layer to be
									// removed

							if (!neuronsToRemove.contains(preSynNeuron)) {
								neuronsToRemove.add(preSynNeuron);
							}
							if (!neuronsToRemove.contains(postSynNeuron)) {
								neuronsToRemove.add(postSynNeuron);
							}
							synapsesToRemove.add((Synapse) syn);
						}
					}
				}
			}
			for (Neuron neur : neuronsToRemove) {
				net.removeNeuron(neur);
			}
			for (Synapse syn : synapsesToRemove) {
				net.removeSynapse(syn);
			}

		}

	}

	public Network createNetwork(String simConfigFile, String synapseConfigFile, long seed, ConfigurationUnit config,
			double timestep,
			double totalTime,
			InputDescriptor inDescriptor) throws Exception { // simConfg
		// general info,colConfList - connections
		loadSimulationConfig(simConfigFile, synapseConfigFile, config);
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
			colBuilder.connectNetwork(net, stpParams, pspParams, secondaryPspParams, timestep);
		}
		net.setAllNodes();
		makeLessions(net);

		InputBuilder inBuild = new InputBuilder();

		inBuild.setInputs(inputs, stpParams, pspParams, secondaryPspParams, net, inDescriptor, totalTime);

		return net;
	}

}
