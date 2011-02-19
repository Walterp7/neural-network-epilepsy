package neuronPackage;

public interface NetworkNode {
	void addInput(double val);
	void advance(double timeStep);
	void setCurrentInput();
}
