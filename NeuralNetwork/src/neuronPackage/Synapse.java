package neuronPackage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Synapse implements NetworkNode { // connects node with neuron
	private double synapseWeight;
	private int timeDelay; // in number of timeSteps
	private final Neuron postSynapticNeuron;
	private final Neuron preSynapticNeuron;
	// Neuron preSynapticNeuron;
	// double ti, trec, tfac, U;
	private final StpParameters stpParam;
	// List<SynapseInputPair> inputs = new ArrayList<SynapseInputPair>();
	private final ConcurrentLinkedQueue<SynapseInputPair> inputs = new ConcurrentLinkedQueue<SynapseInputPair>();
	private final PSPparameters pspParam;

	private volatile double x, y, u;
	private volatile double lastSpike;
	private final boolean noted = false;

	public Synapse(double weight, Neuron preSynaptic, Neuron postSynaptic,
			StpParameters stpPar, PSPparameters pspParameters) {
		synapseWeight = weight;
		postSynapticNeuron = postSynaptic;
		preSynapticNeuron = preSynaptic;
		timeDelay = 1;
		stpParam = stpPar;
		lastSpike = 0;
		x = 1;
		y = 0;
		u = 0;
		pspParam = pspParameters;
		if (((preSynaptic == null) || (preSynaptic.getType() == Type.RS
				&& preSynaptic.getLayer() == Layer.IV))
				&& postSynaptic.getType() == Type.RS
				&& postSynaptic.getLayer() == Layer.V) {
			System.out
					.println("" + preSynaptic.getColNum() + " " + postSynaptic.getType()
							+ " "
							+ postSynaptic.getLayer() + " " + postSynaptic.getColNum());
		}

	}

	private double psp(double dt) { // absolute value of a epsp or ipsp

		return (Math.exp(-dt / pspParam.getSecT()) - Math.exp(-dt / pspParam.getFirstT()))
				/ pspParam.getNormPar();

	}

	@Override
	public void addInput(double val, double timeStep, double time) {
		// assumption val=0 or val=1

		if ((val > 0) && stpParam != null) {
			double dt = time - lastSpike;
			// if (dt > 0.5) {
			double tfac = stpParam.getTfac();
			double ti = stpParam.getTi();
			double trec = stpParam.getTrec();
			double U = stpParam.getU();
			double maxY = stpParam.getMaxY();
			// if ((!noted) && (dt < 0.29) && (preSynapticNeuron != null) &&
			// (!preSynapticNeuron.getType().equals(Type.FS))) {
			// noted = true;
			// System.out.println("---------------------------------------------");
			// System.out.println(" dt =" + dt + "< 1");
			// System.out.println(" pretsynaptic neurID " +
			// preSynapticNeuron.getId() + " type "
			// + preSynapticNeuron.getType());
			// System.out.println("a " + preSynapticNeuron.getA() + " b " +
			// preSynapticNeuron.getB() + " c " + preSynapticNeuron.getC() +
			// " d " +
			// preSynapticNeuron.getD());
			// }
			u = u * Math.exp(-dt / tfac) * (1 - U) + U;
			double xtmp = (x * Math.exp(-dt / trec) + y * ti * (Math.exp(-dt /
					trec) - Math.exp(-dt / ti))
					/ (ti - trec)
					+ 1 - Math.exp(-dt / trec));

			x = xtmp * (1 - u);
			y = (y * Math.exp(-dt / ti) + xtmp * u);

			inputs.add(new SynapseInputPair(-timeDelay * timeStep, synapseWeight * y * val / maxY));
			lastSpike = time;
			// }

		} else {
			// double dt = time - lastSpike;
			if (val > 0) {
				// if (dt > 0.5) {
				inputs.add(new SynapseInputPair(-timeDelay * timeStep, synapseWeight * val));
				lastSpike = time;
				// }
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
			// List<SynapseInputPair> tempInputs = new
			// ArrayList<SynapseInputPair>();
			boolean toRemove = false;
			if (inputs == null) {
				System.out.println("time: " + time + " waga: " + synapseWeight);
			}
			for (SynapseInputPair curInpt : inputs) {

				double curTime = curInpt.getInputTime();

				if (curTime >= 0) {
					inputValue = inputValue + psp(curTime) * curInpt.getInputStrength();

					curInpt.advanceInputTime(timeStep);

				} else {

					curInpt.advanceInputTime(timeStep);

				}
				if (curTime > pspParam.getPspRange()) {
					// tempInputs.add(curInpt);
					// inputs.remove(curInpt);
					toRemove = true;

				}

			}
			if (toRemove) {
				inputs.poll();
			}
			// if (inputs.size() > 410) {
			// System.out.println("	s input " + inputValue + " s size " +
			// tempInputs.size());
			// }
			// inputs = tempInputs;
			if (inputValue != 0) {
				postSynapticNeuron.addInput(inputValue, timeStep, time);
			}
		}

		// old: postSynapticNeuron.addInput(synapseWeight * inputs.remove(0));

		return null;
	}

	public Neuron getPostSynapticNeuron() {
		return postSynapticNeuron;
	}

	public Neuron getPreSynapticNeuron() {
		return preSynapticNeuron;
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

	public void multiplyWeight(double value) {
		synapseWeight = synapseWeight * value;
	}

}
