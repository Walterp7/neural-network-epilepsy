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
import neuronPackage.Inputer;
import neuronPackage.Layer;
import neuronPackage.NetworkNode;
import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
import neuronPackage.StpParameters;
import neuronPackage.Synapse;
import neuronPackage.ThalamicInputer;
import neuronPackage.Type;

public class NetworkBuilder {

	private int totalColumnNumber = 0;
	private final List<Integer> layersInColumn = new ArrayList<Integer>();
	private final List<ModifyWeightPair> modifDescriptions = new ArrayList<NetworkBuilder.ModifyWeightPair>();
	private double percentRewired;
	private double multiplyerRewired;
	private boolean increaseExcitation = false;
	private boolean increaseExcitation2Lts = false;
	// List<ColLayerPair> layersToRemove = new ArrayList<ColLayerPair>();
	private final HashMap<Integer, List> layersToRemove = new HashMap<Integer, List>();

	private final List<String> inputs = new ArrayList<String>();
	private final HashMap<String, StpParameters> stpParams = new HashMap<String, StpParameters>();
	private final HashMap<String, PSPparameters> pspParams = new HashMap<String, PSPparameters>();
	private final HashMap<String, PSPparameters> secondaryPspParams = new HashMap<String, PSPparameters>();

	private double timeStep = 0.1;

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
			parsedLine = newLine.split("\\s+");
			percentRewired = Double.parseDouble(parsedLine[0]);
			multiplyerRewired = Double.parseDouble(parsedLine[0]);
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
		if (!newLine.startsWith("FALSE")) {

			increaseExcitation = true;

		}
		newLine = inSimConf.readLine();
		while ((newLine.charAt(0) == '%')) {
			newLine = inSimConf.readLine();

		}
		if (!newLine.startsWith("FALSE")) {

			increaseExcitation2Lts = true;

		}
		newLine = inSimConf.readLine();
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
				// System.out.println("modifying weights");
				List<NeuronTypePool> poolsToModify = new ArrayList<NeuronTypePool>();
				if (desc.colNum > -1) {

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

	private void makeLessions(Network net, Random gen) {
		if (!layersToRemove.isEmpty()) {
			List<Neuron> neuronsToRemove = new LinkedList<Neuron>();
			List<Synapse> synapsesToRemove = new LinkedList<Synapse>();
			System.out.println("makeLessions");

			for (NetworkNode syn : net.getAllSynapses()) {
				Neuron postSynNeuron = ((Synapse) syn).getPostSynapticNeuron();
				Neuron preSynNeuron = ((Synapse) syn).getPreSynapticNeuron();
				if (layersToRemove.containsKey(postSynNeuron.getColNum())) {
					int removedColNum = postSynNeuron.getColNum();
					Layer postLayer = postSynNeuron.getLayer();
					if (layersToRemove.get(removedColNum).contains(postLayer)) {

						boolean isPreNeurNotToBeRemoved = (preSynNeuron == null)
								|| (!((preSynNeuron.getColNum() == removedColNum) && (preSynNeuron.getLayer() == postLayer)));

						if (isPreNeurNotToBeRemoved) { // if
														// the
														// presynaptic
														// neuron
							// isn't in the layer to be
							// removed
							// rewire (but only excitatory connections)
							if ((percentRewired > 0)
									&& (((Synapse) syn).getWeight() > 0)) {
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

									int dt;

									if (preSynNeuron == null) {
										dt = ((Synapse) syn).getTimeDelay();
									} else {
										dt = calculateDelay(newPostSynNeuron.getCoordinates(),
												preSynNeuron.getCoordinates(), timeStep);
									}
									((Synapse) syn).setTimeDelay(dt);
									((Synapse) syn).setPostSynapticNeuron(newPostSynNeuron);
									((Synapse) syn).multiplyWeight(multiplyerRewired);

								} else {

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
			// System.out.println("lesions done");
		}

	}

	private void increaseExcitation(Network net, Random gen) {
		// System.out.println("increasing excitation");

		List<NetworkNode> allSynapses = new ArrayList<NetworkNode>(net.getAllSynapses());

		for (Inputer in : net.getAllInputs()) {
			List<Synapse> newSynapses = new ArrayList<Synapse>();
			if (in instanceof ThalamicInputer) {
				for (Synapse syn : ((ThalamicInputer) in).getConnections()) {
					int colNum = syn.getPostSynapticNeuron().getColNum();
					if ((colNum > 0) && (colNum < 4) && (!syn.getPostSynapticNeuron().getLayer().equals(Layer.III))) {
						if (gen.nextDouble() > 0.5) {
							Synapse newSynapse = new Synapse(syn.getWeight(), null,
									syn.getPostSynapticNeuron(), syn.getSTP(), syn.getPSP());
							newSynapse.setTimeDelay(syn.getTimeDelay() + gen.nextInt(syn.getTimeDelay() / 2 + 1)
									+ 1);
							newSynapses.add(newSynapse);

							net.addConnection(newSynapse);

						}
					}
				}
				for (Synapse s : newSynapses) {
					((ThalamicInputer) in).getConnections().add(s);
				}
			}
		}

		for (NetworkNode synapse : allSynapses) {
			Synapse syn = (Synapse) synapse;
			if ((syn.getWeight() > 0) && !(syn.getPreSynapticNeuron() == null)) {
				int colNum = syn.getPostSynapticNeuron().getColNum();
				if ((colNum > 0) && (colNum < 4) && (!syn.getPostSynapticNeuron().getLayer().equals(Layer.III))) {
					if (gen.nextDouble() > 0.5) {
						Synapse newSynapse = new Synapse(syn.getWeight(), syn.getPreSynapticNeuron(),
								syn.getPostSynapticNeuron(), syn.getSTP(), syn.getPSP());
						newSynapse.setTimeDelay(syn.getTimeDelay() + gen.nextInt(syn.getTimeDelay() / 2 + 1)
								+ 1);

						syn.getPreSynapticNeuron().addSynapse(newSynapse);
						net.addConnection(newSynapse);

					}
				}
			}

		}
	}

	private void increaseExcitation2Lts(Network net, Random gen) {
		// System.out.println("increasing lts excitation");

		List<NetworkNode> allSynapses = new ArrayList<NetworkNode>(net.getAllSynapses());

		for (NetworkNode synapse : allSynapses) {
			Synapse syn = (Synapse) synapse;
			if ((syn.getWeight() > 0) && (syn.getPostSynapticNeuron().getType() == Type.LTS)) {
				int colNum = syn.getPostSynapticNeuron().getColNum();
				if ((colNum > 0) && (colNum < 4) && (!syn.getPostSynapticNeuron().getLayer().equals(Layer.III))) {

					Synapse newSynapse = new Synapse(syn.getWeight(), syn.getPreSynapticNeuron(),
							syn.getPostSynapticNeuron(), syn.getSTP(), syn.getPSP());
					newSynapse.setTimeDelay(syn.getTimeDelay() + gen.nextInt(syn.getTimeDelay() / 2 + 1)
							+ 1);

					syn.getPreSynapticNeuron().addSynapse(newSynapse);
					net.addConnection(newSynapse);
					if (!increaseExcitation) {
						newSynapse = new Synapse(syn.getWeight(), syn.getPreSynapticNeuron(),
								syn.getPostSynapticNeuron(), syn.getSTP(), syn.getPSP());
						newSynapse.setTimeDelay(syn.getTimeDelay() + gen.nextInt(syn.getTimeDelay() / 2 + 1)
								+ 1);

						syn.getPreSynapticNeuron().addSynapse(newSynapse);
						net.addConnection(newSynapse);
					}

				}
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
		timeStep = timestep;
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

		InputBuilder inBuild = new InputBuilder();

		inBuild.setInputs(inputs, stpParams, pspParams, secondaryPspParams, net, inDescriptor, totalTime);
		Random gen = new Random(seed);
		makeLessions(net, gen);
		if (increaseExcitation) {
			increaseExcitation(net, gen);
		}

		if (increaseExcitation2Lts) {
			increaseExcitation2Lts(net, gen);
		}

		return net;
	}

	private int calculateDelay(int[] pre, int post[], double timestep) {
		double delay = 0;
		int delayInSteps = 0;
		int n = pre.length;
		for (int i = 0; i < n; i++) {
			delay = delay + (pre[i] - post[i]) * (pre[i] - post[i]); // microns
		}
		// ms -> how many steps
		delayInSteps = (int) (Math.round(Math.sqrt(delay) / (1000 * 0.4 * timestep)));

		if (delayInSteps <= 0) {
			// System.out.println("dist in microns" + delay);
			delayInSteps++;
		}

		return delayInSteps; // not ms, but the number of timesteps
	}

	class ModifyWeightPair {
		int colNum = -1;
		Layer layer = null;
		Type type = null;
		double multiplyer;
	}

}
