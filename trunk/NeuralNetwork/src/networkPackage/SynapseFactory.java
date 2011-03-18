package networkPackage;

import neuronPackage.Neuron;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class SynapseFactory {

	final private double[][] paramsSTP = { // trec, t1, tfac, U, isDepressing
	{ 800, 3, -1, 0.5, 0 }, // ei
			{ 800, 3, 0.00001, 0.5, 1 }, // ee
			{ 100, 3, 1000, 0.04, 0 }, // ie
			{ 100, 3, 1000, 0.04, 0 } // ii
	};

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double weight, int delay) {
		double[] params = new double[5];
		Type preType = preSynaptic.getType();
		Type postType = postSynaptic.getType();

		if (preType == Type.RS || preType == Type.IB) {
			if (postType == Type.RS || postType == Type.IB) {
				params = paramsSTP[1];
			} else {
				params = paramsSTP[0];
			}
		} else {
			if (postType == Type.RS || postType == Type.IB) {
				params = paramsSTP[2];
			} else {
				params = paramsSTP[3];
			}
		}

		Synapse newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
				params);
		newSynapse.setTimeDelay(delay);
		preSynaptic.addSynapse(newSynapse);
		return newSynapse;
	}
}
