package neuronPackage;

public class PickInputer extends Inputer {
	double value;
	int startTime;
	int signalTime;

	public PickInputer(int interTime, int signalTime, double value) {
		this.value = value;
		this.startTime = interTime;
		this.signalTime = signalTime;
	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		if ((timeofSimulation > startTime)
				&& (startTime + signalTime > timeofSimulation)) {
			for (Neuron n : inputConnections) {
				n.addInput(value, timeStep, timeofSimulation);
			}
		}
		return null;
	}

	@Override
	public void addInput(double val, double time, double timeStep) {
		// TODO Auto-generated method stub

	}
}
