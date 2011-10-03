package neuronPackage;

import java.util.ArrayList;
import java.util.HashMap;

import networkPackage.Network;
import networkPackage.SynapseFactory;

public class PickInputer extends Inputer {
	double value;
	int startTime;
	int signalTime;
	protected final ArrayList<Synapse> inputConnections = new ArrayList<Synapse>();
	SynapseFactory synFact = null;

	public PickInputer(int interTime, int signalTime, double value, HashMap<String, StpParameters> stpParams) {
		this.value = value;
		this.startTime = interTime;
		this.signalTime = signalTime;
		synFact = new SynapseFactory(stpParams);
	}

	@Override
	public void addConnection(Neuron n, Network net) {
		// inputConnections.add(n);
		Synapse s = synFact.getSynapse(this, n, 1, 1);
		inputConnections.add(s);
		net.addConnection(s);

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		if ((timeofSimulation <= startTime) && (startTime < timeofSimulation + timeStep)) {

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
