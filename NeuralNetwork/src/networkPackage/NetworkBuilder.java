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
	// private double weightMultiplier = 1;
	// private double fsInhMultiplier = 1;
	// private double ltsInhMultiplier = 1;
	// private Layer layerToMultiplyFS = null;
	// private Layer layerToMultiplyLTS = null;
	// private int colNumFS = -1;
	// private int colNumLTS = -1;
	List<ModifyWeightPair> modifDescriptions = new ArrayList<NetworkBuilder.ModifyWeightPair>();
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
			ModifyWeightPair newPair = new ModifyWeightPair();
			if (parsedLine[0].equals("ALL")) {
				newPair.multiplyer = Double.parseDouble(parsedLine[1]);
			} else {
				newPair.type = Type.valueOf(parsedLine[0]);

				if (!parsedLine[1].equals("*")) {
					newPair.colNum = Integer.parseInt(parsedLine[1]);
				}

				if (!parsedLine[2].equals("*")) {
					newPair.layer = Layer.valueOf(parsedLine[2]);
				}

			}
			modifDescriptions.add(newPair);
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

		for (ModifyWeightPair desc : modifDescriptions) {
			if (desc.type != null) {
				System.out.println("modifying weights");
				List<NeuronTypePool> poolsToModify = new ArrayList<NeuronTypePool>();
				if (desc.colNum > -1) {
					System.out.println("column " + desc.colNum);
					if (desc.layer != null) {
						poolsToModify.add(net.getColumn(desc.colNum).getPool(desc.layer).getTypePool(desc.type));
					} else {
						for (NeuronPool nPool : net.getColumn(desc.colNum).getPools()) {

							poolsToModify.add(nPool.getTypePool(desc.type));
						}
					}
					for (NeuronTypePool tPool : poolsToModify) {
						for (Neuron neur : tPool.getNeurons()) {
							for (Synapse syn : neur.getNeuronConnections()) {
								syn.multiplyWeight(desc.multiplyer);

							}
						}
					}
				} else {
					for (int colNum = 0; colNum < net.getNumberOfColumns(); colNum++) {
						System.out.println("column " + colNum);
						if (desc.layer != null) {
							poolsToModify.add(net.getColumn(colNum).getPool(desc.layer).getTypePool(desc.type));
						} else {
							for (NeuronPool nPool : net.getColumn(colNum).getPools()) {

								poolsToModify.add(nPool.getTypePool(desc.type));
							}
						}
						for (NeuronTypePool tPool : poolsToModify) {
							for (Neuron neur : tPool.getNeurons()) {
								for (Synapse syn : neur.getNeuronConnections()) {
									syn.multiplyWeight(desc.multiplyer);

								}
							}
						}

					}

				}

			} else {
				if (desc.multiplyer != 1) {
					List<Neuron> allNeurons = net.getAllNeurons();
					for (Neuron neur : allNeurons) {
						for (Synapse syn : neur.getNeuronConnections()) {
							syn.multiplyWeight(desc.multiplyer);

						}
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
					int removedColNum = postSynNeuron.getColNum();
					if (layersToRemove.get(removedColNum).contains(postSynNeuron.getLayer())) {

						if (!((preSynNeuron.getColNum() == removedColNum) && (preSynNeuron.getLayer() == postSynNeuron
								.getLayer()))) { // if the presynaptic neuron
													// isn't in the layer to be
													// removed
							// rewire (but only excitatory connections)
							if ((percentRewired > 0)
									&& (preSynNeuron.getType().equals(Type.RS) || preSynNeuron.getType()
											.equals(Type.IB))) {
								int randomNumber = gen.nextInt(101);
								if (randomNumber <= percentRewired * 100) {
									// System.out.println("rewire");
									Type type = postSynNeuron.getType();
									int colToConnect = removedColNum + 2 * gen.nextInt(2) - 1;
									// System.out.println(colToConnect);
									Layer layer = postSynNeuron.getLayer();
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

	class ModifyWeightPair {
		int colNum = -1;
		Layer layer = null;
		Type type = null;
		double multiplyer;
	}

}
