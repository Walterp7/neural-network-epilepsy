package networkGUI;

public class ColDescr {
	String colConfigDir;
	int layersInCol;

	public ColDescr(String str, int n) {
		colConfigDir = str;
		layersInCol = n;
	}

	public String getColConfDir() {
		return colConfigDir;
	}

	void setColConfDir(String colConfList) {
		this.colConfigDir = colConfList;
	}

	public int getLayersInCol() {
		return layersInCol;
	}

	void setLayersInCol(int layersInCol) {
		this.layersInCol = layersInCol;
	}
}
