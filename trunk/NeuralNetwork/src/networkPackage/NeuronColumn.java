package networkPackage;

import java.util.ArrayList;

public class NeuronColumn {
	ArrayList<NeuronPool> pools = new ArrayList<NeuronPool>();

	void addPool(NeuronPool newPool) {
		pools.add(newPool);
	}

	public ArrayList<NeuronPool> getPools() {
		return pools;
	}

	public void setPools(ArrayList<NeuronPool> pools) {
		this.pools = pools;
	}

}
