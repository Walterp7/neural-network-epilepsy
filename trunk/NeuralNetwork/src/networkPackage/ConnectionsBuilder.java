package networkPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import neuronPackage.Neuron;
import neuronPackage.StpParameters;

public class ConnectionsBuilder {
	final private double velocity = 0.4; // m/s

	int calculateDelay(int[] pre, int post[], double timestep) {
		int delay = 0;
		int n = pre.length;
		for (int i = 0; i < n; i++) {
			delay = delay + (pre[i] - post[i]) * (pre[i] - post[i]); // microns
		}
		// ms -> how many steps
		delay = (int) (Math.sqrt(delay) / (1000 * velocity * timestep));

		if (delay <= 0) {
			// System.out.println(delay);
			delay++;
		}

		return delay; // ms -> how many timesteps
	}

	public void setUpConnections(Network net,
			List<ConnectionDescriptor> allProbabilities, HashMap<String, StpParameters> stpParams,
			int currentColumnNum,
			double timestep) {
		Random generator = new Random(19580427);

		NeuronColumn currentColumn = net.getColumn(currentColumnNum);

		for (ConnectionDescriptor conDesc : allProbabilities) {

			double prob = conDesc.getProbability();
			double weight = conDesc.getWeight();
			double std = conDesc.getStdWeight();

			NeuronTypePool outPool = currentColumn.getPool(conDesc.getPoolName())
					.getTypePool(conDesc.getType());

			ArrayList<NeuronTypePool> inPools = new ArrayList<NeuronTypePool>();

			if (conDesc.getTargetCol() == 0) {
				inPools.add(currentColumn.getPool(conDesc.getTargetPoolName())
						.getTypePool(conDesc.getTargetType()));
			} else {
				if (net.getColumn(currentColumnNum + conDesc.getTargetCol()) != null) {
					inPools.add(net
							.getColumn(currentColumnNum + conDesc.getTargetCol())
							.getPool(conDesc.getTargetPoolName())
							.getTypePool(conDesc.getTargetType()));
				}
				if (net.getColumn(currentColumnNum - conDesc.getTargetCol()) != null) {
					inPools.add(net
							.getColumn(currentColumnNum - conDesc.getTargetCol())
							.getPool(conDesc.getTargetPoolName())
							.getTypePool(conDesc.getTargetType()));
				}
			}
			SynapseFactory synFact = new SynapseFactory(stpParams);

			for (Neuron outNeuron : outPool.getNeurons()) {
				for (NeuronTypePool inP : inPools) {
					if (inP != null) {
						for (Neuron inNeuron : inP.getNeurons()) {
							double r = generator.nextDouble(); // probability of
																// the
																// connection
							if (r < prob) {
								// add here randomization of the weight
								net.addConnection(synFact.getSynapse(
										outNeuron,
										inNeuron,
										weight, std,
										calculateDelay(
												outNeuron.getCoordinates(),
												inNeuron.getCoordinates(),
												timestep)));

							}

						}
					}
				}
			}

		}
	}
}
