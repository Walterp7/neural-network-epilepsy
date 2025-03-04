package neuronPackage;

import java.util.HashMap;

import networkPackage.Network;
import networkPackage.SynapseFactory;

public class PickInputer extends ThalamicInputer {

	int startTime;
	int signalTime;

	SynapseFactory synFact = null;

	boolean flag = false;

	public PickInputer(int interTime, int signalTime, double value, HashMap<String, StpParameters> stpParams,
			HashMap<String, PSPparameters> pspParams,
			HashMap<String, PSPparameters> secondaryPspParams) {
		this.value = value;
		this.startTime = interTime;
		this.signalTime = signalTime;
		synFact = new SynapseFactory(stpParams, pspParams, secondaryPspParams);
	}

	@Override
	public void addConnection(Neuron n, Network net, double strenght) {
		// inputConnections.add(n);
		Synapse s = synFact.getSynapse(this, n, strenght, 1);
		inputConnections.add(s);
		net.addConnection(s);

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		if ((timeofSimulation <= startTime) && (startTime < timeofSimulation + timeStep) && (!flag)) {
			flag = true;
			for (Synapse n : inputConnections) {
				n.addInput(value, timeStep, timeofSimulation);
			}
		}
		return null;
	}

	@Override
	public void addInput(double val, double time, double timeStep) {
		// TODO Auto-generated method stub

	}
}
