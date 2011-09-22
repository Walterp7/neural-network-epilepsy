package neuronPackage;

import java.util.ArrayList;
import java.util.List;

public class Synapse implements NetworkNode { // connects node with neuron
	double synapseWeight;
	int timeDelay; // in number of timeSteps
	Neuron postSynapticNeuron;
	// Neuron preSynapticNeuron;
	// double ti, trec, tfac, U;
	StpParameters stpParam = null;
	List<SynapseInputPair> inputs = new ArrayList<SynapseInputPair>();

	PSPparameters pspParam;

	double x, y, u;
	double lastSpike;

	public Synapse(double weight, Neuron postSynaptic,
			StpParameters stpPar, PSPparameters pspParameters) {
		synapseWeight = weight;
		postSynapticNeuron = postSynaptic;
		timeDelay = 1;
		stpParam = stpPar;
		lastSpike = 0;
		x = 1;
		y = 0;
		u = 0;
		pspParam = pspParameters;

	}

	private double psp(double dt) { // absolute value of a epsp or ipsp

		return (-Math.exp(-dt / pspParam.getFirstT()) + Math.exp(-dt / pspParam.getSecT()))
				/ pspParam.getNormPar();

	}

	@Override
	public void addInput(double val, double timeStep, double time) {
		// assumption val=0 or val=1

		if ((val > 0) && stpParam != null) {
			double dt = time - lastSpike;
			double tfac = stpParam.getTfac();
			double ti = stpParam.getTi();
			double trec = stpParam.getTrec();
			double U = stpParam.getU();
			double maxY = stpParam.getMaxY();

			u = u * Math.exp(-dt / tfac) * (1 - U) + U;
			double xtmp = (x * Math.exp(-dt / trec) + y * ti * (Math.exp(-dt /
					trec) - Math.exp(-dt / ti))
					/ (ti - trec)
					+ 1 - Math.exp(-dt / trec));

			x = xtmp * (1 - u);
			y = (y * Math.exp(-dt / ti) + xtmp * u);

			inputs.add(new SynapseInputPair(-timeDelay * timeStep, synapseWeight * y * val / maxY));
			lastSpike = time;
			// System.out.println("addInput(1) " + synapseWeight * y * val);
			// System.out.println("addInput(1) y " + y);

		} else {
			if (val > 0) {
				// System.out.println("addInput(2) " + synapseWeight * val);

				inputs.add(new SynapseInputPair(-timeDelay * timeStep, synapseWeight * val));
			}

		}

	}

	@Override
	public void setCurrentInput() {
		// currentValue = nextValue;
		// nextValue = 0;
	}

	@Override
	public Status advance(double timeStep, double time) {

		// put learning here

		if (!inputs.isEmpty()) {

			double inputValue = 0;
			List<SynapseInputPair> tempInputs = new ArrayList<SynapseInputPair>();
			for (SynapseInputPair curInpt : inputs) {

				double curTime = curInpt.getInputTime();
				if (curTime >= 0) {
					inputValue = inputValue + psp(curTime) * curInpt.getInputStrength();

					curInpt.advanceInputTime(timeStep);

				} else {

					curInpt.advanceInputTime(timeStep);

				}
				if (curTime < pspParam.getPspRange()) {
					tempInputs.add(curInpt);
				}

			}
			// if (inputs.size() > 110) {
			// System.out.println("	s input " + inputValue + " s size " +
			// tempInputs.size());
			// }
			inputs = tempInputs;

			postSynapticNeuron.addInput(inputValue, timeStep, time);
		}

		// old: postSynapticNeuron.addInput(synapseWeight * inputs.remove(0));

		return null;
	}

	public Neuron getPostSynapticNeuron() {
		return postSynapticNeuron;
	}

	public void setTimeDelay(int value) {
		if (value <= 0) {
			value = 1;
		}
		timeDelay = value;

	}

	public int getTimeDelay() {
		return timeDelay;
	}

	public double getWeight() {
		return synapseWeight;
	}

}
