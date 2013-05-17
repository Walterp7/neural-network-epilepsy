package neuronPackage;

import java.util.ArrayList;
import java.util.HashMap;

import networkPackage.Network;
import networkPackage.SynapseFactory;

public class LocalizedInputer extends Inputer {

	int duration;
	int startTime;
	Type type;
	Layer layer = null;
	double radius;
	public final ArrayList<Synapse> inputConnections = new ArrayList<Synapse>();
	public SynapseFactory synFact = null;

	public LocalizedInputer(int start, int duration, double value, double radius, Type type, Layer layer,
			HashMap<String, StpParameters> stpParams,
			HashMap<String, PSPparameters> pspParams,
			HashMap<String, PSPparameters> secondaryPspParams) {

		this.value = value;
		this.duration = duration;
		this.type = type;
		this.layer = layer;
		this.startTime = start;
		this.radius = radius;
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

		if ((timeofSimulation >= startTime) && (timeofSimulation <= startTime + duration)) {

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

		int originX = colNum * 400;
		int originY = 0;
		int originZ = 0;

		if (layer == Layer.IV) {
			originZ = 400;
		}
		if (layer == Layer.V) {
			originZ = 600;
		}
		if (layer == Layer.VI) {
			originZ = 1200;
		}

		for (Neuron neur : network.getAllNeurons()) {
			if ((colNum == -1) || (neur.getColNum() == colNum)) {
				if (neur.getType() == type) {

					int[] coords = neur.getCoordinates();
					double distance = Math.sqrt((originX - coords[0]) * (originX - coords[0]) + (originY - coords[1])
							* (originY - coords[1]) + (originZ - coords[2]) * (originZ - coords[2]));

					if (distance < radius) {

						addConnection(neur, network, 1);
					}

				}
			}
		}

	}
}
