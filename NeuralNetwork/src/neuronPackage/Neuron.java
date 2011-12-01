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
	private final Layer layer;
	private final ArrayList<Synapse> neuronConnections = new ArrayList<Synapse>();
	// boolean wasPositiveInput = false;
	private final int colNum;

	private final int[] coordinates = new int[3];

	public Neuron(double[] parameters, Type type, int col, Layer l, double dv) {

		v = -65;

		currentInput = 0;

		nextInput = 0;
		this.type = type;
		a = parameters[0];
		b = parameters[1];
		c = parameters[2];
		d = parameters[3];
		u = b * v;
		layer = l;
		colNum = col;
	}

	@Override
	public void addInput(double val, double time, double timeStep) {
		nextInput = nextInput + val;

	}

	public Layer getLayer() {
		return layer;
	}

	public void setCoordinates(int[] coor) {
		for (int i = 0; i < 3; i++) {
			coordinates[i] = coor[i];
		}
	}

	public int[] getCoordinates() {
		return coordinates;
	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {

		boolean isFiring = (v >= 30);

		Status stat = null;
		if (isFiring) {
			v = c;
			u = u + d;
		}
		v = v + timeStep * (0.04 * v * v + 5 * v + 140 - u + currentInput);
		u = u + timeStep * a * (b * v - u);

		if (isFiring) {
			for (Synapse s : neuronConnections) {
				s.addInput(1, timeStep, timeofSimulation);
			}
		}
		if (v > 30) {
			v = 30;
		}

		stat = new Status(isFiring, neuronId, timeofSimulation, v, type, currentInput, colNum, layer);

		// v = v + timeStep * (0.04 * v * v + 5 * v + 140 - u + currentInput);
		//
		// u = u + timeStep * a * (b * v - u);
		//
		// if (v >= 30) { // is firing
		//
		// stat = new Status(true, neuronId, timeofSimulation, v, type,
		// currentInput);
		// v = c;
		// u = u + d;
		//
		// for (Synapse s : neuronConnections) {
		// s.addInput(1, timeStep, timeofSimulation);
		// }
		// } else {
		//
		// stat = new Status(false, neuronId, timeofSimulation, v, type,
		// currentInput);
		// }
		return stat;
	}

	@Override
	public void setCurrentInput() {
		currentInput = nextInput;

		nextInput = 0;
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

	public double getV() {
		return v;
	}

	public String typeLayer2String() {
		String str = type.toString();

		if (str.equals("IB")) {
			str = "RS";
		}

		return str + layer.toString();
	}
}
