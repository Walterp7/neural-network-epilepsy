package networkPackage;

import neuronPackage.Neuron;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class SynapseFactory {
	//
	// final private double[][] paramsSTP = { // trec, t1, tfac, U
	// { 800, 3, 0.00001, 0.5 }, // ei
	// { 800, 3, 0.00001, 0.5 }, // ee
	// { 100, 3, 1000, 0.04 }, // ie
	// { 100, 3, 1000, 0.04 } // ii
	// };

	final private StpParameters eeParam = new StpParameters(800, 3, 0.00001, 0.5);
	final private StpParameters eiParam = new StpParameters(800, 3, 0.00001, 0.5);
	final private StpParameters iiParam = new StpParameters(100, 3, 1000, 0.04);
	final private StpParameters ieParam = new StpParameters(100, 3, 1000, 0.04);

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double weight, int delay) {

		Type preType = preSynaptic.getType();
		Type postType = postSynaptic.getType();
		Synapse newSynapse;
		if (preType == Type.RS || preType == Type.IB) {
			if (postType == Type.RS || postType == Type.IB) {
				newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
						eeParam);
			} else {
				newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
						eiParam);
			}
		} else {
			if (postType == Type.RS || postType == Type.IB) {
				newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
						ieParam);
			} else {
				newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
						iiParam);
			}
		}

		newSynapse.setTimeDelay(delay);
		preSynaptic.addSynapse(newSynapse);
		return newSynapse;
	}
}
