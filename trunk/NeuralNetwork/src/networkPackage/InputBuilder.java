package networkPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import neuronPackage.ConstantInputer;
import neuronPackage.FrequencyInputer;
import neuronPackage.GaussianInputer;
import neuronPackage.Inputer;
import neuronPackage.Layer;
import neuronPackage.Neuron;
import neuronPackage.PickInputer;
import neuronPackage.StpParameters;
import neuronPackage.Type;

public class InputBuilder {

	void setInputs(List<String> inputList, HashMap<String, StpParameters> stpParams, Network net,
			InputDescriptor inDescriptor, double totalTime)
			throws IOException {

		for (String newLine : inputList) {
			String[] parsedLine = newLine.trim().split("\\s+");

			Inputer newInput = null;
			int wordIndex = 0;
			if (!(newLine.charAt(0) == '%')) {
				if (parsedLine[wordIndex].equals("Gaussian")) {
					wordIndex++;
					double mean = Double.parseDouble(parsedLine[wordIndex++]);
					double deviation = Double.parseDouble(parsedLine[wordIndex++]);
					newInput = new GaussianInputer(mean, deviation);
					System.out.println("Gaussian");
				} else {
					inDescriptor.addInputer(newLine, totalTime);
					if (parsedLine[wordIndex].equals("Step")) {
						wordIndex++;
						int interTime = Integer
								.parseInt(parsedLine[wordIndex++]);
						int signalTime = Integer
								.parseInt(parsedLine[wordIndex++]);
						double value = Double
								.parseDouble(parsedLine[wordIndex++]);
						System.out.println("inter " + interTime + " signal " + signalTime + " value " + value);
						newInput = new FrequencyInputer(interTime, value, stpParams);
					} else {
						if (parsedLine[wordIndex].equals("Pick")) {
							wordIndex++;
							int startTime = Integer
									.parseInt(parsedLine[wordIndex++]);
							int signalTime = Integer
									.parseInt(parsedLine[wordIndex++]);
							double value = Double
									.parseDouble(parsedLine[wordIndex++]);
							newInput = new PickInputer(startTime, signalTime,
									value, stpParams);
						} else {
							if (parsedLine[wordIndex].equals("Constant")) {
								wordIndex++;

								double value = Double
										.parseDouble(parsedLine[wordIndex++]);
								newInput = new ConstantInputer(value);
							} else {
								throw new IOException();
							}
						}
					}
				}

				List<Type> typesToConnect = new ArrayList<Type>();

				if (!(parsedLine[wordIndex].equals("*"))) {
					typesToConnect.add(stringToType(parsedLine[wordIndex++]));
				} else {
					for (Type t : Type.values()) {
						typesToConnect.add(t);
					}
					wordIndex++;
				}

				int colNum = Integer.parseInt(parsedLine[wordIndex++]);
				String poolName = parsedLine[wordIndex++];

				if (colNum >= 0) { // input to a specific column
					if (!poolName.equals("-1")) { // input to a specific layer
						Layer layer = stringToLayer(poolName);
						for (Type type : typesToConnect) {
							if (net.getColumn(colNum).getPool(layer).getTypePool(type) != null) {
								ArrayList<Neuron> listNeuron = net.getColumn(colNum).getPool(layer)
										.getTypePool(type)
										.getNeurons();

								for (Neuron neur : listNeuron) {
									newInput.addConnection(neur, net);
								}
							}
						}

					} else { // specific column but all layers

						for (NeuronPool layer : net.getColumn(colNum).getPools()) {
							for (Type type : typesToConnect) {
								if (layer.getTypePool(type) != null) {
									ArrayList<Neuron> listNeuron = layer.getTypePool(type)
											.getNeurons();

									for (Neuron neur : listNeuron) {
										newInput.addConnection(neur, net);
									}
								}
							}
						}
					}

				} else { // all columns

					if (!poolName.equals("-1")) { // all columns but specific
						Layer layer = stringToLayer(poolName); // layer
						for (NeuronColumn col : net.getAllColumns()) {
							for (Type type : typesToConnect) {
								if (col.getPool(layer).getTypePool(type) != null) {
									ArrayList<Neuron> listNeuron = col.getPool(layer).getTypePool(type)
											.getNeurons();

									for (Neuron neur : listNeuron) {
										newInput.addConnection(neur, net);
									}
								}
							}
						}

					} else { // all columns all layers
						for (NeuronColumn col : net.getAllColumns()) {
							for (NeuronPool pool : col.getPools()) {
								for (Type type : typesToConnect) {
									if (pool.getTypePool(type) != null) {
										ArrayList<Neuron> listNeuron = pool.getTypePool(type)
												.getNeurons();

										for (Neuron neur : listNeuron) {
											newInput.addConnection(neur, net);
										}
									}
								}
							}

						}

					}

				}
				net.addInput(newInput);
			}
		}

	}

	Type stringToType(String s) throws IOException {

		if (s.equals("RS")) {
			return Type.RS;
		}
		if (s.equals("IB")) {
			return Type.IB;
		}
		if (s.equals("FS")) {
			return Type.FS;
		}
		if (s.equals("LTS")) {
			return Type.LTS;
		} else {
			throw new IOException();
		}
	}

	Layer stringToLayer(String s) throws IOException {

		if (s.equals("III")) {
			return Layer.III;
		}
		if (s.equals("IV")) {
			return Layer.IV;
		}
		if (s.equals("V")) {
			return Layer.V;
		}
		if (s.equals("VI")) {
			return Layer.VI;
		} else {
			throw new IOException();
		}
	}
}
