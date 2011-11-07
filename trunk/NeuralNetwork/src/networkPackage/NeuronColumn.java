package networkPackage;

import java.util.Collection;
import java.util.HashMap;

import neuronPackage.Layer;

public class NeuronColumn {
	public int colID;

	// private final ArrayList<NeuronPool> poolsArray = new
	// ArrayList<NeuronPool>();

	HashMap<Layer, NeuronPool> pools = new HashMap<Layer, NeuronPool>();

	public NeuronColumn(int num) {
		// TODO Auto-generated constructor stub
		colID = num;
	}

	void addPool(NeuronPool newPool) {
		pools.put(newPool.getLayerName(), newPool);
		// poolsArray.add(newPool);
	}

	public Collection<NeuronPool> getPools() {
		// return poolsArray;
		return pools.values();
	}

	// public void setPools(ArrayList<NeuronPool> pools) {
	// this.pools = pools;
	// }

	public NeuronPool getPool(Layer name) {
		return pools.get(name);
	}

	public int numberOfPools() {

		return pools.size();
	}

	public int getNumberOfNeurons() {

		int sum = 0;
		for (NeuronPool p : pools.values()) {
			sum = sum + p.getNumberOfNeurons();
		}
		return sum;
	}
}
