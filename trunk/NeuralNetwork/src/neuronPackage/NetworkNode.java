package neuronPackage;

public interface NetworkNode {
	// void addInput(double val);

	void addInput(double val, double time, double timeStep);

	Status advance(double timeStep, double timeofSimulation);

	void setCurrentInput();
}
