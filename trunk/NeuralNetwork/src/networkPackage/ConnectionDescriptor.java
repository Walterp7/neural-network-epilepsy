package networkPackage;

import neuronPackage.Layer;
import neuronPackage.Type;

class ConnectionDescriptor {
	private int targetCol;
	private Layer poolName;
	private Layer targetPoolNumber;
	private Type type, targetType;
	private double weight;
	private double probability;

	void setDescription(int targetcol, Layer pool, Layer targetPool, Type t,
			Type tart, double w, double p) {

		targetCol = targetcol;
		poolName = pool;
		targetPoolNumber = targetPool;
		type = t;
		targetType = tart;
		weight = w;
		probability = p;
	}

	double getWeight() {
		return weight;
	}

	int getTargetCol() {
		return targetCol;
	}

	Layer getPoolName() {
		return poolName;
	}

	Layer getTargetPoolName() {
		return targetPoolNumber;
	}

	Type getType() {
		return type;
	}

	Type getTargetType() {
		return targetType;
	}

	double getProbability() {
		return probability;
	}

}
