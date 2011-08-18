package networkPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import neuronPackage.Type;

public class NeuronPool {

	Map<Type, NeuronTypePool> typePools = new HashMap<Type, NeuronTypePool>();

	// ArrayList<NeuronTypePool> typePool = new ArrayList<NeuronTypePool>();

	void addTypePool(Type t, NeuronTypePool newType) {
		typePools.put(t, newType);
	}

	public ArrayList<NeuronTypePool> getTypePools() {
		ArrayList<NeuronTypePool> l = new ArrayList<NeuronTypePool>();

		for (NeuronTypePool entry : typePools.values()) {
			l.add(entry);
		}
		return l;

	}

	NeuronTypePool getTypePool(Type t) {
		return typePools.get(t);
	}

	public void setTypePool(HashMap<Type, NeuronTypePool> neuronTypes) {
		this.typePools = neuronTypes;
	}

	public int getNumberOfNeurons() {
		ArrayList<NeuronTypePool> allPools = getTypePools();

		int sum = 0;
		for (NeuronTypePool p : allPools) {
			sum = sum + p.getNeurons().size();
		}

		return sum;
	}
}
