package neuronPackage;

import java.util.ArrayList;

public class Neuron implements NetworkNode {
	private double v;
	private double u;
	private double currentInput;
	private double nextInput;
	private final Type type;
	private final double a, b, c, d;
	private int neuronId;
	private final ArrayList<Synapse> neuronConnections = new ArrayList<Synapse>();

	public Neuron(double[] parameters, Type type) {
		v = -65;

		currentInput = 0;
		nextInput = 0;
		this.type = type;
		a = parameters[0];
		b = parameters[1];
		c = parameters[2];
		d = parameters[3];
		u = b * v;
	}

	@Override
	public void addInput(double val) {
		nextInput = nextInput + val;

	}

	@Override
	public Status advance(double timeStep, int timeofSimulation) {

		Status stat = null;
		v = v + timeStep * 0.5
				* (0.04 * v * v + 5 * v + 140 - u + currentInput);
		v = v + timeStep * 0.5
				* (0.04 * v * v + 5 * v + 140 - u + currentInput);
		u = u + timeStep * a * (b * v - u);

		v = v + currentInput;
		if (isFiring()) {
			// System.out.println(neuronId + " cInput: " + currentInput + "v: "
			// + v);
			stat = new Status(neuronId, timeofSimulation, v, type);
			v = c;
			u = u + d;

			for (Synapse s : neuronConnections) {
				s.addInput(1);
			}
		}
		return stat;
	}

	@Override
	public void setCurrentInput() {
		currentInput = nextInput;
		nextInput = 0;
	}

	boolean isFiring() {
		return v >= 30;
		// return v>=20;
	}

	double getMembraneVoltage() {
		return v;
	}

	public void addSynapse(Synapse newSyn) {
		neuronConnections.add(newSyn);
	}

	public Type getType() {
		return type;
	}

	public ArrayList<Synapse> getNeuronConnections() {
		return neuronConnections;
	}

	public void setId(int n) {

		this.neuronId = n;
	}

	public int getId() {
		return neuronId;
	}
}
