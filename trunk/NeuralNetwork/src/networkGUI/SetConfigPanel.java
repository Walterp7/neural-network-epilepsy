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

public class SetConfigPanel extends JPanel {
	private final JTextField txtFile;
	private final JTextField txtName;

	/**
	 * Create the panel.
	 */
	public SetConfigPanel(String numStr, String defaultSetConf) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 50, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 20, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblSimulation = new JLabel("Simulation " + numStr);
		GridBagConstraints gbc_lblSimulation = new GridBagConstraints();
		gbc_lblSimulation.insets = new Insets(0, 0, 5, 5);
		gbc_lblSimulation.gridx = 0;
		gbc_lblSimulation.gridy = 0;
		add(lblSimulation, gbc_lblSimulation);

		JLabel lblConfigurationFile = new JLabel("Configuration file");
		GridBagConstraints gbc_lblConfigurationFile = new GridBagConstraints();
		gbc_lblConfigurationFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfigurationFile.anchor = GridBagConstraints.EAST;
		gbc_lblConfigurationFile.gridx = 0;
		gbc_lblConfigurationFile.gridy = 1;
		add(lblConfigurationFile, gbc_lblConfigurationFile);

		txtFile = new JTextField();
		txtFile.setText(defaultSetConf);
		GridBagConstraints gbc_txtFile = new GridBagConstraints();
		gbc_txtFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFile.gridx = 1;
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
					int length = file.getName().length();
					txtName.setText(file.getName().substring(0, length - 4));

				}
			}
		});
		GridBagConstraints gbc_btnOpen = new GridBagConstraints();
		gbc_btnOpen.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpen.gridx = 2;
		gbc_btnOpen.gridy = 1;
		add(btnOpen, gbc_btnOpen);

		JLabel lblSimulationName = new JLabel("Simulation Name");
		GridBagConstraints gbc_lblSimulationName = new GridBagConstraints();
		gbc_lblSimulationName.anchor = GridBagConstraints.EAST;
		gbc_lblSimulationName.insets = new Insets(0, 0, 0, 5);
		gbc_lblSimulationName.gridx = 0;
		gbc_lblSimulationName.gridy = 2;
		add(lblSimulationName, gbc_lblSimulationName);

		txtName = new JTextField();
		txtName.setText("default" + numStr);
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 0, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 2;
		add(txtName, gbc_txtName);
		txtName.setColumns(10);

	}

	public String getPath() {
		return txtFile.getText();
	}

	public String getSimName() {
		return txtName.getText();
	}

}
