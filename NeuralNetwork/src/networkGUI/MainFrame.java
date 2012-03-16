package networkGUI;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import simulationPackage.ProgressListener;
import simulationPackage.Simulator;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	// required for simulation
	ConfigurationUnit config = new ConfigurationUnit();
	// for GUI
	private final JPanel contentPane;
	private final JTextField txtNumCol;
	private final JTextField textFieldTotalTime;
	private final JTextField textFieldTimeStep;

	JProgressBar progressBar;

	private final JTextField txtSimul;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					MainFrame frame = new MainFrame();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		final String defaultColConf = "settings/default_col_config.txt";
		final String defaultSimConf = "settings/control-allSTP/focal.txt";

		setTitle("Neural Network Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 319);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 10, 80, 48, 58, 75, 31, 10,
				20, 10, 0 };
		gbl_contentPane.rowHeights = new int[] { 20, 0, 0, 20, 0, 20, 0, 0, 0,
				0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!config.isColumnConfigured()) {
					int n = Integer.parseInt(txtNumCol.getText());
					for (int i = 0; i < n; i++) {

						config.addCol2List(defaultColConf, 4);

					}
					config.setNumColumns(n);
				}
				if (!config.isSimConfigured()) {
					int n = Integer.parseInt(txtSimul.getText());

					for (int i = 0; i < n; i++) {

						config.addSim2List(defaultSimConf, "default_focal_" + (i + 1));
						// config.addSimName("default" + (i + 1));
					}

					config.setNumSim(n);
				}
				config.setTimeStep(Double.parseDouble(textFieldTimeStep
						.getText()));
				config.setTotalTime(Integer.parseInt(textFieldTotalTime
						.getText()));

				setEnabled(false);

				final Simulator newSimulator = new Simulator(config);
				newSimulator.setListener(new ProgressListener() {
					@Override
					public void reportProgress(final double percent) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								progressBar.setValue((int) (100 * percent));
							}
						});
					}
				});
				ExecutorService exec = Executors.newSingleThreadExecutor();
				exec.execute(new Runnable() {
					@Override
					public void run() {
						try {
							newSimulator.runSimulation();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);

						} catch (InterruptedException e1) {

							JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);

						} finally {
							setEnabled(true);
						}
					}
				});

			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStart.gridwidth = 3;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 4;
		gbc_btnStart.gridy = 1;
		contentPane.add(btnStart, gbc_btnStart);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExit.gridwidth = 3;
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 4;
		gbc_btnExit.gridy = 2;
		contentPane.add(btnExit, gbc_btnExit);

		progressBar = new JProgressBar(0, 100);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 9;
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 4;
		contentPane.add(progressBar, gbc_progressBar);

		JLabel lblTotalTimems = new JLabel("Total time [ms]");
		GridBagConstraints gbc_lblTotalTimems = new GridBagConstraints();
		gbc_lblTotalTimems.anchor = GridBagConstraints.WEST;
		gbc_lblTotalTimems.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTimems.gridx = 1;
		gbc_lblTotalTimems.gridy = 6;
		contentPane.add(lblTotalTimems, gbc_lblTotalTimems);

		textFieldTotalTime = new JTextField();
		textFieldTotalTime.setText("1000");
		GridBagConstraints gbc_textFieldTotalTime = new GridBagConstraints();
		gbc_textFieldTotalTime.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTotalTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTotalTime.gridx = 2;
		gbc_textFieldTotalTime.gridy = 6;
		contentPane.add(textFieldTotalTime, gbc_textFieldTotalTime);
		textFieldTotalTime.setColumns(10);

		JLabel lblTimeStep = new JLabel("Time step [ms]");
		GridBagConstraints gbc_lblTimeStep = new GridBagConstraints();
		gbc_lblTimeStep.anchor = GridBagConstraints.WEST;
		gbc_lblTimeStep.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeStep.gridx = 1;
		gbc_lblTimeStep.gridy = 7;
		contentPane.add(lblTimeStep, gbc_lblTimeStep);

		textFieldTimeStep = new JTextField();
		textFieldTimeStep.setText("0.1");
		GridBagConstraints gbc_textFieldTimeStep = new GridBagConstraints();
		gbc_textFieldTimeStep.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTimeStep.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTimeStep.gridx = 2;
		gbc_textFieldTimeStep.gridy = 7;
		contentPane.add(textFieldTimeStep, gbc_textFieldTimeStep);
		textFieldTimeStep.setColumns(10);

		JLabel lblNumberOfColumns = new JLabel("# columns");
		GridBagConstraints gbc_lblNumberOfColumns = new GridBagConstraints();
		gbc_lblNumberOfColumns.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfColumns.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfColumns.gridx = 1;
		gbc_lblNumberOfColumns.gridy = 9;
		contentPane.add(lblNumberOfColumns, gbc_lblNumberOfColumns);

		txtNumCol = new JTextField();

		txtNumCol.setText("5");
		GridBagConstraints gbc_txtNumCol = new GridBagConstraints();
		gbc_txtNumCol.insets = new Insets(0, 0, 5, 5);
		gbc_txtNumCol.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNumCol.gridx = 2;
		gbc_txtNumCol.gridy = 9;
		contentPane.add(txtNumCol, gbc_txtNumCol);
		txtNumCol.setColumns(10);
		txtNumCol.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				config.clearColLists();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				config.clearColLists();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				config.clearColLists();

			}
		});

		JLabel lblNumberOfSimulations = new JLabel("#simulations");
		GridBagConstraints gbc_lblNumberOfSimulations = new GridBagConstraints();
		gbc_lblNumberOfSimulations.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfSimulations.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfSimulations.gridx = 4;
		gbc_lblNumberOfSimulations.gridy = 9;
		contentPane.add(lblNumberOfSimulations, gbc_lblNumberOfSimulations);

		txtSimul = new JTextField();
		txtSimul.setText("1");
		GridBagConstraints gbc_txtSimul = new GridBagConstraints();
		gbc_txtSimul.insets = new Insets(0, 0, 5, 5);
		gbc_txtSimul.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSimul.gridx = 5;
		gbc_txtSimul.gridy = 9;
		contentPane.add(txtSimul, gbc_txtSimul);
		txtSimul.setColumns(10);

		txtSimul.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				config.clearSimLists();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				config.clearSimLists();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				config.clearSimLists();

			}
		});

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridwidth = 9;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 10;
		contentPane.add(separator, gbc_separator);

		JButton btnConfigureColumns = new JButton("Configure columns");
		btnConfigureColumns.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				config.setNumColumns(Integer.parseInt(txtNumCol.getText()));
				ConfigureColumnsFrame conFrame = new ConfigureColumnsFrame(
						config, defaultColConf);
				conFrame.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnConfigureColumns = new GridBagConstraints();
		gbc_btnConfigureColumns.gridwidth = 2;
		gbc_btnConfigureColumns.insets = new Insets(0, 0, 0, 5);
		gbc_btnConfigureColumns.gridx = 1;
		gbc_btnConfigureColumns.gridy = 11;
		contentPane.add(btnConfigureColumns, gbc_btnConfigureColumns);

		JButton btnConfigureSimulations = new JButton("Configure simulations");
		btnConfigureSimulations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.setNumSim(Integer.parseInt(txtSimul.getText()));
				ConfigureSettingsFrame setFrame = new ConfigureSettingsFrame(
						config, defaultSimConf);
				setFrame.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnConfigureSimulations = new GridBagConstraints();
		gbc_btnConfigureSimulations.gridwidth = 3;
		gbc_btnConfigureSimulations.insets = new Insets(0, 0, 0, 5);
		gbc_btnConfigureSimulations.gridx = 4;
		gbc_btnConfigureSimulations.gridy = 11;
		contentPane.add(btnConfigureSimulations, gbc_btnConfigureSimulations);
	}
}
