package networkPackage;

import java.util.ArrayList;

public class NeuronColumn {
	private ArrayList<NeuronPool> pools = new ArrayList<NeuronPool>();

	void addPool(NeuronPool newPool) {
		pools.add(newPool);
	}

	public ArrayList<NeuronPool> getPools() {
		return pools;
	}

	public void setPools(ArrayList<NeuronPool> pools) {
		this.pools = pools;
	}

	public NeuronPool getPool(int index) {
		return pools.get(index);
	}
}
