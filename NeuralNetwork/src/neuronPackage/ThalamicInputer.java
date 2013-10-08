package neuronPackage;

import java.util.ArrayList;

public abstract class ThalamicInputer extends Inputer {
	public ArrayList<Synapse> inputConnections = new ArrayList<Synapse>();

	public ArrayList<Synapse> getConnections() {
		return inputConnections;
	}
}
