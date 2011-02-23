package neuronPackage;

public interface NetworkNode {
	void addInput(double val);

	Status advance(double timeStep, int timeofSimulation);

	void setCurrentInput();
}
