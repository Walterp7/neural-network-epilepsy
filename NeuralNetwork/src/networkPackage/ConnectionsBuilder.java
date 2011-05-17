package networkPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neuronPackage.Neuron;

public class ConnectionsBuilder {
	final private int velocity = 3; // m/s

	int calculateDelay(int[] pre, int post[], double timestep) {
		int delay = 0;
		int n = pre.length;
		for (int i = 0; i < n; i++) {
			delay = delay + (pre[i] - post[i]) * (pre[i] - post[i]); // microns
		}

		delay = (int) (Math.sqrt(delay) / (1000 * velocity * timestep));
		if (delay > 0) {
			// System.out.println(delay);

		} else {
			delay++;
		}
		return delay; // ms
	}

	public void setUpConnections(Network net,
			List<ConnectionDescriptor> allProbabilities, int totalColumnNumber,
			double timestep) {
		Random generator = new Random(19580427);

		for (ConnectionDescriptor conDesc : allProbabilities) {

			for (int colnumber = 0; colnumber < totalColumnNumber; colnumber++) {
				double prob = conDesc.getProbability();
				double weight = conDesc.getWeight();

				NeuronTypePool outPool = net.getColumn(colnumber)
						.getPool(conDesc.getPoolNumber())
						.getTypePool(conDesc.getType());

				ArrayList<NeuronTypePool> inPools = new ArrayList<NeuronTypePool>();

				if (conDesc.getTargetCol() == 0) {
					inPools.add(net.getColumn(colnumber)
							.getPool(conDesc.getTargetPoolNumber())
							.getTypePool(conDesc.getTargetType()));
				} else {
					if (net.getColumn(colnumber + conDesc.getTargetCol()) != null) {
						inPools.add(net
								.getColumn(colnumber + conDesc.getTargetCol())
								.getPool(conDesc.getTargetPoolNumber())
								.getTypePool(conDesc.getTargetType()));
					}
					if (net.getColumn(colnumber - conDesc.getTargetCol()) != null) {
						inPools.add(net
								.getColumn(colnumber - conDesc.getTargetCol())
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
}
