package networkPackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import neuronPackage.ConstantInputer;
import neuronPackage.FrequencyInputer;
import neuronPackage.GaussianInputer;
import neuronPackage.Inputer;
import neuronPackage.Layer;
import neuronPackage.LocalizedInputer;
import neuronPackage.PSPparameters;
import neuronPackage.PickInputer;
import neuronPackage.StpParameters;
import neuronPackage.Type;

public class InputBuilder {

	void setInputs(List<String> inputList, HashMap<String, StpParameters> stpParams,
			HashMap<String, PSPparameters> pspParams,
			HashMap<String, PSPparameters> secondaryPspParams, Network net,
			InputDescriptor inDescriptor, double totalTime)
			throws Exception {

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

				} else {
					// inDescriptor.addInputer(newLine, totalTime);
					if (parsedLine[wordIndex].equals("Step")) {
						wordIndex++;
						int startTime = Integer
								.parseInt(parsedLine[wordIndex++]);
						int interTime = Integer
								.parseInt(parsedLine[wordIndex++]);
						int signalTime = Integer
								.parseInt(parsedLine[wordIndex++]);
						double value = Double
								.parseDouble(parsedLine[wordIndex++]);
						String typeString = parsedLine[wordIndex++];
						Type type = null;
						if (!typeString.equals("*")) {
							type = Type.valueOf(typeString);
						}
						String layerString = parsedLine[wordIndex++];
						Layer layer = null;
						if (!layerString.equals("*")) {
							layer = Layer.valueOf(layerString);
						}

						// System.out.println("inter " + interTime + " signal "
						// + signalTime + " value " + value);
						newInput = new FrequencyInputer(startTime, interTime, value, type, layer, stpParams, pspParams,
								secondaryPspParams);
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
									value, stpParams, pspParams, secondaryPspParams);

						} else {
							if (parsedLine[wordIndex].equals("Constant")) {
								wordIndex++;

								double value = Double
										.parseDouble(parsedLine[wordIndex++]);
								String typeString = parsedLine[wordIndex++];
								String layerString = parsedLine[wordIndex++];

								newInput = new ConstantInputer(value, stringToType(typeString), layerString);
							} else {
								if (parsedLine[wordIndex].equals("Localized")) {

									wordIndex++;
									int startTime = Integer
											.parseInt(parsedLine[wordIndex++]);
									int duration = Integer
											.parseInt(parsedLine[wordIndex++]);
									double value = Double
											.parseDouble(parsedLine[wordIndex++]);
									double radius = Double
											.parseDouble(parsedLine[wordIndex++]);
									String typeString = parsedLine[wordIndex++];
									Type type = null;
									if (!typeString.equals("*")) {
										type = Type.valueOf(typeString);
									}
									String layerString = parsedLine[wordIndex++];
									Layer layer = null;
									if (!layerString.equals("*")) {
										layer = Layer.valueOf(layerString);
									}

									// System.out.println("inter " + duration +
									// " signal " + signalTime + " value "
									// + value);
									newInput = new LocalizedInputer(startTime, duration, value, radius, type, layer,
											stpParams, pspParams,
											secondaryPspParams);
								} else {
									throw new IOException();
								}
							}
						}
					}
				}

				int colNum = Integer.parseInt(parsedLine[wordIndex++]);
				System.out.println("input connected to: ");
				System.out.println(colNum);
				newInput.connect(colNum, net);

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
