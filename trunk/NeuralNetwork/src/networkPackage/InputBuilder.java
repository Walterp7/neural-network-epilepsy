package networkPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neuronPackage.FrequencyInputer;
import neuronPackage.GaussianInputer;
import neuronPackage.Inputer;
import neuronPackage.Neuron;
import neuronPackage.PickInputer;
import neuronPackage.Type;

public class InputBuilder {

	void setInputs(String pathname, Network net, InputDescriptor inDescriptor, double totalTime)
			throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(pathname));
		String newLine;
		String[] parsedLine;

		newLine = in.readLine();
		while ((newLine.charAt(0) == '%')) {
			newLine = in.readLine();
		}

		newLine = in.readLine(); // one line is for general config

		while ((newLine = in.readLine()) != null) {
			parsedLine = newLine.trim().split("\\s+");
			Inputer newInput = null;
			int wordIndex = 0;
			if (!(newLine.charAt(0) == '%')) {
				if (parsedLine[wordIndex].equals("Gaussian")) {
					wordIndex++;
					double mean = Double.parseDouble(parsedLine[wordIndex++]);
					double deviation = Double.parseDouble(parsedLine[wordIndex++]);
					newInput = new GaussianInputer(mean, deviation);

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
						newInput = new FrequencyInputer(interTime, signalTime,
								value);
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
									value);
						} else {
							throw new IOException();
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
				int poolNum = Integer.parseInt(parsedLine[wordIndex++]);

				if (colNum >= 0) { // input to a specific column
					if (poolNum >= 0) { // input to a specific layer

						for (Type type : typesToConnect) {
							if (net.getColumn(colNum).getPool(poolNum).getTypePool(type) != null) {
								ArrayList<Neuron> listNeuron = net.getColumn(colNum).getPool(poolNum).getTypePool(type)
										.getNeurons();

								for (Neuron neur : listNeuron) {
									newInput.addConnection(neur);
								}
							}
						}

					} else { // specific column but all layers
						int size = net.numberOfPools();
						for (NeuronPool layer : net.getColumn(colNum).getPools()) {
							for (Type type : typesToConnect) {
								if (layer.getTypePool(type) != null) {
									ArrayList<Neuron> listNeuron = layer.getTypePool(type)
											.getNeurons();

									for (Neuron neur : listNeuron) {
										newInput.addConnection(neur);
									}
								}
							}
						}
					}

				} else { // all columns

					if (poolNum >= 0) { // all columns but specific layer
						for (NeuronColumn col : net.getAllColumns()) {
							for (Type type : typesToConnect) {
								if (col.getPool(poolNum).getTypePool(type) != null) {
									ArrayList<Neuron> listNeuron = col.getPool(poolNum).getTypePool(type)
											.getNeurons();

									for (Neuron neur : listNeuron) {
										newInput.addConnection(neur);
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
											newInput.addConnection(neur);
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
		in.close();
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
}
