package networkGUI;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationUnit {
	int numColumns;
	int numSim;

	int totalTime;
	double timeStep;

	List<ColDescr> colConfList = new ArrayList<ColDescr>();

	List<String> simConfList = new ArrayList<String>();
	List<String> simulNames = new ArrayList<String>();

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public double getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(double timeStep) {
		this.timeStep = timeStep;
	}

	public ConfigurationUnit() {
		numColumns = 5;
		numSim = 2;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}

	public int getNumSim() {
		return numSim;
	}

	public void setNumSim(int numSim) {
		this.numSim = numSim;
	}

	public void clearColLists() {
		colConfList.clear();

	}

	public void clearSimLists() {
		colConfList.clear();
		simulNames.clear();
	}

	public void addCol2List(String str, int num) {
		ColDescr newDescr = new ColDescr(str, num);
		colConfList.add(newDescr);
	}

	public void addSim2List(String str) {
		simConfList.add(str);
	}

	public void addSimName(String name) {
		simulNames.add(name);
	}

	public boolean isSimConfigured() {
		return !simConfList.isEmpty();
	}

	public boolean isColumnConfigured() {
		return !colConfList.isEmpty();
	}

	// public void print() {
	// System.out
	// .println("total time: " + totalTime + " timeStep " + timeStep);
	// System.out
	// .println("columns: " + numColumns + " simulations: " + numSim);
	// System.out.println("Columns:");
	// for (String str : colConfList) {
	// System.out.print(str + " ");
	// }
	// System.out.println();
	// System.out.println("Simulations: ");
	// for (String str : simConfList) {
	// System.out.print(str + " ");
	// }
	// System.out.println();
	// System.out.println("Sim names: ");
	//
	// for (String str : simulNames) {
	// System.out.print(str + " ");
	// }
	// System.out.println();
	// System.out.println("layers in col: ");
	// for (int n : layersInCol) {
	// System.out.print(n + " ");
	// }
	// }

	public String getColConf(int i) {
		return colConfList.get(i).getColConfDir();
	}

	public String getSimConf(int i) {
		return simConfList.get(i);
	}

	public String getSimName(int index) {
		return simulNames.get(index);
	}

	public int getLayNum(int i) {
		return colConfList.get(i).getLayersInCol();
	}

	public int getColListSize() {
		return colConfList.size();
	}

	public List<ColDescr> getAllColConfDescr() {
		return colConfList;
	}

	public List<String> getAllSimulations() {
		return simConfList;
	}
}
