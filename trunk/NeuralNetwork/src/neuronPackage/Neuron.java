package neuronPackage;

import java.util.ArrayList;

public class Neuron implements NetworkNode {
	private double v;
	private double u;
	private double currentInput;
	private double nextInput;
	private final Type type;
	private final double a, b, c, d;

	private final ArrayList<Synapse> neuronConnections = new ArrayList<Synapse>();

	public Neuron(double[] parameters, Type type) {
		v = 0;
		u = 0;
		currentInput = 0;
		nextInput = 0;
		this.type = type;
		a = parameters[0];
		b = parameters[1];
		c = parameters[2];
		d = parameters[3];
	}

	@Override
	public void addInput(double val) {
		nextInput = nextInput + val;

	}

	@Override
	public void advance(double timeStep) {

		v = v + timeStep * 0.5
				* (0.04 * v * v + 5 * v + 140 - u + currentInput);
		u = u + timeStep * a * (b * v - u);

		v = v + currentInput;
		System.out.println();
		// System.out.println("action potential  "+v);
		if (isFiring()) {
			v = c;
			u = u + d;
			v = 0;
			for (Synapse s : neuronConnections) {
				s.addInput(1);
			}
			// System.out.println("action potential AFTER SPIKE "+v);
		}

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

}
