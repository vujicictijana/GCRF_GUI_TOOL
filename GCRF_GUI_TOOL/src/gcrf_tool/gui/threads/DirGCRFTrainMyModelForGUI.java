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
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gcrf_tool.data.datasets.Dataset;
import gcrf_tool.data.generators.GraphGenerator;
import gcrf_tool.file.Reader;
import gcrf_tool.file.Writer;
import gcrf_tool.gui.frames.ProgressBar;
import gcrf_tool.gui.style.Style;
import gcrf_tool.learning.Parameters;
import gcrf_tool.methods.DirGCRF;
import gcrf_tool.methods.GCRF;

public class DirGCRFTrainMyModelForGUI extends Thread {
	private ProgressBar frame;
	private JFrame mainFrame;

	private double s[][];
	private double r[];
	private double y[];
	private double alpha;
	private double beta;
	private double lr;
	private int maxIter;
	private boolean both;
	private String modelFolder;
	private String time;
	private Thread thisThread;
	DecimalFormat df = new DecimalFormat("#.##");

	DecimalFormat df1 = new DecimalFormat("#.####");

	private double[][] sTest;
	private double[] rTest;
	private double[] yTest;
	public double[] outputs;
	public double[] outputsS;

	public DirGCRFTrainMyModelForGUI(String modelFolder, ProgressBar frame, JFrame mainFrame, double[][] s, double[] r,
			double[] y, double alpha, double beta, double lr, int maxIter, boolean both, double[][] sTest, double[] rTest,
			double[] yTest) {
		super();
		this.frame = frame;
		this.mainFrame = mainFrame;
		this.s = s;
		this.r = r;
		this.y = y;
		this.alpha = alpha;
		this.beta = beta;
		this.lr = lr;
		this.maxIter = maxIter;
		this.both = both;
		this.modelFolder = modelFolder;
		time = "Time in seconds: ";
		this.thisThread = this;
		this.sTest = sTest;
		this.rTest = rTest;
		this.yTest = yTest;
	}

	public void run() {
		mainFrame.setEnabled(false);
		frame.setTitle("Progress DirGCRF");
		JButton cancel = new JButton();
		frame.add(cancel);
		cancel.setBounds(0, 0, 80, 30);
		cancel.setText("Cancel");
		Style.buttonStyle(cancel);
		cancel.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(true);
				frame.setVisible(false);
				Reader.deleteDir(new File(modelFolder));
				thisThread.stop();
				JOptionPane.showMessageDialog(frame, "Training process is canceled.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		try {
			long start = System.currentTimeMillis();

			Parameters p = new Parameters(alpha, beta, maxIter, lr, false, frame.getCurrent());
			Dataset d = new Dataset(s, r, y);
			DirGCRF alg = new DirGCRF(p, d);
			double r2 = alg.rSquared();
			double[] res = alg.getParameters();

			long elapsedTime = System.currentTimeMillis() - start;
			time += "\n* DirGCRF: " + df.format((double) elapsedTime / 1000);
			double[] resS = null;
			double r2S = -1;
			if (both) {
				frame.getCurrent().setValue(0);
				frame.setTitle("Progress standard GCRF");
				start = System.currentTimeMillis();
				double[][] sS = GraphGenerator.converteGraphToUndirected(s);

				Parameters p1 = new Parameters(alpha, beta, maxIter, lr, false, frame.getCurrent());
				Dataset d1 = new Dataset(sS, r, y);
				GCRF algS = new GCRF(p1, d1);
				r2S = algS.rSquared();
				resS = algS.getParameters();
				elapsedTime = System.currentTimeMillis() - start;
				time += "\n* GCRF: " + df.format((double) elapsedTime / 1000);
			}
			createFile("DirGCRF.txt", res);
			if (resS != null) {
				createFile("GCRF.txt", resS);
			}
			String message = "Testing with same data:\n* R^2 value for DirGCRF is: " + df1.format(r2);
			if (resS != null) {
				message += "\n* R^2 value for standard GCRF is: " + df1.format(r2S);
			}
			message += "\n" + time;

			double[] param = read(modelFolder + "/parameters/DirGCRF.txt");
			double result = resultAsymmetric(param[0], param[1]);
			double[] paramS = read(modelFolder + "/parameters/GCRF.txt");
			double resultS = -1;

			if (paramS != null) {
				resultS = resultSymmetric(paramS[0], paramS[1]);

			}
			message += "\n\nTesting with test data:" +  exportResults(result, resultS, "test");

			JOptionPane.showMessageDialog(mainFrame, message, "Results", JOptionPane.INFORMATION_MESSAGE);
			mainFrame.setEnabled(true);
			frame.setVisible(false);
		} catch (Exception e) {

		}

	}

	public double[] read(String file) {
		String[] txt = Reader.read(file);
		if (txt != null) {
			double[] params = new double[txt.length];
			for (int i = 0; i < txt.length; i++) {
				params[i] = Double.parseDouble(txt[i].substring(txt[i].indexOf("=") + 1));
			}
			return params;
		}
		return null;
	}

	public double resultAsymmetric(double alpha, double beta) {
		Dataset d = new Dataset(sTest, rTest, yTest);
		DirGCRF alg = new DirGCRF(alpha, beta, d);
		outputs = alg.predictOutputs();
		return alg.rSquared();
	}

	public double resultSymmetric(double alpha, double beta) {
		Dataset d = new Dataset(sTest, rTest, yTest);
		GCRF alg = new GCRF(alpha, beta, d);
		outputsS = alg.predictOutputs();
		return alg.rSquared();
	}

	public String[] exportTxt(double[] array, double result, String method, String type) {
		String[] txt = new String[array.length + 1];

		for (int i = 0; i < array.length; i++) {
			txt[i] = array[i] + "";
		}
		if (type.equalsIgnoreCase("test")) {
			txt[outputs.length] = "R^2 " + method + ": " + df1.format(result);
		} else {
			txt[outputs.length] = "";
		}
		return txt;
	}

	private String exportResults(double result, double resultS, String folder) {
		Writer.createFolder(modelFolder + "/" + folder);
		String fileName = modelFolder + "/" + folder + "/resultsDirGCRF.txt";
		String[] text = exportTxt(outputs, result, "DirGCRF", folder);
		Writer.write(text, fileName);
		if (resultS != -1) {
			String fileName1 = modelFolder + "/" + folder + "/resultsGCRF.txt";
			String[] text1 = exportTxt(outputsS, resultS, "GCRF", folder);
			Writer.write(text1, fileName1);
		}
		String textDialog = "";
		if (resultS != -1) {
			textDialog += ""
					+ "\n*R^2 DirGCRF: " + df1.format(result) + "\nR^2 GCRF: " + df1.format(resultS);
		} else {
			textDialog += "\n*R^2 DirGCRF: " + df1.format(result);
		}
		return textDialog + "\nPredicted values for test data successfully exported.\nFile location: " + modelFolder + "/" + folder + ".";
	}

	public Object[][] fillData(double result, double resultS) {
		Object[][] data;
		if (resultS != -1) {
			data = new Object[1][2];
			data[0][0] = df1.format(result);
			data[0][1] = df1.format(resultS);

		} else {
			data = new Object[1][1];
			data[0][0] = df1.format(result);
		}
		return data;
	}

	public void createFile(String symmetric, double[] results) {
		Writer.createFolder(modelFolder + "/parameters");
		String fileName = modelFolder + "/parameters/" + symmetric;
		String[] resultsS = { "Alpha=" + results[0], "Beta=" + results[1] };
		Writer.write(resultsS, fileName);
	}

}
