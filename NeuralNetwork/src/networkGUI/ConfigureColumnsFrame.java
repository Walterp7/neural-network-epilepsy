package networkGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ConfigureColumnsFrame extends JFrame {

	private final JPanel contentPane;

	List<ColConfigPanel> panelList = new ArrayList<ColConfigPanel>();

	public ConfigureColumnsFrame(final ConfigurationUnit config,
			String defaultFile) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		int number = config.getNumColumns();
		setBounds(100, 100, 600, 150 + number * 60);
		// setBounds(100, 100, 600, 221);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 31, 31, 161, 20, 20, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 20, 30, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 0.0,
				0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0,
				1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.clearColLists();
				for (ColConfigPanel pan : panelList) {
					config.addCol2List(pan.getPath(), pan.getNumberOfLayers());

				}
				dispose();
			}
		});
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.anchor = GridBagConstraints.EAST;
		gbc_btnDone.insets = new Insets(0, 0, 5, 5);
		gbc_btnDone.gridx = 3;
		gbc_btnDone.gridy = 0;
		contentPane.add(btnDone, gbc_btnDone);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.anchor = GridBagConstraints.EAST;
		gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 1;
		contentPane.add(btnCancel, gbc_btnCancel);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 4;
		gbc_panel.gridwidth = 4;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		contentPane.add(panel, gbc_panel);

		String[] tabFiles = new String[number];
		int[] tabLayNums = new int[number];
		if (number == config.getColListSize()) {
			for (int i = 0; i < number; i++) {
				tabFiles[i] = config.getColConf(i);
				tabLayNums[i] = config.getLayNum(i);
			}
		} else {
			for (int i = 0; i < number; i++) {
				tabFiles[i] = defaultFile;
				tabLayNums[i] = 4;
			}
		}
		for (int i = 0; i < number; i++) {
			ColConfigPanel newColPanel = new ColConfigPanel(i + 1,
					tabLayNums[i], tabFiles[i]);
			panel.add(newColPanel);
			panelList.add(newColPanel);

		}
	}
}
