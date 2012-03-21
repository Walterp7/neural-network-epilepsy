package neuronPackage;

public class Status {
	private int neuronNumber;
	private final double voltage;
	private final Type type;
	private double time;
	private final boolean hasFired;
	private final double epsp;
	private final double ipsp;
	private Layer layer = null;
	private int column = -1;

	public Status(boolean fired, int number, double t, double voltage,
			Type type, double ipsp, double epsp, int col, Layer lay) {
		super();
		this.neuronNumber = number;
		this.voltage = voltage;
		this.type = type;
		this.time = t;
		this.hasFired = fired;
		this.ipsp = ipsp;
		this.epsp = epsp;
		this.column = col;

		this.layer = lay;
	}

	public void setTime(int t) {
		time = t;
	}

	public int getColumn() {
		return column;
	}

	public Layer getLayer() {
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

	public double getEPSP() {
		return epsp;
	}

	public double getIPSP() {
		return ipsp;
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
