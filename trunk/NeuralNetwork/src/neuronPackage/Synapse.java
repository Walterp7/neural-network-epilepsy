package neuronPackage;

import java.util.ArrayList;
import java.util.List;

import networkPackage.StpParameters;

public class Synapse implements NetworkNode { // connects node with neuron
	double synapseWeight;
	int timeDelay; // in number of timeSteps
	Neuron postSynapticNeuron;
	Neuron preSynapticNeuron;
	// double ti, trec, tfac, U;
	StpParameters stpParam;
	List<SynapseInputPair> inputs = new ArrayList<SynapseInputPair>();

	PSPparameters pspParam;

	double x, y, u;
	double lastSpike;

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic,
			StpParameters params, PSPparameters pspParameters) {
		synapseWeight = weight;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
		timeDelay = 1;
		stpParam = params;
		lastSpike = 0;
		x = 1;
		y = 0;
		u = 0;
		pspParam = pspParameters;

	}

	private double psp(double dt) { // absolute value of a epsp or ipsp

		if (synapseWeight > 0) {
			return (Math.exp(-dt / pspParam.getEpspTr()) - Math.exp(-dt / pspParam.getEpspTd()))
					/ pspParam.getEpspNormPar();
		} else {
			return (Math.exp(-dt / pspParam.getIpspTf()) - Math.exp(-dt /
					pspParam.getIpspTs()))
					/ pspParam.getIpspNormPar();

		}
	}

	@Override
	public void addInput(double val, double timeStep, double time) {
		// assumption val=0 or val=1

		if (val > 0) {
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
			y = (y * Math.exp(-dt / ti) + xtmp * u);// / maxY;
			if (y > 100) {
				System.out.println(" y ZA DUZE");
				y = 10;
				// System.out.println(" dt " + dt);
				System.out.println(preSynapticNeuron.getType());
				System.out.println(postSynapticNeuron.getType());
			}
			// if (y < -10) {
			// y = -10;
			// System.out.println("ABRAKADABRA");
			// }

			lastSpike = time;
			inputs.add(new SynapseInputPair(-timeDelay * timeStep, synapseWeight * y));
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
			if (inputs.size() > 100) {
				System.out.println("list size " + inputs.size() + " time delay" + timeDelay + " v "
						+ postSynapticNeuron.getV());
				System.out.println("synapse weigth " + synapseWeight + " y " + y);
			}
			double inputValue = 0;
			List<SynapseInputPair> tempInputs = new ArrayList<SynapseInputPair>();
			for (SynapseInputPair curInpt : inputs) {

				double curTime = curInpt.getInputTime();
				if (curTime >= 0) {
					inputValue = inputValue + psp(curTime) * curInpt.getInputStrength();
					if (inputs.size() > 100) {
						System.out.println("                psp  " + psp(curTime) + "strength  "
								+ curInpt.getInputStrength());
					}
					curInpt.advanceInputTime(timeStep);

				} else {
					// dodaj
					curInpt.advanceInputTime(timeStep);

				}
				if (curTime < pspParam.getPspRange()) {
					tempInputs.add(curInpt);
				}

			}
			if (inputs.size() > 100) {
				System.out.println("	new input " + inputValue + " new size " + tempInputs.size());
			}
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
