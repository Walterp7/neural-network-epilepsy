package simulationPackage;

import networkPackage.Network;
import networkPackage.SynapseFactory;
import neuronPackage.GaussianInputer;
import neuronPackage.Layer;
import neuronPackage.Neuron;
import neuronPackage.Status;
import neuronPackage.Synapse;
import neuronPackage.Type;

public class testSim {

	public static void main(String[] args) {
		SynapseFactory fact = new SynapseFactory(null);

		double[] rsPar = { 0.02, 0.2, -65, 8 };
		double[] ibPar = { 0.02, 0.2, -55, 4 };
		double[] ltsPar = { 0.02, 0.25, -65, 2 };
		double[] fsPar = { 0.1, 0.2, -65, 2 };

		Neuron neur1 = new Neuron(rsPar, Type.RS, 0, Layer.IV);
		Neuron neur2 = new Neuron(rsPar, Type.RS, 0, Layer.IV);

		Network net = new Network();

		// FrequencyInputer input = new FrequencyInputer(10, 10);
		GaussianInputer gInput = new GaussianInputer(4, 10);
		// input.addConnection(neur, null);

		Synapse s = fact.getSynapse(neur1, neur2, 1, 1);

		gInput.addConnection(neur1, net);
		// input.inputConnections.add(s);

		for (int i = 1; i < 1500; i++) {
			gInput.advance(0.1, 0.1 * i);
			Status stat1 = neur1.advance(0.1, 0.1 * i);
			s.advance(0.1, 0.1 * i);
			neur2.advance(0.1, 0.1 * i);
			// Status stat1 = neur.advance(0.1, i * 0.1);
			neur1.setCurrentInput();
			neur2.setCurrentInput();
			System.out.println("Neuron1 v " + stat1.getVoltage() + " psp " + stat1.getPSP());
		}

	}
}
