/** This file is part of GCRF GUI TOOL.

    GCRF GUI TOOL is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GCRF GUI TOOL is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GCRF GUI TOOL.  If not, see <https://www.gnu.org/licenses/>.*/

package gcrf_tool.gui.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gcrf_tool.calculations.BasicCalcs;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.methods.matlab.UmGCRF;

public class UmGCRFTestMyModelForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;
	private String modelFolder;
	private double[][] s;
	private double[] r;
	private double[] y;
	public double[] outputs;
	private String matlabPath;
	DecimalFormat df = new DecimalFormat("#.######");
	private long proxyTime;
	private boolean cancelTrain;

	public UmGCRFTestMyModelForGUI(String matlabPath, JFrame mainFrame, String modelFolder, double[][] s, double[] r,
			double[] y, ProgressBar frame, long proxyTime) {
		super();
		this.mainFrame = mainFrame;
		this.modelFolder = modelFolder;
		this.s = s;
		this.r = r;
		this.y = y;
		this.frame = frame;
		this.matlabPath = matlabPath;
		this.proxyTime = proxyTime;
	}

	public void run() {
		if (Reader.checkFile(matlabPath) == false) {
			JOptionPane.showMessageDialog(mainFrame,
					"Path to MATLAB.exe is not good. Please change path in Settings->Configuration", "Results",
					JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(frame, "Testing process is canceled.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		double theta = read(modelFolder + "/parameters/UmGCRF.txt");

		outputs = UmGCRF.test(matlabPath, s, y, r, theta, proxyTime);
		if (!cancelTrain) {
			double r2 = BasicCalcs.rSquared(outputs, y);
			if (outputs == null) {
				JOptionPane.showMessageDialog(mainFrame, "An internal MATLAB exception occurred.", "Results",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			exportResults(r2, "test");

			mainFrame.setEnabled(true);
			frame.setVisible(false);
		}
	}

	public double read(String file) {
		String[] txt = Reader.read(file);
		return Double.parseDouble(txt[0].substring(txt[0].indexOf("=") + 1));
	}

	private void exportResults(double result, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/results.txt";
		String[] text = exportTxt(result, folder);
		Writer.write(text, fileName);
		JOptionPane.showMessageDialog(mainFrame, "R^2 UmGCRF: " + df.format(result)
				+ "\nExport successfully completed. \nFile location: " + modelFolder + "/" + folder + ".");
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
