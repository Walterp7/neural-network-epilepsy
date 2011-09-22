package networkPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neuronPackage.Neuron;

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
			List<ConnectionDescriptor> allProbabilities, int currentColumnNum,
			double timestep) {
		Random generator = new Random(19580427);

		NeuronColumn currentColumn = net.getColumn(currentColumnNum);

		for (ConnectionDescriptor conDesc : allProbabilities) {

			double prob = conDesc.getProbability();
			double weight = conDesc.getWeight();

			NeuronTypePool outPool = currentColumn.getPool(conDesc.getPoolNumber())
					.getTypePool(conDesc.getType());

			ArrayList<NeuronTypePool> inPools = new ArrayList<NeuronTypePool>();

			if (conDesc.getTargetCol() == 0) {
				inPools.add(currentColumn.getPool(conDesc.getTargetPoolNumber())
						.getTypePool(conDesc.getTargetType()));
			} else {
				if (net.getColumn(currentColumnNum + conDesc.getTargetCol()) != null) {
					inPools.add(net
							.getColumn(currentColumnNum + conDesc.getTargetCol())
							.getPool(conDesc.getTargetPoolNumber())
							.getTypePool(conDesc.getTargetType()));
				}
				if (net.getColumn(currentColumnNum - conDesc.getTargetCol()) != null) {
					inPools.add(net
							.getColumn(currentColumnNum - conDesc.getTargetCol())
							.getPool(conDesc.getTargetPoolNumber())
							.getTypePool(conDesc.getTargetType()));
				}
			}
			SynapseFactory synFact = new SynapseFactory();

			for (Neuron outNeuron : outPool.getNeurons()) {
				for (NeuronTypePool inP : inPools) {
					if (inP != null) {
						for (Neuron inNeuron : inP.getNeurons()) {
							double r = generator.nextDouble();
							if (r < prob) {

								net.addConnection(synFact.getSynapse(
										outNeuron,
										inNeuron,
										weight,
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
