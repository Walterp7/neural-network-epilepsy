package neuronPackage;

import java.util.ArrayList;
import java.util.HashMap;

import networkPackage.Network;
import networkPackage.SynapseFactory;

public class FrequencyInputer extends Inputer {

	double value;
	int interTime;
	int signalTime;

	public final ArrayList<Synapse> inputConnections = new ArrayList<Synapse>();
	public SynapseFactory synFact = null;

	public FrequencyInputer(int interTime, double value, HashMap<String, StpParameters> stpParams) {
		this.value = value;
		this.interTime = interTime;
		// this.signalTime = signalTime;
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
		int w = ((int) ((timeofSimulation + 1) / timeStep)) % (int) (interTime / timeStep);

		if (w < timeStep) {
			System.out.println(w + "-------->" + timeofSimulation);
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
