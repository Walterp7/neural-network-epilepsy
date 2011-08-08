package neuronPackage;

public class SynapseInputPair {
	double inputTime;
	double inputStrength;

	public SynapseInputPair() {

	}

	public SynapseInputPair(double time, double str) {
		inputStrength = str;
		inputTime = time;

	}

	double getInputTime() {
		return inputTime;
	}

	void setInputTime(double inputTime) {
		this.inputTime = inputTime;
	}

	void advanceInputTime(double timestep) {
		this.inputTime += timestep;
	}

	double getInputStrength() {
		return inputStrength;
	}

	void setInputStrength(double inputStrength) {
		this.inputStrength = inputStrength;
	}
}
