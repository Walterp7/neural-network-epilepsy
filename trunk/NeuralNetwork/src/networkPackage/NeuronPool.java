package networkPackage;

import java.util.ArrayList;

public class NeuronPool {
	ArrayList<NeuronTypePool> neuronTypes = new ArrayList<NeuronTypePool>();

	void addTypePool(NeuronTypePool newType) {
		neuronTypes.add(newType);
	}

	public ArrayList<NeuronTypePool> getNeuronTypes() {
		return neuronTypes;
	}

	public void setNeuronTypes(ArrayList<NeuronTypePool> neuronTypes) {
		this.neuronTypes = neuronTypes;
	}
}
