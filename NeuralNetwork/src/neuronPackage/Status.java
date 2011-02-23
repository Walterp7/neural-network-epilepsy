package neuronPackage;

public class Status {
	int neuronNumber;
	double voltage;
	Type type;
	int time;

	public Status(int number, int t, double voltage, Type type) {
		super();
		this.neuronNumber = number;
		this.voltage = voltage;
		this.type = type;
		this.time = t;
	}

	public void setTime(int t) {
		time = t;
	}

	public void setNumber(int n) {
		neuronNumber = n;
	}

	@Override
	public String toString() {

		return time + " " + neuronNumber + " " + type.ordinal();
	}

}
