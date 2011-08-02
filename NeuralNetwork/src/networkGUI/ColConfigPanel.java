package networkGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColConfigPanel extends JPanel {
	private final JTextField txtNumberLayers;
	private final JTextField txtFile;

	/**
	 * Create the panel.
	 */
	public ColConfigPanel(int number, int layerNumber, String defaultFile) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 60, 28, 19, 20, 30, 84, 57, 0,
				0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 30, 20, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblColumn = new JLabel("Column " + Integer.toString(number));
		GridBagConstraints gbc_lblColumn = new GridBagConstraints();
		gbc_lblColumn.anchor = GridBagConstraints.WEST;
		gbc_lblColumn.insets = new Insets(0, 0, 5, 5);
		gbc_lblColumn.gridx = 0;
		gbc_lblColumn.gridy = 0;
		add(lblColumn, gbc_lblColumn);

		JLabel lblNumberOfLayers = new JLabel("# Layers");
		GridBagConstraints gbc_lblNumberOfLayers = new GridBagConstraints();
		gbc_lblNumberOfLayers.fill = GridBagConstraints.VERTICAL;
		gbc_lblNumberOfLayers.insets = new Insets(0, 0, 0, 5);
		gbc_lblNumberOfLayers.anchor = GridBagConstraints.EAST;
		gbc_lblNumberOfLayers.gridx = 0;
		gbc_lblNumberOfLayers.gridy = 1;
		add(lblNumberOfLayers, gbc_lblNumberOfLayers);

		txtNumberLayers = new JTextField();
		txtNumberLayers.setText(Integer.toString(layerNumber));
		GridBagConstraints gbc_txtNumberLayers = new GridBagConstraints();
		gbc_txtNumberLayers.insets = new Insets(0, 0, 0, 5);
		gbc_txtNumberLayers.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNumberLayers.gridx = 1;
		gbc_txtNumberLayers.gridy = 1;
		add(txtNumberLayers, gbc_txtNumberLayers);
		txtNumberLayers.setColumns(10);

		JLabel lblCofingFile = new JLabel("Config File");
		GridBagConstraints gbc_lblCofingFile = new GridBagConstraints();
		gbc_lblCofingFile.anchor = GridBagConstraints.EAST;
		gbc_lblCofingFile.insets = new Insets(0, 0, 0, 5);
		gbc_lblCofingFile.gridx = 3;
		gbc_lblCofingFile.gridy = 1;
		add(lblCofingFile, gbc_lblCofingFile);

		txtFile = new JTextField();
		txtFile.setText(defaultFile);
		GridBagConstraints gbc_txtFile = new GridBagConstraints();
		gbc_txtFile.gridwidth = 4;
		gbc_txtFile.insets = new Insets(0, 0, 0, 5);
		gbc_txtFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFile.gridx = 4;
		gbc_txtFile.gridy = 1;
		add(txtFile, gbc_txtFile);
		txtFile.setColumns(10);

		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would open the file.
					txtFile.setText(file.getAbsolutePath());

				}

			}
		});
		GridBagConstraints gbc_btnOpen = new GridBagConstraints();
		gbc_btnOpen.insets = new Insets(0, 0, 0, 5);
		gbc_btnOpen.gridx = 8;
		gbc_btnOpen.gridy = 1;
		add(btnOpen, gbc_btnOpen);

	}

	public String getPath() {
		return txtFile.getText();
	}

	public int getNumberOfLayers() {
		return Integer.parseInt(txtNumberLayers.getText());
	}
}
