package networkPackage;

import java.util.HashMap;

import neuronPackage.Inputer;
import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
import neuronPackage.StpParameters;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class SynapseFactory {

	HashMap<String, StpParameters> stdpParams = new HashMap<String, StpParameters>();
	HashMap<Type, PSPparameters> pspParams = new HashMap<Type, PSPparameters>();

	PSPparameters thalamicPSP = new PSPparameters(1, 5, 0.535, 15);

	public SynapseFactory(HashMap<String, StpParameters> params) {
		this.stdpParams = params;
		pspParams.put(Type.RS, new PSPparameters(1, 10, 0.6968, 25));
		pspParams.put(Type.IB, new PSPparameters(1, 10, 0.6968, 25));
		pspParams.put(Type.LTS, new PSPparameters(15, 1, -0.76918, 30));
		pspParams.put(Type.FS, new PSPparameters(15, 1, -0.76918, 30));
	}

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double weight, int delay) {

		StpParameters stp = stdpParams.get(preSynaptic.typeLayer2String()
				+ "2" + postSynaptic.typeLayer2String());

		Synapse newSynapse = new Synapse(weight, postSynaptic, stp, pspParams.get(preSynaptic.getType()));

		newSynapse.setTimeDelay(delay);
		preSynaptic.addSynapse(newSynapse);
		return newSynapse;
	}

	public Synapse getSynapse(Inputer preSynaptic, Neuron postSynaptic,
			double weight, int delay) {
		StpParameters stp = null;
		if (stdpParams != null) {
			stp = stdpParams.get("th2" + postSynaptic.typeLayer2String());
		}
		Synapse newSynapse = new Synapse(weight, postSynaptic, stp, thalamicPSP);

		newSynapse.setTimeDelay(delay);

		return newSynapse;
	}
}
