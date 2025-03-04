package neuronPackage;

import java.util.ArrayList;

public class Neuron implements NetworkNode {
	private volatile double v;
	private volatile double u;
	// private volatile double currentInput;
	// private volatile double nextInput;
	private volatile double nextIPSP;
	private volatile double nextEPSP;
	private volatile double currentIPSP;
	private volatile double currentEPSP;
	private final Type type;
	private final double a, b, c, d;
	private int neuronId;
	private final Layer layer;
	private final ArrayList<Synapse> neuronConnections = new ArrayList<Synapse>();
	private final double minDt;
	// boolean wasPositiveInput = false;
	private final int colNum;

	double lastFiring = 0;

	private final int[] coordinates = new int[3];

	public Neuron(double[] parameters, Type type, int col, Layer l, double minDt) {

		v = -65;

		// currentInput = 0;
		currentIPSP = 0;
		currentEPSP = 0;
		// nextInput = 0;
		nextEPSP = 0;
		nextIPSP = 0;
		this.type = type;
		a = parameters[0];
		b = parameters[1];
		c = parameters[2];
		d = parameters[3];
		u = b * v;
		layer = l;
		colNum = col;
		this.minDt = minDt;
	}

	@Override
	public synchronized void addInput(double val, double time, double timeStep) {
		// nextInput = nextInput + val;
		if (val == 0) {
			System.out.println("" + time + " " + layer + " " + neuronId + " " + type);
		}
		if (val < 0) {
			nextIPSP += val;
		} else {
			nextEPSP += val;
		}

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

		double dt = 0.0;

		Status stat = null;

		boolean isFiring = (v >= 30);

		if (isFiring) {

			dt = timeofSimulation - lastFiring;

			if (dt < minDt) {
				v = 30;
				isFiring = false;
			} else {
				v = c;
				u = u + d;
				lastFiring = timeofSimulation;
			}
		}
		v = v + timeStep * (0.04 * v * v + 5 * v + 140 - u + currentEPSP + currentIPSP);
		u = u + timeStep * a * (b * v - u);

		if ((isFiring)) {

			for (Synapse s : neuronConnections) {
				s.addInput(1, timeStep, timeofSimulation);
			}

		}

		stat = new Status(isFiring, neuronId, timeofSimulation, v, type, currentIPSP, currentEPSP, colNum, layer);

		return stat;
	}

	@Override
	public void setCurrentInput() {
		// currentInput = nextInput;

		currentEPSP = nextEPSP;
		currentIPSP = nextIPSP;
		// nextInput = 0;
		nextEPSP = 0;
		nextIPSP = 0;
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

	public int getColNum() {
		return colNum;
	}

	public String type2String() {
		String result;
		if (type == Type.IB) {
			result = "RS";
		} else {
			result = type.toString();
		}
		return result;
	}

	public String typeLayer2String() {
		String str = type.toString();

		if (str.equals("IB")) {
			str = "RS";
		}

		return str + layer.toString();
	}

	double getA() {
		return a;
	}

	double getB() {
		return b;
	}

	double getC() {
		return c;
	}

	double getD() {
		return d;
	}

}
