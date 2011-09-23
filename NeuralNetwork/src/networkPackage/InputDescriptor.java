package networkPackage;

import java.util.Collection;
import java.util.HashMap;

import org.jfree.data.xy.XYSeries;

public class InputDescriptor {

	HashMap<String, XYSeries> inputs = new HashMap<String, XYSeries>();

	void addInputer(String strLine, double totalTime) {
		double[][] points = null;
		int size = 0;
		int index = 0; // where info about types and layers starts
		String[] parsedLine = strLine.trim().split("\\s+");

		if (parsedLine[0].equals("Step")) {

			int interTime = Integer.parseInt(parsedLine[1]);
			int signalTime = Integer.parseInt(parsedLine[2]);
			double value = Double.parseDouble(parsedLine[3]);
			index = 4;
			size = 4 * (int) (totalTime / (interTime + signalTime));
			points = new double[2][size];

			for (int i = 0; i < size; i = i + 4) {
				points[0][i] = ((i / 4) * (interTime + signalTime) + interTime);
				points[1][i] = 0;

				points[0][i + 1] = ((i / 4) * (interTime + signalTime) + interTime);
				points[1][i + 1] = value;

				points[0][i + 2] = (i / 4 + 1) * (interTime + signalTime);
				points[1][i + 2] = value;

				points[0][i + 3] = (i / 4 + 1) * (interTime + signalTime);
				points[1][i + 3] = 0;
			}

		} else {
			if (parsedLine[0].equals("Pick")) {
				points = new double[2][4];
				int startTime = Integer.parseInt(parsedLine[1]);
				int signalTime = Integer.parseInt(parsedLine[2]);
				double value = Double.parseDouble(parsedLine[3]);
				// only four points will be added
				points[1][0] = 0;
				points[0][0] = startTime;
				points[1][1] = value;
				points[0][1] = startTime;
				points[1][2] = value;
				points[0][2] = startTime + signalTime;
				points[1][3] = 0;
				points[0][3] = startTime + signalTime;

				index = 4;
				size = 4;
			}
		}

		String keyTitle = "";

		if (!parsedLine[index + 1].equals("-1")) {
			keyTitle = "Col" + (Integer.parseInt(parsedLine[index + 1]) + 1) + ", ";
		}
		if (parsedLine[index + 2].equals("0")) {
			keyTitle = keyTitle + "Layer II/III, ";
		}
		if (parsedLine[index + 2].equals("1")) {
			keyTitle = keyTitle + "Layer IV, ";
		}
		if (parsedLine[index + 2].equals("2")) {
			keyTitle = keyTitle + "Layer IV, ";
		}
		if (parsedLine[index + 2].equals("3")) {
			keyTitle = keyTitle + "Layer VI, ";
		}
		if (parsedLine[index + 2].equals("-1")) {
			keyTitle = keyTitle + "all layers, ";
		}

		keyTitle = keyTitle + parsedLine[index] + " neurons";

		if (inputs.containsKey(keyTitle)) {
			for (int i = 0; i < size; i++) {
				inputs.get(keyTitle).add(points[0][i], points[1][i]);
			}

		} else {
			XYSeries newSeries = new XYSeries(keyTitle);
			newSeries.add(0, 0);
			for (int i = 0; i < size; i++) {
				newSeries.add(points[0][i], points[1][i]);
			}
			newSeries.add(totalTime, 0);
			newSeries.setDescription(keyTitle);
			inputs.put(keyTitle, newSeries);
		}

	}

	public Collection<XYSeries> getAllSeries() {
		return inputs.values();
	}

	public boolean isEmpty() {
		return inputs.isEmpty();
	}
}
