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

	}

	public Synapse getSynapse(Neuron preSynaptic, Neuron postSynaptic,
			double meanWeight, double std, int delay) {

		// StpParameters stp = stdpParams.get(preSynaptic.typeLayer2String()
		// + postSynaptic.typeLayer2String());

		StpParameters stpTemplate = stdpParams.get(preSynaptic.typeLayer2String()
				+ postSynaptic.typeLayer2String());

		StpParameters stp = null;
		if (stpTemplate != null) {
			double newTi = stpTemplate.getTi() + generator.nextGaussian() * stpTemplate.getTi() / 4;
			double newTrec = Math.max(5, stpTemplate.getTrec() + generator.nextGaussian() * stpTemplate.getTrec() / 4);
			double newTfac = Math.max(0.000001,
					stpTemplate.getTfac() + generator.nextGaussian() * stpTemplate.getTfac() / 4);
			double newU = Math.max(0.01, stpTemplate.getU() + generator.nextGaussian() * stpTemplate.getU() / 4);
			stp = new StpParameters(newTi, newTrec, newTfac, newU, stpTemplate.getMaxY());
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
