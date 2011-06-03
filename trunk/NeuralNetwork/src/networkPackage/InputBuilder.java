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
	void setInputs(String pathname, Network net) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(pathname));
		String newLine;
		String[] parsedLine;

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
				List<Integer> columns = new ArrayList<Integer>();
				List<Integer> pools = new ArrayList<Integer>();
				if (colNum >= 0) {
					columns.add(colNum);
				} else {
					for (int i = 0; i < net.numberOfColumns(); i++) {
						columns.add(i);
					}
				}

				if (poolNum >= 0) {
					pools.add(poolNum);
				} else {
					int size = net.numberOfPools();
					for (int i = 0; i < size; i++) {
						pools.add(i);
					}
				}

				for (Type type : typesToConnect) {
					for (Integer col : columns) {
						for (Integer pool : pools) {
							if (net.getColumn(col).getPool(pool)
									.getTypePool(type) != null) {
								ArrayList<Neuron> listNeuron = net
										.getColumn(col).getPool(pool)
										.getTypePool(type).getNeurons();

								for (Neuron neur : listNeuron) {
									newInput.addConnection(neur);
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
