package neuronPackage;

import java.util.ArrayList;
import java.util.Random;

import networkPackage.Network;

public abstract class Inputer implements NetworkNode {

	double value;

	public void addConnection(Neuron n, Network net, double strength) {
		// inputConnections.add(n);

	}

	@Override
	public void setCurrentInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public Status advance(double timeStep, double timeofSimulation) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setUpConnections(Network network, int colNum, Layer layer, Type type, double strength,
			double probability) {
		Random gen = new Random(438965135);
		if (network.getColumn(colNum).getPool(layer) != null) {
			if (network.getColumn(colNum).getPool(layer).getTypePool(type) != null) {
				ArrayList<Neuron> listNeuron = network.getColumn(colNum).getPool(layer)
						.getTypePool(type).getNeurons();

				for (Neuron neur : listNeuron) {
					double r = gen.nextDouble();
					if (r <= probability) {
						addConnection(neur, network, strength);
					}
				}
			}
		}
	}

	public void connect(int colNum, Network network) throws Exception {

		int totalNumCol = network.getNumberOfColumns();
		if (colNum > 0) { // specific column
			// connect to this column

			setUpConnections(network, colNum, Layer.IV, Type.RS, 1, 1);
			setUpConnections(network, colNum, Layer.IV, Type.FS, 1, 1);
			setUpConnections(network, colNum, Layer.V, Type.RS, 0.8, 1);
			setUpConnections(network, colNum, Layer.V, Type.FS, 0.9, 1);
			setUpConnections(network, colNum, Layer.V, Type.IB, 0.9, 1);
			// column to the left
			if (colNum - 1 >= 0) {
				setUpConnections(network, colNum - 1, Layer.IV, Type.RS, 0.05, 0.2);
				setUpConnections(network, colNum - 1, Layer.IV, Type.FS, 0.96, 0.8);

			}
			if (colNum + 1 <= totalNumCol - 1) {
				setUpConnections(network, colNum + 1, Layer.IV, Type.RS, 0.05, 0.2);
				setUpConnections(network, colNum + 1, Layer.IV, Type.FS, 0.96, 0.8);
			}

			if (colNum - 2 >= 0) {
				setUpConnections(network, colNum - 2, Layer.IV, Type.FS, 0.77, 0.6);

			}
			if (colNum + 2 <= totalNumCol) {
				setUpConnections(network, colNum + 2, Layer.IV, Type.FS, 0.77, 0.6);

			}
		} else { // all columns
			throw new Exception("No column specified for input");

		}

	}
}
