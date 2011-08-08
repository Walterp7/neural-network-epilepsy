package neuronPackage;

public class PSPparameters {
	private double epspTd = 1;
	private double epspTr = 15;
	private double epspNormPar = 0.7692;
	private double ipspTf = 1;
	private double ipspTs = 20;
	private double ipspNormPar = -0.76918;
	private final double pspRange = 40; // in ms

	double getPspRange() {
		return pspRange;
	}

	void setEpspTd(double epspTd) {
		this.epspTd = epspTd;
	}

	void setEpspTr(double epspTr) {
		this.epspTr = epspTr;
	}

	void setEpspNormPar(double epspNormPar) {
		this.epspNormPar = epspNormPar;
	}

	void setIpspTf(double ipspTf) {
		this.ipspTf = ipspTf;
	}

	void setIpspTs(double ipspTs) {
		this.ipspTs = ipspTs;
	}

	void setIpspNormPar(double ipspNormPar) {
		this.ipspNormPar = ipspNormPar;
	}

	double getEpspTd() {
		return epspTd;
	}

	protected double getEpspTr() {
		return epspTr;
	}

	double getEpspNormPar() {
		return epspNormPar;
	}

	double getIpspTf() {
		return ipspTf;
	}

	double getIpspTs() {
		return ipspTs;
	}

	double getIpspNormPar() {
		return ipspNormPar;
	}

}
