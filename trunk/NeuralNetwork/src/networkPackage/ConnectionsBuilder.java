package networkPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
import neuronPackage.StpParameters;

public class ConnectionsBuilder {
	final private double velocity = 0.4; // m/s

	int calculateDelay(int[] pre, int post[], double timestep) {
		double delay = 0;
		int delayInSteps = 0;
		int n = pre.length;
		for (int i = 0; i < n; i++) {
			delay = delay + (pre[i] - post[i]) * (pre[i] - post[i]); // microns
		}
		// ms -> how many steps
		delayInSteps = (int) (Math.round(Math.sqrt(delay) / (1000 * velocity * timestep)));

		if (delayInSteps <= 0) {
			// System.out.println("dist in microns" + delay);
			delayInSteps++;
		}

		return delayInSteps; // not ms, but the number of timesteps
	}

	public void setUpConnections(Network net,
			List<ConnectionDescriptor> allProbabilities, HashMap<String, StpParameters> stpParams,
			HashMap<String, PSPparameters> pspParams,
			HashMap<String, PSPparameters> secondaryPspParams, int currentColumnNum, double timestep) {

		Random generator = new Random(19580427);

		NeuronColumn currentColumn = net.getColumn(currentColumnNum);

		for (ConnectionDescriptor conDesc : allProbabilities) {

			double prob = conDesc.getProbability();
			double weight = conDesc.getWeight();
			double std = conDesc.getStdWeight();

			NeuronTypePool outPool = currentColumn.getPool(conDesc.getPoolName())
					.getTypePool(conDesc.getType());
			if ((outPool != null) && (!outPool.isEmpty())) {
				ArrayList<NeuronTypePool> inPools = new ArrayList<NeuronTypePool>();

				int[] targetCols;
				if (conDesc.getTargetCol() == 0) {
					targetCols = new int[1];
					targetCols[0] = currentColumnNum;
				} else {
					targetCols = new int[2];
					targetCols[0] = currentColumnNum + conDesc.getTargetCol();
					targetCols[1] = currentColumnNum - conDesc.getTargetCol();
				}

				for (int targetCol : targetCols) {
					if (net.getColumn(targetCol) != null) {
						NeuronColumn col = net.getColumn(targetCol);
						if (col.getPool(conDesc.getTargetPoolName()) != null) {
							NeuronPool neuronPool = col.getPool(conDesc.getTargetPoolName());
							if (neuronPool.getTypePool(conDesc.getTargetType()) != null) {
								inPools.add(neuronPool.getTypePool(conDesc.getTargetType()));

							}
						}
					}
				}

				SynapseFactory synFact = new SynapseFactory(stpParams, pspParams, secondaryPspParams);

				for (Neuron outNeuron : outPool.getNeurons()) {
					for (NeuronTypePool inP : inPools) {
						if (inP != null) {
							for (Neuron inNeuron : inP.getNeurons()) {
								double r = generator.nextDouble(); // probability
																	// of
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
}
