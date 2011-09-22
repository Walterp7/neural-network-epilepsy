package neuronPackage;

public class PSPparameters {

	/*
	 * PSP is always of the form (e^dt/t1 - e^dt/t2)/c
	 */

	private final double firstT;
	private final double secT;
	private final double normPar;
	private final double pspRange; // in ms

	public PSPparameters(double t1, double t2, double norm, double range) {
		firstT = t1;
		secT = t2;
		normPar = norm;
		pspRange = range;
	}

	double getFirstT() {
		return firstT;
	}

	double getSecT() {
		return secT;
	}

	double getNormPar() {
		return normPar;
	}

	double getPspRange() {
		return pspRange;
	}

}
