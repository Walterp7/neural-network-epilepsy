package neuronPackage;

public class FrequencyInputer extends Inputer {

	double value;
	int interTime;
	int signalTime;

	public FrequencyInputer(int interTime, int signalTime, double value) {
		this.value = value;
		this.interTime = interTime;
		this.signalTime = signalTime;
	}

	@Override
	public Status advance(double timeStep, int timeofSimulation) {
		int w = timeofSimulation % (interTime + signalTime);
		if (w > interTime) {
			w = 1;
		} else {
			w = 0;
		}
		for (Neuron n : inputConnections) {

			n.addInput(w * value);
		}
		return null;
	}

}
