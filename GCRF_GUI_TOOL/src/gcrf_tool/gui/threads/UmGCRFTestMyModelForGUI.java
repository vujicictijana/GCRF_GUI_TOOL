package gcrf_tool.gui.threads;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.methods.matlab.UmGCRF;

public class UmGCRFTestMyModelForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;
	private JPanel panel;
	private String modelFolder;
	private double[][] s;
	private double[] r;
	private double[] y;
	public double[] outputs;
	private String matlabPath;
	DecimalFormat df = new DecimalFormat("#.######");
	private long proxyTime;
	private boolean cancelTrain;

	public UmGCRFTestMyModelForGUI(String matlabPath, JFrame mainFrame,
			JPanel panel, String modelFolder, double[][] s, double[] r,
			double[] y, ProgressBar frame, long proxyTime) {
		super();
		this.mainFrame = mainFrame;
		this.panel = panel;
		this.modelFolder = modelFolder;
		this.s = s;
		this.r = r;
		this.y = y;
		this.frame = frame;
		this.matlabPath = matlabPath;
		this.proxyTime = proxyTime;
		if (panel != null) {
			panel.removeAll();
			panel.revalidate();
			panel.repaint();
		}
	}

	public void run() {
		if (Reader.checkFile(matlabPath) == false) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"Path to MATLAB.exe is not good. Please change path in Settings->Configuration",
							"Results", JOptionPane.ERROR_MESSAGE);
			frame.setVisible(false);
			return;
		}
		cancelTrain = false;
		mainFrame.setEnabled(false);
		frame.setTitle("Please wait - UmGCRF is in progress ");
		JButton cancel = new JButton();
		frame.add(cancel);
		cancel.setBounds(0, 0, 80, 30);
		cancel.setText("Cancel");
		Style.buttonStyle(cancel);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(true);
				frame.setVisible(false);
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec("taskkill /F /IM MATLAB.exe");
				} catch (IOException e1) {
				}
				cancelTrain = true;
				JOptionPane.showMessageDialog(frame,
						"Testing process is canceled.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		double theta = read(modelFolder + "/parameters/UmGCRF.txt");

		outputs = UmGCRF.test(matlabPath, s, y, r, theta, proxyTime);
		if (!cancelTrain) {
			double r2 = BasicCalcs.rSquared(outputs, y);
			if (outputs == null) {
				JOptionPane.showMessageDialog(mainFrame,
						"An internal MATLAB exception occurred.", "Results",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (panel != null) {
				createTable(r2);
				exportResults(r2, "test");
			} else {
				exportResults(r2, "predict");
			}

			mainFrame.setEnabled(true);
			frame.setVisible(false);
		}
	}

	public double read(String file) {
		String[] txt = Reader.read(file);
		return Double.parseDouble(txt[0].substring(txt[0].indexOf("=") + 1));
	}

	public JTable createTable(double resultS) {
		JTable table = null;
		String[] columnNames = { "R^2 UmGCRF" };
		Object[][] data = new Object[1][1];
		data[0][0] = df.format(resultS);
		table = new JTable(data, columnNames);

		table.setBackground(new Color(240, 240, 240));
		JScrollPane scrollPane = new JScrollPane(table);
		Style.resultTable(table, -1);
		panel.add(scrollPane);
		scrollPane.setBounds(10, 10, 700, 200);
		return table;
	}

	private void exportResults(double result, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/results.txt";
		String[] text = exportTxt(result, folder);
		Writer.write(text, fileName);
		JOptionPane.showMessageDialog(mainFrame,
				"Export successfully completed. \nFile location: "
						+ modelFolder + "/" + folder + ".");
	}

	public String[] exportTxt(double resultS, String type) {
		String[] txt = new String[outputs.length + 1];

		for (int i = 0; i < outputs.length; i++) {
			txt[i] = outputs[i] + "";
		}

		if (type.equalsIgnoreCase("test")) {
			txt[outputs.length] = "R^2 UmGCRF: " + df.format(resultS);
		} else {
			txt[outputs.length] = "";
		}
		return txt;
	}

}
