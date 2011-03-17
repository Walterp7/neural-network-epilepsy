package neuronPackage;

public interface NetworkNode {
	void addInput(double val);

	Status advance(double timeStep, double timeofSimulation);

	void setCurrentInput();
}
