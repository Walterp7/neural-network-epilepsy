package networkPackage;

import java.util.HashMap;
import java.util.Random;

import neuronPackage.Inputer;
import neuronPackage.Neuron;
import neuronPackage.PSPparameters;
import neuronPackage.StpParameters;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class SynapseFactory {

	HashMap<String, StpParameters> stdpParams;
	HashMap<String, PSPparameters> pspParams;

	HashMap<String, PSPparameters> secondaryPspParams;

	PSPparameters thalamicPSP = new PSPparameters(1, 5, 0.535, 15);
	PSPparameters defaultParameters = new PSPparameters(1, 15, 0.7695, 35);
	Random generator = new Random(948234541);

	public SynapseFactory(HashMap<String, StpParameters> stpparams, HashMap<String, PSPparameters> pspParams,
			HashMap<String, PSPparameters> secondaryPspParams) {
		this.stdpParams = stpparams;
		this.pspParams = pspParams;
		this.secondaryPspParams = secondaryPspParams;
		// pspParams.put("RSIIILTSIII", new PSPparameters(0.1, 20, 0.7692, 40));
		// pspParams.put("RSVLTSV", new PSPparameters(0.1, 7, 0.9261, 15));
		// pspParams.put("RSIVLTSIV", new PSPparameters(0.5, 15, 0.8569, 35));
		// pspParams.put("RSVILTSVI", new PSPparameters(1, 7, 0.6197, 15));
		//
		// pspParams.put("RSVIFSVI", new PSPparameters(0.1, 7, 0.9261, 15));
		// pspParams.put("RSVIFSIV", new PSPparameters(0.01, 3, 0.9672, 10));
		// pspParams.put("RSVFSV", new PSPparameters(0.1, 12, 0.9525, 20));
		// // pspParams.put("RSVFSV", new PSPparameters(0.08, 15, 0.9669, 30));
		// pspParams.put("RSIVFSIV", new PSPparameters(0.1, 7, 0.9261, 15));
		// pspParams.put("RSIIIFSIII", new PSPparameters(0.5, 5, 0.6968, 15));
		//
		// pspParams.put("LTSIIIRSIII", new PSPparameters(0.5, 24, 0.9017, 45));
		// pspParams.put("LTSIVRSIV", new PSPparameters(0.3, 25, 0.9362, 55));
		//
		// pspParams.put("FSIIIRSIII", new PSPparameters(0.3, 24, 0.9342, 45));
		// pspParams.put("FSVRSVI", new PSPparameters(1, 27, 0.8483, 55));
		// pspParams.put("FSIVRSIV", new PSPparameters(0.08, 20, 0.9735, 40));
		//
		// pspParams.put("RSIIIRSV", new PSPparameters(1, 18, 0.7967, 30));
		// pspParams.put("RSVIRSVI", new PSPparameters(0.8, 10, 0.7386, 25));
		// pspParams.put("RSVIRSV", new PSPparameters(0.8, 20, 0.8395, 40));
		// pspParams.put("RSVIRSIV", new PSPparameters(0.1, 15, 0.9605, 30));
		// pspParams.put("RSVRSV", new PSPparameters(0.8, 15, 0.8267, 30));
		// pspParams.put("RSIVRSIV", new PSPparameters(0.8, 18, 0.8267, 40));
		// pspParams.put("RSIVRSIII", new PSPparameters(0.5, 15, 0.8596, 35));
		// pspParams.put("RSIIIRSIII", new PSPparameters(0.5, 20, 0.8870, 40));
		//
		// secondaryPspParams.put("RSLTS", new PSPparameters(0.1, 5, 0.9048,
		// 10));
		//
		// secondaryPspParams.put("RSFS", new PSPparameters(0.1, 5, 0.9048,
		// 15));
		// secondaryPspParams.put("RSRS", new PSPparameters(1, 12, 0.7313, 25));
		// // secondaryPspParams.put("RSRS", new PSPparameters(0.8, 15, 0.8267,
		// // 30));
		//
		// secondaryPspParams.put("LTSRS", new PSPparameters(1, 20, 0.8114,
		// 40));
		// secondaryPspParams.put("LTSFS", new PSPparameters(1, 10, 0.6968,
		// 25));
		// secondaryPspParams.put("LTSLTS", new PSPparameters(1, 10, 0.6968,
		// 25));
		//
		// secondaryPspParams.put("FSRS", new PSPparameters(1, 15, 0.7692, 30));
		// secondaryPspParams.put("FSLTS", new PSPparameters(1, 10, 0.6968,
		// 25));
		// secondaryPspParams.put("FSFS", new PSPparameters(1, 10, 0.6968, 25));

	}

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double meanWeight, double std, int delay) {

		// StpParameters stp = stdpParams.get(preSynaptic.typeLayer2String()
		// + postSynaptic.typeLayer2String());

		StpParameters stpTemplate = stdpParams.get(preSynaptic.typeLayer2String()
				+ postSynaptic.typeLayer2String());

		StpParameters stp = null;
		if (stpTemplate != null) {
			stp = new StpParameters(stpTemplate.getTi(), stpTemplate.getTrec() + generator.nextGaussian()
					* stpTemplate.getTrec() / 3, stpTemplate.getTfac() + generator.nextGaussian()
					* stpTemplate.getTfac() / 3,
					stpTemplate.getU(), stpTemplate.getMaxY());
		}
		double weight;

		double lowBound = meanWeight - std;
		double upperBound = meanWeight + std;

		if ((preSynaptic.getType() == Type.RS || preSynaptic.getType() == Type.IB) && lowBound < 0) {
			lowBound = 0;
		} else {
			if ((preSynaptic.getType() == Type.LTS || preSynaptic.getType() == Type.FS) && upperBound > 0) {
				upperBound = 0;
			}
		}
		weight = lowBound + (upperBound - lowBound) * generator.nextDouble();
		PSPparameters pspNew = pspParams.get(preSynaptic.typeLayer2String() + postSynaptic.typeLayer2String());
		if (pspNew == null) {
			pspNew = secondaryPspParams.get(preSynaptic.type2String() + postSynaptic.type2String());
		}

		Synapse newSynapse = new Synapse(weight, postSynaptic, stp, pspNew);

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
