package neuronPackage;

import java.util.ArrayList;

import networkPackage.Network;
import networkPackage.SynapseFactory;

public class FrequencyInputer extends Inputer {

	double value;
	int interTime;
	int signalTime;

	public final ArrayList<Synapse> inputConnections = new ArrayList<Synapse>();
	SynapseFactory synFact = new SynapseFactory();

	@Override
	public void addConnection(Neuron n, Network net) {
		// inputConnections.add(n);

		Synapse s = synFact.getSynapse(this, n, 1, 1);
		inputConnections.add(s);
		net.addConnection(s);
	}

	public FrequencyInputer(int interTime, double value) {
		this.value = value;
		this.interTime = interTime;
		// this.signalTime = signalTime;
	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		int w = ((int) timeofSimulation) % (interTime);

		if (w < timeStep) {

			for (Synapse n : inputConnections) {
				// System.out.println("frequency " + inputConnections.size() +
				// " " +
				// value);
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
