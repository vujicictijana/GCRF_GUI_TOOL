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
import java.io.File;
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

public class UmGCRFTrainMyModelForGUI extends Thread {
	private ProgressBar progressTrain;
	private JFrame mainFrame;

	private double s[][];
	private double r[];
	private double y[];
	private String modelFolder;
	private String time;
	private String matlabPath;
	private long proxyTime;
	private boolean cancelTrain;
	DecimalFormat df = new DecimalFormat("#.##");

	private double[][] sTest;
	private double[] rTest;
	private double[] yTest;
	public double[] outputs;
	DecimalFormat df1 = new DecimalFormat("#.######");

	public UmGCRFTrainMyModelForGUI(String matlabPath, String modelFolder, ProgressBar progressTrain, JFrame mainFrame,
			double[][] s, double[] r, double[] y, long proxyTime, double[][] sTest, double[] rTest, double[] yTest) {
		super();
		this.progressTrain = progressTrain;
		this.mainFrame = mainFrame;
		this.s = s;
		this.r = r;
		this.y = y;
		this.modelFolder = modelFolder;
		this.matlabPath = matlabPath;
		this.proxyTime = proxyTime;
		time = "Time in seconds: ";
		this.sTest = sTest;
		this.rTest = rTest;
		this.yTest = yTest;
	}

	public void run() {
		if (Reader.checkFile(matlabPath) == false) {
			JOptionPane.showMessageDialog(mainFrame,
					"Path to MATLAB.exe is not good. Please change path in Settings->Configuration", "Results",
					JOptionPane.ERROR_MESSAGE);
			progressTrain.setVisible(false);
			return;
		}
		cancelTrain = false;
		mainFrame.setEnabled(false);
		progressTrain.setTitle("Please wait - UmGCRF is in progress ");
		JButton cancelTrainBtn = new JButton();
		progressTrain.add(cancelTrainBtn);
		cancelTrainBtn.setBounds(0, 0, 80, 30);
		cancelTrainBtn.setText("Cancel");
		Style.buttonStyle(cancelTrainBtn);
		cancelTrainBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(true);
				progressTrain.setVisible(false);
				Reader.deleteDir(new File(modelFolder));
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec("taskkill /F /IM MATLAB.exe");
				} catch (IOException e1) {
				}
				cancelTrain = true;
				JOptionPane.showMessageDialog(progressTrain, "UmGCRF process is canceled.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		long start = System.currentTimeMillis();

		String returnedMessage = UmGCRF.train(matlabPath, s, y, r, modelFolder, proxyTime, sTest, rTest, yTest);

		progressTrain.setVisible(false);
		String output = returnedMessage.split("OUTPUT:")[1];
		String message = returnedMessage.split("OUTPUT:")[0];
		if (!cancelTrain) {
			long elapsedTime = System.currentTimeMillis() - start;
			time += df.format((double) elapsedTime / 1000);

			if (message.contains("R^2")) {
				message += "\n" + time;
				progressTrain.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(mainFrame, message, "Results", JOptionPane.ERROR_MESSAGE);
				mainFrame.setEnabled(true);
				progressTrain.setVisible(false);
				return;
			}

			if (!cancelTrain) {
				if (!output.equalsIgnoreCase("null")) {
					String[] outputsString = output.split("/");
					outputs = new double[outputsString.length];
					for (int i = 0; i < outputsString.length; i++) {
						outputs[i] = Double.parseDouble(outputsString[i]);
					}
					double r2 = BasicCalcs.rSquared(outputs, yTest);
					if (outputs == null) {
						JOptionPane.showMessageDialog(mainFrame, "An internal MATLAB exception occurred.", "Results",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					message += "\n\nTesting with test data:" + exportResults(r2, "test");

					JOptionPane.showMessageDialog(mainFrame, message, "Results", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			mainFrame.setEnabled(true);
			progressTrain.setVisible(false);
		}
	}

	public double read(String file) {
		String[] txt = Reader.read(file);
		return Double.parseDouble(txt[0].substring(txt[0].indexOf("=") + 1));
	}

	private String exportResults(double result, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/results.txt";
		String[] text = exportTxt(result, folder);
		Writer.write(text, fileName);
		return "\nR^2 UmGCRF: " + df1.format(result) + "\nExport successfully completed. \nFile location: "
				+ modelFolder + "/" + folder + ".";
	}

	public String[] exportTxt(double resultS, String type) {
		String[] txt = new String[outputs.length + 1];

		for (int i = 0; i < outputs.length; i++) {
			txt[i] = outputs[i] + "";
		}

		if (type.equalsIgnoreCase("test")) {
			txt[outputs.length] = "R^2 UmGCRF: " + df1.format(resultS);
		} else {
			txt[outputs.length] = "";
		}
		return txt;
	}

}
