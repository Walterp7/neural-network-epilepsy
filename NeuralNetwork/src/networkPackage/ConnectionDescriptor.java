package networkPackage;

import neuronPackage.Type;

class ConnectionDescriptor {
	private int targetCol;
	private int poolNumber;
	private int targetPoolNumber;
	private Type type, targetType;
	private double weight;
	private double probability;

	void setDescription(int targetcol, int poolNum, int targetPool, Type t,
			Type tart, double w, double p) {

		targetCol = targetcol;
		poolNumber = poolNum;
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

	int getPoolNumber() {
		return poolNumber;
	}

	int getTargetPoolNumber() {
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
