package networkPackage;

import java.util.ArrayList;

public class NeuronColumn {
	public int colID;
	private ArrayList<NeuronPool> pools = new ArrayList<NeuronPool>();

	public NeuronColumn(int num) {
		// TODO Auto-generated constructor stub
		colID = num;
	}

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

	public int numberOfPools() {

		return pools.size();
	}

	public int getNumberOfNeurons() {

		int sum = 0;
		for (NeuronPool p : pools) {
			sum = sum + p.getNumberOfNeurons();
		}
		return sum;
	}
}
