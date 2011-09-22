package neuronPackage;

public class Status {
	private int neuronNumber;
	private final double voltage;
	private final Type type;
	private double time;
	private final boolean hasFired;
	private final double psp;
	private int layer = -1;
	private int column = -1;

	public Status(boolean fired, int number, double t, double voltage,
			Type type, double psp, int col, int lay) {
		super();
		this.neuronNumber = number;
		this.voltage = voltage;
		this.type = type;
		this.time = t;
		this.hasFired = fired;
		this.psp = psp;
		if (col == -1) {
			this.column = neuronNumber % 782; // TODO!!!!!!
		} else {
			this.column = col;
		}
		this.layer = lay;
	}

	public void setTime(int t) {
		time = t;
	}

	public int getColumn() {
		return column;
	}

	public int getLayer() {
		return layer;
	}

	public void setNumber(int n) {
		neuronNumber = n;
	}

	public boolean fired() {
		return hasFired;
	}

	public double getVoltage() {
		return voltage;
	}

	public double getPSP() {
		return psp;
	}

	public double getTime() {
		return time;
	}

	public int getNumber() {
		return neuronNumber;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {

		return time + " " + neuronNumber + " " + type.ordinal();
	}

}
