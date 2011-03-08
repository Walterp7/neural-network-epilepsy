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
	public Status advance(double timeStep, int timeofSimulation) {
		if ((timeofSimulation > startTime)
				&& (startTime + signalTime > timeofSimulation)) {
			for (Neuron n : inputConnections) {
				n.addInput(value);
			}
		}
		return null;
	}
}
