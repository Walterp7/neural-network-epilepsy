package networkPackage;

public class StpParameters {
	double trec, ti, tfac, U, maxY;

	public StpParameters(double trec, double ti, double tfac, double u, double maxy) {
		super();
		this.trec = trec;
		this.ti = ti;
		this.tfac = tfac;
		U = u;
		maxY = maxy;
	}

	public double getTrec() {
		return trec;
	}

	public double getTi() {
		return ti;
	}

	public double getTfac() {
		return tfac;
	}

	public double getU() {
		return U;
	}

	public double getMaxY() {
		return maxY;
	}

}
