package networkPackage;

import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
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

	// double trec, double ti, double tfac, double u, double maxy
	final private StpParameters fs2rsParam = new StpParameters(250, 3, 0.00001, 0.26, 0.2743);
	final private StpParameters rs2fsParam = new StpParameters(250, 3, 0.00001, 0.26, 0.26);
	final private StpParameters rs2ltsParam = new StpParameters(20, 3, 300, 0.01, 0.01);
	final private StpParameters lts2rsParam = new StpParameters(70, 2, 60, 0.09, 0.0949);
	final private StpParameters rs2rsParam = new StpParameters(1, 3, 0.000001, 0.5, 0.5);
	final private StpParameters lts2fsParam = new StpParameters(1, 3, 0.000001, 0.5, 0.5);
	final private StpParameters lts2ltsParam = new StpParameters(1, 3, 0.00001, 0.5, 0.5);
	final private StpParameters fs2ltsParam = new StpParameters(1, 3, 0.000001, 0.5, 0.5);
	final private StpParameters fs2fsParam = new StpParameters(1, 3, 0.000001, 0.5, 0.5);
	PSPparameters pspParams = new PSPparameters();

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double weight, int delay) {

		Type preType = preSynaptic.getType();
		Type postType = postSynaptic.getType();
		Synapse newSynapse;
		if (preType == Type.RS || preType == Type.IB) {
			if (postType == Type.LTS) {
				newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
						rs2ltsParam, pspParams);
			} else {
				if (preType == Type.RS || preType == Type.IB) {
					newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
							rs2rsParam, pspParams);
				}
				else { // FS
					newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
							rs2fsParam, pspParams);
				}
			}
		} else {
			if (preType == Type.LTS) {
				if (postType == Type.LTS) {
					newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
							lts2ltsParam, pspParams);
				} else {
					if (preType == Type.RS || preType == Type.IB) {
						newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
								lts2rsParam, pspParams);
					}
					else { // FS
						newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
								lts2fsParam, pspParams);
					}
				}
			} else { // pre = fs
				if (postType == Type.LTS) {
					newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
							fs2ltsParam, pspParams);
				} else {
					if (preType == Type.RS || preType == Type.IB) {
						newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
								fs2rsParam, pspParams);
					}
					else { // FS
						newSynapse = new Synapse(weight, preSynaptic, postSynaptic,
								fs2fsParam, pspParams);
					}
				}
			}
		}

		newSynapse.setTimeDelay(delay);
		preSynaptic.addSynapse(newSynapse);
		return newSynapse;
	}
}
