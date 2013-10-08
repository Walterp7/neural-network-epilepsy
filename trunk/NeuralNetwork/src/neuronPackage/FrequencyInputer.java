package neuronPackage;

import java.util.HashMap;

import networkPackage.Network;
import networkPackage.SynapseFactory;

public class FrequencyInputer extends ThalamicInputer {

	int interTime;
	int startTime;
	Type type;
	Layer layer = null;

	public SynapseFactory synFact = null;

	public FrequencyInputer(int start, int interTime, double value, Type type, Layer layer,
			HashMap<String, StpParameters> stpParams,
			HashMap<String, PSPparameters> pspParams,
			HashMap<String, PSPparameters> secondaryPspParams) {
		this.value = value;
		this.interTime = interTime;
		this.type = type;
		this.layer = layer;
		this.startTime = start;
		// this.signalTime = signalTime;
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
		int w = ((int) ((timeofSimulation + 1 - startTime) / timeStep)) % (int) (interTime / timeStep);

		if ((w < timeStep) && (w >= 0)) {

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

	@Override
	public void connect(int colNum, Network network) {

		for (Neuron neur : network.getAllNeurons()) {
			if ((colNum == -1) || (neur.getColNum() == colNum)) {
				if (neur.getType() == type) {

					if (layer != null) {

						if (neur.getLayer() == layer) {
							addConnection(neur, network, 1);

						}
					} else {

						addConnection(neur, network, 1);
					}

				}
			}
		}

	}

}
