package networkGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

public class SimulationEndDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final List<PlotFrame> plotFrames;

	public SimulationEndDialog(List<PlotFrame> frames, final String lfpFileName, final String ipspFileName) {

		plotFrames = frames;

		setBounds(100, 100, 343, 89);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblSimulationEnded = new JLabel(
					" Do you want to save all files?");
			contentPanel.add(lblSimulationEnded);
		}

		JButton yesButton = new JButton("Yes");
		contentPanel.add(yesButton);
		yesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser("F:/workspaces/neuronWorkspace/NeuralNetwork/data");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would open the file.
					if (!file.exists()) {
						new File(file.getAbsolutePath()).mkdir();
					}
					try {
						for (PlotFrame frame : plotFrames) {

							frame.save(file.getAbsolutePath());

						}

						FileUtils.copyFileToDirectory(new File(lfpFileName), file);
						FileUtils.copyFileToDirectory(new File(ipspFileName), file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				dispose();
			}
		}
				);
		yesButton.setActionCommand("Yes");
		JButton noButton = new JButton("No");
		contentPanel.add(noButton);
		noButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		noButton.setActionCommand("No");
		getRootPane().setDefaultButton(noButton);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.NORTH);
			{
				buttonPane.setLayout(new GridLayout(1, 0, 0, 0));
			}
		}
		getRootPane().setDefaultButton(noButton);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(new GridLayout(0, 1, 0, 0));
			}
		}
		this.setVisible(true);
	}

}
