package networkPackage;

import java.util.HashMap;

import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
import neuronPackage.Synapse;

public class SynapseFactory {

	HashMap<String, StpParameters> stdpParams = new HashMap<String, StpParameters>();

	public SynapseFactory() {
		// parameters: double trec, double ti, double tfac, double u, double
		// maxy
		stdpParams.put("RSII2RSII", new StpParameters(3, 100, 0.00001, 0.3, 0.3));
		stdpParams.put("RSII2RSV", new StpParameters(3, 100, 0.000001, 0, 0.4));
		stdpParams.put("RSV2RSV", new StpParameters(3, 350, 0.000001, 0.5, 0.5));
		stdpParams.put("RSV2RSII", new StpParameters(3, 100, 0, 0.000001, 0.4));
		stdpParams.put("RSVI2RSVI", new StpParameters(3, 150, 0.000001, 0.15, 0.15));

		stdpParams.put("RSII2FSII", new StpParameters(3, 110, 0.000001, 0.2, 0.2));
		stdpParams.put("RSIV2FSIV", new StpParameters(3, 250, 0.000001, 0.26, 0.26));
		stdpParams.put("RSVI2FSVI", new StpParameters(2, 70, 100, 0.09, 0.09));

		stdpParams.put("RSII2LTSII", new StpParameters(3, 150, 200, 0.02, 0.02));
		stdpParams.put("RSIV2LTSIV", new StpParameters(3, 20, 300, 0.01, 0.01));

		stdpParams.put("FSII2RSII", new StpParameters(3, 100, 0.000001, 0.5, 0.5));
		stdpParams.put("FSIV2RSIV", new StpParameters(3, 250, 0.000001, 0.26, 0.26));
		stdpParams.put("FSV2RSV", new StpParameters(3, 60, 0.000001, 0.6, 0.6));

		stdpParams.put("FSII2FSII", new StpParameters(3, 100, 0.000001, 0.5, 0.5));
		stdpParams.put("FSV2FSV", new StpParameters(3, 80, 0.000001, 0.5, 0.5));

		stdpParams.put("FSII2LTSII", new StpParameters(3, 100, 0.000001, 0.4, 0.4));

		stdpParams.put("LTSII2RSII", new StpParameters(3, 250, 0.000001, 0.3, 0.3));
		stdpParams.put("LTSIV2RSIV", new StpParameters(2, 70, 60, 0.09, 0.0949));

		stdpParams.put("LTSII2FSII", new StpParameters(3, 100, 0.000001, 0.5, 0.5));

		stdpParams.put("LTSII2LTSII", new StpParameters(3, 600, 1000, 0.09, 0.09));

	}

	PSPparameters pspParams = new PSPparameters();

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double weight, int delay) {

		Synapse newSynapse;
		StpParameters stp = stdpParams.get(preSynaptic.typeLayer2String()
				+ postSynaptic.typeLayer2String());
		if (stp == null) {
			stp = new StpParameters(1, 3, 0.0000001, 0.5, 0.5); // nothing

		}
		newSynapse = new Synapse(weight, preSynaptic, postSynaptic, stp, pspParams);

		newSynapse.setTimeDelay(delay);
		preSynaptic.addSynapse(newSynapse);
		return newSynapse;
	}
}
